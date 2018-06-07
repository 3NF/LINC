package Database;

import Core.User;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;

import static Database.Config.MYSQL_DATABASE_NAME;

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

	/**
	 *
	 * @param email
	 * @return a true if a account with this email exist
	 */
	public boolean userExists(String email) {
		if (getUserByEmail(email) != null)
			return true;
		return false;
	}


	/**
	 * @param email User email
	 *
	 * @return Corresponding user object
	 */
	public User getUserByEmail(String email) {
		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+MYSQL_DATABASE_NAME+".users WHERE email=?");
			statement.setString(1,email);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				String firstName = result.getString("firstname");
				String lastName = result.getString("secondname");
				connection.close();
				return new User(email, firstName, lastName);
			}
		} catch (SQLException e) {
			System.err.println("exception in executing query");
		}
		return null;
	}
}
