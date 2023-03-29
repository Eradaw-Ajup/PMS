package gov.nist.csd.pm.common.constants;

import java.io.Serializable;

public enum AccessType implements Serializable {
	A("A"), AK("AK");

	private String label;

	AccessType(String label) {
		this.label = label;
	}

	public static AccessType accessType(String type) {
		if (type == null)
			return null;
		switch (type.toUpperCase()) {

		case "A":
			return AccessType.A;
		case "AK":
			return AccessType.AK;
		default:
			return null;
		}
	}
}
