package Database;

        import Models.*;
        import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.*;

public class CodeFilesDAO {

    public static final Set<String> extentions = new HashSet<String>(Arrays.asList(new String[]{"cpp","c","cc","h","chudo","java"}));
    private final MysqlDataSource connectionPool;

    public CodeFilesDAO(MysqlDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CodeFile getFilesContent(String codeFilesID) throws SQLException {
        Connection connection = connectionPool.getConnection();

        String query = "SELECT * FROM code_files WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,codeFilesID);
        ResultSet result = statement.executeQuery();
        String codeContent = null;
        String fileName = null;
        String codeLang = null;
        String codeFileId = null;
        if (result.next()){
            codeContent = result.getString("content");
            fileName = result.getString("path");
            codeLang = "";
            codeFileId = result.getString("id");
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

    /*public void tempSaveCodeFile (long userId, long fileID, String content) throws SQLException {
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
    }*/

    /*public void tempSaveSuggestion (Suggestion suggestion) throws SQLException {
        Connection connection = connectionPool.getConnection();

        String query = "INSERT INTO suggestions (userID, Code_FileID, text, time, type, startInd, endInd) VALUES\n" +
                "  (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, suggestion.user.getUserId());
        statement.setString(2, suggestion.fileID);
        statement.setString(3, suggestion.content);
        statement.setTimestamp(4, new java.sql.Timestamp(suggestion.timeStamp.getTime()));
        statement.setString(5, suggestion.type.toString());
        statement.setInt(6, suggestion.startInd);
        statement.setInt(7, suggestion.endInd);

        statement.executeUpdate();
        statement.close();
        connection.close();
    }*/

    private HashMap<String,String> getIdNameMap(UploadedAssignment assignment) throws SQLException {
        HashMap<String,String> fileId = new HashMap<String,String>();
        Connection connection = connectionPool.getConnection();
        String query = "Select assignments.idInClassroom,assignment_files.id,assignment_files.name FROM assignments " +
                "inner join assignment_files on assignments.id=assignment_files.assignmentID where assignments.idInClassroom=?";
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

    public void addAssignments(UploadedAssignment assignment) throws SQLException {
        HashMap<String,String> fileId = getIdNameMap(assignment);
        Connection connection = connectionPool.getConnection();
        String query = "INSERT INTO code_files(userID,filesID,content) VALUES(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        int i = 0;
        boolean isToBeInserted = false;
        for (Object file: assignment) {
            //System.err.println(((File) fi0le).getFileName());
            String fileName = ((File) file).getFileName();
            int index = fileName.lastIndexOf(".");
            String extention = fileName.substring(index + 1);
            if (!extentions.contains(extention)) continue;
            statement.setString(1, fileName);
            statement.setString(2, "-1");
            statement.setString(3, ((File)file).getContent());
            isToBeInserted = true;
            statement.addBatch();
            ++i;
            if (i % 1000 == 0 || i == assignment.size()) {
                statement.executeBatch();
                isToBeInserted = false;
            }
        }
        if (isToBeInserted)
            statement.executeBatch();
        statement.close();
        connection.close();
        //connection.createStatement().execute(query);
    }

    public List<CodeFile.Info> getAssignmentCodeNames(String assignmentID) throws SQLException {
        String query = "Select assignments.idInClassroom,assignment_files.id,assignment_files.name FROM assignments " +
                "inner join assignment_files on assignments.id=assignment_files.assignmentID where assignments.idInClassroom=?";
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, assignmentID);
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
