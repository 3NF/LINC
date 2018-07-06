package Database;

import Models.Suggestion;
import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;

public class SuggestionDAO {

    private final MysqlDataSource connectionPool;
    private final UserStorage userStorage;

    public SuggestionDAO(MysqlDataSource connectionPool, UserStorage userStorage) {
        this.connectionPool = connectionPool;
        this.userStorage = userStorage;
    }


    public Suggestion addSuggestion(User user, String codeFileID, String type, String content, int startInd, int endInd){
        String query = "INSERT INTO suggestions(userID, Code_FileID, text, time, type, startInd, endInd) VALUES" +
                "(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement;
        String suggestionID;
        Timestamp timestamp;

        try {
            Connection connection = connectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getUserId());
            statement.setString(2, codeFileID);
            statement.setString(3, content);
            timestamp = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(4, timestamp);
            statement.setString(5, type);
            statement.setString(6, ""+startInd);
            statement.setString(7, ""+endInd);
            if (statement.executeUpdate() > 0){
                ResultSet result = statement.getGeneratedKeys();
                if (result.next()) {
                    suggestionID = result.getString(1);
                } else {
                    throw new SQLException();
                }
            } else {
                throw new SQLException();
            }
            statement.close();

            return new Suggestion(Suggestion.SuggestionType.valueOf(type), user.getUserId(), codeFileID, suggestionID, startInd, endInd, content, timestamp);
        } catch (SQLException e) {
            System.err.println("Error in suggestion addition");
            e.printStackTrace();

            return null;
        }
    }

    public void deleteSuggestion(String suggestionID){
        String query = "DELETE suggestions.*,replies.*" +
                "FROM suggestions " +
                "INNER JOIN replies ON suggestions.id= replies.suggestionID where suggestions.id=?";
        PreparedStatement statement;
        try {
            Connection connection = connectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1,suggestionID);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
