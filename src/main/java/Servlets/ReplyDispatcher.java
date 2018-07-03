package Servlets;

import Database.ReplyDAO;
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

@WebServlet(name = "ReplyDispatcher", urlPatterns = "/user/reply_dispatcher")
public class ReplyDispatcher extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            User user = null;

            //Get request data
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            System.out.println(data);
            String suggestionID = data.get("suggestionID").getAsString();

            //Get ReplyDAO
            ReplyDAO replyDAO = (ReplyDAO)request.getServletContext().getAttribute("ReplyDAO");

            //Get replies from database
            List<Reply> replies = replyDAO.getSuggestionReplies(suggestionID);

            //Convert file data into JSON
            String json = new GsonBuilder().disableHtmlEscaping().create().toJson(replies);

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
