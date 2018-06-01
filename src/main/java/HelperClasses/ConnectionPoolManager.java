package HelperClasses;

import Database.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPoolManager {
	public static final int MAX_POOL_SIZE = 100;

	//private Vector connectionPool = new Vector();
	private BlockingQueue connectionPool;

	public ConnectionPoolManager() {
		connectionPool = new LinkedBlockingQueue(1024);
		initializeConnectionPool();
	}

	private void initializeConnectionPool() {
		while (!checkIfConnectionPoolIsFull()) {
			//Adding new connection instance until the pool is full
            try {
                connectionPool.put(createNewConnection());
            } catch (InterruptedException e) {
                System.err.println("error in adding connection");
            }
        }
		System.out.println("Connection Pool is full.");
	}

	private synchronized boolean checkIfConnectionPoolIsFull() {
		//Check if the pool size
		if (connectionPool.size() < MAX_POOL_SIZE) {
			return false;
		}
		return true;
	}

	/**
	 * this void will create the new connection for pool
	 *
	 * @return
	 */
	private Connection createNewConnection() {
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Config.MYSQL_DATABASE_SERVER, Config.MYSQL_USERNAME, Config.MYSQL_PASSWORD);
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle);
			return null;
		} catch (ClassNotFoundException cnfe) {
			System.err.println("ClassNotFoundException: " + cnfe);
			return null;
		}

		return connection;
	}

	public Connection getConnectionFromPool() {
		Connection connection = null;
		try {
			connection = (Connection) connectionPool.take();
		} catch (InterruptedException e) {
			System.err.println("Interupted Exception");
		}
		//Giving away the connection from the connection pool
		return connection;
	}

	public synchronized void returnConnectionToPool(Connection connection) {
		//Adding the connection from the client back to the connection pool
		try {
			connectionPool.put(connection);
		} catch (InterruptedException e) {
			System.err.println("Interupted Exception");
		}
	}
}
