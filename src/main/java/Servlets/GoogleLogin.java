package Servlets;

import Data.Constraints;
import Database.UserDAO;
import Models.User;
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

import static Data.Constraints.CLIENT_ID;

@WebServlet(name = "GoogleLogin", urlPatterns = {"/GoogleLogin"})
public class GoogleLogin extends HttpServlet {


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute(Constraints.USERDAO_NAME);

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory()).setAudience(Collections.singletonList(CLIENT_ID)).build();

		String token = request.getParameter("id_token");
		System.out.println(token);
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
				User user = userDAO.getUserByEmail(email);
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
