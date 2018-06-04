package Servlets;

import HelperClasses.UserDAo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = "/Register")
public class RegisterServlet extends HttpServlet
{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {

        UserDAo userDAo = (UserDAo) request.getServletContext().getAttribute("userDAo");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        userDAo.addUser(firstName, lastName, email, password);
        HttpSession session = request.getSession();
        session.setAttribute("user", userDAo.getUser(email,password));
        response.sendRedirect("user/dashboard.jsp");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getRequestDispatcher("register.jsp").forward(request,response);
    }
}
