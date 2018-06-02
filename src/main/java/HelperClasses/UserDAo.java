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
     * contructor of StudentDAo class
     *
     * @param connectionPool
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
     * @param email
     * @param password
     * @return
     */
    public User getUser(String email, String password) {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting connection");
        }
        try {
            Statement statement = connection.createStatement();
            String query = "Select * FROM " + Config.MYSQL_DATABASE_NAME + "." + "users WHERE email=" + "'" + email + "'" + " AND password=" + "'" + password + "'";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String firstName = result.getString("firstname");
                String lastName = result.getString("secondname");
                String userRole = result.getString("userrole");
                return (User) new User(email,firstName,lastName, User.Role.valueOf(userRole));
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println("exception in creation statement");
        }
        return null;
    }
}
