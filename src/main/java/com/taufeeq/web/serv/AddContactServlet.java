package com.taufeeq.web.serv;

import com.taufeeq.web.dao.ContactDAO;
import com.taufeeq.web.dao.ContactDAOImpl;
import com.taufeeq.web.model.Contact;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int userId = (int) request.getAttribute("userId");
		

		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String dobString = request.getParameter("dob");
		String number = request.getParameter("number");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dob = null;
		try {
			dob = dateFormat.parse(dobString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Boolean favorite = request.getParameter("favorite") == "true" ? true : false;
		Boolean archive = request.getParameter("archive") == "true" ? true : false;

		long createdTime = System.currentTimeMillis() / 1000;

		ContactDAO contactDAO = new ContactDAOImpl();

		if (!contactDAO.doesContactExist(userId, number)) {
			Contact contact = new Contact();
			contact.setUserId(userId);
			contact.setUsername(username);
			contact.setGender(gender);
			contact.setEmail(email);
			contact.setBirthday(new java.sql.Date(dob.getTime()));
			contact.setPhone(number);
			contact.setArchive(archive);
			contact.setFavorite(favorite);
			contact.setCt(createdTime);

			int contactId = contactDAO.addContact(contact);

			if (contactId > 0) {
				contactDAO.addContactEmail(contactId, email);
				contactDAO.addContactPhoneNumber(contactId, number);
				response.sendRedirect("dashboard.jsp");
			} else {
				response.sendRedirect("login.jsp");
			}
		} else {
			response.sendRedirect("dashboard?contact_already_exists");
		}
	}
}
