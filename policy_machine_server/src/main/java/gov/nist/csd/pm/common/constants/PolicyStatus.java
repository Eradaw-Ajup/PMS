package gov.nist.csd.pm.common.constants;

public enum PolicyStatus {
	ACTIVE("ACTIVE"),
	IN_ACTIVE("IN-ACTIVE"),
	TERMINATED("TERMINATED");

	private String label;

	PolicyStatus(String label) {
		this.label = label;
	}

	public static PolicyStatus toPolicyStatus(String policyStatus) {
		if(null == policyStatus)
			return null;
		switch(policyStatus) {
		case "ACTIVE":
			return ACTIVE;
		case "IN-ACTIVE":
			return IN_ACTIVE;
		case "TERMINATED":
			return TERMINATED;
		default:
			return null;
		}
	}
}
