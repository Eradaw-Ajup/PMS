package gov.nist.csd.pm.pip.db.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import gov.nist.csd.pm.common.constants.AssetStatus;
import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.common.constants.Operations;
import gov.nist.csd.pm.common.constants.PolicyStatus;
import gov.nist.csd.pm.common.constants.PolicyType;
import gov.nist.csd.pm.common.constants.StoreType;
import gov.nist.csd.pm.common.exceptions.DBException;
import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.common.exceptions.PMDBException;
import gov.nist.csd.pm.common.util.OperationSet;
import gov.nist.csd.pm.pip.db.mysql.HibernateUtil;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pap.attempt.AttemptMachine;
import gov.nist.csd.pm.pap.authority.AuthorityDAO;
import gov.nist.csd.pm.pap.authority.AuthorityType;
import gov.nist.csd.pm.pap.crypto.CryptoMachine;
import gov.nist.csd.pm.pap.graph.GraphMachine;
import gov.nist.csd.pm.pap.model.Attempt;
import gov.nist.csd.pm.pap.model.Authority;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MAssociation;
import gov.nist.csd.pm.pap.model.MNode;
import gov.nist.csd.pm.pap.model.Policy;
import gov.nist.csd.pm.pap.model.Prohibition;
import gov.nist.csd.pm.pap.policy.PolicyMachine;
import gov.nist.csd.pm.pdp.prohibitions.ProhibitionMachine;
import gov.nist.csd.pm.pip.db.DatabaseContext;
import gov.nist.csd.pm.rap.AssetType;
import gov.nist.csd.pm.rap.asset.AssetMachine;
import gov.nist.csd.pm.rap.model.Asset;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;

