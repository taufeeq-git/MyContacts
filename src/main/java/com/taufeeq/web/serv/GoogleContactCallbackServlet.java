package com.taufeeq.web.serv;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.taufeeq.web.config.GoogleAuthConfig;
import com.taufeeq.web.model.*;
import com.taufeeq.web.dao.TokenDAO;
import com.taufeeq.web.dao.TokenDAOImpl;
import com.taufeeq.web.helper.OauthHelper;
import com.taufeeq.web.helper.StateHandler;

public class GoogleContactCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		int userId = (int) request.getAttribute("userId");
		String code = request.getParameter("code");
		String state = request.getParameter("state");

		if (code == null) {
			response.sendRedirect("login.jsp?error=no_code");
			return;
		}
		if (!StateHandler.validateState(state)) {
			response.sendRedirect("login.jsp?error=invalid_state");
			return;
		}

		try {
			JSONObject tokens = OauthHelper.exchangeCodeForTokens(code, GoogleAuthConfig.CONTACT_REDIRECT_URI);
			String accessToken = tokens.get("access_token").toString();
			String idToken = tokens.get("id_token").toString();
			String email = OauthHelper.emailFromIdToken(idToken);

			TokenDAO tokenDAO = new TokenDAOImpl();
			Boolean isAlreadySynced = tokenDAO.doesEmailExist(userId, email);

			List<Contact> googleContacts;

			int tokenId = -1;
			if (!isAlreadySynced) {
				String refreshToken = tokens.get("refresh_token").toString();
				String uniqueId = OauthHelper.subFromIdToken(idToken);
				tokenId = tokenDAO.saveTokens(userId, uniqueId, email, accessToken, refreshToken, "Google");
			} else {
				tokenId = tokenDAO.getTokenId(userId, email);
				tokenDAO.updateAccessToken(tokenId, accessToken);
			}

			googleContacts = OauthHelper.fetchGoogleContacts(accessToken);
			tokenDAO.updateLastSync(tokenId);

			OauthHelper.syncContacts(userId, googleContacts);

			response.sendRedirect("dashboard");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
