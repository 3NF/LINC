package Database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static Database.Config.*;
import static org.junit.Assert.*;

public class SectionDAOTester {
    private MysqlDataSource source;
    private SectionDAO DAO;

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

        ArrayList <String> students = (ArrayList<String>)DAO.getUsersInSection(courseID, userID);

        assertEquals(students.size(), 1);
    }

    @Test
    public void test2() {
        List <String> usersID = new ArrayList<>();
        usersID.add("23");
        usersID.add("123");
        DAO.addUsersInSection("15887333289","104406936880731130000",usersID);
    }

    @Test
    public void test3(){
        DAO.removeSections("15924932251");
    }
}