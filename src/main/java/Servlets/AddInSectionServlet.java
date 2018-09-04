package Servlets;

import Data.Constraints;
import Database.SectionDAO;
import Database.ValidateDAO;
import Models.User;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpStatus;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static Data.Constraints.USER;
import static Data.Constraints.VALIDATE_DAO;

@WebServlet(name = "Servlet",urlPatterns = "/user/add_in_section_servlet")
public class AddInSectionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
        String courseID = data.get("courseID").getAsString();
        String leaderID = data.get("leaderID").getAsString();
        JsonArray array = data.get("sections").getAsJsonArray();
        String userID = ((User) request.getSession().getAttribute(USER)).getUserId();
        ValidateDAO validateDAO = (ValidateDAO) request.getServletContext().getAttribute(VALIDATE_DAO);
        if (!validateDAO.isTeacher(userID, courseID)){
            response.sendError(HttpStatus.SC_FORBIDDEN);
            return;
        }
        Type listType = new TypeToken<List<String>>() {
			private static final long serialVersionUID = 1L;
        }.getType();
        List <String> list = new Gson().fromJson(array , listType);
        SectionDAO sectionDAO = (SectionDAO) request.getServletContext().getAttribute(Constraints.SECTION_DAO);
        sectionDAO.removeSections(courseID);
        sectionDAO.addUsersInSection(courseID,leaderID,list);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }
}
