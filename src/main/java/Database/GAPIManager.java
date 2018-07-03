package Database;

import Core.Room;
import HelperClasses.Utilities;
import Models.User;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;


public class GAPIManager {

    private static final JacksonFactory JACKSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CLIENT_SECRET_FILE = "client_secret.json";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private static final GAPIManager instance = new GAPIManager();



    private GoogleClientSecrets secrets;

    public static GAPIManager getInstance(){ return instance;}



    private GAPIManager()
    {
        Reader reader = new InputStreamReader(GAPIManager.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_FILE));
        try
        {
            secrets = GoogleClientSecrets.load(JACKSON_FACTORY, reader);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * @param room Unique name of room
     * @return corresponding Room object
     */
    public static Room getRoomByName(String room) {
        return null;
    }


    public static TokenResponse getTokens(String authCode)
    {
        try {
            Reader reader = new InputStreamReader(GAPIManager.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_FILE));
            GoogleClientSecrets secrets = GoogleClientSecrets.load(JACKSON_FACTORY, reader);

            return new GoogleAuthorizationCodeTokenRequest(HTTP_TRANSPORT, JACKSON_FACTORY, "https://www.googleapis.com/oauth2/v4/token", secrets.getDetails().getClientId(), secrets.getDetails().getClientSecret(), authCode, "http://localhost:8080").execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public User getUser(String idToken)
    {
        try {
            GoogleIdToken googleIdToken = GoogleIdToken.parse(JACKSON_FACTORY, idToken);

            if (!googleIdToken.verify(new GoogleIdTokenVerifier(HTTP_TRANSPORT, JACKSON_FACTORY))) return null;

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String sub = payload.getSubject();
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");
            String familyName = Utilities.capitalizeString((String) payload.get("family_name"));
            String givenName = Utilities.capitalizeString((String) payload.get("given_name"));
            DBManager.UserCredential credential = DBManager.getUserCredential(sub);
            if(credential == null) return null;
            User us = new User(email, givenName, familyName, sub, pictureUrl, credential.getAccessToken(), credential.getRefreshToken());
            return us;
        } catch (Exception e) {
            return null;
        }
    }


    public User registerUser(String authCode)
    {
        try
        {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(HTTP_TRANSPORT, JACKSON_FACTORY, "https://www.googleapis.com/oauth2/v4/token", secrets.getDetails().getClientId(), secrets.getDetails().getClientSecret(), authCode, "http://localhost:8080").execute();

            String refreshToken = tokenResponse.getRefreshToken();
            GoogleIdToken idToken = tokenResponse.parseIdToken();
            String accessToken = tokenResponse.getAccessToken();

            String sub = idToken.getPayload().getSubject();

            DBManager.saveUserCredentials(sub, accessToken, refreshToken);
            return getUser(tokenResponse.getIdToken());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Course> getActiveRooms(User user) {
        return getUserRooms(user, true);
    }

    public List<Course> getAllRooms(User user) {
        return getUserRooms(user, false);
    }


    public DBManager.Role getRoleByCourse(User user, String courseId) {
        try {
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
            Course course = service.courses().get(courseId).execute();

            if (course.getOwnerId() == user.getUserId()){
                return DBManager.Role.Teacher;
            }

            List<Teacher> teachers = getTeachers(user, courseId);
            for (Teacher teacher : teachers){
                if (teacher.getUserId() == user.getUserId())
                    return DBManager.Role.TeacherAssistant;
            }

            return DBManager.Role.Guest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DBManager.Role.Guest;
    }


    private List<Course> getUserRooms(User user, boolean activeOnly)
    {
        try
        {
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();

            ListCoursesResponse listCourses =
                    activeOnly ?
                            service.courses().list().setCourseStates(Collections.singletonList("ACTIVE")).execute() :
                            service.courses().list().execute();

            return listCourses.getCourses();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void getAllAssignments(User user, String courseId)
    {
        try
        {
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
            System.out.println(courseId);
            System.out.println(service.courses().courseWork().list(courseId).execute());


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void downloadAssignments(User teacher, String courseID, String assignmentId)
    {
        try
        {
            String accessToken = teacher.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(teacher.getRefreshToken());

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();

            ListStudentSubmissionsResponse assignments = service.courses().courseWork().studentSubmissions().list(courseID, assignmentId).execute();

            System.out.println(assignments);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public List<Teacher> getTeachers(User user, String courseID) {
        try
        {
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();

            List<Teacher> teachers = service.courses().teachers().list(courseID).execute().getTeachers();

            return teachers;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isInRoom(User user, String courseID) {
        try
        {
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());
            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();

            System.out.println(service.courses().students().list(courseID));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
