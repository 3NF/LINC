package Servlets;

import Database.ReplyDAO;
import Database.SuggestionDAO;
import Database.ValidateDAO;
import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import static Data.Constraints.SUGGESTION_DAO;
import static Data.Constraints.VALIDATE_DAO;
import static Database.Config.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuggestionDispatcherTest {
    private StringWriter response_writer;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private HttpSession session;
    private MysqlDataSource source;
    private SuggestionDAO DAO;
    Connection connection;
    @Before
    public void doMocks() throws SQLException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servletContext = mock(ServletContext.class);
        session = mock(HttpSession.class);
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
        connection=source.getConnection();
    }

    @Test
    public void test() throws IOException, ServletException, SQLException, JSONException {
        String json = "{\"codeFileID\":-12, \"content\":222,\"courseID\":222,\"type\":Warning,\"startInd\":-12,\"endInd\":-12}";
        ValidateDAO valDAO = mock(ValidateDAO.class);
        when(request.getSession()).thenReturn(session);
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(json)));
        // define return value for method getUniqueId()
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(VALIDATE_DAO)).thenReturn(valDAO);
        when(servletContext.getAttribute(SUGGESTION_DAO)).thenReturn(new SuggestionDAO(source, null));
        response_writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(response_writer));
        when(session.getAttribute("user")).thenReturn(new User("105303857051815287047"));
        when(valDAO.hasSuggestionWritePermission(any(),any(),any())).thenReturn(true);
        new SuggestionDispatcher().doPost(request,response);
        System.err.println(response_writer.toString());
        JSONObject obj = new JSONObject(response_writer.toString());
        assertEquals(obj.getString("type"),"Warning");
        assertEquals(obj.getString("userID"),"105303857051815287047");
        connection.createStatement().execute("DELETE FROM suggestions WHERE Code_FIleID=-12");
    }
}