package Servlets;

import Data.Constraints;
import Database.*;
import HelperClasses.Validate;
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

import static Data.Constraints.*;

@WebServlet(name = "CodeDispatcher", urlPatterns = "/user/code_dispatcher")
public class CodeDispatcher extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //Get request data
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String json;
            String userID = ((User) request.getSession().getAttribute(USER)).getUserId();
            SectionDAO sectionDAO = (SectionDAO) request.getSession().getAttribute(SECTION_DAO);


            System.out.println("QWERTY");
            if (data.has(Constraints.USER_ID) && sectionDAO.isInSection(userID, data.get(Constraints.USER_ID).getAsString())) {
                userID = data.get(Constraints.USER_ID).getAsString();
            }

            if (data.has(Constraints.ASSIGNMENT_ID)){
                json = loadCodeNames(data,request);
            }
            else {
                System.out.println(userID);
                json = loadCodeWithID(data,request, userID);
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

    private String loadCodeWithID(JsonObject data,HttpServletRequest request, String userID){
        String codeFilesId = data.get(Constraints.CODE_ID).getAsString();
        System.out.println(codeFilesId);
        CodeFilesDAO codeFilesDAO = (CodeFilesDAO) request.getServletContext().getAttribute(Constraints.CODE_FILES_DAO);
        try {
            UserStorage userStorage = (UserStorage)request.getServletContext().getAttribute(USER_STORAGE);

            CodeFile codeFile = codeFilesDAO.getFilesContent(codeFilesId);
            //codeFile.RetrieveUsers(userID, userStorage);

            return new GsonBuilder().create().toJson(codeFile);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String loadCodeNames(JsonObject data,HttpServletRequest request){
        AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(Constraints.ASSIGNMENT_INFO_DAO);
        String userID = ((User) request.getSession().getAttribute(USER)).getUserId();
        if (data.has("userID"))
            userID =  data.get("userID").getAsString();
        String assignmentID = data.get(Constraints.ASSIGNMENT_ID).getAsString();

        return new GsonBuilder().disableHtmlEscaping().create().toJson(assignmentInfoDAO.getAssignmentFilesPath(userID,assignmentID));
    }

    /*
        User must not access this with get request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendError(HttpStatus.SC_NOT_FOUND);
    }
}
