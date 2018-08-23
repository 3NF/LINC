package Database;

import Models.CodeFile;
import Models.File;
import Models.UploadedAssignment;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import static Database.Config.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CodeFilesDAOTest {
    private MysqlDataSource source;
    private CodeFilesDAO DAO;
    private Connection connection;

    @Before
    public void createSuggestionDAO() {
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
        DAO = new CodeFilesDAO(source);
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            System.err.println("error in getting connection");
        }
    }

    @Test
    public void testGetFilesContent() {
        try {
            CodeFile codeFile = DAO.getFilesContent("1");
            DAO.getAssignmentCodeNames("1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddAssigment() {
        try {

            UploadedAssignment assignment = new UploadedAssignment("15888564981");
            assignment.addAssignmentFile(new File("temp_code1.cpp","sdsdsdsdss","-1"));
            assignment.addAssignmentFile(new File("temp_code2.cpp","sdsdsdsssdss","-1"));
            DAO.addAssignments(assignment);
            deleteAllUserAssigment("-23");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    @Test
    public void addSuggestions1() {
        try {
            Suggestion s1 = new Suggestion(Suggestion.SuggestionType.Warning, "1",  "1", "", 1, 4, "Wow, tourist", new java.util.Date());
            Suggestion s2 = new Suggestion(Suggestion.SuggestionType.Warning, "1",  "1", "", 7, 10, "Good job, tourist", new java.util.Date());
            Suggestion s3 = new Suggestion(Suggestion.SuggestionType.Error, "1",  "1", "", 15, 100, "This is error, tourist", new java.util.Date());

            DAO.tempSaveSuggestion(s1);
            DAO.tempSaveSuggestion(s2);
            DAO.tempSaveSuggestion(s3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addSuggestions2() {
          try {
            Suggestion s1 = new Suggestion(Suggestion.SuggestionType.Error, "1",  "2", "", 1, 50, "Text1", new java.util.Date());
            Suggestion s2 = new Suggestion(Suggestion.SuggestionType.Warning, "1",  "2", "", 7, 10, "Text2", new java.util.Date());
            Suggestion s3 = new Suggestion(Suggestion.SuggestionType.Error, "1",  "2", "", 15, 100, "Text3", new java.util.Date());

            DAO.tempSaveSuggestion(s1);
            DAO.tempSaveSuggestion(s2);
            DAO.tempSaveSuggestion(s3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

*/

    private void deleteAllUserAssigment(String userID) throws SQLException {
        connection = source.getConnection();
        String query = "DELETE FROM code_files WHERE userID=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,userID);
        statement.executeUpdate();
    }
}