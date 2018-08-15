package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import Models.User;

public class ValidateDAO {
	private final MysqlDataSource connectionPool;

	public ValidateDAO(MysqlDataSource connectionPool) {
		this.connectionPool = connectionPool;
	}


	public boolean hasSuggestionWritePermission(User user, String courseID, String codeFileID) {
/*
        String query = "Select * FROM (SELECT  INSID, studentID FROM (SELECT id as INSID FROM instructors WHERE classroomID = ? AND userID = ?) T, sections\n" +
                "WHERE T.INSID = sections.instructorID) M, code_files WHERE code_files.id=? AND code_files.userID=M.studentID;";
*/

		String q = "SELECT Instructors.userID,Instructors.classroomID,code_files.id FROM Instructors inner join" +
				" sections on Instructors.id=sections.instructorID inner join code_files " +
				"on code_files.userID=sections.studentID " +
				"where Instructors.classroomID=?  AND Instructors.userID=? and code_files.id=?";

		PreparedStatement statement;

		try {
			Connection connection = connectionPool.getConnection();
			statement = connection.prepareStatement(q);
			statement.setString(1, courseID);
			statement.setString(2, user.getUserId());
			statement.setString(3, codeFileID);
			ResultSet result = statement.executeQuery();

			boolean rtn = result.next();
			statement.close();
			connection.close();

			return rtn;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean hasAccessInstructor(User user, String suggestionID, String courseID) {
		String query = "SELECT Instructors.userID,Instructors.classroomID,sections.InstructorID,sections.studentID,suggestions.id FROM Instructors inner join " +
				"sections on Instructors.id=sections.InstructorID " +
				"inner join code_files on code_files.userID = sections.studentID " +
				"inner join suggestions on suggestions.Code_FileID=code_files.id " +
				"WHERE Instructors.userID=? AND suggestions.id=? AND Instructors.classroomID=?";
		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, user.getUserId());
			statement.setString(2, suggestionID);
			statement.setString(3, courseID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
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


	 private boolean hasAccessStudent(User user, String suggestionID) {
		String query = "SELECT suggestions.id,code_files.userId FROM  code_files " +
				"inner join suggestions on suggestions.Code_FileID=code_files.id " +
				"WHERE code_files.userId=? AND suggestions.id=?";
		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, user.getUserId());
			statement.setString(2, suggestionID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
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

	public boolean isValidate(User user, String suggestionID, String courseID) {
		return hasAccessInstructor(user, suggestionID, courseID) || hasAccessStudent(user, suggestionID);
	}
}
