package gov.nist.csd.pm.pdp.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import gov.nist.csd.pm.common.constants.MessageHelper;
import gov.nist.csd.pm.common.constants.Messages;
import gov.nist.csd.pm.common.constants.StoreType;
import gov.nist.csd.pm.common.exceptions.Errors;
import gov.nist.csd.pm.common.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.common.exceptions.PMConfigurationException;
import gov.nist.csd.pm.common.exceptions.PMDBException;
import gov.nist.csd.pm.common.exceptions.PMGraphException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.graph.model.relationships.Assignment;
import gov.nist.csd.pm.graph.model.relationships.Association;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MAssociation;
import gov.nist.csd.pm.pap.model.MNode;

/**
 * GraphService provides methods to maintain an NGAC graph, while also ensuring
 * any user interacting with the graph, has the correct permissions to do so.
 */
public class GraphService extends Service {

	public GraphService(String owner) throws PMException {
		super(owner);
	}

	public GraphService() throws PMException {

	}

	// here we check the permission for ua to oa
	public boolean checkAssociation(long uaID, long targetID) throws PMException {
		return getGraphPAP().checkPermissions(uaID, targetID);
	}

	private void checkNodeExist(String name, NodeType type, Map<String, String> properties, long policyID)
			throws PMException {
		// search the graph for any node with the same name, type, and namespace
		Set<MNode> nodes = (Set<MNode>) getGraphPAP().getNodes();
		nodes.removeIf(node -> node.getPolicyID() != policyID);
		for(MNode node: nodes) {
			if(name.equals(node.getName()) && type.equals(node.getType()))
				throw new PMGraphException(
						String.format("a node with the name \"%s\" and type %s already exists", name, type));
		}
	}
	

	public MNode createNode(String name, String type, Map<String, String> properties, String storeType, long policyID,
			long attributeID, long authorityID) throws PMException {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("A node cannot have a null or empty name");
		} else if (null == NodeType.toNodeType(type)) {
			throw new IllegalArgumentException("A node cannot have a null type");
		} else if (null == StoreType.toStoreType(storeType)) {
			throw new IllegalArgumentException("A node cannot have a null store type");
		} else if (0 == policyID) {
			throw new IllegalArgumentException("A policyID cannot be 0");
		}

		if (properties == null) {
			properties = new HashMap<>();
		}
		
