package Servlets;

import Database.AssignmentInfoDAO;
import Database.GAPIManager;
import Models.AddAssignmentResponse;
import Models.User;
import com.google.gson.*;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static Data.Constraints.*;


/**
 * This servlet is for adding assignment files in database
 * You could enter data following way:
 * <p>
 * Attribute USER_ID_TOKEN    -  represents google's token
 * Attribute ASSIGNMENT       -  Json object that stores :
 * {
 * Classroom ID
 * Assignment ID
 * JsonArray of {fileName, content}
 * }
 */


@WebServlet(name = "AddAssignmentServlet")
public class AddAssignmentServlet extends HttpServlet {


    private List<String> JSONArrayToList(JsonArray arr) {
        ArrayList<String> res = new ArrayList<>();
        for (JsonElement fileName : arr) {
            res.add(fileName.getAsString());
        }
        return res;
    }


    private List<String> getMissingFiles(List<String> fromColl, List<String> toColl) {
        Set<String> res = new HashSet<>();
        res.addAll(toColl);
        res.removeAll(fromColl);
        ArrayList<String> r = new ArrayList<>();
        r.addAll(res);
        return r;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        GAPIManager gapiManager = (GAPIManager) request.getServletContext().getAttribute(GAPI_MANAGER);
        AssignmentInfoDAO dao = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO);
        User user = gapiManager.getUser((String) request.getAttribute(USER_ID_TOKEN));
        String assignment = (String) request.getAttribute(ASSIGNMENT);
        String courseID = (String) request.getAttribute(COURSE_ID);
        String assignmentID = (String) request.getAttribute(ASSIGNMENT_ID);

        AddAssignmentResponse res = new AddAssignmentResponse();


        if (user == null) {
            res.setMessage(AddAssignmentResponse.ErrorMessage.InvalidUser);
            response.getWriter().write(res.toString());
            return;
        }

        if (!gapiManager.isInRoom(user.getUserId(), courseID)) {
            res.setMessage(AddAssignmentResponse.ErrorMessage.AssignmentForbidden);
            response.getWriter().write(res.toString());
            return;
        }

        JsonParser parser = new JsonParser();
        JsonArray assignmentJSON = parser.parse(assignment).getAsJsonArray();

        List<String> uploadedFiles = JSONArrayToList(assignmentJSON);
        List<String> assignmentFiles = dao.getAssignmentFilesNames(courseID, assignmentID);

        res.setExtraFiles(getMissingFiles(assignmentFiles, uploadedFiles));
        res.setMissingFiles(getMissingFiles(uploadedFiles, assignmentFiles));

        response.getWriter().write(res.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
