package gov.nist.csd.pm.pap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import gov.nist.csd.pm.common.constants.Constants;
import gov.nist.csd.pm.common.constants.StoreType;
import gov.nist.csd.pm.common.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.common.exceptions.PMConfigurationException;
import gov.nist.csd.pm.common.exceptions.PMDBException;
import gov.nist.csd.pm.common.exceptions.PMGraphException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.attempt.AttemptPAP;
import gov.nist.csd.pm.pap.authority.AuthorityDAO;
import gov.nist.csd.pm.pap.authority.AuthorityDAOImpl;
import gov.nist.csd.pm.pap.crypto.CryptoPAP;
import gov.nist.csd.pm.pap.graph.GraphPAP;
import gov.nist.csd.pm.pap.model.MAssignment;
import gov.nist.csd.pm.pap.model.MAssociation;
import gov.nist.csd.pm.pap.model.MNode;
import gov.nist.csd.pm.pap.policy.PolicyPAP;
import gov.nist.csd.pm.pap.sessions.SessionManager;
import gov.nist.csd.pm.pdp.prohibitions.ProhibitionDAO;
import gov.nist.csd.pm.pip.db.DatabaseContext;
import gov.nist.csd.pm.pip.db.mysql.MySQLStore;
import gov.nist.csd.pm.pip.db.neo4j.Neo4jStore;

/**
 * PAP is the Policy Information Point. The purpose of the PAP is to expose the
 * underlying policy data to the PDP and EPP. It initializes the backend using
 * the connection properties in /resource/db.config. This servlet can be access
 * via ../index.jsp upon starting the server.The PAP also stores the in memory
 * graph that will be used for decision making.
 */
public class PAP {
	/**
	 * A static instance of the PAP to be used by the PDP.
	 */
	private static PAP pap;

	private GraphPAP graphPAP;
	private PolicyPAP policiesPAP;
	private AttemptPAP attemptPAP;
	private SessionManager sessionsPAP;
	private CryptoPAP cryptoPAP;
	private AuthorityDAO authorityDAO;
	private ProhibitionDAO prohibitionDAO;

	/**
	 * Initialize a the static PAP variable if not already initialized.
	 *
	 * @return a PAP instance.
	 * @throws PMGraphException         if there is an error checking the metadata
	 *                                  in the graph.
	 * @throws PMDBException            if there is an error connecting to the
	 *                                  database.
	 * @throws PMConfigurationException if there is an error with the configuration
	 *                                  of the PAP.
	 * @throws PMAuthorizationException if the current user cannot carryout an
	 *                                  action.
	 */
	public static synchronized PAP getPAP() throws PMException {
		if (null == pap)
			pap = new PAP();
		return pap;
	}

	/**
	 * Reinitialize the PAP with the given database context information.
	 *
	 * @param ctx The database context to reinitialize the PAP with.
	 * @return a PAP instance
	 * @throws PMGraphException         if there is an error checking the metadata
	 *                                  in the graph.
	 * @throws PMDBException            if there is an error connecting to the
	 *                                  database.
	 * @throws PMConfigurationException if there is an error with the configuration
	 *                                  of the PAP.
	 * @throws PMAuthorizationException if the current user cannot carryout an
	 *                                  action.
	 */
	public static synchronized void getPAP(DatabaseContext ctx) throws PMException {
		pap = new PAP(ctx);
	}

