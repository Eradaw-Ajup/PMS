package gov.nist.csd.pm.pip.db.mysql;

/**
 * MySQL helper methods
 */
public class MySQLHelper {

	private MySQLHelper() {
	}

	// attempt
	public static final String INSERT_ATTEMPT = "INSERT INTO attempt(attempt_type_id, attempt_status, created_by, created_at) VALUES (?,?,?,?)";
	public static final String UPDATE_ATTEMPT = "UPDATE attempt SET attempt_status = ?, updated_at = ? WHERE attempt_id = ? AND created_by = ?";
	public static final String SELECT_ATTEMPT = "SELECT * FROM attempt WHERE attempt_id = ?";

	// attempt_type
	public static final String SELECT_ATTEMPT_TYPES = "SELECT attempt_type_id, name FROM attempt_type";

	// nodeType
	public static final String SELECT_NODE_TYPE_ID_AND_NAME_FROM_NODE_TYPE = "SELECT node_type_id, name FROM node_type";

	// sdk_setup_information
	public static final String SELECT_SDK_SETUP_INFORMAION = "SELECT * FROM sdk_setup_information";
	public static final String INSERT_SDK_SETUP_INFORMATION = "INSERT INTO sdk_setup_information (sdk_setup_info) VALUES (?)";

	// policy
	public static final String SELECT_POLICY_TYPE_ID_AND_TYPE = "SELECT policy_type_id, name FROM policy_type";
	public static final String INSERT_POLICIES = "INSERT INTO policies (policy_type_id, policy_status, policy_owner, created_at, prev_policy, next_policy) VALUES (?, ?, ?, ?, ?, ?)";
	public static final String INSERT_POLICY_TO_ATTEMPT_RELATION = "INSERT INTO rel_policy_to_attempt (policy_id, attempt_id) VALUES (?, ?)";
	public static final String SELECT_POLICIES = "SELECT policy_id, policy_type_id, name, description, policy_json, crypto_json, policy_status, created_at, policy_owner FROM policies";
	public static final String SELECT_POLICY = "SELECT policy_type_id, name, description, policy_json, crypto_json, access_token_gen_json, encryption_key_info_json, policy_status, created_at FROM policies WHERE policy_id = ? AND policy_owner = ?";
	public static final String IS_POLICY_AND_ATTEMPT_VALID = "SELECT rel_policy_to_attempt_id FROM rel_policy_to_attempt WHERE policy_id = ? AND attempt_id = ?";
	public static final String UPDATE_POLICY = "UPDATE policies SET name = ?, description = ?, policy_json = ?, crypto_json = ?, policy_status = ? WHERE policy_id = ? AND policy_owner = ?";
	public static final String INSERT_CRYPTO_JSON = "UPDATE policies SET crypto_json = ? WHERE policy_id = ? AND policy_owner = ? AND policy_status = ?";
	public static final String GET_CRYPTO_JSON = "SELECT crypto_json FROM policies WHERE policy_id = ? AND policy_status = ?";
	public static final String SELECT_POLICY_BY_ID = "SELECT policy_type_id, name, description, policy_json, crypto_json, access_token_gen_json, encryption_key_info_json, policy_status, created_at, policy_owner FROM policies WHERE policy_id = ?";

	// node
	public static final String INSERT_NODE = "INSERT INTO node(node_type_id, policy_id, name, node_property, is_node_active, node_status, owner, authority_id, attribute_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_NODE = "UPDATE node SET name = ?, node_status = ? WHERE node_id = ? AND is_node_active = ? AND owner = ?";
	public static final String SELECT_NODES = "SELECT node_id, node_type_id, policy_id, name, node_property, node_status, owner, authority_id, attribute_id FROM node WHERE is_node_active=?";
	public static final String DELETE_NODE = "UPDATE node SET is_node_active = ? WHERE node_id = ? AND owner = ?";

