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

		String q = "SELECT instructors.userID,instructors.classroomID,code_files.id FROM instructors inner join" +
				" sections on instructors.id=sections.instructorID inner join code_files " +
				"on code_files.userID=sections.studentID " +
				"where instructors.classroomID=?  AND instructors.userID=? and code_files.id=?";

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
		String query = "SELECT instructors.userID,instructors.classroomID,sections.InstructorID,sections.studentID,suggestions.id FROM instructors inner join " +
				"sections on instructors.id=sections.InstructorID " +
				"inner join code_files on code_files.userID = sections.studentID " +
				"inner join suggestions on suggestions.code_fileID=code_files.id " +
				"WHERE instructors.userID=? AND suggestions.id=? AND instructors.classroomID=?";
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


	private boolean checkSuggestionAccessStudent(String userID, String suggestionID) {
		String query = "SELECT suggestions.id,code_files.userId FROM  code_files " +
				"inner join suggestions on suggestions.code_fileID=code_files.id " +
				"WHERE code_files.userId=? AND suggestions.id=?";
		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
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


	/**
	 * Checks if instructor has access to this suggestion
	 *
	 * @param userID       Google user ID of instructor
	 * @param suggestionID Suggestion ID to check
	 * @param courseID     Course ID to check
	 * @return userID (of code author in which suggestion is marked) or null
	 * in case instructor doesn't have access to this suggestion
	 */
	private String getStudentIDViaInstructor(String userID, String suggestionID, String courseID) {
		String query = "SELECT instructors.userID,instructors.classroomID,sections.InstructorID,sections.studentID,suggestions.id, code_files.userID FROM instructors inner join " +
				"sections on instructors.id=sections.InstructorID " +
				"inner join code_files on code_files.userID = sections.studentID " +
				"inner join suggestions on suggestions.code_fileID=code_files.id " +
				"WHERE instructors.userID=? AND suggestions.id=? AND instructors.classroomID=?";
		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, suggestionID);
			statement.setString(3, courseID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				String studentID = result.getString("code_files.userID");
				statement.close();
				connection.close();
				return studentID;
			}
			statement.close();
			connection.close();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Checks user's access for given suggestion
	 *
	 * @return UserID of code author or null (in case there user doesn't have access)
	 */
	public String checkSuggestionAccess(User user, String suggestionID, String courseID) {
		if (checkSuggestionAccessStudent(user.getUserId(), suggestionID)) {
			return user.getUserId();
		}
		return getStudentIDViaInstructor(user.getUserId(), suggestionID, courseID);
	}


	/**
	 * Checks if given instructor has access to this students work
	 */
	public boolean isInstructor(String classroomId, String instructorId, String studentId) {
		String query = "SELECT * " +
				"FROM sections INNER JOIN instructors ON sections.instructorID = instructors.id " +
				"WHERE classroomID = ? AND userID = ?" +
				" AND studentID = ?";

		try {
			boolean res = false;
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, classroomId);
			statement.setString(2, instructorId);
			statement.setString(3, studentId);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				res = true;
			}
			statement.close();
			connection.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}
