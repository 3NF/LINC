package Servlets;

import Database.CodeFilesDAO;
import Database.GAPIManager;
import Database.ReplyDAO;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static Data.Constraints.CODE_FILES_DAO;
import static Data.Constraints.USER;

@WebServlet(name = "GoogleLogin", urlPatterns = {"/GoogleLogin"})
public class GoogleLogin extends HttpServlet {

    private static final String AUTH_CODE_NAME = "auth_code";
    private static final String ID_TOKEN_NAME = "id_token";


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/user/choose-room.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter("action");
        User resultedUser = null;

        if (action.equals("register")) {
            String authCode = request.getParameter(AUTH_CODE_NAME);
            resultedUser = GAPIManager.getInstance().registerUser(authCode);

        } else if (action.equals("login")) {
            String idToken = request.getParameter(ID_TOKEN_NAME);
            resultedUser = GAPIManager.getInstance().getUser(idToken);
            if (resultedUser == null)  {
                response.sendRedirect("/firstLogin.jsp");
                return;
            }
        }

        request.getSession().setAttribute(USER, resultedUser);
        CodeFilesDAO codeFilesDAO = (CodeFilesDAO) request.getServletContext().getAttribute(CODE_FILES_DAO);
       /* try {
            codeFilesDAO.addAssignments(GAPIManager.downloadAssignments(resultedUser,"15887333289","15917000927"));
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        response.sendRedirect("/user/choose-room.jsp");
    }
}
