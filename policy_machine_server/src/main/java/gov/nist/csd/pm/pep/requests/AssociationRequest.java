package gov.nist.csd.pm.pep.requests;

import java.util.Set;

public class AssociationRequest {
	private long policyID;
	private Set<String> operations;

	public Set<String> getOperations() {
		return operations;
	}

	public void setOperations(Set<String> operations) {
		this.operations = operations;
	}

	public long getPolicyID() {
		return policyID;
	}

	public void setPolicyID(long policyID) {
		this.policyID = policyID;
	}
}
