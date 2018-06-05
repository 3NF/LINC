package Servlets;

import HelperClasses.Constraints;
import HelperClasses.User;
import HelperClasses.UserDAo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@WebServlet(name = "GoogleLogin", urlPatterns = {"/GoogleLogin"})
public class GoogleLogin extends HttpServlet {

	private static final String CLIENT_ID = "108555998588-rcq9m8lel3d81vk93othgsg2tolfk9b9.apps.googleusercontent.com";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserDAo userDAo = (UserDAo) request.getServletContext().getAttribute(Constraints.USERDAO_NAME);

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory()).setAudience(Collections.singletonList(CLIENT_ID)).build();

		String token = request.getParameter("id_token");
		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(token);
		} catch (GeneralSecurityException ignore) {}

		String res;

		if (idToken != null) {
			GoogleIdToken.Payload payload = idToken.getPayload();
			String userId = payload.getSubject();
			String email = payload.getEmail();
			boolean emailVerified = payload.getEmailVerified();
			if (!emailVerified) {
				res = "Please verify email";
			} else {
				User user = userDAo.getUserByEmail(email);
				if (user != null) {
					request.getSession().setAttribute("user", user);
					res = "success";
				} else {
					res = "incorrect email";
				}
			}
		} else {
			res = "Invalid ID token";
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new Gson().toJson(res));
	}
}
