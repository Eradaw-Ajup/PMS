-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.12-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             10.3.0.5771
-- --------------------------------------------------------

-- Dumping database structure for policydb_core
DROP DATABASE IF EXISTS `policy_machine`;
CREATE DATABASE IF NOT EXISTS `policy_machine`;
USE `policy_machine`;

-- Dumping structure for table policy_machine.policy_type
CREATE TABLE IF NOT EXISTS `policy_type` (
	`policy_type_id` int(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(50) DEFAULT NULL,
	`description` varchar(300) DEFAULT NULL,
	`created_at` datetime,
	PRIMARY KEY (`policy_type_id`),
	KEY `idx_policy_type_description` (`description`),
	KEY `idx_policy_type_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information to types of policies that we support';

-- Dumping structure for table policy_machine.policies
CREATE TABLE IF NOT EXISTS `policies` (
	`policy_id` int(11) NOT NULL AUTO_INCREMENT,
	`policy_type_id` int(11) NOT NULL,
	`name` varchar(300) DEFAULT NULL,
	`description` varchar(300) DEFAULT NULL,
	`policy_json` json DEFAULT NULL,
	`crypto_json` json DEFAULT NULL,
	`access_token_gen_json` json DEFAULT NULL,
	`encryption_key_info_json` json DEFAULT NULL,
	 policy_status varchar(100) DEFAULT NULL,
	`policy_owner` varchar(500) DEFAULT NULL,
	`created_at` datetime DEFAULT NULL,
	`prev_policy` int(11),
	`next_policy` int(11),
	PRIMARY KEY (`policy_id`),
	CONSTRAINT `fk_policy_type_id` FOREIGN KEY (`policy_type_id`) REFERENCES `policy_type` (`policy_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information to represent graph as policy with secure and access asset';

-- Dumping structure for table policy_machine.node_type
CREATE TABLE IF NOT EXISTS `node_type` (
  `node_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`node_type_id`),
  KEY `idx_node_type_description` (`description`),
  KEY `idx_node_type_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains node types in NGAC graph';

CREATE TABLE IF NOT EXISTS `node` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT,
  `node_type_id` int(11) NOT NULL,
  `policy_id` int(11),
  `name` varchar(300) DEFAULT NULL,
  `node_property` json DEFAULT NULL,
  `is_node_active` boolean,
  `node_status` varchar(200) DEFAULT NULL,
  `authority_id` int(11),
  `attribute_id` int(11),
  `owner` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  CONSTRAINT `fk_node_type_id` FOREIGN KEY (`node_type_id`) REFERENCES `node_type` (`node_type_id`),
  CONSTRAINT `fk_policy_id` FOREIGN KEY (`policy_id`) REFERENCES `policies` (`policy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains node in NGAC graph';

-- Dumping structure for table policy_machine.assignment
CREATE TABLE IF NOT EXISTS `assignment` (
  `assignment_id` int(11) NOT NULL AUTO_INCREMENT,
  `policy_id` int(11) NOT NULL,
  `start_node_id` int(11) NOT NULL,
  `end_node_id` int(11) NOT NULL,
  `is_assignment_active` boolean,
  `assignment_status` varchar(100) DEFAULT NULL,
  `owner` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`assignment_id`),
  CONSTRAINT `fk_assignment_policy_id` FOREIGN KEY (`policy_id`) REFERENCES `policies` (`policy_id`),
  CONSTRAINT `fk_assignment_end_node_id` FOREIGN KEY (`end_node_id`) REFERENCES `node` (`node_id`),
  CONSTRAINT `fk_assignment_start_node_id` FOREIGN KEY (`start_node_id`) REFERENCES `node` (`node_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains relationships between nodes in NGAC graph';

-- Dumping structure for table policy_machine.association
CREATE TABLE IF NOT EXISTS `association` (
  `association_id` int(11) NOT NULL AUTO_INCREMENT,
  `policy_id` int(11) NOT NULL,
  `start_node_id` int(11) NOT NULL,
  `end_node_id` int(11) NOT NULL,
  `operation_set` json NOT NULL,
  `is_association_active` boolean,
  `association_status` varchar(100) DEFAULT NULL,
  `owner` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`association_id`),
  CONSTRAINT `fk_association_policy_id` FOREIGN KEY (`policy_id`) REFERENCES `policies` (`policy_id`),
  CONSTRAINT `fk_association_end_node_id` FOREIGN KEY (`end_node_id`) REFERENCES `node` (`node_id`),
  CONSTRAINT `fk_association_start_node_id` FOREIGN KEY (`start_node_id`) REFERENCES `node` (`node_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains special relationships between nodes with operations';

-- Dumping structure for table policy_machine.asset_type
CREATE TABLE IF NOT EXISTS `asset_type` (
  `asset_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`asset_type_id`),
  KEY `idx_asset_type_description` (`description`),
  KEY `idx_asset_type_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains types of assets';

-- Dumping structure for table policy_machine.asset
CREATE TABLE IF NOT EXISTS `asset` (
  `asset_id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_type_id` int(11) NOT NULL,
  `name` varchar(300) DEFAULT NULL,
  `asset_link` varchar(1000) DEFAULT NULL,
  `asset_owner` varchar(500) DEFAULT NULL,
  `asset_status` varchar(100),
  `is_active` boolean,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`asset_id`),
  CONSTRAINT `fk_asset_type_id` FOREIGN KEY (`asset_type_id`) REFERENCES `asset_type` (`asset_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains assets information';

-- Dumping structure for table policy_machine.consents
CREATE TABLE IF NOT EXISTS `consents` (
  `consent_id` int(11) NOT NULL AUTO_INCREMENT,
  `consent_creator` varchar(500) NOT NULL,
  `consent_on_id` int(11) NOT NULL,
  `consent_approver` json DEFAULT NULL,
  `consent_status` boolean,
  PRIMARY KEY (`consent_id`),
  CONSTRAINT `fk_consent_on_id` FOREIGN KEY (`consent_on_id`) REFERENCES `asset` (`asset_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information about consents on assets';

-- Dumping structure for table policy_machine.asset_attributes
CREATE TABLE IF NOT EXISTS `asset_attribute` (
  `asset_attribute_id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_attribute_info` json DEFAULT NULL,
  `asset_attribute_status` boolean,
  PRIMARY KEY (`asset_attribute_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information about attributes related to asset';

-- Dumping structure for table policy_machine.attempt_type
CREATE TABLE IF NOT EXISTS `attempt_type` (
  `attempt_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `created_at` datetime,
  PRIMARY KEY (`attempt_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information types of attempts on critical operation-set';

-- Dumping structure for table policy_machine.attempt
CREATE TABLE IF NOT EXISTS `attempt` (
  `attempt_id` int(11) NOT NULL AUTO_INCREMENT,
  `attempt_type_id` int(11) NOT NULL,
  `attempt_status` varchar(50),
  `created_by` varchar(300),
  `created_at` datetime,
  `updated_at` datetime,
  PRIMARY KEY (`attempt_id`),
  CONSTRAINT `fk_attempt_type_id` FOREIGN KEY (`attempt_type_id`) REFERENCES `attempt_type` (`attempt_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information of attempts on critical operation-set';

-- Dumping structure for table policy_machine.rel_asset_to_node
CREATE TABLE IF NOT EXISTS `rel_asset_to_node` (
  `rel_asset_to_node_id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_id` int(11) NOT NULL,
  `node_id` int(11) NOT NULL,
  PRIMARY KEY (`rel_asset_to_node_id`),
  CONSTRAINT `fk_rel_asset_id1` FOREIGN KEY (`asset_id`) REFERENCES `asset` (`asset_id`),
  CONSTRAINT `fk_rel_asset_node_id` FOREIGN KEY (`node_id`) REFERENCES `node` (`node_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information of relation of asset with node';

-- Dumping structure for table policy_machine.rel_asset_attribute_to_node
CREATE TABLE IF NOT EXISTS `rel_asset_attribute_to_node` (
  `rel_asset_attribute_to_node_id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_attribute_id` int(11) NOT NULL,
  `asset_id` int(11) NOT NULL,
  PRIMARY KEY (`rel_asset_attribute_to_node_id`),
  CONSTRAINT `fk_rel_asset_attribute_id` FOREIGN KEY (`asset_attribute_id`) REFERENCES `asset_attribute` (`asset_attribute_id`),
  CONSTRAINT `fk_rel_attribute_asset_id1` FOREIGN KEY (`asset_id`) REFERENCES `asset` (`asset_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information of relation of asset_attribute with node';

-- Dumping structure for table policy_machine.rel_asset_to_asset_attribute
CREATE TABLE IF NOT EXISTS `rel_asset_to_asset_attribute` (
  `rel_asset_to_asset_attribute_id` int(11) NOT NULL AUTO_INCREMENT,
  `asset_id` int(11) NOT NULL,
  `asset_attribute_id` int(11) NOT NULL,
  PRIMARY KEY (`rel_asset_to_asset_attribute_id`),
  CONSTRAINT `fk_rel_asset_id2` FOREIGN KEY (`asset_id`) REFERENCES `asset` (`asset_id`),
  CONSTRAINT `fk_rel_attribute_asset_id2` FOREIGN KEY (`asset_attribute_id`) REFERENCES `asset_attribute` (`asset_attribute_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information of relation of asset with asset_attribute';

-- Dumping structure for table policy_machine.rel_policy_to_asset
CREATE TABLE IF NOT EXISTS `rel_policy_to_asset` (
  `rel_policy_to_asset_id` int(11) NOT NULL AUTO_INCREMENT,
  `policy_id` int(11) NOT NULL,
  `asset_id` int(11) NOT NULL,
  PRIMARY KEY (`rel_policy_to_asset_id`),
  CONSTRAINT `fk_rel_policy_id1` FOREIGN KEY (`policy_id`) REFERENCES `policies` (policy_id),
  CONSTRAINT `fk_rel_asset_id3` FOREIGN KEY (`asset_id`) REFERENCES `asset` (`asset_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information of relation of policy with asset';

-- Dumping structure for table policy_machine.rel_policy_to_attempt
CREATE TABLE IF NOT EXISTS `rel_policy_to_attempt` (
  `rel_policy_to_attempt_id` int(11) NOT NULL AUTO_INCREMENT,
  `policy_id` int(11) NOT NULL,
  `attempt_id` int(11) NOT NULL,
  PRIMARY KEY (`rel_policy_to_attempt_id`),
  CONSTRAINT `fk_rel_policy_id2` FOREIGN KEY (`policy_id`) REFERENCES `policies` (policy_id),
  CONSTRAINT `fk_rel_attempt_id` FOREIGN KEY (`attempt_id`) REFERENCES `attempt` (`attempt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information of relation of policy with its attempt';


-- Dumping structure for table pm_authorities.authority_type
CREATE TABLE IF NOT EXISTS `authority_type` (
	`authority_type_id` int(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(100),
	`created_at` datetime DEFAULT now(),
	PRIMARY KEY (`authority_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information of types of authorities in system';

-- Dumping structure for table pm_authorities.authority
CREATE TABLE IF NOT EXISTS `authority` (
	`authority_id` int(11) NOT NULL AUTO_INCREMENT,
	`authority_type_id` int(11) NOT NULL,
	`name` varchar(100),
	`api_endpoint`varchar (300),
	`secret` varchar(300),
	`is_active` boolean,
	`created_at` datetime DEFAULT now(),
	PRIMARY KEY (`authority_id`),
	CONSTRAINT `fk_authority_type_id` FOREIGN KEY (`authority_type_id`) REFERENCES `authority_type` (`authority_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information of types of authorities in system';

-- Dumping structure for table pm_authorities.authority_type
CREATE TABLE IF NOT EXISTS `authority_type` (
	`authority_type_id` int(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(100),
	`created_at` datetime DEFAULT now(),
	PRIMARY KEY (`authority_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='This table contains information of types of authorities in system';

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