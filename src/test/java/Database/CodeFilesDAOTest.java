package Database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import static Database.Config.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class CodeFilesDAOTest {
    private MysqlDataSource source;
    private CodeFilesDAO DAO;
    private Connection connection;

    @Before
    public void createSuggestionDAO() {
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
        DAO = new CodeFilesDAO(source);
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting connection");
        }
    }

    @Test
    public void testGetFilesContent() {
        try {
            DAO.getFilesContent("1", "2");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}