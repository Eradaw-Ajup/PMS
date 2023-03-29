package gov.nist.csd.pm.pap;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

import gov.nist.csd.pm.common.constants.StoreType;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.graph.model.relationships.Assignment;
import gov.nist.csd.pm.graph.model.relationships.Association;
import gov.nist.csd.pm.graph.model.relationships.Relationship;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MAssociation;
import gov.nist.csd.pm.pap.model.MNode;

public class TMemGraph {
	private static final String NODE_NOT_FOUND_MSG = "node %s does not exist in the graph";

	private DirectedGraph<Long, Relationship> graph;
	private HashSet<Long> pcs;
	private HashMap<Long, MNode> nodes;

	public TMemGraph() {
		graph = new DirectedMultigraph<>(Relationship.class);
		nodes = new HashMap<>();
		pcs = new HashSet<>();
	}
	public MNode createNode(long id, String name, NodeType type, Map<String, String> properties, boolean isActive,
			StoreType storeType, long policyID, String owner, long attributeID, long authorityID) throws PMException{
		// check for null values
		if (id == 0) {
			throw new IllegalArgumentException("no ID was provided when creating a node in the in-memory graph");
		} else if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("no name was provided when creating a node in the in-memory graph");
		} else if (type == null) {
			throw new IllegalArgumentException("a null type was provided to the in memory graph when creating a node");
		} else if (null == storeType) {
			throw new IllegalArgumentException(
					"a null node store type was provided to the in memory graph when creating a node");
		}

		// if the node being created is a PC, add it to the list of policies
		if (type.equals(NodeType.PC)) {
			pcs.add(id);
		}

		// add the vertex to the graph
		graph.addVertex(id);
		
