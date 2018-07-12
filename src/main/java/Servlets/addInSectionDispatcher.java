package Servlets;

import Data.Constraints;
import Database.SectionDAO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(name = "Servlet",urlPatterns = "/user/addInSectionDispatcher")
public class addInSectionDispatcher extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
        //if (data.hz)
        String courseID = data.get("courseID").getAsString();
        String leaderID = data.get("leaderID").getAsString();
        JsonArray array = data.get("sections").getAsJsonArray();
        System.err.println(array);

        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List <String> list = new Gson().fromJson(array , listType);
        SectionDAO sectionDAO = (SectionDAO) request.getServletContext().getAttribute(Constraints.SECTION_DAO);
        sectionDAO.addUsersInSection(courseID,leaderID,list);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
