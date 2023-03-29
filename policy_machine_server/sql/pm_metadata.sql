
INSERT INTO `node_type` (`name`, `description`) VALUES
	('OA', 'Object Attribute'),
	('UA', 'User Attribute'),
	('U', 'User'),
	('O', 'Object'),
	('PC', 'Policy Class'),
	('OS', 'Operation Set');

INSERT INTO `asset_type` (`name`, `description`) VALUES
	('FILE', 'Document or File type asset'),
	('TEXT', 'Sensitive information or Text type info');
	
INSERT INTO `attempt_type` (`name`, `created_at`) VALUES
	('CREATE_POLICY', now()),
	('ADD_ATTRIBUTE', now()),
	('REVOKE_ATTRIBUTE', now()),
	('INCREASE_THRESHOLD', now()),
	('ACCESS_ASSET', now()),
	('SECURE_ASSET', now()),
	('GET_ACCESS_TOKEN', now()),
	('CREATE_AUTHORITY', now());
	
INSERT INTO `policy_type` (`name`, `description`, `created_at`) VALUES
	('NONE', 'This type will support policies without any authority', now()),
	('MULTI', 'This type will support policies with multiple authority', now());
	
INSERT INTO `authority_type` (`name`) VALUES
	('DEFAULT'),
	('CUSTOM');