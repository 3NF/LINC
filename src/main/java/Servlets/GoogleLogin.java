package Servlets;

import Data.Constraints;
import Database.GAPIManager;
import Models.User;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.api.services.classroom.model.Course;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static Data.Constraints.CLIENT_ID;

@WebServlet(name = "GoogleLogin", urlPatterns = {"/GoogleLogin"})
public class GoogleLogin extends HttpServlet {

	private static final String AUTH_CODE_NAME = "auth_code";


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        response.sendRedirect("/user/chooseRoom.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
        String authCode = request.getParameter(AUTH_CODE_NAME);
        User user = GAPIManager.getUser(authCode);
        request.getSession().setAttribute("user", user);
        response.sendRedirect("/user/chooseRoom.jsp");
	}
}
