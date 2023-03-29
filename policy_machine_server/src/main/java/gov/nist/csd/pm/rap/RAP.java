package gov.nist.csd.pm.rap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import gov.nist.csd.pm.common.constants.Constants;
import gov.nist.csd.pm.common.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.common.exceptions.PMConfigurationException;
import gov.nist.csd.pm.common.exceptions.PMDBException;
import gov.nist.csd.pm.common.exceptions.PMGraphException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.db.DatabaseContext;
import gov.nist.csd.pm.pip.db.mysql.MySQLStore;
import gov.nist.csd.pm.rap.asset.AssetPAP;

public class RAP {

	private static RAP mRAP;
	private AssetPAP mAssetPAP;

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
	public static synchronized RAP getRAP() throws PMException {
		if(null == mRAP) {
			mRAP = new RAP();
		}
		return mRAP;
	}

	/**
	 * Reinitialize the RAP with the given database context information.
	 *
	 * @param ctx The database context to reinitialize the RAP with.
	 * @return a PAP instance
	 * @throws PMGraphException         if there is an error checking the metadata
	 *                                  in the graph.
	 * @throws PMDBException            if there is an error connecting to the
	 *                                  database.
	 * @throws PMConfigurationException if there is an error with the configuration
	 *                                  of the PAP.
	 * @throws PMAuthorizationException if the current user cannot carry out an
	 *                                  action.
	 */
	public static synchronized void getRAP(DatabaseContext ctx) throws PMException {
		mRAP = new RAP(ctx);
	}

	private RAP() throws PMException {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("rap.config");
			if (is == null) {
				throw new PMConfigurationException("/resource/rap.config does not exist");
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

	private RAP(DatabaseContext ctx) throws PMException {
		init(ctx);
	}

	/**
	 * Instantiate the backend database, in memory graph, and DAOs. Then, make sure
	 * the super user, user attribute, and policy class exist, if not, add them to
	 * the graph. This is done because there must always be a super or root user.
	 *
	 * @param ctx the database connection information.
	 */
	private void init(DatabaseContext ctx) throws PMException {

		/*
		 * Load the database settings from database file here
		 */

		if (ctx.getDatabase().equalsIgnoreCase(Constants.MYSQL)) {
			mAssetPAP = new AssetPAP(new MySQLStore(ctx));
		} else {
			throw new PMException("Failed to init RAP!");
		}
	}

	public void reset() throws PMException {
		// nothing to reset here
	}

	public AssetPAP getAssetPAP() {
		return mAssetPAP;
	}

	/**
	 * Reinitialize the RAP. Set up the NGAC backend. This is primarily used when
	 * loading a configuration. Loading a configuration makes changes to the
	 * database but not the in memory graph. So, the in-memory graph needs to get
	 * the updated graph from the database.
	 */
	public static void reinitialize() throws PMException {
		mRAP = new RAP();
	}
}
