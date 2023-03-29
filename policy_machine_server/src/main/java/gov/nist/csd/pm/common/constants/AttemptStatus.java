package gov.nist.csd.pm.common.constants;

public enum AttemptStatus {
	ACTIVE("ACTIVE"), EXPIRED("EXPIRED");

	private String label;

	AttemptStatus(String label) {
		this.label = label;
	}

	public static AttemptStatus toAttemptStatus(String attemptStatus) {
		if (null == attemptStatus)
			return null;
		switch (attemptStatus) {
		case "ACTIVE":
			return ACTIVE;
		case "EXPIRED":
			return EXPIRED;
		default:
			return null;
		}
	}
}
