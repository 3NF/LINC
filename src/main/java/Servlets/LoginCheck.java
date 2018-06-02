package Servlets;

import HelperClasses.User;
import HelperClasses.UserDAo;
import com.google.gson.Gson;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LoginCheck", urlPatterns = {"/LoginCheck"})
public class LoginCheck extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UserDAo userDAo = (UserDAo) request.getServletContext().getAttribute("UserDAo");
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String email = request.getParameter("email");
		String pass = request.getParameter("password");
		User user = userDAo.getUser(email, pass);
		if (user != null) {
			System.out.println(user.getFirstName());
			request.getSession().setAttribute("user", user);
			response.sendRedirect("/dashboard.jsp");
		} else {
			request.setAttribute("wrongPassword", true);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}
