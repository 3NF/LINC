package Database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.dbcp2.BasicDataSource;


/**
 * Singleton class for connection pool
 */
public class ConnectionPool extends MysqlDataSource {
	private static final ConnectionPool ourInstance = new ConnectionPool();

	public static ConnectionPool getInstance() {
		return ourInstance;
	}

	private ConnectionPool() {
		this.setServerName(Config.MYSQL_DATABASE_SERVER);
		this.setDatabaseName(Config.MYSQL_DATABASE_NAME);
		//this.setUrl(Config.MYSQL_DATABASE_SERVER);
		this.setUser(Config.MYSQL_USERNAME);
		this.setPassword(Config.MYSQL_PASSWORD);
	}
}
