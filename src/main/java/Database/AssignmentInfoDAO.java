package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javafx.util.Pair;


public class AssignmentInfoDAO {

	private MysqlDataSource connectionPool;

	public AssignmentInfoDAO(MysqlDataSource connectionPool) {
		this.connectionPool = connectionPool;
	}

	public List<Pair<String, String>> getAssignmentFilesPath(String userID, String assignmentID) throws SQLException {
		String query = "SELECT path,id FROM code_files WHERE userID=? AND assignmentID=?";
		List<Pair<String, String>> answer = new ArrayList<>();

		Connection connectionn = connectionPool.getConnection();
		PreparedStatement statement = connectionn.prepareStatement(query);
		statement.setString(1, userID);
		statement.setString(2, assignmentID);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			String path = result.getString("path");
			String id = result.getString("id");
			answer.add(new Pair<>(id, path));
		}
		statement.close();
		connectionn.close();
		return answer;
	}

	/**
	 * Gets ids of assignments which already have assigned to teacher-assistants for checking
	 *
	 * @param classroomId id if classroom
	 * @return List of strings containing assignment ids
	 */
	public List<String> getAssignmentIds(String classroomId) {
		Connection connection;
		ArrayList<String> result = new ArrayList<>();
		try {
			connection = connectionPool.getConnection();
		} catch (SQLException e) {
			return result;
		}
		PreparedStatement statement;
		String query = "SELECT idInClassroom FROM assignments WHERE courseID=?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, classroomId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				result.add(resultSet.getString("idInClassroom"));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}

		return result;
	}

	public void addAssignment(String assignmentID, String courseID) {
		String query = "INSERT INTO assignments(courseID,idInClassroom) VALUES (?,?)";
		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, courseID);
			statement.setString(2, assignmentID);
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public enum Grade {
		PlusPlus("Plus Plus"),
		Plus("Plus"),
		CheckPlus("Check Plus"),
		Check("Check"),
		CheckMinus("Check Minus"),
		Minus("Minus"),
		MinusMinus("Minus Minus"),
		None("0");

		private String gName;

		Grade(String name) {
			this.gName = name;
		}

		public String getName() {
			return gName;
		}
	}

	public void addGrade(String userID, Grade grade, String assignmentID) {
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			String query = "INSERT INTO `grades`(`assignmentID`, `userID`, `grade`) VALUES (?,?,?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, assignmentID);
			statement.setString(2, userID);
			statement.setString(3, grade.getName());
			statement.executeUpdate();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	String getGrade(String userID, String assignmentID) {
		String grade = "Not graded yet";
		try {
			Connection connection = ConnectionPool.getInstance().getConnection();
			String query = "SELECT * FROM grades WHERE assignmentID=? AND userID=?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, assignmentID);
			statement.setString(2, userID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				grade = result.getString("grade");
			}
			statement.close();
			connection.close();
			return grade;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return grade;
	}

	public void updateGrade(String assignmentID, String userID, String grade) {
		try {
			Connection connection = connectionPool.getConnection();
			String query = "INSERT INTO `grades`(`assignmentID`, `userID`, `grade`) VALUES (?,?,?) " +
					" ON DUPLICATE KEY UPDATE grade=?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, assignmentID);
			statement.setString(2, userID);
			statement.setString(3, grade);
			statement.setString(4, grade);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Pair<String, String>> getUsersGrades(String classroomID, String userID) {
		List<Pair<String, String>> list = new ArrayList<>();
		try {
			String query = "SELECT assignments.idInClassroom,grades.assignmentID,grades.grade FROM assignments inner join " +
					"grades on grades.assignmentID=assignments.idInClassroom WHERE userID=? AND assignments.courseID=?";
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, classroomID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				String assignmentID = result.getString("grades.assignmentID");
				String grade = result.getString("grades.grade");
				list.add(new Pair<>(assignmentID, grade));
			}
			statement.close();
			connection.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	/**
	 * Gets grades for all student in all assignment
	 * For given instructor
	 *
	 * @param classroomId  identifier of classroom
	 * @param instructorId identifier of assignment
	 */
	public JsonElement getAssignmentsGrades(String classroomId, String instructorId) {
		JsonObject result = new JsonObject();
		String assignments = "SELECT idInClassroom FROM assignments WHERE courseID=?";
		String users = "SELECT studentID FROM instructors, sections " + "WHERE instructors.classroomID=? AND instructors.userID=? AND sections.instructorID = instructors.id";
		String query = "SELECT * FROM grades WHERE userID IN (" + users + ") AND assignmentID IN (" + assignments + ");";
		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, classroomId);
			statement.setString(2, instructorId);
			statement.setString(3, classroomId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String assignmentID = resultSet.getString("grades.assignmentID");
				String grade = resultSet.getString("grades.grade");
				String userId = resultSet.getString("grades.userId");
				if (!result.has(assignmentID)) {
					result.add(assignmentID, new JsonObject());
				}
				((JsonObject) result.get(assignmentID)).addProperty(userId, grade);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets checked assignments for given classroom and instructor
	 */
	public List<String> getSubmittedAssignments(String classroomId, String instructorId) {
		String query = "SELECT assignmentId FROM uploaded_assignments WHERE assignmentId IN (SELECT idInClassroom FROM assignments where courseID = ?) AND instructorId = ?";
		List<String> res = new ArrayList<>();
		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, classroomId);
			statement.setString(2, instructorId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String assignmentID = resultSet.getString("uploaded_assignments.assignmentID");
				res.add(assignmentID);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return res;
	}

	public void submitAssignmentGrades(String assignmentId, String instructorId){
		String query = "INSERT INTO uploaded_assignments(assignmentId, instructorId) VALUES (?, ?)";
		try {
			Connection connection = connectionPool.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, assignmentId);
			statement.setString(2, instructorId);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
