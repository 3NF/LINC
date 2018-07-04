package Servlets;

import Database.ReplyDAO;
import Database.ValidateDAO;
import HelperClasses.Validate;
import Models.Reply;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Data.Constraints.USER;

@WebServlet(name = "ReplyDispatcher", urlPatterns = "/user/reply_dispatcher")
public class ReplyDispatcher extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        try {
            User user = null;

            //Get request data
            String json;
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            user = (User) session.getAttribute(USER);
            String courseID = data.get("courseID").getAsString();
            ValidateDAO validateDAO = (ValidateDAO) request.getServletContext().getAttribute("ValidateDAO");
            //Get ReplyDAO
            ReplyDAO replyDAO = (ReplyDAO) request.getServletContext().getAttribute("ReplyDAO");
            String suggestionID = data.get("suggestionID").getAsString();

            if (!validateDAO.isValidate(user,suggestionID,courseID)){
                //erroris dabruneba minda
                return;
            }

            if (data.has("content")){
                String UserID = ((User) session.getAttribute(USER)).getUserId();
                Reply reply = replyDAO.addReply(data.get("content").getAsString(),UserID,suggestionID);

                json = new GsonBuilder().disableHtmlEscaping().create().toJson(reply);
            }
            else {
                //Get replies from database
                List<Reply> replies = replyDAO.getSuggestionReplies(suggestionID);

                //Convert file data into JSON
                json = new GsonBuilder().disableHtmlEscaping().create().toJson(replies);
            }

            //Send response to client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF8");
            response.getWriter().write(json);
        } catch (NumberFormatException|NullPointerException e) {
            e.printStackTrace();
            response.sendError(HttpStatus.SC_NOT_FOUND);
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
