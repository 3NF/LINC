package Database;

import Models.User;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        if (role != Role.Guest)
            return role;
        if (isTeacherAssistant(user , courseId))
            return Role.TeacherAssistant;
        return Role.Guest;
    }

    private static boolean isTeacherAssistant(User user, String courseId){
        return false;
    }

    //TODO 3NF-Bagdu maqsimaluri komunicireba bazebtan
    public static List<String> getUsersByRole(String classroomID,Role role){
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
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


}
