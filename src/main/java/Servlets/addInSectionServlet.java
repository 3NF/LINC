package Servlets;

import Data.Constraints;
import Database.SectionDAO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(name = "Servlet",urlPatterns = "/user/add_in_section_servlet")
public class addInSectionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("HeyThere");
        JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
        String courseID = data.get("courseID").getAsString();
        String leaderID = data.get("leaderID").getAsString();
        JsonArray array = data.get("sections").getAsJsonArray();

        Type listType = new TypeToken<List<String>>() {
			private static final long serialVersionUID = 1L;
        }.getType();
        List <String> list = new Gson().fromJson(array , listType);
        System.out.println(list.size());
        SectionDAO sectionDAO = (SectionDAO) request.getServletContext().getAttribute(Constraints.SECTION_DAO);
        sectionDAO.addUsersInSection(courseID,leaderID,list);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }
}
