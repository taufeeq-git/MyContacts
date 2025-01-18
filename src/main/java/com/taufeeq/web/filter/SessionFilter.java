package com.taufeeq.web.filter;

import com.taufeeq.web.cache.UserCache;
import com.taufeeq.web.dao.SessionDAO;
import com.taufeeq.web.dao.SessionDAOImpl;
import com.taufeeq.web.helper.SessionValidator;
import com.taufeeq.web.model.Session;
import com.taufeeq.web.cache.SessionCache;
import com.taufeeq.web.logger.CustomLogger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SessionFilter implements Filter {

	private static final CustomLogger logger = CustomLogger.getInstance();

	private SessionDAO sessionDAO = new SessionDAOImpl();
	private static final SessionCache sessionCache = SessionCache.getInstance();
	private static final UserCache userCache = UserCache.getInstance();

	private long sessionTimeoutMillis = 30 * 60 * 1000;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requestURI = httpRequest.getRequestURI();
		String clientIP = httpRequest.getRemoteAddr();
		String httpMethod = httpRequest.getMethod();
		String userAgent = httpRequest.getHeader("User-Agent");
		long startTime = System.currentTimeMillis();

		LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedTime = dateTime.format(formatter);

		if (requestURI.endsWith("index.jsp") || requestURI.endsWith("login.jsp") || requestURI.endsWith("signup.jsp")
				|| requestURI.endsWith("/login") || requestURI.endsWith("/signup")
				|| requestURI.endsWith("/google-auth") || requestURI.endsWith("/oauth/callback")
				|| requestURI.endsWith("/NotifyServerServlet") || requestURI.endsWith("/oauth-profilecallback")
				|| requestURI.endsWith("/oauth-profilecallback")) {
			chain.doFilter(request, response);
			return;
		}

		String sessionId = SessionValidator.getSIDFromCookie(httpRequest);
		int userId = -1;
		Session session = null;

		if (sessionId != null) {
			session = sessionCache.getSession(sessionId);
		}

		if (sessionId != null && session != null) {
			session.setExpirationTime(System.currentTimeMillis() + sessionTimeoutMillis);
			sessionCache.addSession(session);

			userId = session.getUserId();
			request.setAttribute("userId", userId);
			userCache.hitFrequency(userId);
			logger.infoApp("Cache Hit for Session ID: " + sessionId);

		} else {
			if (sessionId == null) {
				httpResponse.sendRedirect("login.jsp");
				logger.warnApp("No session for Client IP: " + clientIP);
				return;
			}	

			userId = sessionDAO.getUserIdWithSessionId(sessionId);
			if (userId > 0) {
				Session newSession = new Session();
				newSession.setSessionId(sessionId);
				newSession.setUserId(userId);
				newSession.setCreatedTime(System.currentTimeMillis());
				newSession.setExpirationTime(System.currentTimeMillis() + sessionTimeoutMillis);
				sessionCache.addSession(newSession);

				request.setAttribute("userId", userId);
				userCache.hitFrequency(userId);
				logger.infoApp("Cache Miss, Validated from DB, Session ID: " + sessionId);
			} else {
				httpResponse.sendRedirect("login.jsp");
				logger.warnApp("Invalid session from DB for Client IP: " + clientIP + ", Session ID: " + sessionId);
				return;
			}
		}

		if (userId != -1) {
			request.setAttribute("userId", userId);
		}

		int statusCode = httpResponse.getStatus();
		logger.infoAccess("Access log - Client IP: " + clientIP + ", Request URI: " + requestURI + ", HTTP Method: "
				+ httpMethod + ", Session ID: " + sessionId + ", User ID: " + userId + ", Time: " + formattedTime
				+ ", User-Agent: " + userAgent + ", Status Code: " + statusCode);

		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		logger.infoApp("Request processing time for URI: " + requestURI + " - " + executionTime + " ms");

		chain.doFilter(request, response);
	}

}