package Sockets;

import Data.Constraints;
import Database.ReplyDAO;
import Database.UserDAO;
import Database.UserStorage;
import Database.ValidateDAO;
import Models.Reply;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletContext;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static Data.Constraints.*;

@ServerEndpoint(value = "/reply_socket", configurator = ReplySocketConfigurator.class)
public class ReplySocket {

    private ConcurrentHashMap<String, Vector<ReplySocket> > replySockets;
    private Session session;
    private User user;
    private ValidateDAO validateDAO;
    private ReplyDAO replyDAO;


    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.user = (User) config.getUserProperties().get(USER);
        this.validateDAO = (ValidateDAO) config.getUserProperties().get(VALIDATE_DAO);
        this.replyDAO = (ReplyDAO)  config.getUserProperties().get(REPLY_DAO);
        this.replySockets = (ConcurrentHashMap<String, Vector<ReplySocket> > ) config.getUserProperties().get(REPLY_SOCKETS);
        Vector <ReplySocket> replySocketVector;
        if (this.replySockets.contains(user.getUserId())) {
            replySocketVector = this.replySockets.get(user.getUserId());
        } else {
            replySocketVector = new Vector<ReplySocket>();
            System.out.println("Add vector for " + user.getUserId());
            this.replySockets.put(user.getUserId(), replySocketVector);
        }
        replySocketVector.add(this);

        System.out.println(user.getFirstName() + " Connected!");
    }

    @OnMessage
    public void onMessage(String message) throws IOException, EncodeException {
        try {
            JsonObject data = new Gson().fromJson(message, JsonObject.class);

            String courseID = data.get(Constraints.COURSE_ID).getAsString();
            String suggestionID = data.get(Constraints.SUGGESTION_ID).getAsString();
            String studentID = validateDAO.isValidateNew(user, suggestionID, courseID);
            if (studentID == null) {
                System.out.println("User doesn't have permission on suggestion, ID " + suggestionID);
                //broadcast(new Reply(suggestionID, null, null, null, null), studentID);
                return;
            }

            if (data.has("content")) {
                String content = data.get("content").getAsString();
                Reply reply = replyDAO.addReply(content, user.getUserId(), suggestionID);
                reply.user = user;
                broadcast(reply, studentID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println(session.getId() + " Socket Closed");
        Vector <ReplySocket> replySocketVector = replySockets.get(user.getUserId());
        //Race condition!
        replySocketVector.remove(user.getUserId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("There was an error!");
    }

    private void broadcast(Reply reply, String studentID) throws IOException {
        System.out.println("Broadcasting reply, ID - " + reply.getReplyID());
        try {
            List<String> userIDs = UserDAO.getLinkedUserIDs(reply.getSuggestionID());
            userIDs.add(studentID);

            //Needs some functional programming
            userIDs.forEach(userID -> {
                if (replySockets.containsKey(userID)) {
                    System.out.println(userID);
                    Vector<ReplySocket> replySocketVector = replySockets.get(userID);
                    synchronized (replySocketVector) {
                        for (ReplySocket replySocket : replySocketVector) {
                            replySocket.session.getAsyncRemote().sendText(reply.toString());
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
