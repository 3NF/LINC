package Sockets;

import Data.Constraints;
import Database.*;
import Models.UploadedAssignment;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;

import static Data.Constraints.*;

@ServerEndpoint(value = "/download_assignment", configurator = DownloadAssignmentConfigurator.class)
public class DownloadAssignment {

    private int counter;
    private int totalJobs;

    private Session session;
    private User user;
    private AssignmentInfoDAO assignmentInfoDAO;
    private CodeFilesDAO codeFilesDAO;

    public void setTotalJobs (int totalJobs) {
        this.totalJobs = totalJobs;
    }

    public void increaseCounter() {
        counter ++;
        System.out.println("Increased " + counter);
        broadcastCounter();
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException {
        this.counter = -1;

        this.session = session;
        this.user = (User)config.getUserProperties().get(USER);
        this.assignmentInfoDAO = (AssignmentInfoDAO)config.getUserProperties().get(ASSIGNMENT_INFO_DAO);
        this.codeFilesDAO = (CodeFilesDAO)config.getUserProperties().get(CODE_FILES_DAO);

        System.out.println(user.getFirstName() + " Connected!");
    }

    @OnMessage
    public void onMessage(String message) throws IOException, EncodeException {
        try {
            JsonObject data = new Gson().fromJson(message, JsonObject.class);

            String assignmentID = data.get(Constraints.ASSIGNMENT_ID).getAsString();
            String courseID = data.get(Constraints.COURSE_ID).getAsString();

            UploadedAssignment uploadedAssignment = GAPIManager.downloadAssignments(user, courseID, assignmentID, this);

            assignmentInfoDAO.addAssignment(assignmentID, courseID);
            try {
                codeFilesDAO.addAssignments(uploadedAssignment);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            broadcastDone();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println(session.getId() + " Socket Closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("There was an error!");
    }

    private void broadcastCounter() {
        System.out.println("Broadcasting counter -" + counter);
        try {
            this.session.getAsyncRemote().sendText((1.0*counter)/totalJobs + " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastDone () {
        System.out.println("Download has completed!");
        counter = -1;

        try {
            this.session.getAsyncRemote().sendText("Completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
