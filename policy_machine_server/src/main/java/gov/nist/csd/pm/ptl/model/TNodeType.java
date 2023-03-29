package gov.nist.csd.pm.ptl.model;

public enum TNodeType {
	ROOT("ROOT"),
	NON_LEAF("NON_LEAF"),
	LEAF("LEAF");

	private String label;

	TNodeType(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static TNodeType toTNodeType(String type) {
		if(null == type)
			return null;
		switch(type) {
		case "ROOT":
			return TNodeType.ROOT;
		case "NON_LEAF":
			return TNodeType.NON_LEAF;
		case "LEAF":
			return TNodeType.LEAF;
			default:
				return null;
		}
	}
}

