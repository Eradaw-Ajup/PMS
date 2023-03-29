package gov.nist.csd.pm.rap;

import java.io.Serializable;

import gov.nist.csd.pm.common.exceptions.PMResourceException;

public enum AssetType implements Serializable {
	FILE("FILE"), TEXT("TEXT");

	private String label;

	AssetType(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static AssetType toType(String type) throws PMResourceException {
		if (type == null) {
			return null;
		}

		switch (type.toUpperCase()) {
		case "FILE":
			return AssetType.FILE;
		case "TEXT":
			return AssetType.TEXT;
		default:
			throw new PMResourceException("Invalid type!");
		}
	}
}
