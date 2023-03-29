package gov.nist.csd.pm.pdp.services;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import gov.nist.csd.pm.common.constants.AttemptStatus;
import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.common.constants.PolicyStatus;
import gov.nist.csd.pm.common.constants.StoreType;
import gov.nist.csd.pm.common.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pap.model.Attempt;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MNode;
import gov.nist.csd.pm.pap.model.Policy;
import gov.nist.csd.pm.ptl.model.Translation;

public class PolicyService extends Service {

	public PolicyService(String owner) throws PMException {
		super(owner);
	}

	public void importPolicy(Policy pc, String name) throws PMException {

	}

	public Policy exportPolicy(long policyID) throws PMException {
		return null;
	}

	public void publish(long policyID, long attemptID, String name, String description) throws PMException, Exception {
		if (0 == policyID) {
			throw new IllegalArgumentException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, policyID));
		} else if (0 == attemptID) {
			throw new IllegalArgumentException(String.format(MessageHelper.ATTEMPT_NOT_FOUND, attemptID));
		} else if (null == name || name.isEmpty()) {
			throw new IllegalArgumentException(MessageHelper.POLICY_NAME_NOT_FOUND);
		} else if (null == description) {
			throw new IllegalArgumentException(MessageHelper.POLICY_DESCRIPTION_NOT_FOUND);
		}

		Policy policy = getPolicyPAP().getPolicy(getOwner(), policyID);

		if (null == policy) {
			throw new PMAuthorizationException(MessageHelper.UNAUTHORIZED_OWNER);
		}

		// Check if the attempt of the policy is valid or is it expired
		if (!getPolicyPAP().isPolicyAndAttemptValid(policyID, attemptID)) {
			throw new IllegalArgumentException(
					String.format(MessageHelper.INVALID_POLICY_AND_ATTEMPT, policyID, attemptID));
		}

		AttemptService attemptService = new AttemptService(getOwner());
		Attempt attempt = attemptService.getAttempt(attemptID);
		if (AttemptStatus.EXPIRED == AttemptStatus.toAttemptStatus(attempt.getAttemptStatus())) {
			throw new PMException(String.format(MessageHelper.ATTEMPT_EXPIRED, attemptID));
		}

		GraphService graph = new GraphService(getOwner());
		Set<MNode> nodes = graph.getNodes();
		// remove the nodes which are not owned by owner
		nodes.removeIf(node -> node.getPolicyID() != policyID);

		Set<MAssignment> assignments = graph.getAssignments(policyID);
		assignments.removeIf(assignment -> assignment.getPolicyID() != policyID);

		Translation translation = new Translation();
		String json = null;
		try {
			json = translation.convert(nodes, assignments, getGraphPAP());
		} catch (PMException | IOException e) {
			throw new PMException(e.getMessage());
		}

		// Automate NGAC policy
		MNode root = null;
		for (MNode node : nodes) {
			if (node.getType() == NodeType.O) {
				root = node;
				break;
			}
		}

		if (null == root)
			throw new PMException(String.format(MessageHelper.POLICY_AUTOMATION_FAILED, policyID));
		/*
		 * POLICY AUTOMATION PENDING
		 *
		 * 1. Create PC type nodes 2. Assign all OA leaf nodes to PC 3. Replicate the OA
		 * structure to UA 4. Create U to O
		 */
		Set<MNode> parents = graph.getParents(root.getID());
		if (null == parents || parents.isEmpty())
			throw new PMException(String.format(MessageHelper.POLICY_AUTOMATION_FAILED, policyID));

		for (MNode node : nodes) {
			graph.updateNode(node.getID(), node.getName(), node.getProperties(), StoreType.NORMAL.name(), policyID);
		}

		for (MAssignment assignment : assignments) {
			graph.updateAssignment(policyID, assignment.getSourceID(), assignment.getTargetID(),
					StoreType.NORMAL.name());
		}

		// Update policy status
		getPolicyPAP().updatePolicy(name, description, json, json, PolicyStatus.ACTIVE, policyID, getOwner());
	}

	public void cancel(Policy policy) throws PMException {

	}

	public void update(Policy policy) {
		if (0 == policy.getId()) {
			throw new IllegalArgumentException(MessageHelper.POLICY_ID_NOT_FOUND);
		} else if (null == policy.getName() || policy.getName().trim().isEmpty()) {
			throw new IllegalArgumentException(MessageHelper.POLICY_NAME_NOT_FOUND);
		} else if (null == policy.getDescription()) {
			throw new IllegalArgumentException(MessageHelper.POLICY_DESCRIPTION_NOT_FOUND);
		}
	}

	public List<Policy> getPolicies(String status) throws PMException {
		if (null == PolicyStatus.toPolicyStatus(status)) {
			return getPolicyPAP().getPolicies(getOwner());
		} else {
			List<Policy> policies = getPolicyPAP().getPolicies(getOwner());
			policies.removeIf(policy -> !policy.getStatus().equals(status));
			return policies;
		}
	}

	public Policy getPolicy(long policyID) throws PMException {
		if (0 == policyID)
			throw new IllegalArgumentException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, policyID));
		return getPolicyPAP().getPolicy(getOwner(), policyID);
	}
}
