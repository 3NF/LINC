package Servlets;


import Data.Constraints;
import Database.UserDAO;
import Models.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GoogleLoginTest {

	private String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjE5MjMzOTczODFkOTU3NGJiODczMjAyYTkwYzMyYjdjZWVhZWQwMjcifQ.eyJhenAiOiIxMDg1NTU5OTg1ODgtcmNxOW04bGVsM2Q4MXZrOTNvdGhnc2cydG9sZms5YjkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIxMDg1NTU5OTg1ODgtcmNxOW04bGVsM2Q4MXZrOTNvdGhnc2cydG9sZms5YjkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDUzMDM4NTcwNTE4MTUyODcwNDciLCJoZCI6ImZyZWV1bmkuZWR1LmdlIiwiZW1haWwiOiJnYmFnaDE2QGZyZWV1bmkuZWR1LmdlIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJvamRnYkh3NXlYcm5nZTlVZzdDemRRIiwiZXhwIjoxNTI4MjI0ODk1LCJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwianRpIjoiMGY0ODlhZGUxNWZlM2FkZWNmMzZmMDMyN2YzMDg3NGI3ZDFhMGRjMyIsImlhdCI6MTUyODIyMTI5NSwibmFtZSI6Imdpb3JnaSBiYWdoZGF2YWR6ZSIsInBpY3R1cmUiOiJodHRwczovL2xoNC5nb29nbGV1c2VyY29udGVudC5jb20vLWJsSXQyUmljWGRZL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUFBL0FCNnFvcTI3SDdFT0twOVFaaWlhamJWZEluMDJHek9tT1Evczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6Imdpb3JnaSIsImZhbWlseV9uYW1lIjoiYmFnaGRhdmFkemUiLCJsb2NhbGUiOiJlbiJ9.OUofmAOC0nmzFdO-HlCb7JYO0CUkS1d2GzS5aeTRCpzvF5xa1Kjvw6xwIC7Y0VaZ0zjOQwwGddIW_RxrVA6tdiwUSpRjaLHbjnC0YMOBm1rNK4t_6pc2gHe9ilwFAO-03-hJWaEXj5kpZTsgRQT-0GM1HePPCg0LTOyBJ-dyp6YIInN357rN8pfysqy0vbCdJW2NX4oFc4E19KSTGlaLlo-234uz0_7KDQ9YDTsHCxmNtWBSgmxFMT2LSH8_QLGjcq-MnHJiAU4sTq8rDobXcHLQC8Txw0U1dsrK5grzEIF6RvvBh2WHxaoRpMAnPkjfRynAbAvJpFD68IR9YznpeA";
	private String failToken = "asd";
	private HttpServletRequest request;
	private HttpServletResponse response;
	private UserDAO userDAo;
	private ServletContext servletContext;
	private GoogleIdTokenVerifier verifier;
	private HttpSession session;

	@Before
	public void build() {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		userDAo = mock(UserDAO.class);
		verifier = mock(GoogleIdTokenVerifier.class);
		servletContext = Mockito.mock(ServletContext.class);
		session = Mockito.mock(HttpSession.class);
	}

	@Test
	public void testServlet() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		UserDAO userDAO = mock(UserDAO.class);
		GoogleIdTokenVerifier verifier = mock(GoogleIdTokenVerifier.class);

		when(request.getParameter("id_token")).thenReturn(accessToken);
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		when(request.getServletContext()).thenReturn(servletContext);
		when(request.getSession()).thenReturn(session);
		when(servletContext.getAttribute(Constraints.USERDAO_NAME)).thenReturn(userDAo);
		when(userDAo.getUserByEmail(any(String.class))).thenReturn(new User("gbagh16@freeuni.edu.ge", "giorgi", "bagdu", "1", "er", "er", "er"));
		//
		when(request.getParameter("id_token")).thenReturn("gbagh16@freeuni.edu.ge");
		when(userDAO.getUserByEmail(any(String.class))).thenReturn(new User("gbagh16@freeuni.edu.ge", "giorgi", "bagdu", "1", "er", "er", "er"));

		GoogleLogin someServlet = new GoogleLogin();
		someServlet.doPost(request, response);
		writer.flush();
		System.out.println(stringWriter.toString() + " deda");
		assertTrue(stringWriter.toString().contains("success"));
	}

	@Test
	public void testServlet_2() throws IOException, ServletException {
		when(request.getParameter("id_token")).thenReturn(failToken);
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);
		when(request.getServletContext()).thenReturn(servletContext);
		when(request.getSession()).thenReturn(session);
		when(servletContext.getAttribute(Constraints.USERDAO_NAME)).thenReturn(userDAo);
		when(userDAo.getUserByEmail(any(String.class))).thenReturn(new User("gbagh16@freeuni.edu.ge", "giorgi", "bagdu", "1", "er", "er", "er"));

		GoogleLogin someServlet = new GoogleLogin();
		someServlet.doPost(request, response);
		writer.flush();
		System.out.println(stringWriter.toString() + " deda");
		assertTrue(stringWriter.toString().contains("Invalid ID token"));
	}
}