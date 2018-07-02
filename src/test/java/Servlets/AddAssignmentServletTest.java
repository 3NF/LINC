package Servlets;

import Database.AssignmentInfoDAO;
import Database.GAPIManager;
import Models.User;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        when(gapiManager.isInRoom("-", "-")).thenReturn(true);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(USER_ID_TOKEN)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT)).thenReturn("-");
        when(req.getParameter(ROOM_ID)).thenReturn("-");
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
        when(gapiManager.isInRoom("-", "-")).thenReturn(false);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(USER_ID_TOKEN)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT)).thenReturn("-");
        when(req.getParameter(ROOM_ID)).thenReturn("-");
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
    public void doPostAccessWrongFiles() throws IOException, ServletException, JSONException {

        AddAssignmentServlet servlet = new AddAssignmentServlet();

        AssignmentInfoDAO dao = mock(AssignmentInfoDAO.class);
        GAPIManager gapiManager = mock(GAPIManager.class);
        List<String> st = new ArrayList<>();
        st.addAll(Arrays.asList(new String[] {"sample.h", "sample.cpp", "main.c"}));
        when(dao.getAssignmentFilesNames("-", "-")).thenReturn(st);
        when(gapiManager.getUser("-")).thenReturn(new User("-", "-", "-"));
        when(gapiManager.isInRoom("-", "-")).thenReturn(true);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(USER_ID_TOKEN)).thenReturn("-");
        when(req.getParameter(ASSIGNMENT)).thenReturn("");
        when(req.getParameter(ROOM_ID)).thenReturn("-");
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








}