package Servlets;

import Data.Constraints;
import Database.AssignmentInfoDAO;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static Data.Constraints.ASSIGNMENT_INFO_DAO;

@WebServlet(name = "GetGradesServlet", urlPatterns = "/user/get-grades")
public class GetGradesServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		AssignmentInfoDAO dao = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO);

		User user = (User) request.getSession().getAttribute(Constraints.USER);
		String courseID = request.getParameter(Constraints.COURSE_ID);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF8");


		JsonElement json = dao.getAssignmentsGrades(courseID, user.getUserId());

		response.getWriter().write(new Gson().toJson(json));
	}
}
