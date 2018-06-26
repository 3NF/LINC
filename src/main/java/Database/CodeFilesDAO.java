package Database;

import Data.Suggestion;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.xml.transform.Result;
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

    public void getFilesContent(String userId,String codeFileId) throws SQLException {
        Connection connection = connectionPool.getConnection();

        String query = "SELECT content FROM " + Config.MYSQL_DATABASE_NAME + ".code_files where userID=? AND filesID=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,userId);
        statement.setString(2,codeFileId);
        ResultSet result = statement.executeQuery();
        String codeContent = "";
        if (result.next()){
            codeContent = result.getString("content");
        }

        query = "SELECT * FROM " + Config.MYSQL_DATABASE_NAME + ".suggestions WHERE fileId=?";
        statement = connection.prepareStatement(query);
        statement.setString(1,codeFileId);
        //System.err.println(query);
        result = statement.executeQuery();
        List<Suggestion> suggestions = new ArrayList<Suggestion>();
        while (result.next()){
            String suggestionId = result.getString("suggestionID");
            String uId = result.getString("uid");
            String text = result.getString("text");
            String type = result.getString("type");
            Date date = new Date(result.getDate("time").getTime());
        }
        connection.close();
    }
}