	// assignment
	public static final String INSERT_ASSIGNMENT = "INSERT INTO assignment(policy_id, start_node_id, end_node_id, is_assignment_active, assignment_status, owner) VALUES (?, ?, ?, ?, ?, ?)";
	public static final String SELECT_ASSIGNMENTS = "SELECT start_node_id, end_node_id, assignment_status, policy_id FROM assignment WHERE is_assignment_active = ?";
	public static final String UPDATE_ASSIGNMENT = "UPDATE assignment SET assignment_status = ? WHERE policy_id = ? AND start_node_id = ? AND end_node_id = ? AND owner = ? AND is_assignment_active = ?";
	public static final String DELETE_ASSIGNMENT = "UPDATE assignment SET is_assignment_active = ? WHERE start_node_id = ? AND end_node_id = ? AND policy_id = ? AND assignment_status = ? AND owner = ?";

	// association
	public static final String INSERT_ASSOCIATION = "INSERT into association(policy_id, start_node_id, end_node_id, operation_set, is_association_active, association_status) VALUES (?, ?, ?, ?, ?, ?)";
	public static final String SELECT_ASSOCIATIONS = "SELECT start_node_id, end_node_id, operation_set, association_status, policy_id FROM association WHERE is_association_active = ?";
	public static final String UPDATE_ASSOCIATION = "UPDATE association SET operation_set =?, association_status = ? WHERE start_node_id= ? AND end_node_id = ? AND policy_id = ?";

	// asset_type
	public static final String SELECT_ALL_ASSET_TYPE = "SELECT * FROM asset_type";

	// asset
	public static final String INSERT_ASSET = "INSERT INTO asset(asset_type_id, name, asset_link, asset_owner, asset_status, created_at, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
	public static final String ASSET_EXISTS = "SELECT asset_id FROM asset WHERE name = ? AND asset_owner = ? AND is_active = ?";
	public static final String ASSET_EXISTS_BY_ID = "SELECT asset_id FROM asset WHERE asset_id = ? AND is_active = ? AND asset_owner = ?";
	public static final String UPDATE_ASSET_BY_ID = "UPDATE asset SET name = ?, asset_status = ?, asset_link = ? WHERE asset_id = ? AND asset_owner = ? AND is_active = ?";
	public static final String DELETE_ASSET_BY_ID = "UPDATE asset SET is_active = ? WHERE asset_id = ? AND asset_owner = ?";
	public static final String GET_ASSET_BY_ID = "SELECT asset_type_id, name, asset_link, created_at, asset_status FROM asset WHERE asset_id = ? AND asset_owner = ? AND is_active = ?";
	public static final String GET_ASSETS_BY_OWNER = "SELECT asset_id, asset_type_id, name, asset_link, created_at, asset_status FROM asset WHERE asset_owner = ? AND is_active = ?";
	public static final String GET_ASSETS = "SELECT asset_id, asset_type_id, name, asset_link, created_at, asset_status, asset_owner FROM asset WHERE is_active = ?";
	public static final String LINK_ASSET_WITH_POLICY = "INSERT INTO rel_policy_to_asset (policy_id, asset_id) VALUES (?, ?)";

	// authority
	public static final String INSERT_AUTHORITY = "INSERT INTO authority (authority_type_id, name, api_endpoint, secret, is_active) VALUES (?, ?, ?, ?, ?)";
	public static final String UPDATE_AUTHORITY = "UPDATE authority SET name = ?, api_endpoint = ? WHERE authority_id = ? AND is_active = ?";
	public static final String DELETE_AUTHORITY = "UPDATE authority SET is_active = ? WHERE authority_id = ?";
	public static final String SELECT_AUTHORITY = "SELECT authority_type_id, name, api_endpoint, secret, created_at FROM authority WHERE authority_id = ? AND is_active = ?";
	public static final String AUTHORITY_EXISTS = "SELECT name FROM authority WHERE authority_id = ? AND is_active = ?";
	public static final String SELECT_AUTHORITIES = "SELECT authority_id, authority_type_id, name, api_endpoint, secret, created_at FROM authority WHERE is_active = ?";

	// authority_type
	public static final String SELECT_AUTHORITY_TYPES = "SELECT authority_type_id, name FROM authority_type";

	public static final String SELECT_NODE_ID_BY_NAME = "SELECT node_id from node where name =? ";

	public static final String SELECT_NODE_ID_NAME_FROM_NODE = "select count(*) AS total from node where name = ?";
	public static final String SELECT_NODE_ID_FROM_NODE = "select count(*) AS total from node where node_id = ?";
	public static final String SELECT_ALL_FROM_NAME = "SELECT node_id, node_name, node_type_id, node_property from node where name = ?";
	public static final String SELECT_ALL_FROM_NODE_ID = "SELECT name, node_type_id, node_property from node where node_id = ? && status=?";
	public static final String SELECT_NON_STAGE_OPERATION = "SELECT operation_set from association where start_node_id=? AND end_node_id=? ";

