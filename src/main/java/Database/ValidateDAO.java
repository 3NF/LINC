package Database;

import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidateDAO {
    private final MysqlDataSource connectionPool;
    public ValidateDAO(MysqlDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public boolean isAnswer(String query,User user, String suggestionID){
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,user.getUserId());
            statement.setString(2,suggestionID);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                statement.close();
                connection.close();
                return true;
            }
            statement.close();
            connection.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasAccessInstructor(User user, String suggestionID){
        String query = "SELECT sections.InstructorID,sections.studentID,suggestions.id FROM sections " +
                "inner join code_files on code_files.userID = sections.studentID " +
                "inner join suggestions on suggestions.Code_FileID=code_files.id " +
                "WHERE sections.InstructorID=? AND suggestions.id=?";
        return isAnswer(query,user,suggestionID);
    }


    public boolean hasAccessStudent(User user, String suggestionID){
        String query = "SELECT suggestions.id,code_files.userId FROM  code_files " +
                "inner join suggestions on suggestions.Code_FileID=code_files.id " +
                "WHERE code_files.userId=? AND suggestions.id=?";
        return isAnswer(query,user,suggestionID);
    }

    public boolean isValidate(User user, String suggestionID){
        return hasAccessInstructor(user,suggestionID) || hasAccessStudent(user,suggestionID);
    }
}
