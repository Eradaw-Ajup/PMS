package gov.nist.csd.pm.pep.requests;

public class PublishPolicyRequest {
	private long attemptID;
	private long policyID;
	private String name;
	private String description;

	public long getAttemptID() {
		return attemptID;
	}

	public void setAttemptID(long attemptID) {
		this.attemptID = attemptID;
	}

	public long getPolicyID() {
		return policyID;
	}

	public void setPolicyID(long policyID) {
		this.policyID = policyID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
