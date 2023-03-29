package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.common.exceptions.PMAuthenticationException;
import gov.nist.csd.pm.common.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.common.exceptions.PMConfigurationException;
import gov.nist.csd.pm.common.exceptions.PMDBException;
import gov.nist.csd.pm.common.exceptions.PMGraphException;
import gov.nist.csd.pm.exceptions.PMException;

public class SessionsService extends Service {


	public SessionsService() {
	}

	/**
	 * Given a username and password, check that the user exists and the password
	 * matches the one stored for the user. If the user is authenticated, return a
	 * new session ID.
	 *
	 * @param username The name of the user to create a session for.
	 * @param password The password the user provided, to be checked against the
	 *                 password stored for the user.
	 * @return the ID of the new session.
	 *
	 * @throws PMGraphException          If a node with the user's name does not
	 *                                   exist.
	 * @throws PMAuthenticationException If the provided password does not match the
	 *                                   stored password.
	 * @throws PMGraphException          If there is an error hashing the provided
	 *                                   user password.
	 */
	public String createSession(String username, String password) throws PMException {

		/*
		// get the user node
		Set<Node> nodes = getGraphPAP().search(username, NodeType.U.toString(), null);
		if (nodes.isEmpty()) {
			throw new PMGraphException(String.format("node with name %s could not be found", username));
		}

		Node userNode = nodes.iterator().next();

		// This source code is commented out to disable the password check for sessions
		// check password
		// String storedPass = userNode.getProperties().get(PASSWORD_PROPERTY);
		String storedPass = "10069f1ae652d183ea7888eb353dab8e8aec82cd64f340e79b8971df9e83fdcbc68a15800181b32aa7080574ed8df2b627bdd66dd68bad66be2eae3cb1df8ee3d185feb55a15788d6cbeec3d587ed4f16e4";
		try {
			if (!checkPasswordHash(storedPass, password)) {
				throw new PMAuthenticationException("username or password did not match");
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new PMGraphException(e.getMessage());
		}

		// create session id
		String sessionID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

		// create session in the PAP
		getSessionManager().createSession(sessionID, userNode.getID());

		return sessionID;
		*/
		return null;
	}

	public long getSessionUserID(String sessionID) throws PMException {
		return getSessionManager().getSessionUserID(sessionID);
	}

	/**
	 * Delete the session with the given ID.
	 *
	 * @param sessionID The ID of the session to delete.
	 * @throws PMConfigurationException if there is an error with the PAP
	 *                                  configuration.
	 * @throws PMAuthorizationException if the current user is not permitted to
	 *                                  perform an action.
	 * @throws PMDBException            if there is an error accessing the database.
	 * @throws PMGraphException         if there is an error accessing the graph.
	 */
	public void deleteSession(String sessionID) throws PMException {
		getSessionManager().deleteSession(sessionID);
	}
}
