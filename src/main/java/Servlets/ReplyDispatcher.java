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
public class ReplyDispatcher extends HttpServlet
{
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        try {
            User user = (User) session.getAttribute(USER);

            //Get request data
            String json;
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String courseID = data.get(Constraints.COURSE_ID).getAsString();
            ValidateDAO validateDAO = (ValidateDAO) request.getServletContext().getAttribute(Constraints.VALIDATE_DAO);

            //Get ReplyDAO
            ReplyDAO replyDAO = (ReplyDAO) request.getServletContext().getAttribute(Constraints.REPLY_DAO);
            String suggestionID = data.get(Constraints.SUGGESTION_ID).getAsString();
            if (!validateDAO.isValidate(user,suggestionID,courseID)){
                response.sendError(HttpStatus.SC_FORBIDDEN);
                return;
            }

            if (data.has("content")){
                System.err.print(data.get("content").getAsString());
                String UserID = ((User) session.getAttribute(USER)).getUserId();
                Reply reply = replyDAO.addReply(data.get("content").getAsString(),UserID,suggestionID);
                UserStorage userStorage = (UserStorage) session.getServletContext().getAttribute(USER_STORAGE);
                if (userStorage != null) {
                    reply.user = user;

                }

                json = new GsonBuilder().disableHtmlEscaping().create().toJson(reply);
            }
            else {
                //Get replies from database
                List<Reply> replies = replyDAO.getSuggestionReplies(suggestionID);
                UserStorage userStorage = (UserStorage) request.getServletContext().getAttribute(USER_STORAGE);
                String teacherID = data.get(TEACHER_ID).getAsString();

                for (Reply reply:replies) {
                    if (userStorage != null) {
                        reply.RetrieveUsers(teacherID, userStorage);
                    }
                }

                //Convert file data into JSON
                json = new GsonBuilder().disableHtmlEscaping().create().toJson(replies);
            }

            //Send response to client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF8");
            response.getWriter().write(json);
        } catch (NumberFormatException|NullPointerException e) {
            e.printStackTrace();
            response.sendError(HttpStatus.SC_GATEWAY_TIMEOUT);
        }
    }

    /*
        User must not access this with get request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendError(HttpStatus.SC_NOT_FOUND);
    }
}
