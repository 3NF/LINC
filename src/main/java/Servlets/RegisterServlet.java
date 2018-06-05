package Servlets;

import HelperClasses.Constraints;
import HelperClasses.User;
import HelperClasses.UserDAo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "RegisterServlet", urlPatterns = "/Register")
public class RegisterServlet extends HttpServlet
{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException  {

        UserDAo userDAo = (UserDAo) request.getServletContext().getAttribute(Constraints.USERDAO_NAME);

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String uuid = UUID.randomUUID().toString();
        userDAo.addUser(firstName, lastName, email, password, uuid);
        request.getSession().setAttribute("user", new User(firstName,lastName,email));
        //TODO-show verification send to mail message
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.getRequestDispatcher("register.jsp").forward(request,response);
    }
}