		// store the node in the map
		MNode node = new MNode(id, name, type, properties, isActive, storeType.name(), policyID, owner, attributeID, authorityID);
		nodes.put(id, node);
		// return the Node
		return node;
	}

	public void updateNode(long id, String name, Map<String, String> properties, StoreType storeType)
			throws PMException {
		MNode existingNode = nodes.get(id);
		if (existingNode == null) {
			throw new PMException(String.format("node with the ID %d could not be found to update", id));
		}

		// update name if present
		if (name != null && !name.isEmpty()) {
			existingNode.name(name);
		}

		// update the properties
		if (properties != null) {
			existingNode.properties(properties);
		}

		if (null != storeType) {
			existingNode.setStoreType(storeType.name());
		}

		// update the node information
		nodes.put(existingNode.getID(), existingNode);
	}

	public void deleteNode(long nodeID) {
		// remove the vertex from the graph
		graph.removeVertex(nodeID);
		// remove the node from the policies if it is a policy class
		pcs.remove(nodeID);
		// remove the node from the map
		nodes.remove(nodeID);
	}

	public boolean exists(long nodeID) {
		return graph.containsVertex(nodeID);
	}

	public Set<Long> getPolicies() {
		return pcs;
	}

	public Collection<MNode> getNodes() {
		return nodes.values();
	}

	public MNode getNode(long id) throws PMException {
		MNode node = nodes.get(id);
		if (node == null) {
			throw new PMException(String.format("a node with the ID %d does not exist", id));
		}

		return node;
	}

	public Set<MNode> search(String name, String type, Map<String, String> properties) {
		if (properties == null) {
			properties = new HashMap<>();
		}

		HashSet<MNode> results = new HashSet<>();
		// iterate over the tNodes to find ones that match the search parameters
		for (MNode node : getNodes()) {
			// if the name parameter is not null and the current node name does not equal
			// the name parameter, do not add
			// if the type parameter is not null and the current node type does not equal
			// the type parameter, do not add
			if (name != null && !node.getName().equals(name)
					|| type != null && !node.getType().toString().equals(type)) {
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

	public Set<Long> getChildren(long nodeID) throws PMException {
		if (!exists(nodeID)) {
			throw new PMException(String.format(NODE_NOT_FOUND_MSG, nodeID));
		}

		HashSet<Long> children = new HashSet<>();
		Set<Relationship> rels = graph.incomingEdgesOf(nodeID);
		for (Relationship rel : rels) {
			if (rel instanceof Association) {
				continue;
			}
			children.add(rel.getSourceID());
		}
		return children;
	}

	public Set<Long> getParents(long nodeID) throws PMException {
		if (!exists(nodeID)) {
			throw new PMException(String.format(NODE_NOT_FOUND_MSG, nodeID));
		}

		HashSet<Long> parents = new HashSet<>();
		Set<Relationship> rels = graph.outgoingEdgesOf(nodeID);
		for (Relationship rel : rels) {
			if (rel instanceof Association) {
				continue;
			}
			parents.add(rel.getTargetID());
		}
		return parents;
	}

	public void assign(long childID, long parentID, boolean isAssignmentActive, StoreType assignmentStatus,
			long policyID) throws PMException {
		if (!exists(childID)) {
			throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, childID));
		} else if (!exists(parentID)) {
			throw new IllegalArgumentException(String.format(NODE_NOT_FOUND_MSG, parentID));
		}

		MNode child = getNode(childID);
		MNode parent = getNode(parentID);

		Assignment.checkAssignment(child.getType(), parent.getType());

		graph.addEdge(childID, parentID,
				new MAssignment(childID, parentID, isAssignmentActive, assignmentStatus.name(), policyID));
	}

	public void deassign(long childID, long parentID) {
		graph.removeEdge(childID, parentID);
	}

	public void associate(long uaID, long targetID, Set<String> operations, boolean isAssociationActive,
			StoreType associationStatus, long policyID) throws PMException {
		if (!exists(uaID)) {
			throw new PMException(String.format(NODE_NOT_FOUND_MSG, uaID));
		} else if (!exists(targetID)) {
			throw new PMException(String.format(NODE_NOT_FOUND_MSG, targetID));
		}

		MNode ua = getNode(uaID);
		MNode target = getNode(targetID);

		// check that the assignment is valid
		Association.checkAssociation(ua.getType(), target.getType());

		if (graph.containsEdge(uaID, targetID)) {
			// if the association exists update the operations
			MAssociation assoc = (MAssociation) graph.getEdge(uaID, targetID);
			assoc.setOperations(operations);
			assoc.setAssociationActive(true);
			assoc.setAssociationStatus(StoreType.STAGE.name());

		} else {
			graph.addEdge(uaID, targetID, new MAssociation(uaID, targetID, operations, isAssociationActive,
					associationStatus.name(), policyID));
		}
	}

	public void dissociate(long uaID, long targetID) {
		graph.removeEdge(uaID, targetID);
	}

	public Map<Long, Set<String>> getSourceAssociations(long sourceID) throws PMException {
		if (!exists(sourceID)) {
			throw new PMException(String.format(NODE_NOT_FOUND_MSG, sourceID));
		}

		Map<Long, Set<String>> assocs = new HashMap<>();
		Set<Relationship> rels = graph.outgoingEdgesOf(sourceID);
		for (Relationship rel : rels) {
			if (rel instanceof Association) {
				Association assoc = (Association) rel;
				assocs.put(assoc.getTargetID(), assoc.getOperations());
			}
		}
		return assocs;
	}

	public Map<Long, Set<String>> getTargetAssociations(long targetID) throws PMException {
		if (!exists(targetID)) {
			throw new PMException(String.format(NODE_NOT_FOUND_MSG, targetID));
		}

		Map<Long, Set<String>> assocs = new HashMap<>();
		Set<Relationship> rels = graph.incomingEdgesOf(targetID);
		for (Relationship rel : rels) {
			if (rel instanceof Association) {
				Association assoc = (Association) rel;
				assocs.put(assoc.getSourceID(), assoc.getOperations());
			}
		}
		return assocs;
	}
}
