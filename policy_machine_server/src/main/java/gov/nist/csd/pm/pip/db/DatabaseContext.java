package gov.nist.csd.pm.pip.db;

/**
 * Class to hold information about a mDatabase connection. This can be used for
 * Neo4j and MySQL. The mSchema field will be ignored for Neo4j.
 */
public class DatabaseContext {
	private int mPort;
	private String mHost;
	private String mSchema;
	private String mUsername;
	private String mPassword;
	private String mDatabase;

	public DatabaseContext(String host, int port, String username, String password, String schema) {
		super();
		this.mHost = host;
		this.mPort = port;
		this.mUsername = username;
		this.mPassword = password;
		this.mSchema = schema;
	}

	public DatabaseContext(String host, int port, String username, String password, String schema, String database) {
		this.mHost = host;
		this.mPort = port;
		this.mUsername = username;
		this.mPassword = password;
		this.mSchema = schema;
		this.mDatabase = database;
	}

	public String getHost() {
		return mHost;
	}

	public int getPort() {
		return mPort;
	}

	public String getUsername() {
		return mUsername;
	}

	public String getPassword() {
		return mPassword;
	}

	public String getSchema() {
		return mSchema;
	}

	public String getDatabase() {
		return mDatabase;
	}

	public void setDatabase(String mDatabase) {
		this.mDatabase = mDatabase;
	}

	@Override
	public String toString() {
		return "host: " + mHost + "\nport: " + mPort + "\nusername: " + mUsername + "\npassword: " + mPassword
				+ "\ndatabase: " + mDatabase + "\nschema: " + mSchema;
	}
}
