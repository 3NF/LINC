package Servlets;

import Data.Constraints;
import Database.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangeRoleServlet" , urlPatterns = "/user/change_role")
public class ChangeRoleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String roleS = request.getParameter(Constraints.ROLE);
        String userId = request.getParameter(Constraints.USER_ID);
        String courseID = request.getParameter(Constraints.COURSE_ID);
        String change = request.getParameter("change");
        UserDAO.Role role = UserDAO.Role.valueOf(roleS);
        switch (change){
            case "add" :
                UserDAO.addUser(userId , role , courseID);
                break;
            case "remove" :
                UserDAO.removeUser(userId , role , courseID);
                break;
        }
    }

}
