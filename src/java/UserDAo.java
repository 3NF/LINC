import Database.Config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAo {
    /*
        connection pull manager
     */
    private final ConnectionPoolManager connectionPool;

    /**
     * contructor of StudentDAo class
     *
     * @param connectionPool
     */
    public UserDAo(ConnectionPoolManager connectionPool) {
        this.connectionPool = connectionPool;
    }

    public User getUser(String email, String password) {
        Connection connection = connectionPool.getConnectionFromPool();
        try {
            Statement statement = connection.createStatement();
            String query = "Select email,password FROM " + Config.MYSQL_DATABASE_NAME + "."
                    + "users WHERE email=" + email + " AND password=" + password;
            ResultSet result = statement.executeQuery(query);

        } catch (SQLException e) {
            System.err.println("exception in creation statement");
        }
        return null;
    }
}