	public static final String SELECT_POLICY_ID_FROM_POLICIES = "SELECT policy_id FROM policies WHERE name = ?";
	public static final String SELECT_ALL_FROM_POLICIES = "SELECT * from policies";
	public static final String GET_ACCESS_JSON_FROM_POLICIES = "SELECT key_gen_json FROM policies WHERE policy_id = ? and policy_status = ?";
	public static final String UPDATE_ACCESS_JSON_FROM_POLICIES_WITH_POLICY = "UPDATE policies SET key_gen_json = ? WHERE policy_id = ? and policy_status = ?";
	public static final String UPDATE_EPHEMERAL_JSON_FROM_POLICIES_WITH_POLICY = "UPDATE policies SET ephemeral_json = ? WHERE policy_id = ? and policy_status = ?";
	public static final String GET_EPHEMERAL_JSON_FROM_POLICIES = "SELECT ephemeral_json FROM policies WHERE policy_id = ? and policy_status = ?";

	public static final String SELECT_POLICY_FROM_POLICIES_BY_OWNER = "SELECT policy_id, name, description, created_at from policies where policy_status = ? && owner = ?";
	public static final String SELECT_POLICY_JSON_FROM_POLICIES_BY_OWNER = "SELECT business_json from policies where owner = ?";
	public static final String SELECT_BUSINESS_JSON_FROM_POLICIES_BY_POLICY_ID_AND_STATUS = "SELECT business_json FROM policies WHERE policy_id = ? AND policy_status = ?";
	public static final String SELECT_COUNT_FROM_POLICIES_BY_OWNER = "SELECT count(*) AS total FROM policies WHERE owner = ?";
	public static final String SELECT_POLICY_STATUS_FROM_POLICIES = "select business_json from policies where policy_id = ? && policy_status= ?";

	public static final String SELECT_POLICY_JSON_FROM_POLICIES = "SELECT business_json FROM policies WHERE policy_id = ?";
	public static final String INSERT_IMPORT_POLICY = "INSERT INTO policies(name, description, business_json, policy_status, created_at, owner) VALUES(?,?,?,?,?,?)";

	public static final String INSERT_POLICY = "INSERT INTO policies(name, description, business_json, translation_json, policy_status, created_at, owner) VALUES(?,?,?,?,?,?,?)";
	public static final String SELECT_POLICY_ID_NAME_FROM_POLICIES = "select count(*) AS total from policies where policy_id = ?";

	public static final String SELECT_NODE_ID_BY_NAME_STAGE = "SELECT stage_node_id from stage_node where stage_node_name =? ";
	public static final String INSERT_STAGE_NODE = "INSERT INTO stage_node(node_type_id, stage_node_name, stage_node_property) VALUES(?,?,?)";
	public static final String UPDATE_STAGE_NODE = "UPDATE stage_node SET stage_node_name = ?, stage_node_property = ? WHERE stage_node_id = ?";
	public static final String DELETE_STAGE_NODE = "DELETE from stage_node where stage_node_id = ?";
	public static final String SELECT_NODE_ID_NAME_FROM_STAGE_NODE = "select count(*) AS total from stage_node where stage_node_name = ?";
	public static final String SELECT_NODE_ID_FROM_STAGE_NODE = "select count(*) AS total from stage_node where stage_node_id = ?";
	public static final String SELECT_ALL_FROM_STAGE_NODE = "SELECT stage_node_id, stage_node_name, node_type_id, stage_node_property from stage_node";
	public static final String SELECT_ALL_FROM_NAME_STAGE_NODE = "SELECT stage_node_id, stage_node_name, node_type_id, stage_node_property from stage_node where stage_node_name = ?";
	public static final String SELECT_ALL_FROM_NODE_ID_STAGE_NODE = "SELECT stage_node_name, node_type_id, stage_node_property from stage_node where stage_node_id = ?";

