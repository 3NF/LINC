package Servlets;

import Data.Constraints;
import Database.CodeFilesDAO;
import Database.UserStorage;
import Models.CodeFile;
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
import static Data.Constraints.USER_STORAGE;

@WebServlet(name = "CodeDispatcher", urlPatterns = "/user/code_dispatcher")
public class CodeDispatcher extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //Get request data
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String json;
            if (data.has(Constraints.ASSIGNMENT_ID)){
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
        String codeId = data.get(Constraints.CODE_ID).getAsString();
        HttpSession session = request.getSession();
        CodeFilesDAO codeFilesDAO = (CodeFilesDAO) request.getServletContext().getAttribute(Constraints.CODE_FILES_DAO);
        try {
            User user = (User) session.getAttribute(USER);
            UserStorage userStorage = (UserStorage)request.getServletContext().getAttribute(USER_STORAGE);

            CodeFile codeFile = codeFilesDAO.getFilesContent(user.getUserId(), codeId);
            codeFile.RetrieveUsers(user.getUserId(), userStorage);

            return new GsonBuilder().create().toJson(codeFile);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String loadCodeNames(JsonObject data,HttpServletRequest request){
        String assignmentID = data.get(Constraints.ASSIGNMENT_ID).getAsString();
        CodeFilesDAO codeFilesDAO = (CodeFilesDAO) request.getServletContext().getAttribute(Constraints.CODE_FILES_DAO);
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
