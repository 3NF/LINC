package Database;

        import Models.*;
        import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.*;

public class CodeFilesDAO {

    private static final Set<String> extensions = new HashSet<String>(Arrays.asList("css","html","cpp","c","cc","h","chudo","java","jsp"));
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

        query = "SELECT * FROM " + Config.MYSQL_DATABASE_NAME + ".suggestions WHERE code_fileID=?";
        statement = connection.prepareStatement(query);
        statement.setString(1,codeFileId);
        result = statement.executeQuery();
        List<Suggestion> suggestions = new ArrayList<>();
        while (result.next()){
            String suggestionId = result.getString("id");
            String uId = result.getString("userId");
            String text = result.getString("text");
            String type = result.getString("type");
            Date date = result.getTimestamp("time");
            Suggestion.SuggestionType suggestionType = Suggestion.SuggestionType.valueOf(type);
            int startInd = result.getInt("startInd");
            int endInd = result.getInt("endInd");
            suggestions.add(new Suggestion(suggestionType,uId, codeFileId,suggestionId,startInd,endInd,text,date));
        }
        statement.close();
        connection.close();
        return new CodeFile(codeContent,codeFileId,fileName,suggestions,codeLang);
    }

    public void addAssignments(DownloadedAssignment assignment) throws SQLException {
        Connection connection = connectionPool.getConnection();
        String query = "INSERT INTO code_files(userID,assignmentID,content,path) VALUES(?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        int i = 0;
        boolean isToBeInserted = false;
        for (Object file: assignment) {
            String fileName = ((File) file).getFileName();
            String userID = ((File) file).getUserID();
            String assignmentID = assignment.getAssignmentID();
            int index = fileName.lastIndexOf(".");
            String extention = fileName.substring(index + 1);
            if (!extensions.contains(extention)) continue;
            statement.setString(1, userID);
            statement.setString(2, assignmentID);
            statement.setString(3, ((File)file).getContent());
            statement.setString(4, fileName);
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

    List<CodeFile.Info> getAssignmentCodeNames(String assignmentID) throws SQLException {
        String query = "Select assignments.idInClassroom,assignment_files.id,assignment_files.name FROM assignments " +
                "inner join assignment_files on assignments.id=assignment_files.assignmentID where assignments.idInClassroom=?";
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, assignmentID);
        ResultSet result = statement.executeQuery();
        List <CodeFile.Info> codeFileNames = new ArrayList<>();
        while (result.next()){
            codeFileNames.add(new CodeFile.Info(result.getString("id"),result.getString("name")));
        }
        statement.close();
        connection.close();
        return codeFileNames;
    }
}
