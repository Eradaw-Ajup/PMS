package gov.nist.csd.pm.pdp.services;


import gov.nist.csd.pm.common.exceptions.PMGraphException;
import gov.nist.csd.pm.decider.Decider;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.attempt.AttemptPAP;
import gov.nist.csd.pm.pap.authority.AuthorityDAO;
import gov.nist.csd.pm.pap.crypto.CryptoPAP;
import gov.nist.csd.pm.pap.graph.GraphPAP;
import gov.nist.csd.pm.pap.policy.PolicyPAP;
import gov.nist.csd.pm.pap.sessions.SessionManager;
import gov.nist.csd.pm.pdp.TReviewDecider;
import gov.nist.csd.pm.rap.RAP;
import gov.nist.csd.pm.rap.asset.AssetPAP;

/**
 * Class to provide common methods to all services.
 */
public class Service {

	/**
	 * The ID of the session currently using the service.
	 */
	private long userID;
	private String owner;
	private String username;

	/**
	 * Create a new Service with a sessionID and processID from the request context.
	 *
	 * @param userID the ID of the user.
	 * @throws PMGraphException
	 * @throws IllegalArgumentException if the user ID provided is 0.
	 */
	protected Service(long userID, String owner) throws PMException {
		if (0 == userID && (null == owner || owner.isEmpty())) {
			throw new PMGraphException("no user or owner was provided to the service");
		}
		this.userID = userID;
		this.owner = owner;
	}

	protected Service(String owner) throws PMException {
		if (null == owner || owner.isEmpty())
			throw new PMGraphException("no owner was provided to the service");
		this.owner = owner;
	}

	protected Service() {
	}

	/**
	 * Get the ID of the current session.
	 *
	 * @return the current session's ID.
	 */
	protected long getUserID() {
		return userID;
	}

	/**
	 * Get the owner of the current session.
	 *
	 * @return the current owner.
	 */
	protected String getOwner() {
		return owner;
	}

	protected void setUsername(String username) {
		this.username = username;
	}

	protected String getUsername() {
		return username;
	}

	protected GraphPAP getGraphPAP() throws PMException {
		return PAP.getPAP().getGraphPAP();
	}

	protected PolicyPAP getPolicyPAP() throws PMException {
		return PAP.getPAP().getPolicyPAP();
	}

	protected AssetPAP getAssetPAP() throws PMException {
		return RAP.getRAP().getAssetPAP();
	}

	protected AttemptPAP getAttemptPAP() throws PMException {
		return PAP.getPAP().getAttemptPAP();
	}

	protected CryptoPAP getCryptoPAP() throws PMException {
		return PAP.getPAP().getCryptoPAP();
	}

	protected AuthorityDAO getAuthorityDAO() throws PMException {
		return PAP.getPAP().getAuthorityDAO();
	}

	public void reinitialize() throws PMException {
		PAP.reinitialize();
	}

	SessionManager getSessionManager() throws PMException {
		return PAP.getPAP().getSessionManager();
	}

	public Decider getDecider() throws PMException {
		return new TReviewDecider(getGraphPAP());
	}

	public static long generateRandNum() {
		long min = 1;
		long max = 1000000000;
		return (long) (Math.random() * (max - min)) + min;
	}
}
