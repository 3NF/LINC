package Servlets;

import Database.CodeManager;
import HelperClasses.Validate;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CodeDispatcher", urlPatterns = "/user/code_dispatcher")
public class CodeDispatcher extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            //Simple user validation
            if (!Validate.isLogged(session)) {
                throw new NullPointerException();
            }
            //Temporary
            User user = null;

            //Get request data
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String codeName = data.get("codeName").getAsString();
            CodeManager codeManager = (CodeManager) session.getAttribute("CodeManager");

            if (codeManager == null) {
                codeManager = new CodeManager(user);
                session.setAttribute("CodeManager", codeManager);
            }

            //Convert file data into JSON
            String json = new GsonBuilder().disableHtmlEscaping().create().toJson(codeManager.get(codeName));
            System.out.println(json);

            //Send response to client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF8");
            response.getWriter().write(json);
        } catch (NumberFormatException|NullPointerException e) {
            response.sendError(HttpStatus.SC_NOT_FOUND);
        }
    }

    /*
        User must not access this with get request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendError(HttpStatus.SC_NOT_FOUND);
    }
}
