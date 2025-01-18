package com.taufeeq.web.serv;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taufeeq.web.cache.ServerCache;
import com.taufeeq.web.cache.SessionCache;
import com.taufeeq.web.cache.UserCache;

public class NotifyServerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final SessionCache sessionCache = SessionCache.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		response.getWriter().println("Server cache :" + ServerCache.getInstance().getServerList());
		response.getWriter().println("User cache :" + UserCache.getInstance().getUserCache());

		if (action.equals("sessionLogout")) {
			String sessionId = request.getParameter("sessionId");
			sessionCache.removeSession(sessionId);

		} else if (action.equals("serverJoin")) {
			ServerCache.getInstance().reloadCacheFromDB();

		} else if (action.equals("userUpdate")) {
			int userId = Integer.parseInt(request.getParameter("userId"));
			UserCache.getInstance().removeUserFromCache(userId);
		}

	}
}
