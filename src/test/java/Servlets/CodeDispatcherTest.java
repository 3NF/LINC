package Servlets;

import Data.Constraints;
import Database.AssignmentInfoDAO;
import Database.CodeFilesDAO;
import Database.ConnectionPool;
import Database.UserStorage;
import Models.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;

import static Data.Constraints.USER;
import static Data.Constraints.USER_STORAGE;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CodeDispatcherTest {
    private StringWriter response_writer;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private HttpSession session;

    @Before
    public void doMocks() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servletContext = mock(ServletContext.class);
        session = mock(HttpSession.class);
    }

    @Test
    public void test1() throws IOException {
        //  create mock
        UserStorage userStorage = mock(UserStorage.class);
        String json = "{\"codeID\":1, \"amount\":222}";
        when(request.getSession()).thenReturn(session);
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(json)));
        // define return value for method getUniqueId()
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("CodeFilesDAO")).thenReturn(new CodeFilesDAO(ConnectionPool.getInstance()));
        when(session.getAttribute(USER)).thenReturn(new User("105303857051815287047"));
        when(session.getAttribute(USER_STORAGE)).thenReturn(null);
        when(userStorage.getUserWithID(any(),any())).thenReturn(new User("123123"));
        response_writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(response_writer));
        new CodeDispatcher().doPost(request, response);

        System.out.println(response_writer.toString());
        JsonParser parse = new JsonParser();
        JsonObject object = (JsonObject) parse.parse(response_writer.toString());

        assertEquals(object.get("fileName").getAsString(), "jondo/lato/user.java");
        // use mock in test....
    }

    @Test
    public void test2() throws IOException, JSONException {
        //  create mock
        UserStorage userStorage = mock(UserStorage.class);
        String json = "{\"assignmentID\":123123123123, \"amount\":222}";
        when(userStorage.getUserWithID(any(),any())).thenReturn(new User("123123"));
        when(request.getSession()).thenReturn(session);
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(json)));
        // define return value for method getUniqueId()
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(Constraints.ASSIGNMENT_INFO_DAO)).thenReturn(new AssignmentInfoDAO(ConnectionPool.getInstance()));
        when(session.getAttribute(USER)).thenReturn(new User("105303857051815287047"));
        response_writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(response_writer));
        new CodeDispatcher().doPost(request, response);

        System.out.println(response_writer.toString());
        JSONArray array = new JSONArray(response_writer.toString());
        JSONObject jsonobject = array.getJSONObject(0);
        String id = jsonobject.getString("key");
        String name = jsonobject.getString("value");
        assertEquals(id, "1");
        assertEquals(name, "jondo/lato/user.java");
        // use mock in test....
    }
}