	public static final String SELECT_STAGE_START_NODE_ID = "SELECT stage_start_node_id from stage_assignment where stage_end_node_id=";
	public static final String SELECT_STAGE_END_NODE_ID = "SELECT stage_end_node_id from stage_assignment where stage_start_node_id=";
	public static final String SELECT_STAGE_ASSIGNMENT = "SELECT stage_assignment_id FROM stage_assignment";
	public static final String SELECT_STAGE_ASSIGNMENTS = "SELECT stage_start_node_id, stage_end_node_id FROM stage_assignment";
	public static final String SELECT_STAGE_ASSIGNMENT_ID = "SELECT stage_assignment_id FROM stage_assignment where stage_start_node_id=? AND stage_end_node_id = ?";
	public static final String INSERT_STAGE_ASSIGNMENT = "INSERT into stage_assignment( stage_start_node_id, stage_end_node_id) VALUES (?, ?)";
	public static final String DELETE_STAGE_ASSIGNMENT = "DELETE from stage_assignment where stage_start_node_id=? AND stage_end_node_id = ?";

	public static final String SELECT_START_NODE_ID = "SELECT start_node_id from assignment where end_node_id=";
	public static final String SELECT_END_NODE_ID = "SELECT end_node_id from assignment where start_node_id=";
	public static final String SELECT_ASSIGNMENT = "SELECT assignment_id FROM assignment";

	public static final String SELECT_ASSIGNMENT_ID = "SELECT assignment_id FROM assignment where start_node_id=? AND end_node_id = ?";

	public static final String SELECT_STAGE_ASSOCIATIONS = "SELECT stage_start_node_id, stage_end_node_id, stage_operation_set FROM stage_association";
	public static final String SELECT_STAGE_ASSOCIATION_ID = "Select stage_association_id from stage_association where stage_start_node_id=? AND stage_end_node_id = ?";
	public static final String INSERT_STAGE_ASSOCIATION = "INSERT into stage_association( stage_start_node_id, stage_end_node_id, stage_operation_set) VALUES (?, ?, ?)";
	public static final String UPDATE_STAGE_ASSOCIATION = "UPDATE stage_association SET stage_operation_set =? WHERE stage_start_node_id= ? AND stage_end_node_id = ?";
	public static final String DELETE_STAGE_ASSOCIATION = "DELETE from stage_association where stage_start_node_id=? AND stage_end_node_id = ?";
	public static final String SELECT_STAGE_END_NODE_ID_OPERATION = "SELECT stage_end_node_id, stage_operation_set from stage_association where stage_start_node_id=";
	public static final String SELECT_STAGE_START_NODE_ID_OPERATION = "SELECT stage_start_node_id, stage_operation_set from stage_association where stage_end_node_id=";
	public static final String SELECT_STAGE_OPERATION = "SELECT stage_operation_set from stage_association where stage_start_node_id=? AND stage_end_node_id=? ";

	public static final String SELECT_ASSOCIATION_ID = "Select association_id from association where start_node_id=? AND end_node_id = ? AND status = ?";
	public static final String DELETE_ASSOCIATION = "UPDATE association SET status= ? where start_node_id=? AND end_node_id = ? AND policy_id = ?";
	public static final String SELECT_END_NODE_ID_OPERATION = "SELECT end_node_id, operation_set from association where start_node_id=";
	public static final String SELECT_START_NODE_ID_OPERATION = "SELECT start_node_id, operation_set from association where end_node_id=";

	public static final String INSERT_PROHIBITION = "INSERT INTO deny(deny_name, deny_type_id, subject_name, user_attribute_id, process_id, is_intersection, deny_operations) VALUES(?,?,?,?,?,?,?)";
	public static final String INSERT_CONTAINERS = "INSERT INTO deny_obj_attribute(deny_id, object_attribute_id, object_complement) VALUES(?,?,?)";
	public static final String DELETE_PROHIBITION_CONTAINER = "DELETE from deny_obj_attribute where deny_id = ?";
	public static final String DELETE_PROHIBITION = "DELETE from deny where deny_name = ?";
	public static final String SELECT_PROHIBITION_FROM_NAME = "SELECT deny_id, deny_name, subject_name, is_intersection, deny_operations from deny where deny_name =? ";
	public static final String SELECT_ALL_PROHIBITION = "SELECT deny_id, deny_name, subject_name, is_intersection, deny_operations from deny";
	public static final String SELECT_EXISTS_ID_NODE_ID = "SELECT node_id from node where name =?";
	public static final String SELECT_CONTAINER_DENY_ID = "SELECT object_attribute_id, object_complement from deny_obj_attribute where deny_id=? ";
	public static final String SELECT_CONTAINER_DENY_ID_SIMPLE = "SELECT object_attribute_id, object_complement from deny_obj_attribute where deny_id=";
	public static final String SELECT_ALL_CONTAINERS = "SELECT deny_id, object_attribute_id, object_complement from deny_obj_attribute";
	public static final String UPDATE_PROHIBITION = "UPDATE deny SET deny_name=?, subject_name=?, user_attribute_id=?, process_id=?, is_intersection =?, deny_operations=?, deny_type_id = ? WHERE deny_name=?";

