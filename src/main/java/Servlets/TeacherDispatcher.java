package Servlets;

import Data.Constraints;
import Database.CodeFilesDAO;
import Database.GAPIManager;
import Models.UploadedAssignment;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static Data.Constraints.CODE_FILES_DAO;
import static Models.File.USER;

@WebServlet(name = "TeacherDispatcher")
public class TeacherDispatcher extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
        User user = (User) request.getSession().getAttribute(USER);
        String assignmentID = data.get("assignmentID").getAsString();
        String courseID = data.get("courseID").getAsString();
        UploadedAssignment uploadedAssignment = GAPIManager.downloadAssignments(user,courseID,assignmentID);

        CodeFilesDAO codeFilesDAO = (CodeFilesDAO) request.getServletContext().getAttribute(CODE_FILES_DAO);
        try {
            codeFilesDAO.addAssignments(uploadedAssignment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