public class MySQLStore implements GraphMachine, PolicyMachine, AssetMachine, AttemptMachine, CryptoMachine,
		AuthorityDAO, ProhibitionMachine {

	private final MySQLConnection conn;
	@PersistenceContext
    private EntityManager entityManager;

	private static final Map<Long, String> nodeType = new HashMap<>();
	private static final Map<Long, String> attemptType = new HashMap<>();
	private static final Map<Long, String> policyType = new HashMap<>();
	private static final Map<Long, String> assetType = new HashMap<>();
	private static final Map<Long, String> authorityTypes = new HashMap<>();

	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final ObjectReader reader = new ObjectMapper().readerFor(HashMap.class);
	private static final ObjectReader reader2 = new ObjectMapper().readerFor(OperationSet.class);

	// MESSAGES CONSTANTS
	private static final String SOMETHING_WENT_WRONG_IN_ASSOCIATION = "Something went wrong associating the two nodes.";
	private static final String ONLY_NODE = "node ";

	public MySQLStore(DatabaseContext ctx) throws PIPException {
		this.conn = new MySQLConnection(ctx.getHost(), ctx.getPort(), ctx.getUsername(), ctx.getPassword(),
				ctx.getSchema());
		// Getting node types when the database object is getting created
		getNodeType();

		// Getting attempt types when the database object is getting created
		getAttemptType();

		// Getting policy types when the database object is getting created
		getPolicyType();

		// Getting asset types when the database object is getting created
		getAssetType();

		// Getting authority types
		getAuthorityTypes();
	}

	public MySQLStore(MySQLConnection conn) {
		this.conn = conn;
	}

	private long getAuthorityTypeID(String type) {
		for (Map.Entry<Long, String> entry : authorityTypes.entrySet())
			if (entry.getValue().equals(type))
				return entry.getKey();
		return 0;
	}

	private Map<Long, String> getNodeType() throws PIPException {
		try (Connection con = this.conn.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_NODE_TYPE_ID_AND_NAME_FROM_NODE_TYPE)) {

			nodeType.clear();
			while (rs.next()) {
				nodeType.put(rs.getLong("node_type_id"), rs.getString("name"));
			}
			return nodeType;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	private Map<Long, String> getAttemptType() throws PIPException {
		try (Connection con = this.conn.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_ATTEMPT_TYPES)) {

			attemptType.clear();
			while (rs.next()) {
				attemptType.put(rs.getLong("attempt_type_id"), rs.getString("name"));
			}
			return attemptType;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	private Map<Long, String> getPolicyType() throws PIPException {
		try (Connection con = this.conn.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_POLICY_TYPE_ID_AND_TYPE)) {

			policyType.clear();
			while (rs.next()) {
				policyType.put(rs.getLong("policy_type_id"), rs.getString("name"));
			}
			return policyType;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	private Map<Long, String> getAssetType() throws PIPException {
		try (Connection con = this.conn.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_ALL_ASSET_TYPE)) {

			assetType.clear();
			while (rs.next()) {
				assetType.put(rs.getLong("asset_type_id"), rs.getString("name"));
			}
			return assetType;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	public static String toJSON(Map<String, String> map) throws JsonProcessingException {
		return objectMapper.writeValueAsString(map);
	}

	public static String hashSetToJSON(Set<String> set) throws JsonProcessingException {
		return objectMapper.writeValueAsString(set);
	}

	/**
	 * Create a node in the MySQL graph.
	 *
	 * @return the ID that was passed as part of the node parameter.
	 * @throws IllegalArgumentException When the provided node is null.
	 * @throws IllegalArgumentException When the provided node has a null or empty
	 *                                  name.
	 * @throws IllegalArgumentException When the provided node has a null type.
	 * @throws PIPException             When the provided name already exists in the
	 *                                  MySQL graph
	 */
	@Transactional
	@Override
	public MNode createNode(long id, String name, NodeType type, long policyID, Map<String, String> properties,
			StoreType nodeStoreType, String owner, long attributeID, long authorityID) throws PIPException {
		Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    	transaction = session.beginTransaction();
	    	long typeID = 0;
	    	for (Entry<Long, String> set : nodeType.entrySet()) {
				if (set.getValue().equals(type.name()))
					typeID = set.getKey();
			}

			if (0 == typeID)
				throw new SQLException("Node type id not found");

	    	
	    	MNode node = new MNode(0, name, type, properties, true, nodeStoreType.name(), policyID, owner, attributeID, authorityID, typeID);
//	    	Assertions.assertNull(node.getID());

//	    	if (name == "CCCC") throw new Exception("JUST CHECKING");
	    	Long nodeID = (Long) session.save(node);
	    	if (name == "CCCC") throw new Exception("JUST CHECKING");
//	    	Assertions.assertNotNull(node.getID());
	    	transaction.commit();
	    	session.close();
	    	return new MNode(nodeID, name, type, properties, true, nodeStoreType.name(), policyID, owner, attributeID, authorityID);
		}catch (Exception e) {
			e.printStackTrace();
            transaction.rollback(); 
			throw new PIPException("graph");
		}
	
		
//		try (Connection con = this.conn.getConnection();
//				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_NODE,
//						Statement.RETURN_GENERATED_KEYS)) {
//			
//			long typeID = 0;
//			for (Entry<Long, String> set : nodeType.entrySet()) {
//				if (set.getValue().equals(type.name()))
//					typeID = set.getKey();
//			}
//
//			if (0 == typeID)
//				throw new SQLException("Node type id not found");
//
//			// node_type_id, policy_id, name, node_property, is_node_active, node_status,
//			// owner
//			pstmt.setLong(1, typeID);
//			pstmt.setLong(2, policyID);
//			pstmt.setString(3, name);
//			pstmt.setString(4, toJSON(properties));
//			pstmt.setBoolean(5, true);
//			pstmt.setString(6, nodeStoreType.name());
//			pstmt.setString(7, owner);
//			pstmt.setLong(8, authorityID);
//			pstmt.setLong(9, attributeID);
////			System.out.println(pstmt);
//			pstmt.execute();
//			Collection <MNode> l = getNodes();
//			System.out.println("ENTRIES : ");
//			l.forEach(s -> System.out.println(s.getName()));
//			System.out.println();
//			
//			if (name == "ajup") {throw new Exception("NEW MSG");}
//			long nodeID = 0;
//			
//			
//			ResultSet rs = pstmt.getGeneratedKeys();
//			
////			
//			
//			while (rs.next()) {
//				nodeID = rs.getLong(1);
//			}
//
//			return new MNode(nodeID, name, type, properties, true, nodeStoreType.name(), policyID, owner, attributeID,
//					authorityID);
//		} catch (Exception e) {
//			throw new PIPException("graph", e.getMessage());
//		}
//		com.mysql.cj.jdbc.ClientPreparedStatement: INSERT INTO node(node_type_id, policy_id, name, node_property, is_node_active, node_status, owner, authority_id, attribute_id) VALUES (1, 1, 'Puja', '{"key1":"value1","key2":"value2","key3":"value3"}', 1, 'NORMAL', null, 5, 3)
	}

	/**
	 * Update a node with the given node context. Only the name and properties can
	 * be updated. If the name of the context is null, then the node will not be
	 * updated. The properties provided in the context will overwrite any existing
	 * properties. If the properties are null, they will be skipped. However, if the
	 * properties are an empty map, the empty map will be set as the node's new
	 * properties.
	 *
	 * @throws IllegalArgumentException When the provided node does not exist in the
	 *                                  mysql graph
	 * @throws IllegalArgumentException When the provided name is null
	 * @throws PIPException             if there is an error updating the node
	 */
	@Transactional
	@Override
	public void updateNode(long id, String name, Map<String, String> properties, StoreType storeType, String owner,
			boolean isActive) throws PIPException {
		Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    	transaction = session.beginTransaction();
	    	
	    	MNode node =  getNode(id);
	    	System.out.print(id);
	    	node.setNAME(name);
			node.setPROPERTIES( objectMapper.writeValueAsString(properties == null ? new HashMap<>() : properties));
			node.setStoreType(storeType.name());
			node.setOwner(owner);
			node.setNodeActive(isActive);
	    	session.update(node);
	    	transaction.commit();
	    	session.close();
		}catch (Exception e) {
			e.printStackTrace();
            transaction.rollback(); 
			throw new PIPException("graph");
		}
//		try (Connection con = this.conn.getConnection();
//				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.UPDATE_NODE)) {
//
//			// name = ?, node_status = ? WHERE node_id = ? AND is_node_active = ? AND owner
//			// = ?
//			pstmt.setString(1, name);
//			pstmt.setString(2, storeType.name());
//			pstmt.setLong(3, id);
//			pstmt.setBoolean(4, isActive);
//			pstmt.setString(5, owner);
//			pstmt.executeUpdate();
//		} catch (SQLException | PIPException e) {
//			throw new PIPException(e.getMessage());
//		}
	}

	/**
	 * Delete the node with the given name from the graph. No error handled if
	 * nothing happens while deleting a node that does not exists.
	 *
	 * @param name the name of the node to delete.
	 * @throws PIPException If there was an error deleting the node
	 */
	@Override
	public void deleteNode(long nodeID, String owner) throws PMException {
		Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    	transaction = session.beginTransaction();
	    	session.delete(session.get(MNode.class, nodeID));
	    	transaction.commit();
	    	session.close();
		}catch (Exception e) {
			e.printStackTrace();
            transaction.rollback(); 
			throw new PIPException("graph");
		}
//		try (Connection con = this.conn.getConnection();
//				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.DELETE_NODE)) {
//			pstmt.setBoolean(1, false);
//			pstmt.setLong(2, nodeID);
//			pstmt.setString(3, owner);
//			pstmt.executeUpdate();
//		} catch (SQLException | PIPException e) {
//			throw new PIPException("graph", e.getMessage());
//		}
	}

	/**
	 * Check that a node with the given name exists in the graph.
	 *
	 * @param name of the node to check for.
	 * @return true or False if a node with the given name exists or not.
	 * @throws PIPException if there is an error checking if the node exists in the
	 *                      graph.
	 */
	@Override
	public boolean exists(long nodeID) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_NODE_ID_FROM_STAGE_NODE)) {
			pstmt.setLong(1, nodeID);

			ResultSet rs = pstmt.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("total");
			}
			return (count != 0);
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	/**
	 * Check that a node with the given name exists in the graph.
	 *
	 * @param name of the node to check for.
	 * @return true or False if a node with the given name exists or not.
	 * @throws PIPException if there is an error checking if the node exists in the
	 *                      graph.
	 */
	public boolean exists(String name) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_NODE_ID_NAME_FROM_STAGE_NODE)) {
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("total");
			}
			return (count != 0);
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	/**
	 * Get the set of policy classes. The returned set is just the names of each
	 * policy class.
	 *
	 * @return the set of policy class names.
	 * @throws PIPException if there is an error retrieving the names of the policy
	 *                      classes.
	 */
	@Override
	public Set<Long> getPC() throws PIPException {
		Set<Node> nodes = new HashSet<>(getNodes());
		Set<Long> namesPolicyClasses = new HashSet<>();
		for (Node node : nodes) {
			if (node.getType().equals(NodeType.toNodeType("PC"))) {
				namesPolicyClasses.add(node.getID());
			}
		}

		if (namesPolicyClasses.isEmpty()) {
			throw new PIPException("graph", "There are no Policies in the current database");
		}
		return namesPolicyClasses;
	}

	@Override
	public long importPolicy(String owner, String name, String policy) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_IMPORT_POLICY,
						Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, name);
			pstmt.setString(2, null);
			pstmt.setString(3, policy);
			pstmt.setBoolean(4, true);
			pstmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
			pstmt.setString(6, owner);
			pstmt.execute();

			ResultSet rs = pstmt.getGeneratedKeys();
			long newID = -1;
			if (rs.next()) {
				newID = rs.getLong(1);
			}
			return newID;
		} catch (SQLException e) {
			throw new PIPException(e.getMessage());
		}
	}

	/**
	 * Retrieve the set of all nodes in the graph.
	 *
	 * @return a Set of all the nodes in the graph.
	 * @throws PIPException if there is an error retrieving all nodes in the graph.
	 */
	@Override
	public Collection<MNode> getNodes() throws PIPException {
//		Transaction transaction = null;
//	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//	    	transaction = session.beginTransaction();
//	    	CriteriaQuery criteria = ((Object) session).createCriteria(MNode.class);
//	    	
//	    	List<MNode> nodes = session.createQuery("SELECT * FROM node", MNode.class).getResultList(); 
//	    	Set<MNode> result_set = new HashSet<MNode> ();
//	    	for (int i = 0; i < nodes.size(); i++) {
//	    		MNode node =  nodes.get(i);
//		    	long nodeTypeID = node.getTYPE();
//		    	node.id(node.getIDD());
//		    	node.name(node.getNAME());
//		    	for (Map.Entry<Long, String> node_type_k : getNodeType().entrySet()) {
//					if (nodeTypeID == node_type_k.getKey()) {
//						node.type(NodeType.toNodeType(node_type_k.getValue()));
//					}
//				}
//		    	String propertiesJSON = node.getPROPERTIES();
//				if (propertiesJSON != null) {
//					node.properties(reader.readValue(propertiesJSON));
//				}
//				result_set.add(node);
//	    	}
//	    	
//	    	transaction.commit();
//	    	session.close();
//	    	return result_set;
//		}catch (Exception e) {
//			e.printStackTrace();
//            transaction.rollback(); 
//			throw new PIPException("graph");
//		}
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_NODES)) {

			pstmt.setBoolean(1, true);

			Set<MNode> nodes = new HashSet<>();
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MNode node = new MNode();
				node.id(rs.getInt("node_id"));
				node.name(rs.getString("name"));

				long nodeTypeID = rs.getInt("node_type_id");
				node.setTYPE(nodeTypeID);
//				System.out.println(node.getTYPE());			
				
				node.setPolicyID(rs.getLong("policy_id"));

				String propertiesJSON = rs.getString("node_property");
				if (null != propertiesJSON) {
					node.properties(reader.readValue(propertiesJSON));
				}

				for (Map.Entry<Long, String> node_type_k : getNodeType().entrySet()) {
					if (nodeTypeID == node_type_k.getKey()) {
						node.type(NodeType.toNodeType(node_type_k.getValue()));
					}
				}
				
				node.setStoreType(rs.getString("node_status"));
				node.setOwner(rs.getString("owner"));
				node.setNodeActive(true);
				node.setAuthorityID(rs.getLong("authority_id"));
				node.setAttributeID(rs.getLong("attribute_id"));
				nodes.add(node);
			}
			return nodes;
		} catch (SQLException | PIPException | IOException s) {
			throw new PIPException("graph", s.getMessage());
		}
	}

	/**
	 * Retrieve the node in the graph.
	 *
	 * @return a node in the graph.
	 * @throws PIPException if there is an error retrieving node in the graph.
	 */
	@Transactional
	@Override
	public MNode getNode(long id) throws PIPException {
		Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    	transaction = session.beginTransaction();
	    	
	    	MNode node =  session.get(MNode.class, id);
	    	long nodeTypeID = node.getTYPE();
	    	node.id(node.getIDD());
	    	node.name(node.getNAME());
	    	for (Map.Entry<Long, String> node_type_k : getNodeType().entrySet()) {
				if (nodeTypeID == node_type_k.getKey()) {
					node.type(NodeType.toNodeType(node_type_k.getValue()));
				}
			}
	    	String propertiesJSON = node.getPROPERTIES();
			if (propertiesJSON != null) {
				node.properties(reader.readValue(propertiesJSON));
			}
	    	transaction.commit();
	    	session.close();
	    	return node;
		}catch (Exception e) {
			e.printStackTrace();
            transaction.rollback(); 
			throw new PIPException("graph");
		}
