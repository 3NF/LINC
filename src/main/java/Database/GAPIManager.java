package Database;

import Core.Room;
import HelperClasses.Utilities;
import Models.Assignment;
import Models.UploadedAssignment;
import Models.User;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.*;
import com.google.api.services.drive.Drive;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GAPIManager {

    private static final JacksonFactory JACKSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String CLIENT_SECRET_FILE = "client_secret.json";
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final String APPNAME = "LINC";

    private static final GAPIManager instance = new GAPIManager();


    public static GoogleClientSecrets secrets;

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
        GoogleCredential credential = UserDAO.getGoogleCredentials(requesterId);

        Classroom room = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName(APPNAME).build();

        UserProfile profile;

        try {
            profile = room.userProfiles().get(targetId).setFields("name").execute();
        } catch (IOException e) {
            return new User (targetId, null, "/Images/temp_user_icon.svg");
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
        Classroom service;

        try {
            Credential credential = user.getCredential();
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
            Credential credential = user.getCredential();
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
            Credential credential = user.getCredential();

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void listStudents(String courseId)
    {

    }



    public static UploadedAssignment downloadAssignments(User teacher, String courseID, String assignmentId) throws IOException {
        Credential credential = teacher.getCredential();
        Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential)
                .setApplicationName("LINC")
                .build();
        Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
        //DriveService driveService;
        List<StudentSubmission> assignments = service.courses().courseWork().studentSubmissions().list(courseID, assignmentId).execute().getStudentSubmissions();

        UploadedAssignment uploadedAssignment = new UploadedAssignment(assignmentId);
	    for (StudentSubmission assignment : assignments) {
		    if (assignment.getAssignmentSubmission() == null) continue;
		    if (assignment.getAssignmentSubmission().getAttachments() == null) continue;
		    String actorUserID = assignment.getSubmissionHistory().get(0).getStateHistory().getActorUserId();
		    String fileId = assignment.getAssignmentSubmission().getAttachments().get(0).getDriveFile().getId();

		    OutputStream outputStream = new ByteArrayOutputStream();
		    driveService.files().get(fileId)
				    .executeMediaAndDownloadTo(outputStream);

		    //convert OutPutStream into inputStream
		    ByteArrayInputStream inStream = Utilities.convertOutputIntoInputStream(outputStream);
		    Utilities.unzipInputStream(inStream, uploadedAssignment, actorUserID);
	    }
        return uploadedAssignment;
    }


    /** shoudl be called from teachers user only*/
    public List<UserProfile> getUsers(User user, String courseID) {
        try {
            Credential credential = user.getCredential();

            Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
            List<Student> students = service.courses().students().list(courseID).execute().getStudents();
            if (students == null)
                return Collections.emptyList();

            List<UserProfile> users = new ArrayList<>();

            students.forEach(student -> users.add(student.getProfile()));
            return users;
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
            Credential credential = user.getCredential();
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


    public User getUserProfile(String requesterId, String targetId)
    {
        GoogleCredential credential = UserDAO.getGoogleCredentials(requesterId);
        Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JACKSON_FACTORY, credential).setApplicationName("LINC").build();
        try
        {
            UserProfile profile = service.userProfiles().get(targetId).execute();
            return new User(profile.getId(), profile.getEmailAddress(), profile.getPhotoUrl());
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
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
