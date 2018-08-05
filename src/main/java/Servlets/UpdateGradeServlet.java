package Servlets;

import Data.Constraints;
import Database.AssignmentInfoDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UpdateGradeServlet" , urlPatterns = "/user/update_grade")
public class UpdateGradeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String grade = request.getParameter("grade");
        String assignmentID = request.getParameter(Constraints.ASSIGNMENT_ID);
        String userID = request.getParameter(Constraints.USER_ID);
        AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(Constraints.ASSIGNMENT_INFO_DAO);
        assignmentInfoDAO.updateGrade(assignmentID , userID , grade);
    }

}
