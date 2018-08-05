package Servlets;

import Data.Constraints;
import Database.UserDAO;
import Models.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "rooms", urlPatterns = "/user/rooms")
public class RoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request , response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String courseId = request.getParameter(Constraints.COURSE_ID);
        User user = (User) request.getSession().getAttribute(Constraints.USER);
        UserDAO.Role userRole = UserDAO.getRoleByCourse(user , courseId);
        String url = "";
    
        switch(userRole){
            case Guest:
                url = "choose-room.jsp";
                break;
            case Pupil:
                url = "/user/dashboard.jsp?" + Constraints.COURSE_ID + "=" + courseId;
                break;
            case SeminarReader :
            case TeacherAssistant :
                url = "instructor-dashboard.jsp?" + Constraints.COURSE_ID + "=" + courseId;
                break;
            default:
                break;
        }
        response.setContentType("text/html;charset = UTF-8");
	    response.getWriter().write(url);
    }
}
