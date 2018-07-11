package Database;

import Core.Room;
import HelperClasses.Utilities;
import Models.Assignment;
import Models.File;
import Models.UploadedAssignment;
import Models.User;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.*;
import com.google.api.services.drive.Drive;
import com.sun.mail.iap.ByteArray;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class GAPIManager {

    private static final JacksonFactory JACKSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CLIENT_SECRET_FILE = "client_secret.json";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String APPNAME = "LINC";

    private static final GAPIManager instance = new GAPIManager();


    private static GoogleClientSecrets secrets;

    public static GAPIManager getInstance() {
        return instance;
    }


    private GAPIManager() {
        Reader reader = new InputStreamReader(GAPIManager.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_FILE));
        try {
            secrets = GoogleClientSecrets.load(JACKSON_FACTORY, reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param room Unique name of room
     * @return corresponding Room object
     */
    public Room getRoomByName(String room) {
        return null;
    }


    public TokenResponse getTokens(String authCode) {
        try {
            Reader reader = new InputStreamReader(GAPIManager.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_FILE));
            GoogleClientSecrets secrets = GoogleClientSecrets.load(JACKSON_FACTORY, reader);

            return new GoogleAuthorizationCodeTokenRequest(HTTP_TRANSPORT, JACKSON_FACTORY, "https://www.googleapis.com/oauth2/v4/token", secrets.getDetails().getClientId(), secrets.getDetails().getClientSecret(), authCode, "http://localhost:8080").execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public User getUser(String idToken) {
        try {
            GoogleIdToken googleIdToken = GoogleIdToken.parse(JACKSON_FACTORY, idToken);

            if (!googleIdToken.verify(new GoogleIdTokenVerifier(HTTP_TRANSPORT, JACKSON_FACTORY))) return null;

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String sub = payload.getSubject();
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");
            String familyName = Utilities.capitalizeString((String) payload.get("family_name"));
            String givenName = Utilities.capitalizeString((String) payload.get("given_name"));
            UserDAO.UserCredential credential = UserDAO.getUserCredential(sub);
            if (credential == null) return null;
            return new User(email, givenName, familyName, sub, pictureUrl, credential.getAccessToken(), credential.getRefreshToken());
        } catch (Exception e) {
            return null;
        }
    }


    public User getUserById(String requesterId, String targetId) {

        UserDAO.UserCredential cred = UserDAO.getUserCredential(requesterId);
        String accessToken = cred.getAccessToken();
        String refreshToken = cred.getRefreshToken();
        GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).
                setClientSecrets(secrets).
                setTransport(HTTP_TRANSPORT).build().
                setAccessToken(accessToken).setRefreshToken(refreshToken);

        Classroom room = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName(APPNAME).build();

        UserProfile profile = null;

        try {
            profile = room.userProfiles().get(targetId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new User(profile.getEmailAddress(), profile.getName().getGivenName(), profile.getName().getFamilyName(), targetId, profile.getPhotoUrl(), "", "");
    }


    public User registerUser(String authCode) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(HTTP_TRANSPORT, JACKSON_FACTORY, "https://www.googleapis.com/oauth2/v4/token", secrets.getDetails().getClientId(), secrets.getDetails().getClientSecret(), authCode, "http://localhost:8080").execute();

            String refreshToken = tokenResponse.getRefreshToken();
            GoogleIdToken idToken = tokenResponse.parseIdToken();
            String accessToken = tokenResponse.getAccessToken();

            String sub = idToken.getPayload().getSubject();

            UserDAO.saveUserCredentials(sub, accessToken, refreshToken);
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


    public UserDAO.Role getRoleByCourse(User user, String courseId) {
        Classroom service = null;

        try {
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());

            service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
            Teacher teachers = null;

            try {
                Student student = service.courses().students().get(courseId, user.getUserId()).execute();

                return UserDAO.Role.Pupil;
            } catch (Exception e) {
                Teacher teacher = service.courses().teachers().get(courseId, user.getUserId()).execute();
                return UserDAO.Role.Teacher;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Course> getUserRooms(User user, boolean activeOnly) {
        try {
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
            return Collections.emptyList();
        }
    }


    public void getAllAssignments(User user, String courseId) {
        try {
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
            System.out.println(service.courses().courseWork().list(courseId).execute());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static UploadedAssignment downloadAssignments(User teacher, String courseID, String assignmentId) throws IOException {
        String accessToken = teacher.getAccessToken();
        GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(teacher.getRefreshToken());
        Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential)
                .setApplicationName("LINC")
                .build();
        ;
        Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
        //DriveService driveService;
        List<StudentSubmission> assignments = service.courses().courseWork().studentSubmissions().list(courseID, assignmentId).execute().getStudentSubmissions();

        //System.err.println(assignments);
        UploadedAssignment uploadedAssignment = new UploadedAssignment(assignmentId);
        //System.err.println(assignments.get(1).getSubmissionHistory().get(0).getStateHistory().getActorUserId());
        for (int k = 0; k < assignments.size(); ++k) {
            if (assignments.get(k).getAssignmentSubmission() == null) continue;
            if (assignments.get(k).getAssignmentSubmission().getAttachments() == null) continue;
            String actorUserID = assignments.get(k).getSubmissionHistory().get(0).getStateHistory().getActorUserId();
            String fileId = assignments.get(k).getAssignmentSubmission().getAttachments().get(0).getDriveFile().getId();
           // System.out.println(actorUserID);
            //System.err.println(fileId);
            OutputStream outputStream = new ByteArrayOutputStream();
            driveService.files().get(fileId)
                    .executeMediaAndDownloadTo(outputStream);

            //convert OutPutStream into inputStream
            ByteArrayInputStream inStream = Utilities.convertOutputIntoInputStream(outputStream);
            Utilities.unzipInputStream(inStream, uploadedAssignment, actorUserID);
        }
       /* for (Object gio : uploadedAssignment) {
            System.err.println(((File) gio).getFileName());
        }*/
        return uploadedAssignment;
    }

    public List<Student> getUsers(User user, String courseID) {
        try {
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
            List<Student> students = service.courses().students().list(courseID).execute().getStudents();
            if (students == null)
                return Collections.emptyList();
            else
                return students;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Checks if user is in classroom
     */
    public boolean isInRoom(User user, String courseID) {
        try {
            String accessToken = user.getAccessToken();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(user.getRefreshToken());
            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
            try {
                service.courses().get(courseID).execute();
                return true;
            } catch (IOException e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List<Assignment> getCourseAssignments(String accessToken, String refreshToken, String courseId) {
        GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JACKSON_FACTORY).setClientSecrets(secrets).setTransport(HTTP_TRANSPORT).build().setAccessToken(accessToken).setRefreshToken(refreshToken);
        Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
        try {
            List<Assignment> assignments = new ArrayList<>();
            List<CourseWork> courseWorks = service.courses().courseWork().list(courseId).execute().getCourseWork();
            if (courseWorks == null) return Collections.emptyList();
            for (CourseWork courseWork : courseWorks) {
                assignments.add(new Assignment(courseWork.getId(), courseWork.getTitle()));
            }
            return assignments;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
