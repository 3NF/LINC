package Database;

import Models.User;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.classroom.model.Student;
import com.google.api.services.classroom.model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDAO
{

    static class UserCredential
    {
        private String accessToken;
        private String refreshToken;

        UserCredential(String accessToken, String refreshToken)
        {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken()
        {
            return accessToken;
        }

        public String getRefreshToken()
        {
            return refreshToken;
        }
    }



    private static final String SELECT_TOKENS_FORMAT = "SELECT aToken, rToken FROM usertokens WHERE sub=?";
    private static final String INSERT_TOKENS_FORMAT = "INSERT INTO usertokens (sub, aToken, rToken) VALUES (?,?,?)";

    public static UserCredential getUserCredential(String sub)
    {
        try
        {
            Connection conn =  ConnectionPool.getInstance().getConnection();
            PreparedStatement st = conn.prepareStatement(SELECT_TOKENS_FORMAT);
            st.setString(1, sub);

            ResultSet result = st.executeQuery();


            if(!result.next()) return null;

            String accessToken = result.getString(1);
            String refreshToken = result.getString(2);

            conn.close();
            return new UserCredential(accessToken, refreshToken);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public static GoogleCredential getGoogleCredentials(String userId)
    {
        UserCredential credential = getUserCredential(userId);

        return new GoogleCredential.Builder().setJsonFactory(GAPIManager.JACKSON_FACTORY).setClientSecrets(GAPIManager.secrets).setTransport(GAPIManager.HTTP_TRANSPORT)
                .build().setAccessToken(credential.getAccessToken()).setRefreshToken(credential.getRefreshToken());
    }

    public static void saveUserCredentials(String sub, String accessToken, String refreshToken)
    {
        try
        {
            Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement st  = conn.prepareStatement(INSERT_TOKENS_FORMAT);
            st.setString(1, sub);
            st.setString(2, accessToken);
            st.setString(3, refreshToken);
            st.executeUpdate();
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public enum Role
    {
        Teacher,
        SeminarReader,
        TeacherAssistant,
        Pupil,
        Guest
    }
    
    public static Role getRoleByCourse(User user, String courseId) {
        GAPIManager gp = GAPIManager.getInstance();
        Role role = gp.getRoleByCourse(user , courseId);
        if (role == Role.Teacher) {
            return role;
        } else {
            String rtn = getInstructorType (user.getUserId(), courseId);
            if (rtn == null) {
                return Role.Pupil;
            }
            return Role.valueOf(rtn);
        }
    }

    public static String getInstructorType(String userID, String courseID){
        try {
            String query = "SELECT Type FROM instructors\n" +
                    "WHERE userID=? AND classroomID=?";

            Connection conn =  ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, userID);
            statement.setString(2, courseID);

            ResultSet result = statement.executeQuery();
            String type = null;

            if(result.next()) {
                type = result.getString(1);
            }

            statement.close();
            conn.close();
            return type;

        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<String> getUserIDsByRole(String classroomID, Role role){
        List<String> users = new ArrayList<String>();
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            String query="SELECT * FROM instructors WHERE classroomID=? AND Type=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,classroomID);
            statement.setString(2,role.toString());
            ResultSet result = statement.executeQuery();
            while (result.next()){
                String userID = result.getString("userID");
                users.add(userID);
            }
            connection.close();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void addUser(String userID , Role role , String courseID){
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            String query = "INSERT IGNORE INTO `instructors`(`classroomID`, `userID`, `Type`) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1 , courseID);
            statement.setString(2 , userID);
            statement.setString(3 , role.toString());
            int result = statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeUser(String userID , Role role, String courseID){
        try {
            Connection connection = ConnectionPool.getInstance().getConnection();
            String query = "DELETE FROM `instructors` WHERE `userID`=? AND`classroomID`=? AND`Type`=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1 , userID);
            statement.setString(2 , courseID);
            statement.setString(3 , role.toString());
            int result = statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
