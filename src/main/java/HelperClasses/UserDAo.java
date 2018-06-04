package HelperClasses;

import Database.Config;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAo {
	/*
		connection pull manager
	 */
	private final BasicDataSource connectionPool;

	/**
	 * constructor of StudentDAo class
	 *
	 * @param connectionPool connecion source from which user data should be retrieved
	 */
	public UserDAo(BasicDataSource connectionPool) {
		this.connectionPool = connectionPool;
	}

	public static void main(String args[]) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl(Config.MYSQL_DATABASE_SERVER);
		ds.setUsername(Config.MYSQL_USERNAME);
		ds.setPassword(Config.MYSQL_PASSWORD);
		UserDAo user = new UserDAo(ds);
		User u = user.getUser("prochi", "traki");
		System.out.println(u.getEmail());
	}

	/**
	 * Will get the user by following query
	 *
	 * @param query Mysql query for retrieving user data
	 *
	 * @return User object created from query result
	 */
	private User getUserByQuery(String query) {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
		} catch (SQLException e) {
			System.err.println("error in getting connection");
		}

		if (connection == null) return null;

		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			if (result.next()) {
				String email = result.getString("email");
				String firstName = result.getString("firstname");
				String lastName = result.getString("secondname");
				String userRole = result.getString("userrole");
				connection.close();
				return new User(email, firstName, lastName, User.Role.valueOf(userRole));
			}
		} catch (SQLException e) {
			System.err.println("exception in creation statement");
		}
		return null;
	}



	/*
	*
	* TODO-BAGHDAVADZE add addUser() method (from GCHKh)
	*
	* */
	public boolean addUser(String firsName, String lastName, String email, String password) {
		return false;
	}


	/**
	 * // TODO: 6/3/18 // TODO-GCHKH16 store and retrieve password hash from database instead of password.
	 *
	 * @param email    User email
	 * @param password User password
	 *
	 * @return Corresponding user object
	 */
	public User getUser(String email, String password) {
		String query = "Select * FROM " + Config.MYSQL_DATABASE_NAME + "." + "users WHERE email=" + "'" + email + "'" + " AND password=" + "'" + password + "'";
		return getUserByQuery(query);
	}

	/**
	 * @param email User email
	 *
	 * @return Corresponding user object
	 */
	public User getUserByEmail(String email) {
		String query = "Select * FROM " + Config.MYSQL_DATABASE_NAME + "." + "users WHERE email=" + "'" + email + "'";
		return getUserByQuery(query);
	}
}
