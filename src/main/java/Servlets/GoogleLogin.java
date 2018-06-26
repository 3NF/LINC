package Servlets;

import Database.GAPIManager;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static Data.Constraints.USER;

@WebServlet(name = "GoogleLogin", urlPatterns = {"/GoogleLogin"})
public class GoogleLogin extends HttpServlet {

	private static final String AUTH_CODE_NAME = "auth_code";
	private static final String ID_TOKEN_NAME = "id_token";


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
			response.sendRedirect("/user/choose-room.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String action = request.getParameter("action");
		User resultedUser = null;

		if(action.equals("register"))
		{
			String authCode = request.getParameter(AUTH_CODE_NAME);
			resultedUser = GAPIManager.getInstance().registerUser(authCode);
			request.getSession().setAttribute(USER, resultedUser);

		}else if(action.equals("login"))
		{
			String idToken = request.getParameter(ID_TOKEN_NAME);
			resultedUser = GAPIManager.getInstance().getUser(idToken);
			if(resultedUser == null) request.getRequestDispatcher("/firstLogin.jsp").forward(request,response);
		}

		request.getSession().setAttribute(USER, resultedUser);
        response.sendRedirect("/user/choose-room.jsp");
	}
}