//		try (Connection con = this.conn.getConnection();
//				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_ALL_FROM_NODE_ID)) {
//			System.out.println("giuhkhli");
//			System.out.println(pstmt);
//			pstmt.setLong(1, id);
//			pstmt.setBoolean(2, true);
//
//			MNode node = null;
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				node = new MNode();
//				node.id(id);
//				node.name(rs.getString("name"));
//				node.type(null);
//				long nodeTypeID = rs.getInt("node_type_id");
//				node.setTYPE(nodeTypeID);
//				String propertiesJSON = rs.getString("node_property");
//				if (propertiesJSON != null) {
//					node.properties(reader.readValue(propertiesJSON));
//				}
//
//				for (Map.Entry<Long, String> node_type_k : getNodeType().entrySet()) {
//					if (nodeTypeID == node_type_k.getKey()) {
//						node.type(NodeType.toNodeType(node_type_k.getValue()));
//					}
//				}
//			}
//			return node;
//		} catch (SQLException | IOException s) {
//			throw new PIPException("graph", s.getMessage());
//		}
	}

	/**
	 * >>>>>>> testcases_rap Search the graph for nodes matching the given
	 * parameters. A node must contain all properties provided to be returned. To
	 * get all the nodes that have a specific property key with any value use "*" as
	 * the value in the parameter. (i.e. {key=*})
	 *
	 * @param name       the name of the node to search for
	 * @param type       the type of the nodes to search for.
	 * @param properties the properties of the nodes to search for.
	 * @return a set of nodes that match the given search criteria.
	 * @throws PIPException if there is an error searching the graph.
	 */
	@Override
	public Set<MNode> search(String name, String type, Map<String, String> properties, long policyID)
			throws PIPException {
		if (properties == null) {
			properties = new HashMap<>();
		}

		HashSet<MNode> results = new HashSet<>();
		// iterate over the nodes to find ones that match the search parameters
		for (MNode node : getNodes()) {

			// if the type parameter is not null and the current node type does not equal
			// the type parameter, do not add
			if ((name != null && !node.getName().equals(name)) || (type != null && !node.getType().equals(type))) {
				continue;
			}

			boolean add = true;
			for (String key : properties.keySet()) {
				String checkValue = properties.get(key);
				String foundValue = node.getProperties().get(key);
				// if the property provided in the search parameters is null or *, continue to
				// the next property
				if (!(checkValue == null || checkValue.equals("*"))
						&& (foundValue == null || !foundValue.equals(checkValue))) {
					add = false;
					break;
				}
			}

			if (add) {
				results.add(node);
			}
		}
		return results;
	}

	/**
	 * Get the set of nodes that are assigned to the node with the given name.
	 *
	 * @param Id of the node to get the children of.
	 * @return the Set of NGACNodes that are assigned to the node with the given
	 *         name.
	 * @throws PIPException if there is an error retrieving the children of the
	 *                      node.
	 */
	@Override
	public Set<Long> getChildren(long nodeID) throws PMException {
		if (!exists(nodeID)) {
			throw new PIPException("graph", "node with the id " + nodeID + "could not be found to update");
		}

		Set<Long> sources = new HashSet<>();
		try (Connection con = this.conn.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_START_NODE_ID + nodeID)) {

			while (rs.next()) {
				long startNodeID = rs.getInt("stage_start_node_id");
				sources.add(startNodeID);
			}
			return sources;
		} catch (SQLException | PIPException ex) {
			throw new PIPException("graph", ex.getMessage());
		}
	}

	/**
	 * Get the set of nodes that the node with the given name is assigned to.
	 *
	 * @param Id of the node to get the parents of.
	 * @return the Set of NGACNodes that are assigned to the node with the given ID.
	 * @throws PIPException if there is an error retrieving the parents of the node.
	 */
	@Override
	public Set<Long> getParents(long nodeID) throws PMException {
		if (!exists(nodeID)) {
			throw new PIPException("graph", "node " + nodeID + " does not exist");
		}

		try (Connection con = this.conn.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_END_NODE_ID + nodeID)) {

			Set<Long> targets = new HashSet<>();
			while (rs.next()) {
				long endNodeID = rs.getInt("stage_end_node_id");
				targets.add(endNodeID);
			}
			return targets;
		} catch (SQLException | PIPException ex) {
			throw new PIPException("graph", ex.getMessage());
		}
	}

	/**
	 * Assign the child node to the parent node. Both nodes must exist and both
	 * types must make a valid assignment.
	 *
	 * @throws PMException
	 *
	 * @throws IllegalArgumentException if the child node context is null or does
	 *                                  not exist in the mysql graph.
	 * @throws IllegalArgumentException if the parent node context is null or does
	 *                                  not exist in the mysql graph.
	 */
	@Transactional
	@Override
	public void assign(long policyID, long childID, long parentID, boolean isAssignmentActive, StoreType storeType,
			String owner) throws PMException {
		Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    	transaction = session.beginTransaction();
	    	MAssignment assignment = new MAssignment(childID, parentID, isAssignmentActive, storeType.name(), policyID);
//	    	Assertions.assertNull(node.getID());

//	    	if (name == "CCCC") throw new Exception("JUST CHECKING");
	    	Long assignmentID = (Long) session.save(assignment);
//	    	if (name == "CCCC") throw new Exception("JUST CHECKING");
//	    	Assertions.assertNotNull(node.getID());
	    	transaction.commit();
	    	session.close();
	    	
		}catch (Exception e) {
			e.printStackTrace();
            transaction.rollback(); 
			throw new PIPException("graph");
		}
