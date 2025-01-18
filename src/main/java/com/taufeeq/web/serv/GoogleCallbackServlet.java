package com.taufeeq.web.serv;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.taufeeq.web.helper.CookieGenerator;
import com.taufeeq.web.helper.OauthHelper;
import com.taufeeq.web.config.GoogleAuthConfig;
import com.taufeeq.web.dao.*;
import com.taufeeq.web.model.User;

public class GoogleCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String code = request.getParameter("code");

		if (code == null) {
			response.sendRedirect("login.jsp?error=no_code");
			return;
		}

		try {
			String redirectUri = GoogleAuthConfig.PROFILE_REDIRECT_URI;
			JSONObject tokens = OauthHelper.exchangeCodeForTokens(code, redirectUri);

			String accessToken = tokens.getString("access_token");

			if (accessToken == null) {
				response.sendRedirect("dashboard?error=selectedAccountIsAlreadyRegisteredInMyContacts");
				return;
			}

			JSONObject userInfo = OauthHelper.getUserInfoFromGoogle(accessToken);

			String email = userInfo.getJSONArray("emailAddresses").getJSONObject(0).getString("value");
			String uniqueId = userInfo.getString("resourceName").split("/")[1];

			UserDAO userDAO = new UserDAOImpl();

			int userId = userDAO.getUserIdByGoogleId(uniqueId);

			if (userId == 0) {

				userId = userDAO.getUserIdByEmail(email);

				if (userId != 0) {
					userDAO.setUniqueId(userId, uniqueId, "google");
				} else {
					String gender = userInfo.optJSONArray("genders") != null
							&& userInfo.getJSONArray("genders").length() > 0
									? userInfo.getJSONArray("genders").getJSONObject(0).optString("value", null)
									: null;

					String address = userInfo.optJSONArray("addresses") != null
							&& userInfo.getJSONArray("addresses").length() > 0
									? userInfo.getJSONArray("addresses").getJSONObject(0).optString("formattedValue",
											null)
									: null;

					String name = userInfo.optJSONArray("names") != null && userInfo.getJSONArray("names").length() > 0
							? userInfo.getJSONArray("names").getJSONObject(0).optString("displayName", null)
							: null;

					User newUser = new User(0, name, "google_pass", gender, address, "google");
					userId = userDAO.addUser(newUser);
					userDAO.setUniqueId(userId, uniqueId, "google");
					userDAO.addUserEmail(userId, email);
				}
			}

			CookieGenerator.createSessionCookie(userId, response);
			response.sendRedirect("/MyContacts/dashboard");

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("login.jsp?error=auth_failed");
		}
	}
}