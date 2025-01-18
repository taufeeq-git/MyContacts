package com.taufeeq.web.serv;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taufeeq.web.cache.UserCache;
import com.taufeeq.web.dao.*;
import com.taufeeq.web.helper.UtilityClass;
import com.taufeeq.web.model.*;

public class EditProfileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userId = (int) request.getAttribute("userId");
		User user = UserCache.getInstance().getUser(userId);

		request.setAttribute("user", user);
		request.getRequestDispatcher("editProfile.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		UserDAOImpl userDAO = new UserDAOImpl();
		int userId = Integer.parseInt(request.getParameter("userId"));

		if ("editProfile".equals(action)) {
			String newUsername = request.getParameter("username");
			String newGender = request.getParameter("gender");
			String newBirthday = request.getParameter("birthday");
			String newLocation = request.getParameter("location");
			userDAO.updateUserProfile(userId, newUsername, newGender, newBirthday, newLocation);
			
		} else if ("addEmail".equals(action)) {
			String email = request.getParameter("email");
			userDAO.addUserEmail(userId, email);

		} else if ("addPhoneNumber".equals(action)) {
			String phoneNumber = request.getParameter("number");
			userDAO.addUserPhoneNumber(userId, phoneNumber);

		} else if ("deleteEmail".equals(action)) {
			String email = request.getParameter("email");
			userDAO.deleteEmail(userId, email);

		} else if ("deletePhoneNumber".equals(action)) {
			String phoneNumber = request.getParameter("phoneNumber");
			userDAO.deletePhoneNumber(userId, phoneNumber);
		}
		
		User updatedUser = userDAO.getUserById(userId);
		UserCache.getInstance().addUserToCache(updatedUser);
		UtilityClass.notifyOtherServersOfUpdate(userId);
		response.sendRedirect("profile.jsp");
	}
}
