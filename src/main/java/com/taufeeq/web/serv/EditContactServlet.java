package com.taufeeq.web.serv;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taufeeq.web.dao.*;
import com.taufeeq.web.model.*;

public class EditContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ContactDAO contactDAO = new ContactDAOImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int contactId = Integer.parseInt(request.getParameter("contactId"));
			int userId = (int) request.getAttribute("userId");
			if (!contactDAO.isContactInId(userId, contactId)) {
				response.sendRedirect("dashboard?error=ContactNotFound");
				return;
			}
			UserDAO userDAO = new UserDAOImpl();
			String format = userDAO.getFormat(userId);
			Contact contact = contactDAO.getContactByContactId(contactId, format);

			if (contact != null) {
				request.setAttribute("contact", contact);
				request.getRequestDispatcher("editContact.jsp").forward(request, response);
			} else {
				response.sendRedirect("dashboard.jsp?error=nosuchcontactId");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("dashboard.jsp?error=invalid");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String action = request.getParameter("action");
			int contactId = Integer.parseInt(request.getParameter("contactId"));

			if ("updateContact".equals(action)) {
				String newUsername = request.getParameter("username");
				String newGender = request.getParameter("gender");
				String newBirthday = request.getParameter("birthday");
				boolean newFavorite = request.getParameter("favorite") != null;
				boolean newArchive = request.getParameter("archive") != null;

				boolean success = contactDAO.updateContact(contactId, newUsername, newGender, newBirthday, newFavorite,
						newArchive);

				if (success) {
					response.sendRedirect("ViewContactServlet?contactId=" + contactId);
				} else {
					request.setAttribute("error", "Failed to update contact.");
					request.getRequestDispatcher("editContact.jsp").forward(request, response);
				}
			} else if ("addEmail".equals(action)) {
				String email = request.getParameter("email");
				if (!contactDAO.doesEmailExist(contactId, email)) {
					contactDAO.addContactEmail(contactId, email);
					response.sendRedirect("editContact?contactId=" + contactId);
					return;
				}
				response.sendRedirect("dashboard?error=EmailAlreadyExists");
				
			} else if ("deleteEmail".equals(action)) {
				String email = request.getParameter("email");
				contactDAO.deleteContactEmail(contactId, email);
				response.sendRedirect("editContact?contactId=" + contactId);
			} else if ("addPhoneNumber".equals(action)) {
				String phoneNumber = request.getParameter("number");
				if (!contactDAO.doesNumberExist(contactId, phoneNumber)) {
					contactDAO.addContactPhoneNumber(contactId, phoneNumber);
					response.sendRedirect("editContact?contactId=" + contactId);
					return;
				}
				response.sendRedirect("dashboard?error=NumberAlreadyExists");

			} else if ("deletePhoneNumber".equals(action)) {
				String phoneNumber = request.getParameter("phoneNumber");
				contactDAO.deleteContactPhoneNumber(contactId, phoneNumber);
				response.sendRedirect("editContact?contactId=" + contactId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("errorPage.jsp");
		}
	}
}