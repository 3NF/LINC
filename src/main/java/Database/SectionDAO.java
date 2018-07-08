package Database;

import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

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
}