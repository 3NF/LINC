package Servlets;

import Database.SuggestionDAO;
import Database.ValidateDAO;
import Models.Suggestion;
import Models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.http.HttpStatus;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static Data.Constraints.*;

@WebServlet(name = "SuggestionDispatcher", urlPatterns = "/user/suggestion_dispatcher")
public class SuggestionDispatcher extends HttpServlet
{
    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            User user = (User)request.getSession().getAttribute(USER);

            //Get request data
            JsonObject data = new Gson().fromJson(request.getReader(), JsonObject.class);
            String json = "";

            String courseID = data.get(COURSE_ID).getAsString();
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
                String suggestionID = data.get(SUGGESTION_ID).getAsString();
                deleteSuggestion(suggestionDAO, suggestionID);
                response.sendError(HttpStatus.SC_OK);
            }

            //Send response to client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF8");
            assert json != null;
            response.getWriter().write(json);
        } catch (NumberFormatException|NullPointerException|SQLException e) {
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

            suggestion.user = user;
            return new GsonBuilder().disableHtmlEscaping().create().toJson(suggestion);
        } catch (ClassCastException|IllegalArgumentException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void deleteSuggestion (SuggestionDAO suggestionDAO, String suggestionID) throws SQLException {
        suggestionDAO.deleteSuggestion(suggestionID);
    }

    /*
        User must not access this with get request
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendError(HttpStatus.SC_NOT_FOUND);
    }
}
