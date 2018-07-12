package Database;

import Models.Assignment;
import Models.Reply;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javafx.util.Pair;

import java.sql.*;
import java.util.*;

public class AssignmentInfoDAO {

    MysqlDataSource connectionPool;

    public AssignmentInfoDAO(MysqlDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<Pair<String,String>> getAssignmentFilesPath(String userID, String assignmentID) {
        String query =  "SELECT path,id FROM code_files WHERE userID=? AND assignmentID=?";
        List<Pair<String,String>> answer = new ArrayList<Pair<String,String>>();
        try {
            Connection connectionn = connectionPool.getConnection();
            PreparedStatement statement = connectionn.prepareStatement(query);
            statement.setString(1,userID);
            statement.setString(2,assignmentID);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String path = result.getString("path");
                String id = result.getString("id");
                answer.add(new Pair<>(id,path));
            }
            statement.close();
            connectionn.close();
            return answer;
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void addAssignment(String assignmentID,String courseID){
        String query = "INSERT INTO assignments(name,courseID,idInClassroom) VALUES (?,?,?)";
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,"123");
            statement.setString(2,courseID);
            statement.setString(3,assignmentID);
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

    public void addGrade(String userID , Grade grade , String assignmentID){
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            String query = "INSERT INTO `grades`(`assignmentID`, `userID`, `grade`) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1 , assignmentID);
            statement.setString(2 , userID);
            statement.setString(3 , grade.getName());
            int result = statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  String getGrade(String userID , String assignmentID){
        String grade = "Not graded yet";
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            String query = "SELECT * FROM grades WHERE assignmentID=? AND userID=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,assignmentID);
            statement.setString(2,userID);
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

    public void updateGrade(String assignmentID, String userID, String grade){
        try {
            Connection connection = connectionPool.getConnection();
            String query = "INSERT INTO `grades`(`assignmentID`, `userID`, `grade`) VALUES (?,?,?) " +
                    " ON DUPLICATE KEY UPDATE grade=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,assignmentID);
            statement.setString(2,userID);
            statement.setString(3,grade);
            statement.setString(4,grade);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
