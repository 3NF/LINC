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
 * Parameter user_id_token    -  represents google's token
 * Parameter room-id          -  represents google classrooms id
 * Parameter assignment-id    -  represents assignment id
 * Parameter assignment       -  JsonArray of {file-name, content}
 *
 *
 *
 * example:
 * {
 *
 *   "user_id_token" : "-",
 *   "room-id" : "-",
 *   "assignment-id" : "-",
 *
 *
 *   "assignment": [
 *     {
 *       "file-name": "sample.h",
 *       "content": "asdasd"
 *     },
 *     {
 *       "file-name": "main.h",
 *       "content": "asdasd"
 *     },
 *     {
 *       "file-name": "main.c",
 *       "content": "asdasd"
 *     },
 *     {
 *       "file-name": "sample.h",
 *       "content": "asdasd"
 *     }
 *   ]
 * }
 */


@WebServlet(name = "AddAssignmentServlet")
public class AddAssignmentServlet extends HttpServlet {

	private List<String> JSONArrayToList(JsonArray arr) {
        ArrayList<String> res = new ArrayList<>();
        for (JsonElement fileName : arr) {
            res.add(fileName.getAsJsonObject().get("file-name").getAsString());
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
        User user = gapiManager.getUser((String) request.getParameter(USER_ID_TOKEN));
        String roomID = (String) request.getParameter(COURSE_ID);
        String assignmentID = (String) request.getParameter(ASSIGNMENT_ID);

        AddAssignmentResponse res = new AddAssignmentResponse();

        if (user == null) {
            res.setMessage(AddAssignmentResponse.ErrorMessage.InvalidUser);
            response.getWriter().write(res.toString());
            return;
        }

        if (!gapiManager.isInRoom(user, roomID)) {
            res.setMessage(AddAssignmentResponse.ErrorMessage.AssignmentForbidden);
            response.getWriter().write(res.toString());
            return;
        }
        String assignment = (String) request.getParameter(ASSIGNMENT);
        JsonParser parser = new JsonParser();
        JsonArray assignmentJSON = parser.parse(assignment).getAsJsonArray();

        List<String> uploadedFiles = JSONArrayToList(assignmentJSON);
        List<String> assignmentFiles = dao.getAssignmentFilesNames(roomID, assignmentID);

        List<String> missingFiles = getMissingFiles(assignmentFiles, uploadedFiles);
        List<String> extraFiles = getMissingFiles(uploadedFiles, assignmentFiles);

        if (extraFiles.size() + missingFiles.size() == 0) {
            res.setMessage(AddAssignmentResponse.ErrorMessage.Success);
            response.getWriter().write(res.toString());
            return;
        }

        res.setExtraFiles(missingFiles);
        res.setMissingFiles(extraFiles);
        res.setMessage(AddAssignmentResponse.ErrorMessage.WrongFiles);

        response.getWriter().write(res.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
