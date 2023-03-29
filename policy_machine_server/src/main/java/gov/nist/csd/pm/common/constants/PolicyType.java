package gov.nist.csd.pm.common.constants;

public enum PolicyType {
	NONE("NONE"), MULTI("MULTI");

	private String label;

	PolicyType(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static PolicyType toPolicyType(String type) {
		if (null == type)
			return null;

		switch (type) {
		case "NONE":
			return NONE;
		case "MULTI":
			return MULTI;
		default:
			return null;
		}
	}
}
