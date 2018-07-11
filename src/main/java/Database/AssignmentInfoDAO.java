package Database;

import Models.Reply;

import java.sql.*;
import java.util.*;

public class AssignmentInfoDAO {

    ConnectionPool connectionPool;

    public AssignmentInfoDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     *
     * Gets names of files for assignment, which must be assigned
     *
     * @param classroomId           id of classroom
     * @param assignmentId          id of assignment
     * @return                      List of Strings containing file names
     */
    public List<String> getAssignmentFilesNames(String classroomId, String assignmentId) {
        return null;
    }

    /**
     *
     * Gets ids of assignments which already have assigned to teacher-assistants for checking
     *
     * @param classroomId           id if classroom
     * @return                      List of strings containing assignment ids
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
            System.err.println("error in creation statement");
            e.printStackTrace();
            return Collections.emptyList();
        }

        return result;
    }

}
