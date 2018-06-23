package Servlets;

import Data.Constraints;
import Database.DBManager;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "rooms", urlPatterns = "/user/rooms")
public class RoomServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request , response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseId = (String) request.getAttribute(Constraints.COURSE_ID);
        User user = (User) request.getSession().getAttribute(Constraints.USER_IN_SESSION);
        DBManager.Role userRole = DBManager.getRoleByCourse(user , courseId);
        String url = "";
        switch(userRole){
            case Guest:
                url = "choose-room.jsp";
                break;
            case Pupil:
                url = "/user/dashboard.jsp";
                break;
            case Teacher:
                url = "teacher-dashboard.jsp";
                break;
            case SeminarReader :
            case TeacherAssistant :
                url = "assistant-dashboard.jsp";
                break;
        }
        response.setContentType("text/html;charset = UTF-8");
        response.getWriter().write(url);
    }
}
