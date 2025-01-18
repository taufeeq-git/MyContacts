package com.taufeeq.web.helper;

import java.sql.Timestamp;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.taufeeq.web.dao.SessionDAO;
import com.taufeeq.web.dao.SessionDAOImpl;

public class CookieGenerator {
	public static void createSessionCookie(int userId, HttpServletResponse response) {
		String sessionId = UUID.randomUUID().toString();
		Timestamp createdTime = new Timestamp(System.currentTimeMillis());
		Timestamp expirationTime = new Timestamp(createdTime.getTime() + 30 * 60 * 1000);

		SessionDAO sessionDAO = new SessionDAOImpl();
		sessionDAO.addSession(sessionId, userId, createdTime, expirationTime);

		Cookie sessionCookie = new Cookie("sessionId", sessionId);
		sessionCookie.setMaxAge(60 * 60 * 24);
		sessionCookie.setPath("/MyContacts");
		response.addCookie(sessionCookie);

	}

}
