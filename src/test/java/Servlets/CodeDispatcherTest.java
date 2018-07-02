package Servlets;

import Database.CodeFilesDAO;
import Database.ConnectionPool;
import Models.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import javax.json.Json;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;

import static Data.Constraints.USER;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CodeDispatcherTest {
    private StringWriter response_writer;
    @Test
    public void test1() throws ServletException, IOException {
        //  create mock
        HttpServletRequest request  = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletContext servletContext = mock(ServletContext.class);
        HttpSession session = mock(HttpSession.class);

        String json = "{\"codeID\":1, \"amount\":222}";
        when(request.getSession()).thenReturn(session);
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(json)));
        // define return value for method getUniqueId()
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("CodeFilesDAO")).thenReturn(new CodeFilesDAO(ConnectionPool.getInstance()));
        when(session.getAttribute(USER)).thenReturn(new User("1"));
        response_writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(response_writer));
        new CodeDispatcher().doPost(request,response);

        System.out.println(response_writer.toString());
        JsonParser parse = new JsonParser();
        JsonObject object = (JsonObject) parse.parse(response_writer.toString());

        assertEquals(object.get("fileName").getAsString(),"temp_code1.cpp");
        assertEquals(object.get("lang").getAsString(),"cpp");
        assertEquals(object.get("fileId").getAsString(),"1");
        // use mock in test....
    }

}