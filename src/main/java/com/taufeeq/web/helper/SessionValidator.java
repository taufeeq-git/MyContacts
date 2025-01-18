package com.taufeeq.web.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class SessionValidator {
	public static String getSIDFromCookie(HttpServletRequest httpRequest) {
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies == null) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if ("sessionId".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}
}
