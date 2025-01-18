package com.taufeeq.web.serv;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taufeeq.web.config.GoogleAuthConfig;
import com.taufeeq.web.helper.SessionValidator;

public class GoogleAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sessionId = SessionValidator.getSIDFromCookie(request);
		String state = String.format("%s.%s", sessionId, "profile_sync");
		String authUrl = GoogleAuthConfig.AUTH_URL + "?" + "client_id=" + GoogleAuthConfig.CLIENT_ID + "&redirect_uri="
				+ URLEncoder.encode(GoogleAuthConfig.PROFILE_REDIRECT_URI, "UTF-8") + "&response_type=code" + "&scope="
				+ URLEncoder.encode(GoogleAuthConfig.SCOPE, "UTF-8") + "&access_type=offline" + "&state="
				+ URLEncoder.encode(state, "UTF-8");
		response.sendRedirect(authUrl);

	}
}