	private PAP() throws PMException {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("db.config");
			if (is == null) {
				throw new PMConfigurationException("/resource/db.config does not exist");
			}
			Properties props = new Properties();
			props.load(is);

			DatabaseContext dbCtx = new DatabaseContext(props.getProperty("host"),
					Integer.valueOf(props.getProperty("port")), props.getProperty("username"),
					props.getProperty("password"), props.getProperty("schema"), props.getProperty("database"));

			init(dbCtx);
		} catch (IOException e) {
			throw new PMConfigurationException(e.getMessage());
		}
	}

	private PAP(DatabaseContext ctx) throws PMException {
		init(ctx);
	}

	public GraphPAP getGraphPAP() {
		return graphPAP;
	}

	public SessionManager getSessionManager() {
		return sessionsPAP;
	}

	public PolicyPAP getPolicyPAP() {
		return policiesPAP;
	}

	public AttemptPAP getAttemptPAP() {
		return attemptPAP;
	}

	public CryptoPAP getCryptoPAP() {
		return cryptoPAP;
	}

	public AuthorityDAO getAuthorityDAO() {
		return authorityDAO;
	}

	public ProhibitionDAO getProhibitionDAO() {
		return prohibitionDAO;
	}

	/**
	 * Instantiate the backend database, in memory graph, and DAOs. Then, make sure
	 * the super user, user attribute, and policy class exist, if not, add them to
	 * the graph. This is done because there must always be a super or root user.
	 *
	 * @param ctx the database connection information.
	 */
	private void init(DatabaseContext ctx) throws PMException {
		// Create a memory graph
		TMemGraph memGraph = new TMemGraph();

		// Create a new graph pap with the in memory graph and db graph
		// Load the database settings from database file here
		if (ctx.getDatabase().equalsIgnoreCase(Constants.NEO4J)) {
			graphPAP = new GraphPAP(memGraph, new Neo4jStore(ctx));
			policiesPAP = new PolicyPAP(new Neo4jStore(ctx));
		} else {
			MySQLStore sqlStore = new MySQLStore(ctx);
			graphPAP = new GraphPAP(memGraph, sqlStore);
			policiesPAP = new PolicyPAP(sqlStore);
			attemptPAP = new AttemptPAP(sqlStore);
			cryptoPAP = new CryptoPAP(sqlStore);
			authorityDAO = new AuthorityDAOImpl(sqlStore);
			prohibitionDAO = new ProhibitionDAO(sqlStore);
		}
		sessionsPAP = new SessionManager();
		load(graphPAP, memGraph);
	}

	private static void load(GraphPAP graphPAP, TMemGraph memGraph) throws PMException {
		// Loading non-staging nodes here
		Set<MNode> nodes = (Set<MNode>) graphPAP.getDatabaseGraph().getNodes();
		for (MNode node : nodes) {
			memGraph.createNode(node.getID(), node.getName(), node.getType(), node.getProperties(), node.isNodeActive(),
					StoreType.toStoreType(node.getStoreType()), node.getPolicyID(), node.getOwner(),
					node.getAttributeID(), node.getAuthorityID());
		}

		// Load non-staging assignments
		Set<MAssignment> assignments = graphPAP.getDatabaseGraph().getAssignments();
		for (MAssignment assignment : assignments) {
			long childID = assignment.getSourceID();
			long parentID = assignment.getTargetID();
			memGraph.assign(childID, parentID, assignment.isAssignmentActive(),
					StoreType.toStoreType(assignment.getAssignmentStatus()), assignment.getPolicyID());
		}

		// Load non-staging associations
		Set<MAssociation> associations = graphPAP.getDatabaseGraph().getAssociations();
		for (MAssociation association : associations) {
			long uaID = association.getSourceID();
			long targetID = association.getTargetID();
			Set<String> operations = association.getOperations();
			memGraph.associate(uaID, targetID, operations, association.isAssociationActive(),
					StoreType.toStoreType(association.getAssociationStatus()), association.getPolicyID());
		}
	}

	public void reset() throws PMException {
		// Delete nodes
		//graphPAP.reset();

		// Delete sessions
		sessionsPAP.reset();
	}

	/**
	 * Reinitialize the PAP. Set up the NGAC backend. This is primarily used when
	 * loading a configuration. Loading a configuration makes changes to the
	 * database but not the in memory graph. So, the in-memory graph needs to get
	 * the updated graph from the database.
	 */
	public static void reinitialize() throws PMException {
		pap = new PAP();
	}
}
