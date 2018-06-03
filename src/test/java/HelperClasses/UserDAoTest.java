package HelperClasses;

import Database.Config;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class UserDAoTest {

    private UserDAo userDAo;
    private BasicDataSource ds;

    @Before
    public void biuld() {
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(Config.MYSQL_DATABASE_SERVER);
        ds.setUsername(Config.MYSQL_USERNAME);
        ds.setPassword(Config.MYSQL_PASSWORD);
        userDAo = new UserDAo(ds);
    }

    @Test
    public void getUser_1() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            System.err.println("eror in get connection");
        }
        Statement statement = null;
        try {
            statement = conn.createStatement();
            String query = "INSERT INTO " + Config.MYSQL_DATABASE_NAME + ".users (firstname,secondname,email,password,userrole) "
                    + "VALUES('giorgi','baghdavadze','gbagh16@freeuni.edu.ge','giorgi121','student')";
            statement.execute(query);
        } catch (SQLException e) {
            System.err.println("eror in creation statement");
        }
        User user = userDAo.getUser("gbagh16@freeuni.edu.ge", "giorgi121");
        assertEquals(user.getEmail(), "gbagh16@freeuni.edu.ge");
        assertEquals(user.getLastName(), "baghdavadze");
        assertEquals(user.getFirstName(), "giorgi");
        assertEquals(user.getRole(), User.Role.student);

        /**
         * deleting inserted row
         */
        String query = "DELETE FROM " + Config.MYSQL_DATABASE_NAME + ".users WHERE email='gbagh16@freeuni.edu.ge'";
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.err.println("eror in creation statement");
        }

        /*
         check if after deletiong giorgi baghdavadze exist or not
         */

        user = userDAo.getUser("gbagh16@freeuni.edu.ge", "giorgi121");
        assertEquals(user, null);
    }
}