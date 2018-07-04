package Database;

import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static Database.Config.*;
import static org.junit.Assert.*;

public class ValidateDAOTest {
    private MysqlDataSource source;
    private ValidateDAO DAO;
    private Connection connection;

    @Before
    public void createValidateDao() {
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
        DAO = new ValidateDAO(source);
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting connection");
        }
    }

    @Test
    public void testValidation(){
       System.err.println(DAO.isValidate(new User("105303857051815287047"),"9"));
    }
}