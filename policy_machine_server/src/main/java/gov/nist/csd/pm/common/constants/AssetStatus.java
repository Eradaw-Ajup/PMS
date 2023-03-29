package gov.nist.csd.pm.common.constants;

public enum AssetStatus {
	SECURED("secured"), UNSECURED("unsecured");

	private String label;

	AssetStatus(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static AssetStatus toAssetStatus(String status) {
		if (null == status)
			return null;

		switch (status) {
		case "secured":
			return SECURED;
		case "unsecured":
			return UNSECURED;
		default:
			return null;
		}
	}
}
