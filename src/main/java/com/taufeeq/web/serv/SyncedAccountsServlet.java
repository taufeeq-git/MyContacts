package com.taufeeq.web.serv;

import java.io.IOException;
import java.util.List;

import com.taufeeq.web.model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.taufeeq.web.dao.*;
import com.taufeeq.web.helper.OauthHelper;

public class SyncedAccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userId = (int) request.getAttribute("userId");

		TokenDAO tokenDAO = new TokenDAOImpl();
		List<Token> accounts = tokenDAO.showSyncedAccounts(userId);
		if (!accounts.isEmpty())

			request.setAttribute("accounts", accounts);

		RequestDispatcher dispatcher = request.getRequestDispatcher("contactSync.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		TokenDAO tokenDAO = new TokenDAOImpl();

		try {

			int userId = (int) request.getAttribute("userId");
			String action = request.getParameter("action");
			String tokenIdStr = request.getParameter("accountId");
			int tokenId = Integer.parseInt(tokenIdStr);

			if ("update".equals(action)) {

				String syncIntervalStr = request.getParameter("syncInterval");
				int syncInterval = Integer.parseInt(syncIntervalStr);

				int sync = tokenDAO.updateSyncInterval(tokenId, syncInterval);

				if (sync == 1) {
					response.sendRedirect("synced-accounts");
				} else {
					response.sendRedirect("contactSync.jsp?error_while_updating");
				}
			} else if ("syncNow".equals(action)) {

				String accessToken = OauthHelper.getValidAccessToken(tokenId);

				List<Contact> googleContacts = OauthHelper.fetchGoogleContacts(accessToken);
				tokenDAO.updateLastSync(tokenId);
				OauthHelper.syncContacts(userId, googleContacts);
				response.sendRedirect("dashboard");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendRedirect("contactSync.jsp?error=true");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
