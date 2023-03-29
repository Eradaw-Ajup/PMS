package gov.nist.csd.pm.pap.authority;

public enum AuthorityType {
	DEFAULT("DEFAULT"), CUSTOM("CUSTOM");

	private String label;

	AuthorityType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static AuthorityType toAuthorityType(String type) {
		if (null == type)
			return null;

		switch (type) {
		case "DEFAULT":
			return DEFAULT;
		case "CUSTOM":
			return CUSTOM;
		default:
			return null;
		}
	}
}
