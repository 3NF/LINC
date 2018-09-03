package Database;

import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static Database.Config.*;
import static org.junit.Assert.*;

public class ValidateDAOTest {
    private MysqlDataSource source;
    private ValidateDAO DAO;

    @Before
    public void createValidateDao() {
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
        DAO = new ValidateDAO(source);
        try {
            source.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting connection");
        }
    }

    @Test
    public void testValidation(){
       System.err.println(DAO.checkSuggestionAccess(new User("114260512501360115146"),"9","15887333289"));
    }

    @Test
    public void testHasSuggestionWritePermissionLuka () {
        User user = new User("", "", "", "114260512501360115146", "", "", "");
        String courseID = "15887333289";
        String codeFileID = "1";

        assertTrue(DAO.hasSuggestionWritePermission(user, courseID, codeFileID));
    }

    @Test
    public void testHasSuggestionWritePermissionGiorgi () {
        User user = new User("", "", "", "105303857051815287047", "", "", "");
        String courseID = "15887333289";
        String codeFileID = "1";

        assertFalse(DAO.hasSuggestionWritePermission(user, courseID, codeFileID));
    }

    @Test
    public void test1 () {
        String courseID = "15924932251";
        String userId = "104176062122048371294";
        System.out.println(DAO.isTeacher(userId, courseID));
    }
}