//		try (Connection con = this.conn.getConnection();
//				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_ASSIGNMENT,
//						Statement.RETURN_GENERATED_KEYS)) {
//			pstmt.setLong(1, policyID);
//			pstmt.setLong(2, childID);
//			pstmt.setLong(3, parentID);
//			pstmt.setBoolean(4, true);
//			pstmt.setString(5, storeType.name());
//			pstmt.setString(6, owner);
//
//			pstmt.executeUpdate();
//		} catch (SQLException | PIPException s) {
//			throw new PIPException("graph", s.getMessage());
//		}
	}
	public MAssignment getAssignement () {
		MAssignment assignment = null;
		return assignment;
	}

	/**
	 * Deassign the child node from the parent node. If the 2 nodes are assigned
	 * several times, it delete all assignment.
	 *
	 * @throws IllegalArgumentException if the child node context is null.
	 * @throws IllegalArgumentException if the parent node context is null.
	 * @throws PIPException             if the nodes do not exist
	 */
	@Override
	public void deassign(long policyID, long childID, long parentID, StoreType storeType, String owner,
			boolean isActive) throws PMException {
		Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    	transaction = session.beginTransaction();
	    	
	    	String hib = "UPDATE MAssignment SET isAssignmentActive= false WHERE policyID = " + policyID + " and srcID = " + parentID + " and trgID = "+ childID + 
	    			" and assignmentStatus = '" + storeType.name() + "' and owner =" + owner + " and isAssignmentActive = true";
	    	System.out.println(hib);
	    	Query query=session.createQuery(hib);  
	    	query.executeUpdate();
	    	
//	    	MAssignment assignment = new MAssignment(childID, parentID, isAssignmentActive, storeType.name(), policyID);
//	    	session.delete(session.get(MNode.class, nodeID));
	    	transaction.commit();
	    	session.close();
		}catch (Exception e) {
			e.printStackTrace();
            transaction.rollback(); 
			throw new PIPException("graph");
		}
