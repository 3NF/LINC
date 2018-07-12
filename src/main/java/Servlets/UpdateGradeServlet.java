package Servlets;

import Data.Constraints;
import Database.AssignmentInfoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateGradeServlet" , urlPatterns = "/user/update_grade")
public class UpdateGradeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String grade = request.getParameter("grade");
        String assignmentID = request.getParameter(Constraints.ASSIGNMENT_ID);
        String userID = request.getParameter(Constraints.USER_ID);
        AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(Constraints.ASSIGNMENT_INFO_DAO);
        assignmentInfoDAO.updateGrade(assignmentID , userID , grade);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
