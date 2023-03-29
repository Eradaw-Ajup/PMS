package gov.nist.csd.pm.pip.db.neo4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nist.csd.pm.common.constants.PolicyStatus;
import gov.nist.csd.pm.common.constants.PolicyType;
import gov.nist.csd.pm.common.constants.StoreType;
import gov.nist.csd.pm.common.exceptions.DBException;
import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.common.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pap.attempt.AttemptMachine;
import gov.nist.csd.pm.pap.graph.GraphMachine;
import gov.nist.csd.pm.pap.model.Attempt;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MAssociation;
import gov.nist.csd.pm.pap.model.MNode;
import gov.nist.csd.pm.pap.model.Policy;
import gov.nist.csd.pm.pap.policy.PolicyMachine;
import gov.nist.csd.pm.pip.db.DatabaseContext;

/**
 * A Neo4j implementation of a NGAC graph
 */
public class Neo4jStore implements PolicyMachine, GraphMachine, AttemptMachine {

	/**
	 * Object to store a connection to a Neo4j database.
	 */
	private Neo4jConnection neo4j;

	/**
	 * Receive context information about the database connection, and create a new
	 * connection to the Neo4j instance.
	 *
	 * @param ctx Context information about the Neo4j connection.
	 * @throws PMDBException When there is an error connecting to Neo4j.
	 */
	public Neo4jStore(DatabaseContext ctx) throws PMDBException {
		// pass schema name instead of username
		this.neo4j = new Neo4jConnection(ctx.getHost(), ctx.getPort(), ctx.getUsername(), ctx.getPassword());

		// create an index on node IDs
		// this will improve read performance
		String cypher = "create index on :NODE(id)";
		try (Connection conn = neo4j.getConnection(); PreparedStatement stmt = conn.prepareStatement(cypher)) {
			stmt.executeQuery();
		} catch (SQLException e) {
			throw new PMDBException(e.getMessage());
		}
	}

	@Override
	public MNode createNode(long id, String name, NodeType type, long policyID, Map<String, String> properties,
			StoreType nodeStoreType, String owner, long attributeID, long authorityID) throws PMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateNode(long id, String name, Map<String, String> properties, StoreType storeType, String owner,
			boolean isActive) throws PMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteNode(long nodeID, String owner) throws PMException {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<MNode> getNodes() throws PMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MNode getNode(long id) throws PMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(long nodeID) throws PMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Long> getPC() throws PMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<MNode> search(String name, String type, Map<String, String> properties, long policyID) throws PMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Long> getChildren(long nodeID) throws PMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Long> getParents(long nodeID) throws PMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void associate(long policyID, long uaID, long targetID, Set<String> operations, boolean isAssociationActive,
			StoreType associationStatus) throws PMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void dissociate(long policyID, long uaID, long targetID) throws PMException {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Long, Set<String>> getSourceAssociations(long sourceID) throws PMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, Set<String>> getTargetAssociations(long targetID) throws PMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getIdByName(String name) throws PIPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean checkAssignment(long childID, long parentID) throws PIPException, PMDBException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkPermissions(long uaID, long targetID) throws PIPException, PMDBException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<MAssignment> getAssignments() throws PIPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<MAssociation> getAssociations() throws PIPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long publishPolicy(String name, String description, String json, String tJson, String owner)
			throws PIPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long importPolicy(String owner, String name, String policy) throws PIPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String exportPolicy(long policyId) throws PIPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attempt createAttempt(String attemptType, String attemptStatus, String createdBy) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAttempt(long attemptID, String attemptStatus, String createdBy) throws DBException {
		// TODO Auto-generated method stub

	}

	@Override
	public Attempt getAttempt(long attemptID) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long createPolicy(PolicyType type, String policyStatus, String policyOwner, long prevPolicy, long nextPolicy)
			throws PIPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long createPolicyAttemptReln(long policyID, long attemptID) throws PIPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Policy> getPolicies(String policyOwner) throws PIPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Policy getPolicy(String policyOwner, long policyID) throws PIPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPolicyAndAttemptValid(long policyID, long attemptID) throws PIPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updatePolicy(String name, String description, String policyJSON, String cryptoJSON,
			PolicyStatus policyStatus, long policyID, String owner) throws PIPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void assign(long policyID, long childID, long parentID, boolean isAssignmentActive, StoreType storeType,
			String owner) throws PMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deassign(long policyID, long childID, long parentID, StoreType storeType, String owner,
			boolean isActive) throws PMException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAssignment(long policyID, long childID, long parentID, StoreType storeType, String owner,
			boolean isActive) throws PIPException {
		// TODO Auto-generated method stub

	}

	@Override
	public Policy getPolicy(long policyID) throws PIPException {
		// TODO Auto-generated method stub
		return null;
	}
}
