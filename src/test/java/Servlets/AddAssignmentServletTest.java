package Servlets;

import Database.AssignmentInfoDAO;
import Database.GAPIManager;
import Models.AddAssignmentResponse;
import Models.File;
import Models.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.junit.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static Data.Constraints.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddAssignmentServletTest {

    @Test
    public void doPostInvalidUser() throws IOException, ServletException, JSONException {

        AddAssignmentServlet servlet = new AddAssignmentServlet();

        AssignmentInfoDAO dao = mock(AssignmentInfoDAO.class);
        GAPIManager gapiManager = mock(GAPIManager.class);
        List<String> st = new ArrayList<>();
        st.addAll(Arrays.asList(new String[] {"sample.h", "sample.cpp", "main.c"}));
        when(dao.getAssignmentFilesNames("-", "-")).thenReturn(st);
        when(gapiManager.getUser("-")).thenReturn(null);
        when(gapiManager.isInRoom(Mockito.any(), Mockito.any())).thenReturn(true);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(USER_ID_TOKEN)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT)).thenReturn("-");
        when(req.getParameter(COURSE_ID)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT_ID)).thenReturn("-");

        ServletContext context = mock(ServletContext.class);
        when(context.getAttribute(ASSIGNMENT_INFO_DAO)).thenReturn(dao);
        when(context.getAttribute(GAPI_MANAGER)).thenReturn(gapiManager);
        when(req.getServletContext()).thenReturn(context);


        HttpServletResponse res = mock(HttpServletResponse.class);
        StringWriter writer = new StringWriter();
        when(res.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPost(req, res);

        String actual = "{\"error-message\":\"InvalidUser\"}";
        JSONAssert.assertEquals(writer.toString(), actual, JSONCompareMode.LENIENT);
    }

    @Test
    public void doPostAccessForbidden() throws IOException, ServletException, JSONException {

        AddAssignmentServlet servlet = new AddAssignmentServlet();

        AssignmentInfoDAO dao = mock(AssignmentInfoDAO.class);
        GAPIManager gapiManager = mock(GAPIManager.class);
        List<String> st = new ArrayList<>();
        st.addAll(Arrays.asList(new String[] {"sample.h", "sample.cpp", "main.c"}));
        when(dao.getAssignmentFilesNames("-", "-")).thenReturn(st);
        when(gapiManager.getUser("-")).thenReturn(new User("-", "-", "-"));
        when(gapiManager.isInRoom(Mockito.any(), Mockito.any())).thenReturn(false);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(USER_ID_TOKEN)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT)).thenReturn("-");
        when(req.getParameter(COURSE_ID)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT_ID)).thenReturn("-");

        ServletContext context = mock(ServletContext.class);
        when(context.getAttribute(ASSIGNMENT_INFO_DAO)).thenReturn(dao);
        when(context.getAttribute(GAPI_MANAGER)).thenReturn(gapiManager);
        when(req.getServletContext()).thenReturn(context);


        HttpServletResponse res = mock(HttpServletResponse.class);
        StringWriter writer = new StringWriter();
        when(res.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPost(req, res);

        String actual = "{\"error-message\":\"AssignmentForbidden\"}";
        JSONAssert.assertEquals(writer.toString(), actual, JSONCompareMode.LENIENT);
    }

    @Test
    public void doPostAccessSuccess() throws IOException, ServletException, JSONException {

        AddAssignmentServlet servlet = new AddAssignmentServlet();

        AssignmentInfoDAO dao = mock(AssignmentInfoDAO.class);
        GAPIManager gapiManager = mock(GAPIManager.class);
        List<String> st = new ArrayList<>();
        st.addAll(Arrays.asList(new String[] {"sample.h", "sample.cpp", "main.c"}));
        when(dao.getAssignmentFilesNames("-", "-")).thenReturn(st);
        when(gapiManager.getUser("-")).thenReturn(new User("-", "-", "-"));
        when(gapiManager.isInRoom(Mockito.any(), Mockito.any())).thenReturn(true);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(USER_ID_TOKEN)).thenReturn("-");
        JsonParser parser = new JsonParser();
        JsonArray jaB = new Gson().toJsonTree(Arrays.asList(new JsonObject[] {
                parser.parse(new File("sample.h", "-").toString()).getAsJsonObject(), parser.parse(new File("sample.cpp", "-").toString()).getAsJsonObject(),
                parser.parse(new File("main.c", "-").toString()).getAsJsonObject()})).getAsJsonArray();

        when(req.getParameter(ASSIGNMENT)).thenReturn(String.valueOf(jaB));
        when(req.getParameter(COURSE_ID)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT)).thenReturn("");
        when(req.getParameter(COURSE_ID)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT_ID)).thenReturn("-");

        ServletContext context = mock(ServletContext.class);
        when(context.getAttribute(ASSIGNMENT_INFO_DAO)).thenReturn(dao);
        when(context.getAttribute(GAPI_MANAGER)).thenReturn(gapiManager);
        when(req.getServletContext()).thenReturn(context);

        HttpServletResponse res = mock(HttpServletResponse.class);
        StringWriter writer = new StringWriter();
        when(res.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPost(req, res);

        String actual = "{\"error-message\":\"Success\"}";
        JSONAssert.assertEquals(writer.toString(), actual, JSONCompareMode.LENIENT);
    }

    @Test
    public void doPostWrongFiles() throws IOException, ServletException, JSONException {

        AddAssignmentServlet servlet = new AddAssignmentServlet();

        AssignmentInfoDAO dao = mock(AssignmentInfoDAO.class);
        GAPIManager gapiManager = mock(GAPIManager.class);
        List<String> st = new ArrayList<>();
        st.addAll(Arrays.asList(new String[] {"sample.h", "sample.cpp", "main.c"}));
        when(dao.getAssignmentFilesNames("-", "-")).thenReturn(st);
        when(gapiManager.getUser("-")).thenReturn(new User("-", "-", "-"));
        when(gapiManager.isInRoom(Mockito.any(), Mockito.any())).thenReturn(true);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(USER_ID_TOKEN)).thenReturn("-");
        JsonParser parser = new JsonParser();
        JsonArray jaB = new Gson().toJsonTree(Arrays.asList(new JsonObject[] {
                parser.parse(new File("sample.j", "-").toString()).getAsJsonObject(), parser.parse(new File("sample.cpp", "-").toString()).getAsJsonObject(),
                parser.parse(new File("main.c", "-").toString()).getAsJsonObject()})).getAsJsonArray();

        when(req.getParameter(ASSIGNMENT)).thenReturn(String.valueOf(jaB));
        when(req.getParameter(COURSE_ID)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT_ID)).thenReturn("-");

        ServletContext context = mock(ServletContext.class);
        when(context.getAttribute(ASSIGNMENT_INFO_DAO)).thenReturn(dao);
        when(context.getAttribute(GAPI_MANAGER)).thenReturn(gapiManager);
        when(req.getServletContext()).thenReturn(context);

        HttpServletResponse res = mock(HttpServletResponse.class);
        StringWriter writer = new StringWriter();
        when(res.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doPost(req, res);

        AddAssignmentResponse rs = new AddAssignmentResponse();
        rs.setMessage(AddAssignmentResponse.ErrorMessage.WrongFiles);
        rs.setExtraFiles(Arrays.asList(new String[] {"sample.j"}));
        rs.setMissingFiles(Arrays.asList(new String[] {"sample.h"}));
        String actual = rs.toString();
        JSONAssert.assertEquals(writer.toString(), actual, JSONCompareMode.LENIENT);
    }










}