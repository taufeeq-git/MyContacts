package com.taufeeq.web.serv;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taufeeq.web.dao.*;
import com.taufeeq.web.model.*;

/**
 * Servlet implementation class EditProfileServlet
 */
@WebServlet("/editProfile")
public class EditProfileServlet extends HttpServlet {
  

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (int) request.getAttribute("userId");

        UserDAOImpl userDAO = new UserDAOImpl();
        User user = userDAO.getUserById(userId);

        request.setAttribute("user", user);
        request.getRequestDispatcher("editProfile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String newUsername = request.getParameter("username");
        String newGender = request.getParameter("gender");
        String newBirthday = request.getParameter("birthday");
        String newLocation = request.getParameter("location");

        // Update user profile
        UserDAOImpl userDAO = new UserDAOImpl();
        userDAO.updateUserProfile(userId, newUsername, newGender, newBirthday, newLocation);

   
        String action = request.getParameter("action");

        if ("addEmail".equals(action)) {
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

        response.sendRedirect("profile.jsp"); 
    }
}
