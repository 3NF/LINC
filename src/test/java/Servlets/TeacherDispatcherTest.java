package Servlets;

import Database.CodeFilesDAO;
import Database.SuggestionDAO;
import Database.ValidateDAO;
import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
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

import static Data.Constraints.CODE_FILES_DAO;
import static Data.Constraints.SUGGESTION_DAO;
import static Data.Constraints.VALIDATE_DAO;
import static Database.Config.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeacherDispatcherTest {
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
        String json = "{\"courseID\":15887333289,\"assignmentID\":15917000927}";
        ValidateDAO valDAO = mock(ValidateDAO.class);
        when(request.getSession()).thenReturn(session);
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(json)));
        // define return value for method getUniqueId()
        when(request.getServletContext()).thenReturn(servletContext);
        when(session.getAttribute("user")).thenReturn(new User("109900197229728431706","","","104176062122048371294","ya29.Glv1BfLAE-SnHa3sDodMnBbQrTFCxtPGF4Kcfe26hEw-8G1XCRRomkAFvS5OTnBu0-iaChQIBjhJVCmyM6diHLf7vcfbKVLxrWti4x5U4m4_3Hh4tWUvsba1gzcm","ya29.Glv1Be1u8bjuL7O9MtmlPAuLBtntvSUIQJi9gxoGeUSDqOZGIPPjIyme_YbHi3OHsUWdmbXUU7iqVcDx-UtnjQKwPAgcJ1LPW_TxPLN1S5U4h2gAc5nGZBrxCmXl","1/tRMw7b25Tm6JMKvFVabthHUBaUrvaoBI5ZCEAXgv11w"));
        when(servletContext.getAttribute(CODE_FILES_DAO)).thenReturn(new CodeFilesDAO(source));
        new TeacherDispatcher().doPost(request,response);
    }

}