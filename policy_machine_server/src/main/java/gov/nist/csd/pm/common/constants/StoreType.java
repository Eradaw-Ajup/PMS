package gov.nist.csd.pm.common.constants;

import java.io.Serializable;

public enum StoreType implements Serializable {
	NORMAL("NORMAL"),
	STAGE("STAGE");

	private String label;

	StoreType(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	public static StoreType toStoreType(String type) {
		if(null == type)
			return null;
		switch(type.toUpperCase()) {
		case "NORMAL":
			return StoreType.NORMAL;
		case "STAGE":
			return StoreType.STAGE;
		default:
			return null;
		}
	}
}