		// check if the node exist
		checkNodeExist(name, NodeType.toNodeType(type), properties, policyID);
		long id = generateRandNum();
		return getGraphPAP().createNode(id, name, NodeType.toNodeType(type), policyID, properties,
				StoreType.toStoreType(storeType), getOwner(), attributeID, authorityID);
	}

	/**
	 * Update the node in the database and in the in-memory graph. If the name is
	 * null or empty it is ignored, likewise for properties.
	 *
	 * @param id         the ID of the node to update.
	 * @param name       the name to give the node.
	 * @param properties the properties of the node.
	 * @throws IllegalArgumentException if the given node id is 0.
	 * @throws PMGraphException         if the given node does not exist in the
	 *                                  graph.
	 * @throws PMConfigurationException if there is a configuration error in the
	 *                                  PAP.
	 * @throws PMAuthorizationException if the user is not authorized to update the
	 *                                  node.
	 */
	public void updateNode(long nodeID, String name, Map<String, String> properties, String storeType, long policyID)
			throws PMException {
		if (0 == nodeID) {
			throw new IllegalArgumentException("no ID was provided when updating the node");
		} else if (!exists(nodeID)) {
			throw new PMGraphException(String.format(Messages.NOT_NOT_FOUND_MSG.name(), nodeID));
		} else if (null == name || name.isEmpty()) {
			throw new IllegalArgumentException("no name was provided when updating the node");
		} else if (null == StoreType.toStoreType(storeType)) {
			throw new PMException(String.format("%s store type not found for %s node", name, storeType));
		}

		if (null == properties) {
			properties = new HashMap<>();
		}

		getGraphPAP().updateNode(nodeID, name, properties, StoreType.toStoreType(storeType), getOwner(), true);
	}

	/**
	 * Delete the node with the given ID from the db and in-memory graphs. First
	 * check that the current user has the correct permissions to do so. Do this by
	 * checking that the user has the permission to deassign from each of the node's
	 * parents, and that the user can delete the node. If the node is a policy class
	 * or is assigned to a policy class, check the permissions on the representative
	 * node.
	 *
	 * @param nodeID the ID of the node to delete.
	 * @throws PMGraphException         if there is an error accessing the graph
	 *                                  through the PAP.
	 * @throws PMDBException            if there is an error deleting the node in
	 *                                  the database.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 * @throws PMAuthorizationException if the user is not authorized to delete the
	 *                                  node.
	 */
	public void deleteNode(long nodeID) throws PMException {
		if (0 == nodeID) {
			throw new IllegalArgumentException("no ID was provided when updating the node");
		} else if (!exists(nodeID)) {
			throw new PMGraphException(String.format(Messages.NOT_NOT_FOUND_MSG.name(), nodeID));
		}
		MNode node = getNode(nodeID);

		if (null == node)
			throw new PMAuthorizationException(Errors.ERR_AUTHORIZATION.getMessage());

		getGraphPAP().deleteNode(nodeID, getOwner());
	}

	/**
	 * Check that a node with the given ID exists. Just checking the in-memory graph
	 * is faster.
	 *
	 * @param nodeID the ID of the node to check for.
	 * @return true if a node with the given ID exists, false otherwise.
	 * @throws PMGraphException         if there is an error checking if the node
	 *                                  exists in the graph through the PAP.
	 * @throws PMDBException            if the PAP accesses the database and an
	 *                                  error occurs.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 */
	public boolean exists(long nodeID) throws PMException {
		return getGraphPAP().exists(nodeID);
	}

	/**
	 * Retrieve the list of all nodes in the graph. Go to the database to do this,
	 * since it is more likely to have all of the node information.
	 *
	 * @return the set of all nodes in the graph.
	 * @throws PMGraphException         if there is an error getting the nodes from
	 *                                  the PAP.
	 * @throws PMDBException            if the PAP accesses the database and an
	 *                                  error occurs.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 */
	public Set<MNode> getNodes() throws PMException {
		Set<MNode> nodes = (Set<MNode>) getGraphPAP().getNodes();
		nodes.removeIf(node -> !node.getOwner().equals(getOwner()));
		return nodes;
	}

	/**
	 * Retrieve the list of all nodes from actual graph
	 */
	public Set<MNode> getAllNodes() throws PMException {
		return (Set<MNode>) getGraphPAP().getNodes();
	}

	/**
	 * Get the set of policy class IDs. This can be performed by the in-memory
	 * graph.
	 *
	 * @return the set of IDs for the policy classes in the graph.
	 * @throws PMGraphException         if there is an error getting the policy
	 *                                  classes from the PAP.
	 * @throws PMDBException            if the PAP accesses the database and an
	 *                                  error occurs.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 */
	public Set<Long> getPC() throws PMException {
		return getGraphPAP().getPC();
	}

	/**
	 * Get the children of the node from the graph. Get the children from the
	 * database to ensure all node information is present. Before returning the set
	 * of nodes, filter out any nodes that the user has no permissions on.
	 *
	 * @param nodeID the ID of the node to get the children of.
	 * @return a set of Node objects, representing the children of the target node.
	 * @throws PMGraphException         if the target node does not exist.
	 * @throws PMGraphException         if there is an error getting the children
	 *                                  from the PAP.
	 * @throws PMDBException            if the PAP accesses the database and an
	 *                                  error occurs.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 *
	 */
	public Set<MNode> getChildren(long nodeID) throws PMException {
		if (!exists(nodeID)) {
			throw new PMGraphException(String.format(MessageHelper.NODE_DOES_NOT_EXIST, nodeID));
		}
		Set<Long> childrenID = getGraphPAP().getChildren(nodeID);
		Set<MNode> retChildren = new HashSet<>();
		for (long childID : childrenID) {
			retChildren.add(getNode(childID));
		}
		return retChildren;
	}

	/**
	 * Get the parents of the node from the graph. Get the parents from the database
	 * to ensure all node information is present. Before returning the set of nodes,
	 * filter out any nodes that the user has no permissions on.
	 *
	 * @param nodeID the ID of the node to get the parents of.
	 * @return a set of Node objects, representing the parents of the target node.
	 * @throws PMGraphException         if the target node does not exist.
	 * @throws PMGraphException         if there is an error getting the parents
	 *                                  from the PAP.
	 * @throws PMDBException            if the PAP accesses the database and an
	 *                                  error occurs.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 */
	public Set<MNode> getParents(long nodeID) throws PMException {
		if (!exists(nodeID)) {
			throw new PMGraphException(String.format(MessageHelper.NODE_DOES_NOT_EXIST, nodeID));
		}
		Collection<Long> parents = getGraphPAP().getParents(nodeID);
		Set<MNode> retParents = new HashSet<>();
		for (long parentID : parents) {
			retParents.add(getNode(parentID));
		}
		return retParents;
	}

	/**
	 * Create the assignment in both the db and in-memory graphs. First check that
	 * the user is allowed to assign the child, and allowed to assign something to
	 * the parent. Both child and parent contexts must include the ID and type of
	 * the node.
	 *
	 * @param childID  the ID of the child node.
	 * @param parentID the ID of the parent node.
	 * @throws IllegalArgumentException if the child ID is 0.
	 * @throws IllegalArgumentException if the parent ID is 0.
	 * @throws PMGraphException         if the child or parent node does not exist.
	 * @throws PMGraphException         if the assignment is invalid.
	 * @throws PMDBException            if the PAP accesses the database and there
	 *                                  is an error.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 * @throws PMAuthorizationException if the current user does not have permission
	 *                                  to create the assignment.
	 */
	public void assign(long policyID, long childID, long parentID) throws PMException {
		// check that the nodes are not null
		if (childID == 0) {
			throw new IllegalArgumentException("the child node ID cannot be 0 when creating an assignment");
		} else if (parentID == 0) {
			throw new IllegalArgumentException("the parent node ID cannot be 0 when creating an assignment");
		} else if (!exists(childID)) {
			throw new PMGraphException(String.format("child node with ID %d does not exist", childID));
		} else if (!exists(parentID)) {
			throw new PMGraphException(String.format("parent node with ID %d does not exist", parentID));
		} else if (0 == policyID) {
			throw new PMGraphException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, policyID));
		}

		// check if the assignment is valid
		Node child = getNode(childID);
		Node parent = getNode(parentID);
		Assignment.checkAssignment(child.getType(), parent.getType());

		// assign in the PAP
		getGraphPAP().assign(policyID, childID, parentID, true, StoreType.STAGE, getOwner());
	}

	/**
	 * Create the assignment in both the db and in-memory graphs. First check that
	 * the user is allowed to assign the child, and allowed to assign something to
	 * the parent.
	 *
	 * @param childID  the ID of the child of the assignment to delete.
	 * @param parentID the ID of the parent of the assignment to delete.
	 * @throws IllegalArgumentException if the child ID is 0.
	 * @throws IllegalArgumentException if the parent ID is 0.
	 * @throws PMGraphException         if the child or parent node does not exist.
	 * @throws PMDBException            if the PAP accesses the database and there
	 *                                  is an error.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 * @throws PMAuthorizationException if the current user does not have permission
	 *                                  to delete the assignment.
	 */
	public void deassign(long policyID, long childID, long parentID) throws PMException {
		// check that the parameters are correct
		if (childID == 0) {
			throw new IllegalArgumentException("the child node ID cannot be 0 when deassigning");
		} else if (parentID == 0) {
			throw new IllegalArgumentException("the parent node ID cannot be 0 when deassigning");
		} else if (!exists(childID)) {
			throw new PMGraphException(
					String.format("child node with ID %d could not be found when deassigning", childID));
		} else if (!exists(parentID)) {
			throw new PMGraphException(
					String.format("parent node with ID %d could not be found when deassigning", parentID));
		} else if (0 == policyID) {
			throw new PMGraphException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, policyID));
		}

		// delete assignment in PAP
		getGraphPAP().deassign(policyID, childID, parentID, StoreType.STAGE, getOwner(), true);
	}

	public void updateAssignment(long policyID, long childID, long parentID, String storeType) throws PMException {
		if (childID == 0) {
			throw new IllegalArgumentException("the child node ID cannot be 0 when deassigning");
		} else if (parentID == 0) {
			throw new IllegalArgumentException("the parent node ID cannot be 0 when deassigning");
		} else if (!exists(childID)) {
			throw new PMGraphException(
					String.format("child node with ID %d could not be found when deassigning", childID));
		} else if (!exists(parentID)) {
			throw new PMGraphException(
					String.format("parent node with ID %d could not be found when deassigning", parentID));
		} else if (0 == policyID) {
			throw new PMGraphException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, policyID));
		} else if (null == StoreType.toStoreType(storeType)) {
			throw new PMException(String.format("%s store type not found", storeType));
		}

		getGraphPAP().updateAssignment(policyID, childID, parentID, StoreType.toStoreType(storeType), getOwner(), true);
	}

	public Set<MAssignment> getAssignments(long policyID) throws PMException {
		if (0 == policyID) {
			throw new PMGraphException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, policyID));
		}
		Set<MAssignment> assignments = getGraphPAP().getAssignments();
		assignments.removeIf(assignment -> assignment.getPolicyID() != policyID);
		return assignments;
	}
	public MAssignment getAssignment(long assignment_id) throws PMException {
		if (0 == assignment_id) {
			throw new PMGraphException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, assignment_id));
		}
		MAssignment assignment = getGraphPAP().getAssignment(assignment_id);
		return assignment;
	}

	/**
	 * Create an association between the user attribute and the target node with the
	 * given operations. First, check that the user has the permissions to associate
	 * the user attribute and target nodes. If an association already exists between
	 * the two nodes than update the existing association with the provided
	 * operations (overwrite).
	 *
	 * @param uaID       the ID of the user attribute.
	 * @param targetID   the ID of the target node.
	 * @param operations a Set of operations to add to the Association.
	 * @throws IllegalArgumentException if the user attribute ID is 0.
	 * @throws IllegalArgumentException if the target node ID is 0.
	 * @throws PMGraphException         if the user attribute node does not exist.
	 * @throws PMGraphException         if the target node does not exist.
	 * @throws PMGraphException         if the association is invalid.
	 * @throws PMDBException            if the PAP accesses the database and there
	 *                                  is an error.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 * @throws PMAuthorizationException if the current user does not have permission
	 *                                  to create the association.
	 */
	public void associate(long policyID, long uaID, long targetID, Set<String> operations) throws PMException {
		if (uaID == 0) {
			throw new IllegalArgumentException("the user attribute ID cannot be 0 when creating an association");
		} else if (targetID == 0) {
			throw new IllegalArgumentException("the target node ID cannot be 0 when creating an association");
		} else if (!exists(uaID)) {
			throw new PMGraphException(
					String.format("node with ID %d could not be found when creating an association", uaID));
		} else if (!exists(targetID)) {
			throw new PMGraphException(
					String.format("node with ID %d could not be found when creating an association", targetID));
		} else if (null == operations || operations.isEmpty()) {
			throw new PMGraphException(
					String.format("node %d could not associaten on %d with no operations", uaID, targetID));
		} else if (0 == policyID) {
			throw new PMGraphException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, policyID));
		}

		Node sourceNode = getNode(uaID);
		Node targetNode = getNode(targetID);

		Association.checkAssociation(sourceNode.getType(), targetNode.getType());

		// create association in PAP
		getGraphPAP().associate(policyID, uaID, targetID, operations, true, StoreType.STAGE);
	}

	/**
	 * Delete the association between the user attribute and the target node. First,
	 * check that the user has the permission to delete the association.
	 *
	 * @param uaID     The ID of the user attribute.
	 * @param targetID The ID of the target node.
	 * @throws IllegalArgumentException If the user attribute ID is 0.
	 * @throws IllegalArgumentException If the target node ID is 0.
	 * @throws PMGraphException         If the user attribute node does not exist.
	 * @throws PMGraphException         If the target node does not exist.
	 * @throws PMDBException            if the PAP accesses the database and there
	 *                                  is an error.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 * @throws PMAuthorizationException If the current user does not have permission
	 *                                  to delete the association.
	 */
	public void dissociate(long policyID, long sourceID, long targetID) throws PMException {
		if (0 == sourceID) {
			throw new IllegalArgumentException("the user attribute ID cannot be 0 when creating an association");
		} else if (0 == targetID) {
			throw new IllegalArgumentException("the target node ID cannot be 0 when creating an association");
		} else if (!exists(sourceID)) {
			throw new PMGraphException(
					String.format("node with ID %d could not be found when creating an association", targetID));
		} else if (!exists(targetID)) {
			throw new PMGraphException(
					String.format("node with ID %d could not be found when creating an association", targetID));
		} else if (0 == policyID) {
			throw new PMGraphException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, policyID));
		}

		// create association in PAP
		getGraphPAP().dissociate(policyID, sourceID, targetID);
	}

	public Set<MAssociation> getAssociations(long policyID) throws PMException {
		if (0 == policyID) {
			throw new PMGraphException(String.format(MessageHelper.POLICY_ID_NOT_FOUND, policyID));
		}
		Set<MAssociation> associations = getGraphPAP().getAssociations();
		associations.removeIf(association -> association.getPolicyID() != policyID);
		return associations;
	}

	/**
	 * Get the associations the given node is the source node of. First, check if
	 * the user is allowed to retrieve this information.
	 *
	 * @param sourceID The ID of the source node.
	 * @return a map of the target ID and operations for each association the given
	 *         node is the source of.
	 * @throws PMGraphException         If the given node does not exist.
	 * @throws PMDBException            if the PAP accesses the database and there
	 *                                  is an error.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 * @throws PMAuthorizationException If the current user does not have permission
	 *                                  to get hte node's associations.
	 */
	public Map<Long, Set<String>> getSourceAssociations(long sourceID) throws PMException {
		if (!exists(sourceID)) {
			throw new PMGraphException(String.format(Messages.NOT_NOT_FOUND_MSG.name(), sourceID));
		}
		return getGraphPAP().getSourceAssociations(sourceID);
	}

	/**
	 * Get the associations the given node is the target node of. First, check if
	 * the user is allowed to retrieve this information.
	 *
	 * @param targetID The ID of the source node.
	 * @return a map of the source ID and operations for each association the given
	 *         node is the target of.
	 * @throws PMGraphException         If the given node does not exist.
	 * @throws PMDBException            if the PAP accesses the database and there
	 *                                  is an error.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 * @throws PMAuthorizationException If the current user does not have permission
	 *                                  to get hte node's associations.
	 */
	public Map<Long, Set<String>> getTargetAssociations(long targetID) throws PMException {
		if (!exists(targetID)) {
			throw new PMGraphException(String.format("node with ID %d could not be found", targetID));
		}
		return getGraphPAP().getTargetAssociations(targetID);
	}

	/**
	 * Search the NGAC graph for nodes that match the given parameters. The given
	 * search parameters are provided in the URI as query parameters. The parameters
	 * name and type are extracted from the URI and passed as parameters to the
	 * search function. Any other query parameters found in the URI will be added to
	 * the search criteria as node properties. A node must match all non null
	 * parameters to be returned in the search.
	 *
	 * @param name       The name of the nodes to search for.
	 * @param type       The type of the nodes to search for.
	 * @param properties The properties of the nodes to search for.
	 * @return a Response with the nodes that match the given search criteria.
	 * @throws PMGraphException         If the PAP encounters an error with the
	 *                                  graph.
	 * @throws PMDBException            if the PAP accesses the database and there
	 *                                  is an error.
	 * @throws PMConfigurationException if there is an error in the configuration of
	 *                                  the PAP.
	 * @throws PMAuthorizationException If the current user does not have permission
	 *                                  to get hte node's associations.
	 */
	public Set<MNode> search(String name, String type, Map<String, String> properties, long policyID)
			throws PMException {
		// user the PAP searcher to search for the intended nodes
		return getGraphPAP().search(name, type, properties, policyID);
	}

	/**
	 * Retrieve the node from the graph with the given ID.
	 *
	 * @param id the ID of the node to get.
	 * @return the Node retrieved from the graph with the given ID.
	 * @throws PMGraphException         If the node does not exist in the graph.
	 * @throws PMGraphException         If the node is a policy class that doesn't
	 *                                  have a rep node.
	 * @throws PMDBException            if the PAP accesses the database and there
	 *                                  is an error.
	 * @throws PMAuthorizationException if the current user is not authorized to
	 *                                  access this node.
	 * @throws PMDBException            if the PAP accesses the database and there
	 *                                  is an error.
	 */
	public MNode getNode(long nodeID) throws PMException {
		if (!exists(nodeID)) {
			throw new PMGraphException(String.format("node with ID %d could not be found", nodeID));
		}
		return getGraphPAP().getNode(nodeID);
	}

	public boolean checkAssignment(long childID, long parentID) throws PMException {
		if (childID == 0 && parentID == 0) {
			throw new PMException("child node and parent node are not null");
		}
		return getGraphPAP().checkAssignment(childID, parentID);
	}
}
