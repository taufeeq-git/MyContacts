package com.taufeeq.web.serv;

import com.taufeeq.web.cache.UserCache;
import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.dao.UserDAOImpl;
import com.taufeeq.web.helper.CookieGenerator;
import com.taufeeq.web.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static UserCache userCache = UserCache.getInstance();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserDAO userDAO = new UserDAOImpl();
		int userId = userDAO.verifyUser(email, password);

		if (userId > 0) {
			CookieGenerator.createSessionCookie(userId, response);
			User fullyPopulatedUser = userDAO.getUserById(userId);
			userCache.addUserToCache(fullyPopulatedUser);
			response.sendRedirect("dashboard");
		} else {
			response.sendRedirect("login.jsp?error=invalid");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("login.jsp");
	}
}
