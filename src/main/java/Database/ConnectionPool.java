package Database;

import org.apache.commons.dbcp2.BasicDataSource;


/**
 * Singleton class for connection pool
 */
public class ConnectionPool extends BasicDataSource {
	private static final ConnectionPool ourInstance = new ConnectionPool();

	public static ConnectionPool getInstance() {
		return ourInstance;
	}

	private ConnectionPool() {
		this.setDriverClassName("com.mysql.jdbc.Driver");
		this.setUrl(Config.MYSQL_DATABASE_SERVER);
		this.setUsername(Config.MYSQL_USERNAME);
		this.setPassword(Config.MYSQL_PASSWORD);
	}
}
