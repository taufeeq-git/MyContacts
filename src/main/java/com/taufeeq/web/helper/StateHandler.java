package com.taufeeq.web.helper;

import com.taufeeq.web.dao.*;

public class StateHandler {
	public static boolean validateState(String state) {
		if (state == null)
			return false;

		String[] parts = state.split("\\.");
		if (parts.length != 2)
			return false;

		String sessionId = parts[0];
		SessionDAO sessionDAO = new SessionDAOImpl();

		return sessionDAO.isValidSession(sessionId);
	}

	public static String getFlowType(String state) {
		if (state == null)
			return null;
		String[] parts = state.split("\\.");
		return parts.length == 2 ? parts[1] : null;
	}

}
