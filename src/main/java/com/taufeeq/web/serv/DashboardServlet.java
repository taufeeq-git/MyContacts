package com.taufeeq.web.serv;

import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.cache.SessionCache;
import com.taufeeq.web.cache.UserCache;
import com.taufeeq.web.dao.GroupDAO;
import com.taufeeq.web.dao.UserDAOImpl;
import com.taufeeq.web.helper.UtilityClass;
import com.taufeeq.web.dao.GroupDAOImpl;
import com.taufeeq.web.model.Group;
import com.taufeeq.web.model.User;
import com.taufeeq.web.dao.SessionDAO;
import com.taufeeq.web.dao.SessionDAOImpl;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	SessionCache sessionCache = SessionCache.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int userId = (int) request.getAttribute("userId");

		User user = UserCache.getInstance().getUser(userId);

		if (user != null) {
			GroupDAO groupDAO = new GroupDAOImpl();
			List<Group> groupList = groupDAO.getUserGroupsWithIds(userId);

			request.setAttribute("user", user);
			request.setAttribute("groupList", groupList);
			request.getRequestDispatcher("dashboard.jsp").forward(request, response);
		} else {
			response.sendRedirect("login.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		SessionDAO sessionDAO = new SessionDAOImpl();

		String sessionId = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("sessionId".equals(cookie.getName())) {
					sessionId = cookie.getValue();
					break;
				}
			}
		}
		int userId = (int) request.getAttribute("userId");

		if ("logout".equals(action)) {
			if (sessionId != null) {
				sessionDAO.deleteSessionById(sessionId);
				sessionCache.removeSession(sessionId);
				UtilityClass.notifyOtherServersOfLogout(sessionId);
				UserCache.getInstance().removeUserFromCache(userId);

			}
			response.sendRedirect("login.jsp");
			return;
		} else if ("setFormat".equals(action)) {
			String selectedFormat = request.getParameter("dateFormat");
			if (selectedFormat != null) {
				UserDAO userDAO = new UserDAOImpl();
				userDAO.updateFormat(userId, selectedFormat);
				User updatedUser = userDAO.getUserById(userId);
				UserCache.getInstance().addUserToCache(updatedUser);
				UtilityClass.notifyOtherServersOfUpdate(userId);
			}
			response.sendRedirect("dashboard");
			return;
		} else if ("addEmail".equals(action)) {
			String email = request.getParameter("email");
			UserDAO userDAO = new UserDAOImpl();
			if (!userDAO.doesEmailExist(userId, email)) {
				userDAO.addUserEmail(userId, email);
				User updatedUser = userDAO.getUserById(userId);
				UserCache.getInstance().addUserToCache(updatedUser);
				UtilityClass.notifyOtherServersOfUpdate(userId);
				response.sendRedirect("profile.jsp");
				return;
			}
			response.sendRedirect("dashboard?EmailAlreayExists");
		} else if ("addPhoneNumber".equals(action)) {
			String phone = request.getParameter("number");
			UserDAO userDAO = new UserDAOImpl();
			if (!userDAO.doesNumberExist(userId, phone)) {
				userDAO.addUserPhoneNumber(userId, phone);
				User updatedUser = userDAO.getUserById(userId);
				UserCache.getInstance().addUserToCache(updatedUser);
				UtilityClass.notifyOtherServersOfUpdate(userId);
				response.sendRedirect("profile.jsp");
				return;
			}
	
			response.sendRedirect("dashboard?NumberAlreayExists");
		}
	}

}
