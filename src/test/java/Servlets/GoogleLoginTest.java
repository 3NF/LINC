package Servlets;

import HelperClasses.User;
import HelperClasses.UserDAo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GoogleLoginTest {
    @Test
    public void testServlet() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        UserDAo userDAo = mock(UserDAo.class);
        GoogleIdTokenVerifier verifier = mock(GoogleIdTokenVerifier.class);

        //
        when(request.getParameter("id_token")).thenReturn("gbagh16@freeuni.edu.ge");
        when(userDAo.getUserByEmail(any(String.class))).thenReturn(new User("gbagh16@freeuni.edu.ge", "giorgi", "bagdu", User.Role.admin));

        //for stringWriter
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        final ServletContext servletContext = Mockito.mock(ServletContext.class);
        when(request.getServletContext()).thenReturn(servletContext);
        GoogleLogin someServlet = new GoogleLogin();
        someServlet.doPost(request,response);
        writer.flush();
        System.out.println(stringWriter.toString() + "deda");
        assertTrue(stringWriter.toString().contains("Please verify email"));
    }

}