package Servlets;

import Data.Constraints;
import Database.AssignmentInfoDAO;
import Database.SectionDAO;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpStatus;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static Data.Constraints.ASSIGNMENT_INFO_DAO;
import static Data.Constraints.SECTION_DAO;
import static Data.Constraints.USER;

@WebServlet(name = "GradesServlet", urlPatterns = "/user/grades")
public class GradesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		AssignmentInfoDAO dao = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO);

		User user = (User) request.getSession().getAttribute(Constraints.USER);
		String courseID = request.getParameter(Constraints.COURSE_ID);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF8");


		JsonElement json = dao.getAssignmentsGrades(courseID, user.getUserId());

		response.getWriter().write(new Gson().toJson(json));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		User user = (User) request.getSession().getAttribute(USER);

		SectionDAO sectionDAO = (SectionDAO) request.getServletContext().getAttribute(SECTION_DAO);
		AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO);


		JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
		String classroomId = data.get("classroomId").getAsString();
		String assignmentId = data.get("assignmentId").getAsString();
		System.out.println(classroomId);
		System.out.println(assignmentId);
		data = data.get("grades").getAsJsonObject();
		System.out.println(data);

		Set<String> usersInSection = new HashSet<>(sectionDAO.getUsersInSection(classroomId, user.getUserId()));
		usersInSection.removeAll(data.keySet());

		if (usersInSection.size() != 0) {
			response.sendError(HttpStatus.SC_BAD_REQUEST);
			return;
		}

		if (assignmentInfoDAO.getSubmittedAssignments(classroomId, user.getUserId()).contains(assignmentId)) {
			response.sendError(HttpStatus.SC_BAD_REQUEST);
			return;
		}

		assignmentInfoDAO.submitAssignmentGrades(assignmentId, user.getUserId());
	}
}
