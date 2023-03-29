package gov.nist.csd.pm.pap.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.common.constants.StoreType;
import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.common.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pap.TMemGraph;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MAssociation;
import gov.nist.csd.pm.pap.model.MNode;

public class GraphPAP implements GraphMachine {

	private GraphMachine db;
	private TMemGraph memGraph;

	public GraphPAP(TMemGraph memGraph, GraphMachine db) {
		this.db = db;
		this.memGraph = memGraph;
	}

	public GraphMachine getDatabaseGraph() {
		return db;
	}

	public void reset(String owner) throws PMException {
		Collection<MNode> nodes = memGraph.getNodes();
		for (Iterator<MNode> iterator = nodes.iterator(); iterator.hasNext();) {
			Node n = iterator.next();
			db.deleteNode(n.getID(), owner);
			iterator.remove();
		}
	}

	public Map<Long, Set<String>> getSourceAssociation(long sourceID) throws PMException {
		return memGraph.getSourceAssociations(sourceID);
	}

	@Override
	public boolean exists(long nodeID) throws PMException {
		return memGraph.exists(nodeID);
	}

	@Override
	public Collection<MNode> getNodes() {
		Collection<MNode> nodes = new HashSet<>();
		nodes.addAll(memGraph.getNodes());
		return nodes;
	}

	@Override
	public Set<Long> getPC() {
		return memGraph.getPolicies();
	}

	@Override
	public Set<Long> getChildren(long nodeID) throws PMException {
		return memGraph.getChildren(nodeID);
	}

	@Override
	public Set<Long> getParents(long nodeID) throws PMException {
		return memGraph.getParents(nodeID);
	}

	@Override
	public void assign(long policyID, long childID, long parentID, boolean isAssignmentActive, StoreType storeType,
			String owner) throws PMException {
		db.assign(policyID, childID, parentID, isAssignmentActive, storeType, owner);
		memGraph.assign(childID, parentID, isAssignmentActive, storeType, policyID);
	}

	@Override
	public void deassign(long policyID, long childID, long parentID, StoreType storeType, String owner,
			boolean isActive) throws PMException {
		db.deassign(policyID, childID, parentID, storeType, owner, isActive);
		memGraph.deassign(childID, parentID);
	}

	@Override
	public void associate(long policyID, long uaID, long targetID, Set<String> operations, boolean isAssociationActive,
			StoreType associationStatus) throws PMException {
		db.associate(policyID, uaID, targetID, operations, isAssociationActive, associationStatus);
		memGraph.associate(uaID, targetID, operations, isAssociationActive, associationStatus, policyID);
	}

	@Override
	public void dissociate(long policyID, long uaID, long targetID) throws PMException {
		db.dissociate(policyID, uaID, targetID);
		memGraph.dissociate(uaID, targetID);
	}

	@Override
	public Map<Long, Set<String>> getSourceAssociations(long sourceID) throws PMException {
		return memGraph.getSourceAssociations(sourceID);
	}

	@Override
	public Map<Long, Set<String>> getTargetAssociations(long targetID) throws PMException {
		return memGraph.getTargetAssociations(targetID);
	}

	@Override
	public Set<MNode> search(String name, String type, Map<String, String> properties, long policyID) {
		Set<MNode> nodes = new HashSet<>();
		for (MNode node : memGraph.search(name, type, properties)) {
			if (policyID != node.getPolicyID())
				continue;

			nodes.add(new MNode(node.getID(), node.getName(), node.getType(), node.getProperties(), node.isNodeActive(),
					node.getStoreType(), node.getPolicyID(), node.getOwner(), node.getAttributeID(),
					node.getAuthorityID()));
		}
		return nodes;
	}

	@Override
	public MNode getNode(long id) throws PMException {
		return memGraph.getNode(id);
	}

	@Override
	public long getIdByName(String name) throws PIPException {
		return db.getIdByName(name);
	}

	@Override
	public boolean checkAssignment(long childID, long parentID) throws PIPException, PMDBException {
		return db.checkAssignment(childID, parentID);
	}

	@Override
	public boolean checkPermissions(long uaID, long targetID) throws PIPException, PMDBException {
		return db.checkPermissions(uaID, targetID);
	}

	@Override
	public MNode createNode(long id, String name, NodeType type, long policyID, Map<String, String> properties,
			StoreType nodeStoreType, String owner, long attributeID, long authorityID) throws PMException {
		MNode node = db.createNode(id, name, type, policyID, properties, nodeStoreType, owner, attributeID,
				authorityID);
		memGraph.createNode(node.getID(), name, type, properties, true, nodeStoreType, policyID, owner, attributeID,
				authorityID);
		return node;
	}

	@Override
	public void updateNode(long id, String name, Map<String, String> properties, StoreType storeType, String owner,
			boolean isActive) throws PMException {
		db.updateNode(id, name, properties, storeType, owner, isActive);
		memGraph.updateNode(id, name, properties, storeType);
	}

	@Override
	public void deleteNode(long nodeID, String owner) throws PMException {
		db.deleteNode(nodeID, owner);
		memGraph.deleteNode(nodeID);
	}

	@Override
	public Set<MAssignment> getAssignments() throws PIPException {
		return db.getAssignments();
	}
	public MAssignment getAssignment(long assignment_num) throws PIPException {
		return db.getAssignment(assignment_num);
	}

	@Override
	public Set<MAssociation> getAssociations() throws PIPException {
		return db.getAssociations();
	}

	@Override
	public void updateAssignment(long policyID, long childID, long parentID, StoreType storeType, String owner,
			boolean isActive) throws PIPException {
		db.updateAssignment(policyID, childID, parentID, storeType, owner, isActive);
	}
}
