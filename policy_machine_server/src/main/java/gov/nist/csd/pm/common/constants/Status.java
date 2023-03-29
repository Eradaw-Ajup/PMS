package gov.nist.csd.pm.common.constants;

public enum Status {
	ACTIVE("ACTIVE"), IN_ACTIVE("IN-ACTIVE");

	private String label;

	Status(String label) {
		this.label = label;
	}

	public static Status toStatus(String status) {
		if(null == status)
			return null;

		switch(status) {
		case "ACTIVE":
			return ACTIVE;
		case "IN-ACTIVE":
			return IN_ACTIVE;
			default:
				return null;
		}
	}
}