//		try (Connection con = this.conn.getConnection();
//				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.DELETE_ASSIGNMENT)) {
//
//			// is_assignment_active = ? WHERE start_node_id = ? AND end_node_id = ? AND
//			// policy_id = ? AND assignment_status = ? AND owner = ?
//			pstmt.setBoolean(1, false);
//			pstmt.setLong(2, childID);
//			pstmt.setLong(3, parentID);
//			pstmt.setLong(4, policyID);
//			pstmt.setString(5, storeType.name());
//			pstmt.setString(6, owner);
//
//			pstmt.executeUpdate();
//		} catch (SQLException ex) {
//			throw new PIPException("graph", ex.getMessage());
//		}
	}

	/**
	 * Create an Association between the user attribute and the Target node with the
	 * provided operations. If an association already exists between these two
	 * nodes, overwrite the existing operations with the ones provided. Associations
	 * can only begin at a user attribute but can point to either an Object or user
	 * attribute
	 *
	 * @param ua         The id of the user attribute.
	 * @param target     The id of the target attribute.
	 * @param operations A Set of operations to add to the association.
	 * @throws PIPException if there is an error associating the two nodes.
	 */
	@Override
	public void associate(long policyID, long sourceID, long targetID, Set<String> operations,
			boolean isAssociationActive, StoreType storeType) throws PIPException {
		if (!isAssociated(sourceID, targetID)) {
			// no edge exists between ua and target -> create a new association
			try (Connection con = this.conn.getConnection();
					PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_ASSOCIATION)) {

				pstmt.setLong(1, policyID);
				pstmt.setLong(2, sourceID);
				pstmt.setLong(3, targetID);
				// Json serialization using Jackson
				try {
					pstmt.setString(4, hashSetToJSON(operations));
				} catch (JsonProcessingException j) {
					throw new PIPException("graph", j.getMessage());
				}
				pstmt.setBoolean(5, true);
				pstmt.setString(6, storeType.name());

				if (0 == pstmt.executeUpdate()) {
					throw new PIPException("Something went wrong associating the two nodes.");
				}
			} catch (SQLException | PIPException ex) {
				throw new PIPException("graph", ex.getMessage());
			}
		} else {
			// if an association exists update it
			try (Connection con = this.conn.getConnection();
					PreparedStatement pstmt = con.prepareStatement(MySQLHelper.UPDATE_ASSOCIATION)) {

				pstmt.setLong(3, sourceID);
				pstmt.setLong(4, targetID);
				pstmt.setLong(5, policyID);

				// Json serialization using Jackson
				try {
					pstmt.setString(1, hashSetToJSON(operations));
				} catch (JsonProcessingException j) {
					throw new PIPException("graph", j.getMessage());
				}
				pstmt.setString(2, storeType.name());

				if (0 == pstmt.executeUpdate()) {
					throw new IllegalArgumentException(SOMETHING_WENT_WRONG_IN_ASSOCIATION);
				}
			} catch (SQLException | PIPException ex) {
				throw new PIPException("graph", ex.getMessage());
			}
		}
	}

	/**
	 * Delete the Association between the user attribute and Target node.
	 *
	 * @param ua     the Id of the user attribute.
	 * @param target the Id of the target attribute.
	 * @throws PIPException if there is an error dissociating the two nodes.
	 */
	@Override
	public void dissociate(long policyID, long uaID, long targetID) throws PMException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.DELETE_ASSOCIATION)) {
			pstmt.setBoolean(1, false);
			pstmt.setLong(2, uaID);
			pstmt.setLong(3, targetID);
			pstmt.setLong(4, policyID);

			if (0 == pstmt.executeUpdate()) {
				throw new IllegalArgumentException("The association you want to delete does not exist");
			}
		} catch (SQLException | PIPException e) {
			throw new PIPException("graph", e.getMessage());
		}
	}

	/**
	 * Retrieve the associations the given node is the source of. The source node of
	 * an association is always a user attribute and this method will throw an
	 * exception if an invalid node is provided. The returned Map will contain the
	 * target and operations of each association.
	 *
	 * @param source the name of the source node.
	 * @return a map of the target node names and the operations for each
	 *         association.
	 * @throws PIPException if there is an retrieving the associations of the source
	 *                      node from the graph.
	 */
	@Override
	public Map<Long, Set<String>> getSourceAssociations(long sourceID) throws PIPException {
		if (!exists(sourceID)) {
			throw new PIPException("graph", ONLY_NODE + sourceID + " does not exist");
		}

		Node ua = getNode(sourceID);
		if (ua.getType() != NodeType.UA) {
			throw new PIPException("graph", "The source node must be an user attribute.");
		}
		Map<Long, Set<String>> sourcesAssoc = new HashMap<>();
		OperationSet operations_set = new OperationSet();

		try (Connection con = this.conn.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_END_NODE_ID_OPERATION + ua.getID())) {

			while (rs.next()) {
				long end_node_id = rs.getInt("stage_end_node_id");
				String operations = rs.getString("stage_operation_set");

				if (operations != null) {
					try {
						operations_set = reader2.readValue(operations);
					} catch (JsonProcessingException j) {
						throw new PIPException("graph", j.getMessage());
					} catch (IOException e) {
						throw new PIPException("graph", e.getMessage());
					}
				}
				sourcesAssoc.put(end_node_id, operations_set);

			}

			return sourcesAssoc;
		} catch (SQLException ex) {
			throw new PIPException("graph", ex.getMessage());
		}
	}

	/**
	 * Retrieve the associations the given node is the target of. The target node
	 * can be an Object Attribute or a User Attribute. This method will throw an
	 * exception if a node of any other type is provided. The returned Map will
	 * contain the source node IDs and the operations of each association.
	 *
	 * @param target the name of the target node.
	 * @return a Map of the source Ids and the operations for each association.
	 * @throws PIPException if there is an retrieving the associations of the target
	 *                      node from the graph.
	 */
	@Override
	public Map<Long, Set<String>> getTargetAssociations(long targetID) throws PMException {
		if (!exists(targetID)) {
			throw new PIPException("graph", ONLY_NODE + targetID + " does not exist");
		}

		Node ua = getNode(targetID);

		if (ua.getType() != NodeType.UA && ua.getType() != NodeType.OA) {
			throw new PIPException("graph", "The target node must be an user attribute or an object attribute.");
		}
		Map<Long, Set<String>> targetsAssoc = new HashMap<>();
		OperationSet operations_set = new OperationSet();

		try (Connection con = this.conn.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_START_NODE_ID_OPERATION + ua.getID())) {
			while (rs.next()) {
				long start_node_id = rs.getInt("stage_start_node_id");
				String operations = rs.getString("stage_operation_set");

				if (operations != null) {
					try {
						operations_set = reader2.readValue(operations);
					} catch (JsonProcessingException j) {
						throw new PIPException("graph", j.getMessage());
					} catch (IOException e) {
						throw new PIPException("graph", e.getMessage());
					}
				}
				targetsAssoc.put(start_node_id, operations_set);
			}

			return targetsAssoc;
		} catch (SQLException ex) {
			throw new PIPException("graph", ex.getMessage());
		}
	}

	/**
	 * Retrieve the name of the node by id
	 */
	public String getNodeNameFromId(long id) throws PMException {
		// Not efficient programming
		// Must select either specific node or name only
		Collection<MNode> nodes = getNodes();
		try {
			List<MNode> ns = nodes.stream().filter(n -> n.getID() == id).collect(Collectors.toList());
			if (!ns.isEmpty()) {
				return ns.get(0).getName();
			}
			return null;
		} catch (Exception p) {
			throw new PIPException("graph", p.getMessage());
		}
	}

	public boolean isAssociated(long child, long parent) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_ASSOCIATION_ID)) {

			pstmt.setLong(1, child);
			pstmt.setLong(2, parent);
			pstmt.setBoolean(3, true);

			ResultSet rs = pstmt.executeQuery();
			List<Integer> ids = new ArrayList<>();
			while (rs.next()) {
				ids.add(rs.getInt("association_id"));
			}
			return (!ids.isEmpty());
		} catch (SQLException | PIPException e) {
			throw new PIPException("graph", e.getMessage());
		}
	}

	@Override
	public long publishPolicy(String name, String description, String businessJson, String tJson, String owner)
			throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_POLICY,
						Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, name);
			pstmt.setString(2, description);
			pstmt.setString(3, businessJson);
			pstmt.setString(4, tJson);
			pstmt.setBoolean(5, true);
			pstmt.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
			pstmt.setString(7, owner);
			pstmt.execute();

			ResultSet rs = pstmt.getGeneratedKeys();
			long policyID = -1;

			while (rs.next()) {
				policyID = rs.getLong(1);
			}
			return policyID;
		} catch (SQLException | PIPException e) {
			throw new PIPException("graph", e.getMessage());
		}
	}

	@Override
	public void updatePolicy(String name, String description, String policyJSON, String cryptoJSON,
			PolicyStatus policyStatus, long policyID, String owner) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.UPDATE_POLICY)) {

			// name = ?, description = ?, policy_json = ?, crypto_json = ?, policy_status =
			//
			pstmt.setString(1, name);
			pstmt.setString(2, description);
			pstmt.setString(3, policyJSON);
			pstmt.setString(4, cryptoJSON);
			pstmt.setString(5, policyStatus.name());
			pstmt.setLong(6, policyID);
			pstmt.setString(7, owner);
			pstmt.executeUpdate();
		} catch (SQLException | PIPException e) {
			throw new PIPException("graph", e.getMessage());
		}
	}

	public boolean policyExists(long policyID) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_POLICY_ID_NAME_FROM_POLICIES)) {

			pstmt.setLong(1, policyID);
			ResultSet rs = pstmt.executeQuery();

			int count = 0;
			while (rs.next()) {
				count = rs.getInt("total");
			}
			return (count != 0);
		} catch (SQLException | PIPException e) {
			throw new PIPException("graph", e.getMessage());
		}
	}

	/**
	 * Retrieve the policy by id
	 *
	 * @param policyId
	 */
	@Override
	public String exportPolicy(long policyID) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_POLICY_JSON_FROM_POLICIES)) {

			pstmt.setLong(1, policyID);
			ResultSet rs = pstmt.executeQuery();

			String policyJSON = null;
			while (rs.next()) {
				policyJSON = rs.getNString("business_json");
			}
			return policyJSON;
		} catch (SQLException | PIPException e) {
			throw new PIPException("graph", e.getMessage());
		}
	}

	/**
	 * Retrieve node_id by name
	 *
	 */
	@Override
	public long getIdByName(String name) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_NODE_ID_BY_NAME)) {

			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();
			long nodeID = 0;
			while (rs.next()) {
				nodeID = rs.getInt("stage_node_id");
			}
			return nodeID;
		} catch (SQLException | PIPException e) {
			throw new PIPException("graph", e.getMessage());
		}
	}

	@Override
	public boolean checkAssignment(long childID, long parentID) throws PIPException, PMDBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_STAGE_ASSIGNMENT_ID)) {

			pstmt.setLong(1, childID);
			pstmt.setLong(2, parentID);

			ResultSet rs = pstmt.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("stage_assignment_id");
			}
			return (count != 0);
		} catch (SQLException | PIPException e) {
			throw new PIPException("graph", e.getMessage());
		}
	}

	@Override
	public boolean checkPermissions(long uaID, long targetID) throws PIPException, PMDBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_NON_STAGE_OPERATION)) {

			pstmt.setLong(1, uaID);
			pstmt.setLong(2, targetID);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String operation = rs.getString("operation_set");
				operation = operation.substring(2, operation.length() - 2);
				if (operation.equalsIgnoreCase(Operations.HAS_ACCESS)) {
					return true;
				}
			}
			return false;
		} catch (SQLException | PIPException e) {
			throw new PIPException("graph", e.getMessage());
		}
	}

	@Override
	public List<Policy> getPolicies(String policyOwner) throws PIPException {
		try (Connection con = conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_POLICIES)) {

			// policy_id, name, description, policy_json, crypto_json, policy_status,
			// created_at
			ResultSet rs = pstmt.executeQuery();
			List<Policy> policies = new ArrayList<>();
			while (rs.next()) {
				Policy policy = new Policy();
				policy.setId(rs.getLong("policy_id"));
				policy.setType(policyType.get(rs.getLong("policy_type_id")));
				policy.setName(rs.getString("name"));
				policy.setDescription(rs.getString("description"));
				policy.setPolicyJSON(rs.getString("policy_json"));
				policy.setCryptoJSON(rs.getString("crypto_json"));
				policy.setStatus(rs.getString("policy_status"));
				policy.setCreatedAt(rs.getDate("created_at"));
				policy.setOwner(rs.getString("policy_owner"));

				policies.add(policy);
			}
			return policies;
		} catch (SQLException | PIPException e) {
			throw new PIPException("policy", e.getMessage());
		}
	}

	@Override
	public Set<MAssignment> getAssignments() throws PIPException {
		try (Connection con = conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_ASSIGNMENTS);) {
			pstmt.setBoolean(1, true);

			ResultSet rs = pstmt.executeQuery();
			Set<MAssignment> assignments = new HashSet<>();
			while (rs.next()) {
				assignments.add(new MAssignment(rs.getLong("start_node_id"), rs.getLong("end_node_id"), true,
						rs.getString("assignment_status"), rs.getLong("policy_id")));
			}
			return assignments;
		} catch (SQLException e) {
			throw new PIPException(e.getMessage());
		}
	}
	public MAssignment getAssignment( long assignment_id) throws PIPException {
		Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    	transaction = session.beginTransaction();
	    	
	    	MAssignment node =  session.get(MAssignment.class, 1);
	
	    	transaction.commit();
	    	session.close();
	    	return node;
		}catch (Exception e) {
			e.printStackTrace();
            transaction.rollback(); 
			throw new PIPException("graph");
		}
	}
	

	@Override
	public Set<MAssociation> getAssociations() throws PIPException {
		try (Connection con = conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_ASSOCIATIONS);) {

			pstmt.setBoolean(1, true);

			ResultSet rs = pstmt.executeQuery();
			Set<MAssociation> associations = new HashSet<>();
			while (rs.next()) {
				Set<String> ops = reader2.readValue(rs.getString("operation_set"));
				associations.add(new MAssociation(rs.getLong("start_node_id"), rs.getLong("end_node_id"), ops, true,
						rs.getString("association_status"), rs.getLong("policy_id")));
			}
			return associations;
		} catch (SQLException | IOException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public Attempt createAttempt(String attemptType, String attemptStatus, String createdBy) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_ATTEMPT,
						Statement.RETURN_GENERATED_KEYS)) {

			long typeID = 0;
			for (Entry<Long, String> set : MySQLStore.attemptType.entrySet()) {
				if (set.getValue().equals(attemptType))
					typeID = set.getKey();
			}

			if (0 == typeID)
				throw new SQLException("Attempt type ID not found");

			Date now = new Date();

			// attempt_type_id, attempt_status, created_by, created_at
			pstmt.setLong(1, typeID);
			pstmt.setString(2, attemptStatus);
			pstmt.setString(3, createdBy);
			pstmt.setDate(4, new java.sql.Date(now.getTime()));
			pstmt.execute();

			long attemptID = 0;
			ResultSet rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				attemptID = rs.getLong(1);
			}
			return new Attempt(attemptID, attemptType, attemptStatus, createdBy, now);
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void updateAttempt(long attemptID, String attemptStatus, String createdBy) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.UPDATE_ATTEMPT)) {

			Date now = new Date();

			// attempt_type_id, attempt_status, created_by, created_at
			pstmt.setString(1, attemptStatus);
			pstmt.setDate(2, new java.sql.Date(now.getTime()));
			pstmt.setLong(3, attemptID);
			pstmt.setString(4, createdBy);

			pstmt.executeUpdate();
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public Attempt getAttempt(long attemptID) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_ATTEMPT)) {

			pstmt.setLong(1, attemptID);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Attempt attempt = new Attempt();
				attempt.setAttemptID(rs.getLong("attempt_id"));

				String type = null;
				for (Entry<Long, String> set : MySQLStore.attemptType.entrySet()) {
					if (set.getKey() == rs.getLong("attempt_type_id"))
						type = set.getValue();
				}

				if (null == type)
					throw new SQLException(String.format(MessageHelper.ATTEMPT_WITH_ID_NOT_FOUND, attemptID));

				attempt.setAttemptType(type);
				attempt.setAttemptStatus("attempt_status");
				attempt.setCreatedBy(rs.getString("created_by"));
				attempt.setCreatedAt(rs.getDate("created_at"));
				attempt.setUpdatedAt(rs.getDate("updated_at"));
				return attempt;
			}
			return null;
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public long createPolicy(PolicyType type, String policyStatus, String policyOwner, long prevPolicy, long nextPolicy)
			throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_POLICIES,
						Statement.RETURN_GENERATED_KEYS)) {

			Date now = new Date();

			long typeID = 0;
			for (Entry<Long, String> set : policyType.entrySet()) {
				if (set.getValue().equals(type.name()))
					typeID = set.getKey();
			}

			if (0 == typeID)
				throw new SQLException("Policy type id not found");

			pstmt.setLong(1, typeID);
			pstmt.setString(2, policyStatus);
			pstmt.setString(3, policyOwner);
			pstmt.setDate(4, new java.sql.Date(now.getTime()));
			pstmt.setLong(5, prevPolicy);
			pstmt.setLong(6, nextPolicy);
			pstmt.execute();

			long policyID = 0;
			ResultSet rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				policyID = rs.getLong(1);
			}
			return policyID;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public long createPolicyAttemptReln(long policyID, long attemptID) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_POLICY_TO_ATTEMPT_RELATION,
						Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setLong(1, policyID);
			pstmt.setLong(2, attemptID);
			pstmt.execute();

			long relID = 0;
			ResultSet rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				relID = rs.getLong(1);
			}
			return relID;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public Policy getPolicy(String policyOwner, long policyID) throws PIPException {
		try (Connection con = conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_POLICY)) {

			pstmt.setLong(1, policyID);
			pstmt.setString(2, policyOwner);

			// policy_id, policy_type_id, name, description, policy_json, crypto_json,
			// access_token_gen_json, encryption_key_info_json, policy_status, created_at
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Policy policy = new Policy();
				policy.setId(policyID);
				policy.setType(policyType.get(rs.getLong("policy_type_id")));
				policy.setName(rs.getString("name"));
				policy.setDescription(rs.getString("description"));
				policy.setPolicyJSON(rs.getString("policy_json"));
				policy.setCryptoJSON(rs.getString("crypto_json"));
				policy.setStatus(rs.getString("policy_status"));
				policy.setOwner(policyOwner);
				policy.setCreatedAt(rs.getDate("created_at"));
				return policy;
			}
			return null;
		} catch (SQLException | PIPException e) {
			throw new PIPException("policy", e.getMessage());
		}
	}

	@Override
	public boolean isPolicyAndAttemptValid(long policyID, long attemptID) throws PIPException {
		try (Connection con = conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.IS_POLICY_AND_ATTEMPT_VALID)) {

			pstmt.setLong(1, policyID);
			pstmt.setLong(2, attemptID);

			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException | PIPException e) {
			throw new PIPException("policy", e.getMessage());
		}
	}

	@Override
	public Asset createAsset(long id, String name, AssetType type, AssetStatus status, String link, String owner,
			boolean isActive) throws DBException {
		try (Connection con = conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_ASSET,
						Statement.RETURN_GENERATED_KEYS)) {

			Date now = new Date();

			long typeID = 0;
			for (Entry<Long, String> set : assetType.entrySet()) {
				if (set.getValue().equals(type.name()))
					typeID = set.getKey();
			}

			if (0 == typeID)
				throw new SQLException("Policy type id not found");

			// asset_type_id, name, asset_link, asset_owner, asset_status, created_at
			pstmt.setLong(1, typeID);
			pstmt.setString(2, name);
			pstmt.setString(3, link);
			pstmt.setString(4, owner);
			pstmt.setString(5, status.name());
			pstmt.setDate(6, new java.sql.Date(now.getTime()));
			pstmt.setBoolean(7, isActive);
			pstmt.execute();

			long assetID = 0;
			ResultSet rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				assetID = rs.getLong(1);
			}
			return new Asset(assetID, name, type.name(), status.name(), link, owner, now, isActive);
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void updateAsset(long id, String name, AssetStatus status, String link, String owner, boolean isActive)
			throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.UPDATE_ASSET_BY_ID)) {

			pstmt.setString(1, name);
			pstmt.setString(2, status.name());
			pstmt.setString(3, link);
			pstmt.setLong(4, id);
			pstmt.setString(5, owner);
			pstmt.setBoolean(6, isActive);

			pstmt.executeUpdate();
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void deleteAsset(long id, String owner) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.DELETE_ASSET_BY_ID)) {

			pstmt.setBoolean(1, false);
			pstmt.setLong(2, id);
			pstmt.setString(3, owner);

			pstmt.executeUpdate();
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public Asset getAsset(long id, String owner, boolean isActive) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.GET_ASSET_BY_ID)) {

			pstmt.setLong(1, id);
			pstmt.setString(2, owner);
			pstmt.setBoolean(3, isActive);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Asset asset = new Asset();
				asset.setId(id);
				asset.setType(assetType.get(rs.getLong("asset_type_id")));
				asset.setLink(rs.getString("asset_link"));
				asset.setName(rs.getString("name"));
				asset.setOwner(owner);
				asset.setStatus(rs.getString("asset_status"));
				asset.setActive(isActive);
				asset.setCreatedAt(rs.getDate("created_at"));
				return asset;
			}
			return null;
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public Set<Asset> getAssets(String owner, boolean isActive) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.GET_ASSETS)) {

			// pstmt.setString(1, owner);
			pstmt.setBoolean(1, isActive);

			ResultSet rs = pstmt.executeQuery();
			Set<Asset> assets = new HashSet<>();
			while (rs.next()) {
				Asset asset = new Asset();
				asset.setId(rs.getLong("asset_id"));
				asset.setType(assetType.get(rs.getLong("asset_type_id")));
				asset.setLink(rs.getString("asset_link"));
				asset.setName(rs.getString("name"));
				asset.setOwner(owner);
				asset.setStatus(rs.getString("asset_status"));
				asset.setActive(isActive);
				asset.setCreatedAt(rs.getDate("created_at"));
				asset.setOwner(rs.getString("asset_owner"));
				assets.add(asset);
			}
			return assets;
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public boolean exists(String name, String owner) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.ASSET_EXISTS)) {

			pstmt.setString(1, name);
			pstmt.setString(2, owner);
			pstmt.setBoolean(3, true);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public boolean exists(long id, String owner) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.ASSET_EXISTS_BY_ID)) {

			// asset_id = ? AND asset_status = ? AND asset_owner
			pstmt.setLong(1, id);
			pstmt.setBoolean(2, true);
			pstmt.setString(3, owner);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void updateJSON(String json, long policyID, String owner, String status) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_CRYPTO_JSON)) {

			pstmt.setString(1, json);
			pstmt.setLong(2, policyID);
			pstmt.setString(3, owner);
			pstmt.setString(4, status);
			pstmt.executeUpdate();
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public String getJSON(long policyID, String owner, PolicyStatus status) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.GET_CRYPTO_JSON)) {

			pstmt.setLong(1, policyID);
			pstmt.setString(2, status.name());

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				return rs.getString("crypto_json");
			}
			return null;
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public void updateAssignment(long policyID, long childID, long parentID, StoreType storeType, String owner,
			boolean isActive) throws PIPException {
		
		Transaction transaction = null;
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	    	transaction = session.beginTransaction();
	    	
	    	String hib = "UPDATE MAssignment SET assignmentStatus= '"+ storeType.name() +"' WHERE policyID = " + policyID + " and srcID = " + parentID + " and trgID = "+ childID + 
	    			" and owner =" + owner + " and isAssignmentActive =" + isActive;
	    	System.out.println(hib);
	    	Query query=session.createQuery(hib);  
	    	query.executeUpdate();
	    	
	    	transaction.commit();
	    	session.close();
		}catch (Exception e) {
			e.printStackTrace();
            transaction.rollback(); 
			throw new PIPException("graph");
		}
