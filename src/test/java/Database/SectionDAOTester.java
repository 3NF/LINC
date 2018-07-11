package Database;

import Models.Reply;
import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static Database.Config.*;
import static org.junit.Assert.*;

public class SectionDAOTester {
    private MysqlDataSource source;
    private SectionDAO DAO;
    private Connection connection;

    @Before
    public void createSectionDAO() {
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
        DAO = new SectionDAO(source);
    }

    @Test
    public void test1() {
        String userID = "114260512501360115146";
        String courseID = "15887333289";

        ArrayList <User> students = (ArrayList)DAO.getUsersInSection(courseID, userID);

        assertEquals(students.size(), 1);
    }

}