package com.taufeeq.web.serv;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taufeeq.web.config.GoogleAuthConfig;
import com.taufeeq.web.helper.SessionValidator;

public class GoogleContactAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String sessionId = SessionValidator.getSIDFromCookie(request);
		String state = String.format("%s.%s", sessionId, "contact_sync");

		String authUrl = GoogleAuthConfig.AUTH_URL + "?" + "client_id=" + GoogleAuthConfig.CLIENT_ID + "&redirect_uri="
				+ URLEncoder.encode(GoogleAuthConfig.CONTACT_REDIRECT_URI, "UTF-8") + "&response_type=code" + "&scope="
				+ URLEncoder.encode(GoogleAuthConfig.CONTACTS_SCOPE, "UTF-8") + "&access_type=offline&prompt=consent" + "&state="
				+ URLEncoder.encode(state, "UTF-8");

		response.sendRedirect(authUrl);

	}
}
