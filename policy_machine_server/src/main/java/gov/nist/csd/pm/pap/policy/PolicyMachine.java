package gov.nist.csd.pm.pap.policy;

import java.util.List;

import gov.nist.csd.pm.common.constants.PolicyStatus;
import gov.nist.csd.pm.common.constants.PolicyType;
import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.pap.model.Policy;

public interface PolicyMachine {

	long createPolicy(PolicyType policyType, String policyStatus, String policyOwner, long prevPolicy, long nextPolicy)
			throws PIPException;

	List<Policy> getPolicies(String policyOwner) throws PIPException;

	Policy getPolicy(String policyOwner, long policyID) throws PIPException;

	Policy getPolicy(long policyID) throws PIPException;

	void updatePolicy(String name, String description, String policyJSON, String cryptoJSON, PolicyStatus policyStatus,
			long policyID, String owner) throws PIPException;

	long publishPolicy(String name, String description, String json, String tJson, String owner) throws PIPException;

	long importPolicy(String owner, String name, String policy) throws PIPException;

	String exportPolicy(long policyId) throws PIPException;

	long createPolicyAttemptReln(long policyID, long attemptID) throws PIPException;

	boolean isPolicyAndAttemptValid(long policyID, long attemptID) throws PIPException;
}
