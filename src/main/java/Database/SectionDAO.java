package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class SectionDAO {
    private final MysqlDataSource connectionPool;

    public SectionDAO(MysqlDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Returns Users in given users section
     * Must be called from user which has TeacherAssistants privileges
     */
    public List<String> getUsersInSection(String classroomID, String userID) {
        ArrayList<String> users = new ArrayList<>();
        String query = "SELECT * FROM instructors, sections\n" +
                "WHERE instructors.classroomID=? AND instructors.userID=? AND sections.instructorID = instructors.id;";

        PreparedStatement statement;

        try {
            Connection connection = connectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, classroomID);
            statement.setString(2, userID);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                users.add(result.getString("studentID"));
            }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Checks if student is in this instructor's section
     */
    public boolean isInSection(String instructorID, String studentID) {
            String query = "SELECT instructors.id,instructors.userID,sections.studentID FROM instructors" +
                    " inner join sections on instructors.id=sections.instructorID " +
                    "where sections.instructorID=? AND sections.studentID=?";
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, instructorID);
            statement.setString(2, studentID);
            ResultSet result = statement.executeQuery();
            if (result.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
        TODO: გიორგი, აქ ამ ფუნქციას გადახედე, მგონი არაა საჭირო და უბრალოდ addUsersInSection-ის გადაკეთება მოგვიწევს
     */

    private String getInstructorID(String classroomID, String leaderID) {
        String query = "SELECT id FROM instructors WHERE classroomID=? AND userID=?";
        try {
            Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, classroomID);
            statement.setString(2, leaderID);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String id = result.getString("id");
                statement.close();
                conn.close();
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "-1";
    }

	/**
	 * Adds students in instructor's section
	 */
	public void addUsersInSection(String classroomID, String userID, List<String> userIDs) {
        String instructorID = getInstructorID(classroomID, userID);
        String query = "INSERT INTO sections(instructorID,studentID) VALUES(?,?)";
        Connection conn;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            boolean isToBeInserted = true;
            int i = 0;
            for (String studentID : userIDs) {
                statement.setString(1,instructorID);
                statement.setString(2,studentID);
                statement.addBatch();
                ++i;
                if (i % 1000 == 0){
                    statement.executeBatch();
                    isToBeInserted = false;
                }
            }
            if (isToBeInserted)
                statement.executeBatch();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeSections(String classroomID){
	    String query = " DELETE sections FROM sections " +
                                "INNER JOIN instructors " +
                                    "ON sections.instructorID=instructors.id " +
                                        "where instructors.classroomID=?";
        Connection conn;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, classroomID);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}