//		try (Connection con = this.conn.getConnection();
//				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.UPDATE_ASSIGNMENT)) {
//
//			// assignment_status = ? WHERE policy_id = ? AND start_node_id = ? AND
//			// end_node_id = ? AND owner = ? AND is_assignment_active = ?
//			pstmt.setString(1, storeType.name());
//			pstmt.setLong(2, policyID);
//			pstmt.setLong(3, childID);
//			pstmt.setLong(4, parentID);
//			pstmt.setString(5, owner);
//			pstmt.setBoolean(6, isActive);
//			pstmt.executeUpdate();
//		} catch (SQLException | PIPException e) {
//			throw new PIPException(e.getMessage());
//		}
	}

	@Override
	public long createAuthority(AuthorityType type, String name, String apiEndpoint, String secret, boolean isActive)
			throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.INSERT_AUTHORITY,
						Statement.RETURN_GENERATED_KEYS)) {

			// authority_type_id, name, owner, is_active
			pstmt.setLong(1, getAuthorityTypeID(type.name()));
			pstmt.setString(2, name);
			pstmt.setString(3, apiEndpoint);
			pstmt.setString(4, secret);
			pstmt.setBoolean(5, isActive);
			pstmt.execute();

			ResultSet rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				return rs.getLong(1);
			}
			return 0;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public void updateAuthority(String name, String apiEndpoint, long id, boolean isActive) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.UPDATE_AUTHORITY)) {

			pstmt.setString(1, name);
			pstmt.setString(2, apiEndpoint);
			pstmt.setLong(3, id);
			pstmt.setBoolean(4, isActive);
			pstmt.executeUpdate();
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public void deleteAuthority(long id) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.DELETE_AUTHORITY)) {

			pstmt.setBoolean(1, false);
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public Authority getAuthority(long id, boolean isActive) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_AUTHORITY)) {

			pstmt.setLong(1, id);
			pstmt.setBoolean(2, isActive);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Authority authority = new Authority();
				authority.setAuthorityID(id);
				authority.setAuthorityType(authorityTypes.get(rs.getLong("authority_type_id")));
				authority.setName(rs.getString("name"));
				authority.setApiEndpoint(rs.getString("api_endpoint"));
				authority.setSecret(rs.getString("secret"));
				authority.setCreatedAt(rs.getDate("created_at"));
				authority.setActive(isActive);

				return authority;
			}
			return null;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public Map<Long, String> getAuthorityTypes() throws PIPException {
		try (Connection con = this.conn.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(MySQLHelper.SELECT_AUTHORITY_TYPES)) {

			authorityTypes.clear();
			while (rs.next()) {
				authorityTypes.put(rs.getLong("authority_type_id"), rs.getString("name"));
			}
			return authorityTypes;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public boolean exists(long authorityID, boolean isActive) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.AUTHORITY_EXISTS)) {

			pstmt.setLong(1, authorityID);
			pstmt.setBoolean(2, isActive);

			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public int add(Prohibition prohibition) throws PIPException {
		String sql = "insert into prohibitions(name,subject,operations,status) values(?,?,?,?)";
		int last_id = 0;
		try (Connection con = this.conn.getConnection();
				PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			st.setString(1, prohibition.getName());
			st.setString(2, prohibition.getSubject());
			st.setObject(3, hashSetToJSON(prohibition.getOperations()));
			st.setBoolean(4, true);
			st.executeUpdate();
			try (ResultSet keys = st.getGeneratedKeys()) {
				if (keys.next())
					last_id = keys.getInt(1);
			} catch (Exception e) {
				System.out.println(e);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return last_id;
	}

	@Override
	public List<Prohibition> getAll() throws PIPException {
		List<Prohibition> prohibitions = new ArrayList<>();
		String sql = "select * from prohibitions";
		try (Connection con = this.conn.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);) {
			while (rs.next()) {
				OperationSet os = reader2.readValue(rs.getString(4));
				Prohibition p = new Prohibition(rs.getInt(1), rs.getString(2), rs.getString(3), os, rs.getDate(5),
						rs.getBoolean(6));
				prohibitions.add(p);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return prohibitions;
	}

	@Override
	public Prohibition get(int prohibitionId) throws PIPException {
		String sql = "select * from prohibitions where id=" + prohibitionId;
		Prohibition p = null;
		try (Connection con = this.conn.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);) {
			if (rs.next()) {
				OperationSet os = reader2.readValue(rs.getString(4));
				p = new Prohibition(rs.getInt(1), rs.getString(2), rs.getString(3), os, rs.getDate(5),
						rs.getBoolean(6));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return p;
	}

	@Override
	public List<Prohibition> getProhibitionsFor(String prohibitionSubject) throws PIPException {
		String sql = "select * from prohibitions where subject=?";
		List<Prohibition> prohibitions = new ArrayList<>();
		try (Connection con = this.conn.getConnection(); PreparedStatement st = con.prepareStatement(sql);) {
			st.setString(1, prohibitionSubject);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				OperationSet os = reader2.readValue(rs.getString(4));
				Prohibition p = new Prohibition(rs.getInt(1), rs.getString(2), rs.getString(3), os, rs.getDate(5),
						rs.getBoolean(6));
				prohibitions.add(p);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return prohibitions;
	}

	@Override
	public void update(int prohibitionId, Prohibition prohibition) throws PIPException {
		String sql = "update prohibitions set name=?, subject=?, operations=? where id=" + prohibitionId;
		try (Connection con = this.conn.getConnection(); PreparedStatement st = con.prepareStatement(sql);) {
			st.setString(1, prohibition.getName());
			st.setString(2, prohibition.getSubject());
			st.setObject(3, hashSetToJSON(prohibition.getOperations()));
			st.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void delete(int prohibitionId) throws PIPException {
		String sql = "update prohibitions set status=? where id=" + prohibitionId;
		try (Connection con = this.conn.getConnection(); PreparedStatement st = con.prepareStatement(sql);) {
			st.setBoolean(1, false);
			st.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public Policy getPolicy(long policyID) throws PIPException {
		try (Connection con = conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_POLICY_BY_ID)) {

			pstmt.setLong(1, policyID);

			// policy_id, policy_type_id, name, description, policy_json, crypto_json,
			// access_token_gen_json, encryption_key_info_json, policy_status, created_at
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Policy policy = new Policy();
				policy.setId(policyID);
				policy.setType(policyType.get(rs.getLong("policy_type_id")));
				policy.setName(rs.getString("name"));
				policy.setDescription(rs.getString("description"));
				policy.setPolicyJSON(rs.getString("policy_json"));
				policy.setCryptoJSON(rs.getString("crypto_json"));
				policy.setStatus(rs.getString("policy_status"));
				policy.setOwner(rs.getString("policy_owner"));
				policy.setCreatedAt(rs.getDate("created_at"));
				return policy;
			}
			return null;
		} catch (SQLException | PIPException e) {
			throw new PIPException("policy", e.getMessage());
		}
	}

	@Override
	public Set<Authority> getAuthorities(boolean isActive) throws PIPException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.SELECT_AUTHORITIES)) {

			pstmt.setBoolean(1, isActive);

			Set<Authority> authorities = new HashSet<>();
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Authority authority = new Authority();
				authority.setAuthorityID(rs.getLong("authority_id"));
				authority.setAuthorityType(authorityTypes.get(rs.getLong("authority_type_id")));
				authority.setName(rs.getString("name"));
				authority.setApiEndpoint(rs.getString("api_endpoint"));
				authority.setSecret(rs.getString("secret"));
				authority.setCreatedAt(rs.getDate("created_at"));
				authority.setActive(isActive);

				authorities.add(authority);
			}
			return authorities;
		} catch (SQLException | PIPException e) {
			throw new PIPException(e.getMessage());
		}
	}

	@Override
	public boolean linkAsset(long assetID, long policyID) throws DBException {
		try (Connection con = this.conn.getConnection();
				PreparedStatement pstmt = con.prepareStatement(MySQLHelper.LINK_ASSET_WITH_POLICY,
						Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setLong(1, policyID);
			pstmt.setLong(2, assetID);
			pstmt.execute();

			ResultSet rs = pstmt.getGeneratedKeys();
			return rs.next();
		} catch (SQLException | PIPException e) {
			throw new DBException(e.getMessage());
		}
	}
}