	public static final String PUBLISH_POLICY = "INSERT INTO policies (name, description, policy_json, policy_status, created_at) VALUES (?, ?, ?, ?, ?)";
	public static final String UPDATE_POLICY_CRYPTO_JSON = "UPDATE policies SET access_json = ? WHERE policy_id = ?";

	public static final String SELECT_CONSENTS = "SELECT consent_id, creator_id, consentor, prohibitions FROM consents WHERE conent_on_id = ? and approved = ?";
	public static final String SELECT_CONSENT = "SELECT creator_id, consent_on_id, consentor, prohibitions FROM consents WHERE consent_id = ? and approved = ?";
	public static final String UPDATE_CONSENT = "UPDATE consents SET consentor = ?, prohibitions = ?, approved = ? WHERE consent_id = ?";
	public static final String INSERT_CONSENT = "INSERT INTO consents (creator_id, consent_on_id, approved) VALUES (?, ?, ?)";

	public static final String GET_TRANSLATION_PC_BY_POLICY_ID_AND_STATUS = "SELECT translation_json FROM policies WHERE policy_id = ? AND policy_status = ?";
	public static final String GET_CRYPTO_PC_BY_POLICY_ID_AND_OWNER = "SELECT access_json FROM policies WHERE policy_id = ? AND owner = ? AND policy_status = ?";

	public static final String VIEW_ATTRIBUTES_IN_GROUP = "SELECT policy_attr_id, policy_attr_info FROM policy_attr WHERE policy_attr_id in (?) and policy_attr_status = ?";
	public static final String VIEW_ATTRIBUTES_ID_IN_ASSET = "SELECT policy_attr_id FROM policy_asset_attr_rel WHERE policy_asset_id = ?";

	public static final String DELETE_ATTRIBUTE = "DELETE FROM policy_attr WHERE policy_attr_id = ?";

	public static final String IS_ASSET_EXISTS_BY_ID = "SELECT name FROM policy_asset WHERE policy_asset_id = ?";
	public static final String IS_ASSET_EXISTS_BY_NAME = "SELECT policy_asset_id FROM policy_asset WHERE name = ?";

	public static final String IS_ATTRIBUTE_EXISTS_BY_ID = "SELECT policy_attr_info FROM policy_attr WHERE policy_attr_id = ?";
	public static final String IS_ATTRIBUTE_EXISTS_BY_NAME = "SELECT policy_attr_info, policy_attr_id FROM policy_attr WHERE policy_attr_status = ?";
	public static final String INSERT_INTO_ATTRIBUTE = "INSERT INTO policy_attr (policy_attr_info, policy_attr_status) VALUES (?,?)";
	public static final String IS_ATTRIBUTE_ALREADY_LINKED_WITH_ASSET = "SELECT policy_asset_attr_rel_id FROM policy_asset_attr_rel WHERE policy_asset_id = ? AND policy_attr_id = ?";

	public static final String INSERT_INTO_POLICY_ASSET_ATTRIBUTE_REL = "INSERT INTO policy_asset_attr_rel (policy_asset_id, policy_attr_id) VALUES (?, ?)";
	public static final String UPDATE_ATTRIBUTE_BY_ID_AND_STATUS = "UPDATE policy_attr SET policy_attr_info = ? WHERE policy_attr_id = ? and policy_attr_status = ?";

	public static final String VIEW_ASSET_BY_OWNER = "SELECT policy_asset_id, policy_asset_type_id, name, policy_asset, policy_asset_status FROM policy_asset WHERE policy_asset_owner = ?";
	public static final String VIEW_ASSET_TYPE_ID_TYPE_NAME = "SELECT policy_asset_type_id, policy_asset_type FROM policy_asset_type";

