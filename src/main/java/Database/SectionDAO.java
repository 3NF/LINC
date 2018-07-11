package Database;

import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SectionDAO {
    private final MysqlDataSource connectionPool;
    public SectionDAO(MysqlDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<User> getUsersInSection (String classroomID, String userID) {
        ArrayList <User> users = new ArrayList<>();

        String query = "SELECT * FROM instructors, sections\n" +
                "WHERE instructors.classroomID=? AND instructors.userID=? AND sections.instructorID = instructors.id;";

        PreparedStatement statement;

        try {
            Connection connection =connectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, classroomID);
            statement.setString(2, userID);
            ResultSet result = statement.executeQuery();

            System.out.println("YES");
            while (result.next()) {
                users.add(new User(result.getString("studentID")));
            }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean isInSection(String instructorID,String studentID){
        String query  = "SELECT instructors.id,instructors.userID,sections.studentID FROM instructors" +
                " inner join sections on instructors.id=sections.instructorID " +
                "where sections.instructorID=? AND sections.studentID=?";
        try {
            Connection connection =connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,instructorID);
            statement.setString(2,studentID);
            ResultSet result = statement.executeQuery();
            if (result.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}