package gov.nist.csd.pm.pap.graph;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.common.constants.StoreType;
import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.common.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MAssociation;
import gov.nist.csd.pm.pap.model.MNode;

public interface GraphMachine {

	// CRUD on node
	MNode createNode(long id, String name, NodeType type, long policyID, Map<String, String> properties,
			StoreType nodeStoreType, String owner, long attributeID, long authorityID) throws PMException;

	void updateNode(long id, String name, Map<String, String> properties, StoreType storeType, String owner,
			boolean isActive) throws PMException;

	void deleteNode(long nodeID, String owner) throws PMException;

	Collection<MNode> getNodes() throws PMException;

	MNode getNode(long id) throws PMException;

	boolean exists(long nodeID) throws PMException;

	Set<Long> getPC() throws PMException;

	Set<MNode> search(String name, String type, Map<String, String> properties, long policyID) throws PMException;

	Set<Long> getChildren(long nodeID) throws PMException;

	Set<Long> getParents(long nodeID) throws PMException;

	void assign(long policyID, long childID, long parentID, boolean isAssignmentActive, StoreType storeType,
			String owner) throws PMException;

	void deassign(long policyID, long childID, long parentID, StoreType storeType, String owner, boolean isActive)
			throws PMException;

	void updateAssignment(long policyID, long childID, long parentID, StoreType storeType, String owner,
			boolean isActive) throws PIPException;

	void associate(long policyID, long uaID, long targetID, Set<String> operations, boolean isAssociationActive,
			StoreType associationStatus) throws PMException;

	void dissociate(long policyID, long uaID, long targetID) throws PMException;

	Map<Long, Set<String>> getSourceAssociations(long sourceID) throws PMException;

	Map<Long, Set<String>> getTargetAssociations(long targetID) throws PMException;

	long getIdByName(String name) throws PIPException;

	boolean checkAssignment(long childID, long parentID) throws PIPException, PMDBException;

	boolean checkPermissions(long uaID, long targetID) throws PIPException, PMDBException;

	/**
	 * Get all of the assignments in the graph.
	 *
	 * @return a set of all the assignments in the graph.
	 * @throws PMDBException if there is an error loading the assignments.
	 */
	Set<MAssignment> getAssignments() throws PIPException;

	/**
	 * Get all of the associations in the graph.
	 *
	 * @return a set of all the associations in the graph.
	 * @throws PMDBException if there is an error loading the associations.
	 */
	Set<MAssociation> getAssociations() throws PIPException;
 
	MAssignment getAssignment(long assignment_num)  throws PIPException;
}