	public static final String INSERT_INTO_RESOURCE = "INSERT INTO policy_asset (policy_asset_type_id, name, policy_asset, policy_asset_owner, policy_asset_status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_RESOURCE_NAME_RESOURCE_BY_ID = "UPDATE policy_asset SET name = ?, policy_asset = ?  WHERE policy_asset_id = ?";
	public static final String CHECK_OWNER_BY_RESOURCE_ID_AND_OWNER = "SELECT policy_asset_id FROM policy_asset WHERE policy_asset_id = ? and policy_asset_owner = ?";
	public static final String UPDATE_ASSET_WITH_ACCESS_KEY = "UPDATE policy_asset SET asset_access_key = ?  WHERE policy_asset_id = ?";
	public static final String UPDATE_ASSET = "UPDATE policy_asset SET policy_asset = ?  WHERE policy_asset_id = ?";

	public static final String FETCH_RESOURCE_NODE_BY_RESOURCE_ID = "SELECT asset_node_id FROM asset_node_rel WHERE asset_id = ?";
	public static final String FETCH_ATTRIBUTE_NODE_BY_ATTRIBUTE_ID = "SELECT attr_node_id FROM attr_node_rel WHERE asset_attr_id = ?";
	public static final String FETCH_POLICY_ASSET_BY_ASSET_ID = "SELECT policy_asset FROM policy_asset WHERE policy_asset_id = ?";
	public static final String FETCH_ENCRYPTED_ACCESS_KEY_BY_ASSET_ID = "SELECT asset_access_key FROM policy_asset WHERE policy_asset_id = ?";

	public static final String FETCH_RESOURCE_BY_NODE_ID = "SELECT assets_id FROM asset_stage_node_rel WHERE asset_stage_node_id = ?";
	public static final String FETCH_ATTRIBUTE_BY_NODE_ID = "SELECT assets_attr_id FROM attr_stage_node_rel WHERE attr_stage_node_id = ?";
	public static final String UPDATE_ATTRIBUTE_NODE = "UPDATE attr_node_rel SET attr_node_id = ?  WHERE asset_attr_id = ?";

	public static final String INSERT_INTO_ASSET_STAGE_NODE_REL = "INSERT INTO asset_stage_node_rel (assets_id, asset_stage_node_id) VALUES (?, ?)";
	public static final String INSERT_INTO_ATTR_STAGE_NODE_REL = "INSERT INTO attr_stage_node_rel (assets_attr_id, attr_stage_node_id) VALUES (?, ?)";
	public static final String DELETE_ATTR_STAGE_NODE_REL = "DELETE from attr_stage_node_rel where assets_attr_id = ? && attr_stage_node_id = ?";
	public static final String DELETE_ASSET_STAGE_NODE_REL = "DELETE from asset_stage_node_rel where assets_id = ? && asset_stage_node_id = ?";

	public static final String INSERT_INTO_ASSET_NODE_REL = "INSERT INTO asset_node_rel (asset_id, asset_node_id) VALUES (?, ?)";
	public static final String INSERT_INTO_ASSET_ATTR_NODE_REL = "INSERT INTO attr_node_rel (asset_attr_id, attr_node_id) VALUES (?, ?)";

	public static final String DELETE_ASSET_NODE_REL = "DELETE from asset_stage_node_rel where assets_id = ?";
	public static final String DELETE_ATTR_NODE_REL = "DELETE from attr_stage_node_rel where assets_attr_id = ?";
	public static final String DELETE_RESOURCE_ATTR_REL = "DELETE from policy_asset_attr_rel where policy_asset_id = ?";
	public static final String DELETE_RESOURCE_ATTR = "DELETE from policy_attr where policy_attr_id = ?";
	public static final String DELETE_RESOURCE = "DELETE from policy_asset where policy_asset_id = ? && policy_asset_owner = ?";
	public static final String GET_SHARE_LIST = "SELECT key_gen_json FROM policies WHERE policy_id = ? AND policy_status = ?";
	public static final String GET_POLICY_ID_BUSINESS_JSON_OF_ALL_POLICIES = "SELECT policy_id, business_json FROM policies;";
}
