package Servlets;

import Data.Constraints;
import Database.UserDAO;
import Models.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.webtoken.JsonWebToken;
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

		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory()).setAudience(Collections.singletonList(CLIENT_ID)).build();

		String token = request.getParameter("idToken");
		System.out.println(token);
		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(token);
		} catch (Exception e) {
			System.err.println("error in using verify");
		}
		String res;
		if (idToken != null) {
			GoogleIdToken.Payload payload = idToken.getPayload();
			String userId = payload.getSubject();
			String email = payload.getEmail();
			String pictureUrl = (String) payload.get("picture");
			String familyName = (String) payload.get("family_name");
			String givenName = (String) payload.get("given_name");
			User user = new User(email, givenName, familyName, userId, pictureUrl, token);
			request.getSession().setAttribute("user", user);
			res = "success";
		}
	}
}
