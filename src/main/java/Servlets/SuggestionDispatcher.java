package Servlets;

import Database.CodeFilesDAO;
import Database.SuggestionDAO;
import Database.UserStorage;
import Database.ValidateDAO;
import HelperClasses.Validate;
import Models.Suggestion;
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
import java.sql.SQLException;

import static Data.Constraints.*;

@WebServlet(name = "SuggestionDispatcher", urlPatterns = "/user/suggestion_dispatcher")
public class SuggestionDispatcher extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = (User)request.getSession().getAttribute(USER);

            //Get request data
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String json;

            String courseID = data.get("courseID").getAsString();
            String codeFileID = data.get("codeFileID").getAsString();

            ValidateDAO validateDAO = (ValidateDAO)request.getServletContext().getAttribute(VALIDATE_DAO);
            SuggestionDAO suggestionDAO;
            suggestionDAO = (SuggestionDAO)request.getServletContext().getAttribute(SUGGESTION_DAO);

            if (!validateDAO.hasSuggestionWritePermission(user, courseID, codeFileID)){
                response.sendError(HttpStatus.SC_FORBIDDEN);
                return;
            }

            if (data.has("content")){
                json = addSuggestion(request, data, codeFileID, user, suggestionDAO);
            }
            else {
                suggestionDAO.deleteSuggestion(data.get("suggestionID").getAsString());
                json = "";//deleteSuggestion(data,request);
            }

            //Send response to client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF8");
            response.getWriter().write(json);
        } catch (NumberFormatException|NullPointerException e) {
            e.printStackTrace();
            response.sendError(HttpStatus.SC_NOT_FOUND);
        }
    }

    private String addSuggestion (HttpServletRequest request, JsonObject data, String codeFileID, User user, SuggestionDAO suggestionDAO) {
        try {
            String content = data.get("content").getAsString();
            String type = data.get("type").getAsString();

            //Might throw ClassCastException
            int startInd = data.get("startInd").getAsInt();
            int endInd = data.get("endInd").getAsInt();

            //Might throw IllegalArgumentException
            Suggestion.SuggestionType.valueOf(type);

            Suggestion suggestion = suggestionDAO.addSuggestion(user, codeFileID, type, content, startInd, endInd);
            UserStorage userStorage = (UserStorage) request.getServletContext().getAttribute(USER_STORAGE);

            suggestion.RetrieveUsers(user.getUserId(), userStorage);
            return new GsonBuilder().disableHtmlEscaping().create().toJson(suggestion);
        } catch (ClassCastException|IllegalArgumentException e) {
            e.printStackTrace();
            return null;
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
