package Database;

import Models.Suggestion;
import Models.CodeFile;
import Models.File;
import Models.UploadedAssignment;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CodeFilesDAO {
    private final MysqlDataSource connectionPool;

    public CodeFilesDAO(MysqlDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CodeFile getFilesContent(String userId, String FileId) throws SQLException {
        Connection connection = connectionPool.getConnection();

        String query = "SELECT assignment_files.lang,assignment_files.name,code_files.content,code_files.id FROM assignment_files inner join " +
                "code_files on assignment_files.id=code_files.filesID where code_files.userID=? AND code_files.filesID=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,userId);
        statement.setString(2,FileId);
        ResultSet result = statement.executeQuery();
        String codeContent = "";
        String fileName = "";
        String codeLang = "";
        String codeFileId = "";
        if (result.next()){
            codeContent = result.getString("content");
            fileName = result.getString("name");
            codeLang = result.getString("lang");
            codeFileId = result.getString("id");
            //System.out.println(codeFileId);
        }

        query = "SELECT * FROM " + Config.MYSQL_DATABASE_NAME + ".suggestions WHERE Code_FileID=?";
        statement = connection.prepareStatement(query);
        statement.setString(1,codeFileId);
        //System.err.println(query);
        result = statement.executeQuery();
        List<Suggestion> suggestions = new ArrayList<>();
        while (result.next()){
            String suggestionId = result.getString("id");
            String uId = result.getString("userId");
            String text = result.getString("text");
            String type = result.getString("type");
            Date date = new Date(result.getDate("time").getTime());
            Suggestion.SuggestionType suggestionType = Suggestion.SuggestionType.valueOf(type);
            int startInd = result.getInt("startInd");
            int endInd = result.getInt("endInd");
            //System.err.println(suggestionId + ' ' + uId + ' ' + type);
            suggestions.add(new Suggestion(suggestionType,uId, codeFileId,suggestionId,startInd,endInd,text,date));
        }
        statement.close();
        connection.close();
        return new CodeFile(codeContent,codeFileId,fileName,suggestions,codeLang);
    }

    public void tempSaveCodeFile (long userId, long fileID, String content) throws SQLException {
        Connection connection = connectionPool.getConnection();

        String query = "INSERT INTO code_files (userID, filesID, content) VALUES\n" +
                "  (?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, userId);
        statement.setLong(2, fileID);
        statement.setString(3, content);

        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    public void tempSaveSuggestion (Suggestion suggestion) throws SQLException {
        Connection connection = connectionPool.getConnection();

        String query = "INSERT INTO suggestions (userID, Code_FileID, text, time, type, startInd, endInd) VALUES\n" +
                "  (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, suggestion.userID);
        statement.setString(2, suggestion.fileID);
        statement.setString(3, suggestion.content);
        statement.setDate(4, new java.sql.Date(suggestion.timeStamp.getTime()));
        statement.setString(5, suggestion.type.toString());
        statement.setInt(6, suggestion.startInd);
        statement.setInt(7, suggestion.endInd);

        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    private HashMap<String,String> getIdNameMap(UploadedAssignment assignment) throws SQLException {
        HashMap<String,String> fileId = new HashMap<String,String>();
        Connection connection = connectionPool.getConnection();
        String query = "SELECT * FROM assignment_files WHERE assignmentID=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,assignment.getAssignmentID());
        ResultSet result = statement.executeQuery();
        while (result.next()){
            String id = result.getString("id");
            String name = result.getString("name");
            fileId.put(name,id);
        }
        statement.close();
        connection.close();
        return fileId;
    }

    // TODO: 6/30/18 giorgi 
    public void addAssignments(String userID, UploadedAssignment assignment) throws SQLException {
        HashMap<String,String> fileId = getIdNameMap(assignment);
        Connection connection = connectionPool.getConnection();
        String query = "INSERT INTO code_files(userID,filesID,content) VALUES(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        boolean isFirst = true;
        for (Object file: assignment) {
            statement.setString(1, userID);
            statement.setString(2, fileId.get(((File) file).getFileName()));
            statement.setString(3, ((File)file).getContent());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
        connection.close();
        //connection.createStatement().execute(query);
    }

    public List<CodeFile.Info> getAssignmentCodeNames(String id) throws SQLException {
        String query = "Select id,name FROM assignment_files where assignmentID=?";
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,id);
        ResultSet result = statement.executeQuery();
        List <CodeFile.Info> codeFileNames = new ArrayList<CodeFile.Info>();
        while (result.next()){
            codeFileNames.add(new CodeFile.Info(result.getString("id"),result.getString("name")));
        }
        statement.close();
        connection.close();
        return codeFileNames;
    }
}
