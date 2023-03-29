package gov.nist.csd.pm.pip.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.jdbc.Driver;

import gov.nist.csd.pm.common.exceptions.PIPException;
import gov.nist.csd.pm.common.exceptions.PMDBException;

/**
 * Object that stores a connection to a MySQL database.
 */
public class MySQLConnection {
	private int port;
	private String host;
	private String username;
	private String password;
	private String schema;

	/**
	 * Establishes a new connection to a MySQL database.
	 *
	 * @param host     the hostname of the Neo4j instance.
	 * @param port     the port the MySQL instance is running on.
	 * @param username the name of the MySQL user.
	 * @param password the password of the MySQL user.
	 * @throws PMDBException When there's an error connecting to the MySQL instance.
	 */
	public MySQLConnection(String host, int port, String username, String password, String schema) throws PIPException {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.schema = schema;
		// Register MySQL driver

		try {
			Driver driver = new com.mysql.cj.jdbc.Driver();
			DriverManager.registerDriver(driver);
		} catch (SQLException e) {
			throw new PIPException(e.getMessage());
		}
	}

	/**
	 * @return the connection to the MySQL instance.
	 * @throws PIPException if there is an error establishing the connection.
	 */
	public Connection getConnection() throws PIPException {
		// Implement getConnection for MySQL Driver

		Connection con;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + schema
					+ "?allowPublicKeyRetrieval=true&useSSL=false", username, password);
			return con;
		} catch (SQLException ex) {
			throw new PIPException(ex.getMessage());
		} catch (ClassNotFoundException e) {
			throw new PIPException(e.getMessage());
		}
	}
}
