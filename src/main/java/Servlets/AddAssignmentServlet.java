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
 * Property user_id_token    -  represents google's token
 * Property room-id          -  represents google classrooms id
 * Property assignment-id    -  represents assignment id
 * Property assignment       -  JsonArray of {file-name, content}
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



@WebServlet(name = "AddAssignmentServlet", urlPatterns = {"/add-assignment"})
public class AddAssignmentServlet extends HttpServlet {


    private List<String> getFileNames(JsonArray arr) {
        ArrayList<String> res = new ArrayList<>();
        for (JsonElement elem : arr) {
            res.add(elem.getAsJsonObject().get("file-name").getAsString());
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



        JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
        System.out.println(data);

        GAPIManager gapiManager = (GAPIManager) request.getServletContext().getAttribute(GAPI_MANAGER);
        AssignmentInfoDAO dao = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO);
        User user = gapiManager.getUser(data.get(USER_ID_TOKEN).getAsString());

        JsonArray assignment = data.get(ASSIGNMENT).getAsJsonArray();
        String roomID = data.get(ROOM_ID).getAsString();
        String assignmentID = data.get(ASSIGNMENT_ID).getAsString();

        AddAssignmentResponse res = new AddAssignmentResponse();

        if (user == null) {
            res.setMessage(AddAssignmentResponse.ErrorMessage.InvalidUser);
            response.getWriter().write(res.toString());
            return;
        }

        if (!gapiManager.isInRoom(user.getUserId(), roomID)) {
            res.setMessage(AddAssignmentResponse.ErrorMessage.AssignmentForbidden);
            response.getWriter().write(res.toString());
            return;
        }

        List<String> uploadedFiles = getFileNames(assignment);
        List<String> assignmentFiles = dao.getAssignmentFilesNames(roomID, assignmentID);

        res.setExtraFiles(getMissingFiles(assignmentFiles, uploadedFiles));
        res.setMissingFiles(getMissingFiles(uploadedFiles, assignmentFiles));

        response.getWriter().write(res.toString());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
