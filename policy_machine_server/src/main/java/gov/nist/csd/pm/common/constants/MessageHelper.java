package gov.nist.csd.pm.common.constants;

public class MessageHelper {
	public static final String ATTEMPT_NOT_FOUND = "Attempt of type %s not found";
	public static final String ATTEMPT_WITH_ID_NOT_FOUND = "Attempt with %d not found";
	public static final String ATTEMPT_STATUS_DOES_NOT_EXISTS = "Attempt status does not exists";
	public static final String ATTEMPT_FAILURE = "Failed to create attempt";
	public static final String ATTEMPT_EXPIRED = "Attempt %d is expired!";

	public static final String POLICY_NAME_NOT_FOUND = "Policy name cannot be null or empty";
	public static final String POLICY_DESCRIPTION_NOT_FOUND = "Policy description cannot be null";
	public static final String POLICY_TYPE_NOT_FOUND = "Policy of type %s not found";
	public static final String POLICY_ID_NOT_FOUND = "Policy of %d not found";
	public static final String INVALID_POLICY_AND_ATTEMPT = "Invalid policy id %d and attempt id %d";
	public static final String POLICY_AUTOMATION_FAILED = "Failed to automate %d policy";

	public static final String NODE_DOES_NOT_EXIST = "node %s does not exist";

	public static final String UNAUTHORIZED_OWNER = "Unauthorized owner";

	// MESSAGES
	public static final String ENTITY = "entity";
	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";
	public static final String SOMETHING_WENT_WRONG = "Something went wrong!";

	// common
	public static final String NAME_CANNOT_BE_NULL = "Name cannot be null";
	public static final String DESCRIPTION_CANNOT_BE_NULL = "Description cannot be null";
	public static final String TYPE_NOT_FOUND = "Type not found";

	// authority
	public static final String AUTHORITY_DOES_NOT_EXIST = "Authority does not exist";
	public static final String AUTHORITY_TYPE_DOES_NOT_EXISTS = "Authority type does not exist";
	public static final String INVALID_AUTHORITY_API_END = "Invalid authority api endpoint";

	// security
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String APPLICATION_JSON = "application/json";
}
