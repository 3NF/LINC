package Servlets;

import Database.CodeFilesDAO;
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
import java.sql.SQLException;

import static Data.Constraints.USER;

@WebServlet(name = "CodeDispatcher", urlPatterns = "/user/code_dispatcher")
public class CodeDispatcher extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //Temporary
            User user = null;

            //Get request data
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String json;
            if (data.has("assignmentID")){
                json = loadCodeNames(data,request);
            }
            else {
                json = loadCodeWithID(data,request);
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

    private String loadCodeWithID(JsonObject data,HttpServletRequest request){
        String codeId = data.get("codeID").getAsString();
        HttpSession session = request.getSession();
        CodeFilesDAO codeFilesDAO = (CodeFilesDAO) request.getServletContext().getAttribute("CodeFilesDAO");
        try {
            User user = (User) session.getAttribute(USER);
            return new GsonBuilder().create().toJson(codeFilesDAO.getFilesContent("1", codeId));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String loadCodeNames(JsonObject data,HttpServletRequest request){
        String assignmentID = data.get("assignmentID").getAsString();
        CodeFilesDAO codeFilesDAO = (CodeFilesDAO) request.getServletContext().getAttribute("CodeFilesDAO");
        try {
            return new GsonBuilder().disableHtmlEscaping().create().toJson(codeFilesDAO.getAssignmentCodeNames(assignmentID));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
