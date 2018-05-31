package HelperClasses;

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

    /**
     *
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @param userRole
     * @return
     */
    private Object getUserClass(String email, String password, String firstName, String lastName, User.Role userRole){
        if (userRole == User.Role.admin){
            return new Admin(email,password,firstName,lastName);
        }
        else if (userRole == User.Role.student){
            return new Student(email,password,firstName,lastName);
        }
        else if (userRole == User.Role.seminarLeader){
            return new SeminarLeader(email,password,firstName,lastName);
        }
        else if (userRole == User.Role.lecturer){
            return new Lecturer(email,password,firstName,lastName);
        }
        return null;
    }

    /**
     *
     * @param email
     * @param password
     * @return if user exist return the  User class object, if not exist return null
     */
    public User getUser(String email, String password) {
        Connection connection = connectionPool.getConnectionFromPool();
        try {
            Statement statement = connection.createStatement();
            String query = "Select * FROM " + Config.MYSQL_DATABASE_NAME + "."
                    + "users WHERE email=" + email + " AND password=" + password;
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String firstName = result.getString(1);
                String lastName = result.getString(2);
                String userRole = result.getString(5);
                User.Role role =User.Role.student;
                return (User) getUserClass(email, password, firstName, lastName, role);
            }
            connectionPool.returnConnectionToPool(connection);
        } catch (SQLException e) {
            System.err.println("exception in creation statement");
        }
        return null;
    }

    public static void main(String args[]){
        UserDAo user = new UserDAo(new ConnectionPoolManager());
        User u = user.getUser("prochi","traki");
        System.out.println(u.getEmail());
    }
}
