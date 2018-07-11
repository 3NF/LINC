package Servlets;

import Data.Constraints;
import Database.ReplyDAO;
import Database.SectionDAO;
import Database.UserStorage;
import Database.ValidateDAO;
import Models.Reply;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.http.HttpStatus;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static Data.Constraints.*;

@WebServlet(name = "ReplyDispatcher", urlPatterns = "/user/reply_dispatcher")
public class SectionDispatcher extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        try {
            User user = (User) session.getAttribute(USER);

            //Get request data
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String userID = data.get(Constraints.USER).getAsString();
            String classroomID = data.get(COURSE_ID).getAsString();
            SectionDAO sectionDAO = (SectionDAO) request.getServletContext().getAttribute(SECTION_DAO);
            String json = new GsonBuilder().disableHtmlEscaping().create().toJson(sectionDAO.getUsersInSection(classroomID,userID));
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
