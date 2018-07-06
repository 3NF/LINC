package Database;

import Models.Reply;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReplyDAO {

    private final MysqlDataSource connectionPool;
    private final UserStorage userStorage;
    private final String replies = "replies";
    /**
     * Constructor of StudentDAo class
     *
     * @param connectionPool
     */


    public ReplyDAO(MysqlDataSource connectionPool, UserStorage userStorage) {
        this.connectionPool = connectionPool;
        this.userStorage = userStorage;
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
            String query = "SELECT * FROM " + Config.MYSQL_DATABASE_NAME + "." + replies + " WHERE suggestionID=" + id + " order by id";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String replyId = result.getString("id");
                String userId = result.getString("userId");
                String content = result.getString("text");
                String suggestionId = result.getString("suggestionID");
                Date date = result.getTimestamp("date");
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


    public Reply addReply(String text, String userId, String suggestionId) {
        Connection connection;
        java.sql.Timestamp timestamp;

        try {
            connection = connectionPool.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting connection");
            return null;
        }
        PreparedStatement statement;
        String query = "INSERT INTO " + replies + " (userid,text,suggestionid,date) VALUES(?,?,?,?)";
        String replyID = "";
        try {
            String generatedColumns[] = { "id" };
            statement = connection.prepareStatement(query,generatedColumns);
            statement.setString(1, userId);
            statement.setString(2, text);
            statement.setString(3, suggestionId);
            timestamp = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(4, timestamp);
            if (statement.executeUpdate() > 0){
                ResultSet result = statement.getGeneratedKeys();
                if (result.next())
                    replyID = result.getString(1);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("error in creation statement");
            e.printStackTrace();
            return null;
        }

        return new Reply(suggestionId, replyID, userId, text, timestamp);
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
        String query = "DELETE FROM " + replies +  " WHERE id=?";
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
