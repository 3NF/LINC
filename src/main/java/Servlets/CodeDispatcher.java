package Servlets;

import static Data.Constraints.SECTION_DAO;
import static Data.Constraints.TEACHER_ID;
import static Data.Constraints.USER;
import static Data.Constraints.USER_STORAGE;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.apache.http.HttpStatus;

import Data.Constraints;
import Database.AssignmentInfoDAO;
import Database.CodeFilesDAO;
import Database.SectionDAO;
import Database.UserStorage;
import Models.CodeFile;
import Models.User;

@WebServlet(name = "CodeDispatcher", urlPatterns = "/user/code_dispatcher")
public class CodeDispatcher extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //Get request data
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String json;
            String teacherID = data.get(TEACHER_ID).getAsString();
            String userID = ((User) request.getSession().getAttribute(USER)).getUserId();
            SectionDAO sectionDAO = (SectionDAO) request.getServletContext().getAttribute(SECTION_DAO);

            if (data.has(Constraints.USER_ID) && sectionDAO.isInSection(userID, data.get(Constraints.USER_ID).getAsString())) {
                userID = data.get(Constraints.USER_ID).getAsString();
            }

            if (data.has(Constraints.ASSIGNMENT_ID)) {
                json = loadCodeNames(data, request);
            } else {
                json = loadCodeWithID(data, request, userID, teacherID);
            }
            //Send response to client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF8");
            response.getWriter().write(json);
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            response.sendError(HttpStatus.SC_NOT_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Couldn't save data in database");
        }
    }

    private String loadCodeWithID(JsonObject data, HttpServletRequest request, String userID, String teacherID) throws SQLException {
        String codeFilesId = data.get(Constraints.CODE_ID).getAsString();
        CodeFilesDAO codeFilesDAO = (CodeFilesDAO) request.getServletContext().getAttribute(Constraints.CODE_FILES_DAO);
        boolean needsContent = data.get("needsContent").getAsBoolean();

        UserStorage userStorage = (UserStorage) request.getServletContext().getAttribute(USER_STORAGE);

        CodeFile codeFile = codeFilesDAO.getFilesContent(codeFilesId);
        if (userStorage != null) {
            System.err.println(teacherID);
            System.err.println(userStorage);
            codeFile.RetrieveUsers(teacherID, userStorage);
        }

        if (!needsContent) {
            codeFile.setCode(null);
        }

        return new GsonBuilder().create().toJson(codeFile);
    }

    private String loadCodeNames(JsonObject data, HttpServletRequest request) throws SQLException {
        AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(Constraints.ASSIGNMENT_INFO_DAO);
        String userID = ((User) request.getSession().getAttribute(USER)).getUserId();
        if (data.has("userID"))
            userID = data.get("userID").getAsString();
        String assignmentID = data.get(Constraints.ASSIGNMENT_ID).getAsString();

        return new GsonBuilder().disableHtmlEscaping().create().toJson(assignmentInfoDAO.getAssignmentFilesPath(userID, assignmentID));
    }

    /*
        User must not access this with get request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.SC_NOT_FOUND);
    }
}
