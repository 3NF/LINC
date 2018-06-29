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

        String query = "SELECT filename,content FROM code_files where userID=? AND filesID=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,userId);
        statement.setString(2,codeFileId);
        ResultSet result = statement.executeQuery();
        String codeContent = "";
        String fileName = "";
        if (result.next()){
            codeContent = result.getString("content");
            fileName = result.getString("filename");
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
            suggestions.add(new Suggestion(suggestionType,uId,codeFileId,suggestionId,0,0,text,date));
        }
        connection.close();
        return new CodeFile(codeContent,codeFileId,fileName,suggestions);
    }
}
