package Database;

import Models.Reply;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReplyDAO {

    private final MysqlDataSource connectionPool;

    /**
     * Constructor of StudentDAo class
     *
     * @param connectionPool
     */


    public ReplyDAO(MysqlDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<Reply> getSuggestionReplies(String id) {
        Connection connection;
        ArrayList<Reply> suggestionsReply = new ArrayList<>();
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM " + Config.MYSQL_DATABASE_NAME + ".reply WHERE suggestionID=" + id + " order by id";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String replyId = result.getString("id");
                String userId = result.getString("userId");
                String content = result.getString("text");
                String suggestionId = result.getString("suggestionID");
                Date date = new Date(result.getDate("date").getTime());
                suggestionsReply.add(new Reply(suggestionId, replyId, userId, content, date));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("error in creation statement");
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return null;
        }


        return suggestionsReply;
    }


    public void addReply(String text, String userId, String suggestionId, Date date) {
        Connection connection;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting conne  ction");
            return;
        }
        PreparedStatement statement;
        String query = "INSERT INTO " + Config.MYSQL_DATABASE_NAME + ".reply(userid,text,suggestionid,date) VALUES(?,?,?,?)";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, userId);
            statement.setString(2, text);
            statement.setString(3, suggestionId);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            statement.setDate(4, sqlDate);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println("error in creation statement");
            e.printStackTrace();
            return;
        }
    }

    public void deleteReply(String replyId) {
        Connection connection;
        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting connection");
            return;
        }
        PreparedStatement statement;
        String query = "DELETE FROM " + Config.MYSQL_DATABASE_NAME + ".reply WHERE id=?";
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, replyId);
            statement.execute();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
