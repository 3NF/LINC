package Servlets;

import Database.AssignmentInfoDAO;
import Database.CodeFilesDAO;
import Models.User;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import static Data.Constraints.*;
import static Database.Config.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeacherDispatcherTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private HttpSession session;
    private MysqlDataSource source;

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
        source.getConnection();
    }

    @Test
    public void test() throws IOException {
        String json = "{\"courseID\":15887333289,\"assignmentID\":15917000927}";
        when(request.getSession()).thenReturn(session);
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(json)));
        // define return value for method getUniqueId()
        when(request.getServletContext()).thenReturn(servletContext);
        when(session.getAttribute("user")).thenReturn(new User("109900197229728431706","","","104176062122048371294","ya29.Glv1BfLAE-SnHa3sDodMnBbQrTFCxtPGF4Kcfe26hEw-8G1XCRRomkAFvS5OTnBu0-iaChQIBjhJVCmyM6diHLf7vcfbKVLxrWti4x5U4m4_3Hh4tWUvsba1gzcm","ya29.Glv1Be1u8bjuL7O9MtmlPAuLBtntvSUIQJi9gxoGeUSDqOZGIPPjIyme_YbHi3OHsUWdmbXUU7iqVcDx-UtnjQKwPAgcJ1LPW_TxPLN1S5U4h2gAc5nGZBrxCmXl","1/tRMw7b25Tm6JMKvFVabthHUBaUrvaoBI5ZCEAXgv11w"));
        when(servletContext.getAttribute(CODE_FILES_DAO)).thenReturn(new CodeFilesDAO(source));
        when(servletContext.getAttribute(ASSIGNMENT_INFO_DAO)).thenReturn(new AssignmentInfoDAO(source));
        new TeacherDispatcher().doPost(request,response);
    }

}