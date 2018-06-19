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
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.ListCoursesResponse;
import javafx.util.Pair;

import java.io.*;
import java.util.List;


public class GAPIManager {

    private static final JacksonFactory JACKSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CLIENT_SECRET_FILE = "client_secret.json";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();


    /**
     * @param room Unique name of room
     * @return corresponding Room object
     */
    public static Room getRoomByName(String room) {
        return null;
    }


    public static TokenResponse getTokens(String authCode)
    {
        try
        {
            Reader reader = new InputStreamReader(GAPIManager.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_FILE));
            GoogleClientSecrets secrets = GoogleClientSecrets.load(JACKSON_FACTORY, reader);

            return new GoogleAuthorizationCodeTokenRequest(
                    HTTP_TRANSPORT,
                    JACKSON_FACTORY,
                    "https://www.googleapis.com/oauth2/v4/token",
                    secrets.getDetails().getClientId(),
                    secrets.getDetails().getClientSecret(),
                    authCode,
                    "http://localhost:8080").execute();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }


    }

    public static User getUser(String idToken)
    {
        try
        {
            GoogleIdToken googleIdToken = GoogleIdToken.parse(JACKSON_FACTORY,idToken);

            if (!googleIdToken.verify(new GoogleIdTokenVerifier(HTTP_TRANSPORT, JACKSON_FACTORY))) return null;

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String sub = payload.getSubject();
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");
            String familyName = Utilities.capitalizeString((String) payload.get("family_name"));
            String givenName = Utilities.capitalizeString((String) payload.get("given_name"));
            DBManager.UserCredential credential = DBManager.getUserCredential(sub);
            return new User(email, givenName, familyName, sub, pictureUrl, credential.getAccessToken(), credential.getRefreshToken());
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public static User registerUser(String authCode)
    {
        try
        {
            Reader reader = new InputStreamReader(GAPIManager.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_FILE));
            GoogleClientSecrets secrets = GoogleClientSecrets.load(JACKSON_FACTORY,reader);
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    HTTP_TRANSPORT,
                    JACKSON_FACTORY,
                    "https://www.googleapis.com/oauth2/v4/token",
                    secrets.getDetails().getClientId(),
                    secrets.getDetails().getClientSecret(),
                    authCode,
                    "http://localhost:8080").execute();

            String refreshToken = tokenResponse.getRefreshToken();
            GoogleIdToken idToken = tokenResponse.parseIdToken();
            String accessToken  = tokenResponse.getAccessToken();

            String sub = idToken.getPayload().getSubject();

            DBManager.saveUserCredentials(sub,accessToken,refreshToken);
            return getUser(tokenResponse.getIdToken());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public static List<Course> getUserRooms(User user)
    {
        try
        {
            Reader reader = new InputStreamReader(GAPIManager.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_FILE));
            GoogleClientSecrets secrets = GoogleClientSecrets.load(JACKSON_FACTORY, reader);
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory(JACKSON_FACTORY)
                    .setClientSecrets(secrets)
                    .setTransport(HTTP_TRANSPORT)
                    .build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential)
                    .setApplicationName("LINC").build();

            ListCoursesResponse listCourses = service.courses().list().execute();

            return listCourses.getCourses();

        }catch (Exception e)
        {
            e.printStackTrace();
            return  null;
        }
    }
}
