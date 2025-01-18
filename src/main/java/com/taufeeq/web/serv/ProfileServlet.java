package com.taufeeq.web.serv;

import com.taufeeq.web.dao.ContactDAO;
import com.taufeeq.web.dao.ContactDAOImpl;
import com.taufeeq.web.model.Contact;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ContactDAO contactDAO = new ContactDAOImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int contactId = Integer.parseInt(request.getParameter("contactId"));
		String format = (String) request.getAttribute("dateFormat");
		Contact contact = contactDAO.getContactByContactId(contactId, format);
		request.setAttribute("contact", contact);
		request.getRequestDispatcher("profile.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int contactId = Integer.parseInt(request.getParameter("contactId"));
		String newEmail = request.getParameter("newEmail");
		String newPhoneNumber = request.getParameter("newPhoneNumber");

		if (newEmail != null && !newEmail.isEmpty()) {
			contactDAO.addContactEmail(contactId, newEmail);
		}
		if (newPhoneNumber != null && !newPhoneNumber.isEmpty()) {
			contactDAO.addContactPhoneNumber(contactId, newPhoneNumber);
		}

		response.sendRedirect("ProfileServlet?contactId=" + contactId);
	}
}
