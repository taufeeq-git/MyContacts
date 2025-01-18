package com.taufeeq.web.dao;

import java.sql.Timestamp;
import java.util.List;

import com.taufeeq.web.model.Session;
import com.taufeeq.web.model.User;

public interface SessionDAO {
	String addSession(String sessionId, int userId, Timestamp createdTime, Timestamp expirationTime);

	void updateSessionExpiration(String sessionId, Timestamp newExpirationTime);

	int deleteExpiredSessions(Timestamp currentTime);

	void deleteAllSessions();

	boolean isValidSession(String sessionId);

	User getUserBySessionId(String sessionId);

	void deleteSession(String sessionId);

	int getUserIdBySessionId(String sessionId);

	void deleteSessionById(String sessionId);

	Integer getUserIdFromSession(String sessionId);

	void batchUpdateSessionExpirations(List<Session> sessionsToUpdate);

	int getUserIdWithSessionId(String sessionId);
}
