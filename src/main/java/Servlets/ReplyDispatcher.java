package Servlets;

import static Data.Constraints.TEACHER_ID;
import static Data.Constraints.USER;
import static Data.Constraints.USER_STORAGE;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.apache.http.HttpStatus;

import Data.Constraints;
import Database.ReplyDAO;
import Database.UserStorage;
import Database.ValidateDAO;
import Models.Reply;
import Models.User;

@WebServlet(name = "ReplyDispatcher", urlPatterns = "/user/reply_dispatcher")
public class ReplyDispatcher extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        try {
            User user = (User) session.getAttribute(USER);

            //Get request data
            String json;
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String courseID = data.get(Constraints.COURSE_ID).getAsString();
            String teacherID = data.get(TEACHER_ID).getAsString();
            String suggestionID = data.get(Constraints.SUGGESTION_ID).getAsString();

            //Get necessary objects from ServletContext
            ValidateDAO validateDAO = (ValidateDAO) request.getServletContext().getAttribute(Constraints.VALIDATE_DAO);
            ReplyDAO replyDAO = (ReplyDAO) request.getServletContext().getAttribute(Constraints.REPLY_DAO);
            UserStorage userStorage = (UserStorage) request.getServletContext().getAttribute(USER_STORAGE);

            //Get ReplyDAO
            if (validateDAO.checkSuggestionAccess(user, suggestionID, courseID) == null) {
                response.sendError(HttpStatus.SC_FORBIDDEN);
                return;
            }

            //Convert get replies and convert them to JSON
            List <Reply> replies = getReplies(suggestionID, teacherID, userStorage, replyDAO);
            json = new GsonBuilder().disableHtmlEscaping().create().toJson(replies);

            //Send response to client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF8");
            response.getWriter().write(json);
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            response.sendError(HttpStatus.SC_GATEWAY_TIMEOUT);
        }
    }

    private List <Reply> getReplies (String suggestionID, String teacherID, UserStorage userStorage, ReplyDAO replyDAO) {
        List<Reply> replies = replyDAO.getSuggestionReplies(suggestionID);

        for (Reply reply : replies) {
            if (userStorage != null) {
                reply.RetrieveUsers(teacherID, userStorage);
            }
        }

        return replies;
    }

    /*
        User must not access this with get request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.SC_NOT_FOUND);
    }
}
