package Servlets;

import Database.CodeFilesDAO;
import Database.ConnectionPool;
import Database.ReplyDAO;
import Models.Reply;
import Models.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import static Data.Constraints.USER;
import static Database.Config.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReplyDispatcherTest {
    private StringWriter response_writer;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private HttpSession session;
    private MysqlDataSource source;
    private ReplyDAO DAO;
    Connection connection;
    @Before
    public void doMocks() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servletContext = mock(ServletContext.class);
        session = mock(HttpSession.class);
        source = new MysqlDataSource();
        source.setServerName(MYSQL_DATABASE_SERVER);
        source.setDatabaseName(MYSQL_DATABASE_NAME);
        source.setUser(MYSQL_USERNAME);
        source.setPassword(MYSQL_PASSWORD);
    }

    @Test
    public void test1() throws ServletException, IOException, SQLException, JSONException {
        ReplyDAO DAO = new ReplyDAO(source);
        DAO.addReply("asdd","asd","-12");
        String json = "{\"suggestionID\":-12, \"amount\":222}";
        when(request.getSession()).thenReturn(session);
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(json)));
        // define return value for method getUniqueId()
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("ReplyDAO")).thenReturn(new ReplyDAO(source));
        response_writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(response_writer));
        new ReplyDispatcher().doPost(request, response);
        System.out.println(response_writer.toString());
        JSONArray array = new JSONArray(response_writer.toString());
        JSONObject jsonobject = array.getJSONObject(0);
        assertEquals(jsonobject.get("suggestionID"),"-12");
        assertEquals(jsonobject.get("userID"),"asd");
        String query = "DELETE FROM replies WHERE suggestionID=-12";
        source.getConnection().createStatement().execute(query);
    }
}