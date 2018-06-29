package Database;

import Data.Suggestion;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CodeFilesDAO {
    private final MysqlDataSource connectionPool;

    public CodeFilesDAO(MysqlDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CodeFile getFilesContent(String userId,String codeFileId) throws SQLException {
        Connection connection = connectionPool.getConnection();

        String query = "SELECT assignment_files.lang,assignment_files.name,code_files.content FROM assignment_files inner join " +
                "code_files on assignment_files.id=code_files.filesID where code_files.userID=? AND code_files.filesID=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,userId);
        statement.setString(2,codeFileId);
        ResultSet result = statement.executeQuery();
        String codeContent = "";
        String fileName = "";
        String codeLang = "";
        if (result.next()){
            codeContent = result.getString("content");
            fileName = result.getString("name");
            codeLang = result.getString("lang");
        }

        query = "SELECT * FROM " + Config.MYSQL_DATABASE_NAME + ".suggestions WHERE fileId=?";
        statement = connection.prepareStatement(query);
        statement.setString(1,codeFileId);
        //System.err.println(query);
        result = statement.executeQuery();
        List<Suggestion> suggestions = new ArrayList<>();
        while (result.next()){
            String suggestionId = result.getString("suggestionID");
            String uId = result.getString("uid");
            String text = result.getString("text");
            String type = result.getString("type");
            Date date = new Date(result.getDate("time").getTime());
            Suggestion.SuggestionType suggestionType = Suggestion.SuggestionType.valueOf(type);
            suggestions.add(new Suggestion(suggestionType,uId,"sds",codeFileId,suggestionId,0,0,text,date));
        }
        connection.close();
        return new CodeFile(codeContent,codeFileId,fileName,suggestions,codeLang);
    }

    public void tempSave (long userId, long fileID, String content) throws SQLException {
        Connection connection = connectionPool.getConnection();

        String query = "INSERT INTO code_files (userID, filesID, content) VALUES\n" +
                "  (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, userId);
        statement.setLong(2, fileID);
        statement.setString(3, content);

        statement.executeUpdate();

        connection.close();

    }
}
