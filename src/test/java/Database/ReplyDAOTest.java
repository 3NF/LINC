package Database;

import Models.Reply;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.List;

import static Database.Config.*;
import static org.junit.Assert.*;

public class ReplyDAOTest {
    private MysqlDataSource source;
    private ReplyDAO DAO;
    private Connection connection;

    @Before
    public void createSuggestionDAO() {
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
        DAO = new ReplyDAO(source, new UserStorage(GAPIManager.getInstance()));
        DAO.getSuggestionReplies("1");
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting connection");
        }
    }

    private void insertIntoDataBase(String id, String userId, String text, String suggestionId) throws SQLException {
        String query = "INSERT INTO " + Config.MYSQL_DATABASE_NAME + ".replies(id,userid,text,suggestionid,date) VALUES(?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        statement.setString(2, userId);
        statement.setString(3, text);
        statement.setString(4, suggestionId);

        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        statement.setDate(5, sqlDate);
        statement.executeUpdate();
    }

    private void deleteFromDataBase(String id) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "DELETE FROM " + Config.MYSQL_DATABASE_NAME + ".replies WHERE id=" + id;
        statement.execute(query);
    }

    @Test
    public void testGetSuggestionReplys() throws SQLException {
        insertIntoDataBase("-1", "2323", "232", "-1");
        insertIntoDataBase("-2", "2323", "232", "-1");
        insertIntoDataBase("-3", "2323", "232", "-1");
        List<Reply> replies = DAO.getSuggestionReplies("-1");
        assertEquals(replies.get(0).getSuggestionID(), "-1");
        assertEquals(replies.get(0).getReplyID(), "-3");
        assertEquals(replies.get(1).getSuggestionID(), "-1");
        assertEquals(replies.get(1).getReplyID(), "-2");
        assertEquals(replies.get(2).getSuggestionID(), "-1");
        assertEquals(replies.get(2).getReplyID(), "-1");
        assertEquals(replies.size(), 3);
        deleteFromDataBase("-1");
        deleteFromDataBase("-2");
        deleteFromDataBase("-3");
    }

    /*
    @Test
    public void testAddReply() {
        java.util.Date date = new java.util.Date();
        DAO.addReply("ვერ გავიგე", "2", "6", date);
        DAO.addReply("მაინც ვერ გავიგე", "2", "6", date);

        DAO.addReply("ვერ გავიგე", "2", "7", date);
        DAO.addReply("Reply2 sugg2", "2", "7", date);

        DAO.addReply("ვერ გავიგე", "2", "8", date);
        DAO.addReply("Reply2 sugg3", "2", "8", date);

        DAO.addReply("ვერ გავიგე", "2", "9", date);
        DAO.addReply("Reply2 sugg4", "2", "9", date);

        DAO.addReply("ვერ გავიგე", "2", "10", date);
        DAO.addReply("Reply2 sugg5", "2", "10", date);
    }*/

    @Test
    public void testDeleteReply() throws SQLException {
        insertIntoDataBase("-1", "2323", "232", "-1");
        DAO.deleteReply("-1");
        String query = "SELECT * FROM " + Config.MYSQL_DATABASE_NAME + ".replies WHERE userId='-1'";
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        if (resultSet.next()) {
            assertEquals(1, 2);
        }
    }
}