package gov.nist.csd.pm.pap.policy;

import java.util.List;

import gov.nist.csd.pm.common.constants.PolicyStatus;
import gov.nist.csd.pm.common.constants.PolicyType;
import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.pap.model.Policy;

public class PolicyPAP implements PolicyMachine {

	private PolicyMachine db;

	public PolicyPAP(PolicyMachine db) {
		this.db  = db;
	}

	@Override
	public long publishPolicy(String name, String description, String businessJson, String tJson, String owner) throws PIPException {
		return db.publishPolicy(name, description, businessJson, tJson, owner);
	}

	@Override
	public long importPolicy(String owner, String name, String policy) throws PIPException {
		return db.importPolicy(owner, name, policy);
	}

	@Override
	public String exportPolicy(long policyID) throws PIPException {
		return db.exportPolicy(policyID);
	}

	@Override
	public long createPolicy(PolicyType policyType, String policyStatus, String policyOwner, long prevPolicy, long nextPolicy) throws PIPException {
		return db.createPolicy(policyType, policyStatus, policyOwner, prevPolicy, nextPolicy);
	}

	@Override
	public long createPolicyAttemptReln(long policyID, long attemptID) throws PIPException {
		return db.createPolicyAttemptReln(policyID, attemptID);
	}

	@Override
	public List<Policy> getPolicies(String policyOwner) throws PIPException {
		return db.getPolicies(policyOwner);
	}

	@Override
	public Policy getPolicy(String policyOwner, long policyID) throws PIPException {
		return db.getPolicy(policyOwner, policyID);
	}

	@Override
	public boolean isPolicyAndAttemptValid(long policyID, long attemptID) throws PIPException {
		return db.isPolicyAndAttemptValid(policyID, attemptID);
	}

	@Override
	public void updatePolicy(String name, String description, String policyJSON, String cryptoJSON,
			PolicyStatus policyStatus, long policyID, String owner) throws PIPException {
		db.updatePolicy(name, description, policyJSON, cryptoJSON, policyStatus, policyID, owner);
	}

	@Override
	public Policy getPolicy(long policyID) throws PIPException {
		return db.getPolicy(policyID);
	}
}
