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
        String gradeS = request.getParameter("grade");
        String courseID = request.getParameter(Constraints.COURSE_ID);
        String userID = request.getParameter(Constraints.USER_ID);
        String noSpaceStr = gradeS.replaceAll("\\s", "");
        AssignmentInfoDAO.Grade grade = AssignmentInfoDAO.Grade.valueOf(noSpaceStr);
        System.out.println(grade.getName());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
