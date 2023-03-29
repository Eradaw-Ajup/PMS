package gov.nist.csd.pm.common.constants;

public enum AttemptType {
	CREATE_POLICY("CREATE_POLICY"), ADD_ATTRIBUTE("ADD_ATTRIBUTE"), REVOKE_ATTRIBUTE("REVOKE_ATTRIBUTE"),
	INCREASE_THRESHOLD("INCREASE_THRESHOLD"), SECURE_ASSET("SECURE_ASSET"), ACCESS_ASSET("ACCESS_ASSET"),
	GET_ACCESS_TOKEN("GET_ACCESS_TOKEN"), CREATE_ASSET("CREATE_ASSET"), CREATE_AUTHORITY("CREATE_AUTHORITY");

	private String label;

	AttemptType(String label) {
		this.label = label;
	}

	public static AttemptType toAttemptType(String attemptType) {
		if (null == attemptType)
			return null;

		switch (attemptType) {
		case "CREATE_POLICY":
			return CREATE_POLICY;
		case "ADD_ATTRIBUTE":
			return ADD_ATTRIBUTE;
		case "REVOKE_ATTRIBUTE":
			return REVOKE_ATTRIBUTE;
		case "INCREASE_THRESHOLD":
			return INCREASE_THRESHOLD;
		case "SECURE_ASSET":
			return SECURE_ASSET;
		case "ACCESS_ASSET":
			return ACCESS_ASSET;
		case "GET_ACCESS_TOKEN":
			return GET_ACCESS_TOKEN;
		case "CREATE_AUTHORITY":
			return CREATE_AUTHORITY;
		default:
			return null;
		}
	}
}
