package Database;

import Models.Reply;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;

import static Database.Config.*;
import static org.junit.Assert.*;

public class SuggestionDAOTest {
    private MysqlDataSource source;
    private SuggestionDAO DAO;
    private Connection connection;
    @Before
    public void createSuggestionDAO(){
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
        DAO = new SuggestionDAO(source);
        DAO.getSuggestionReplys("1");
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting connection");
        }
    }

    private void insertIntoDataBase(String id,String userId,String text,String suggestionId) throws SQLException {
        String query = "INSERT INTO " + Config.MYSQL_DATABASE_NAME + ".reply(id,userid,text,suggestionid,date) VALUES(?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,id);
        statement.setString(2,userId);
        statement.setString(3,text);
        statement.setString(4,suggestionId);

        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        statement.setDate(5, sqlDate);
        statement.executeUpdate();
    }

    private void deleteFromDataBase(String id) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "DELETE FROM " + Config.MYSQL_DATABASE_NAME + ".reply WHERE id=" + id;
        statement.execute(query);
    }
    @Test
    public void testGetSuggestionReplys() throws SQLException {
        insertIntoDataBase("-1","2323","232","-1");
        insertIntoDataBase("-2","2323","232","-1");
        insertIntoDataBase("-3","2323","232","-1");
        List<Reply> replys = DAO.getSuggestionReplys("-1");
        assertEquals(replys.get(0).getSuggestionID(),"-1");
        assertEquals(replys.get(0).getReplyID(),"-3");
        assertEquals(replys.get(1).getSuggestionID(),"-1");
        assertEquals(replys.get(1).getReplyID(),"-2");
        assertEquals(replys.get(2).getSuggestionID(),"-1");
        assertEquals(replys.get(2).getReplyID(),"-1");
        assertEquals(replys.size(),3);
        deleteFromDataBase("-1");
        deleteFromDataBase("-2");
        deleteFromDataBase("-3");
    }
}