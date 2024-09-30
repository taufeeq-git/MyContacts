package com.taufeeq.web.serv;

import com.taufeeq.web.dao.ContactDAO;

import com.taufeeq.web.dao.ContactDAOImpl;
import com.taufeeq.web.model.Contact;
import com.taufeeq.web.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/addcontact")
public class AddContact extends HttpServlet{
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 	HttpSession session= request.getSession(false);
		 	
		 	String username = request.getParameter("username");
	        String email = request.getParameter("email");
	        String gender = request.getParameter("gender");
	        String dob = request.getParameter("dob");
	        String number = request.getParameter("number");
	        
	        int favorite = request.getParameter("favorite") != null ? 1 : 0; 
	        int archive = request.getParameter("archive") != null ? 1 : 0; 


	    
	        Contact contact = new Contact();
	        contact.setUserId((int)session.getAttribute("userId"));
	        contact.setUsername(username);
	        contact.setGender(gender);
	        contact.setEmail(email);
	        contact.setBirthday(dob);
	        contact.setPhone(number);
	        contact.setArchive(archive);
	        contact.setFavorite(favorite);
	        
	        

	        ContactDAO contactDAO = new ContactDAOImpl();
	        int ContactId = contactDAO.addContact(contact); 

	        if (ContactId > 0) {
	        		contactDAO.addContactEmail(ContactId, email);
	        		contactDAO.addContactPhoneNumber(ContactId, number);
	                response.sendRedirect("dashboard.jsp");
	        	}else {
	        		response.sendRedirect("login.jsp");
	        	}
	 }
}
