package com.taufeeq.web.serv;

import com.taufeeq.web.dao.ContactDAO;
import com.taufeeq.web.dao.ContactDAOImpl;
import com.taufeeq.web.model.Contact;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/addcontact")
public class AddContact extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userId = (int) request.getAttribute("userId");

 
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String number = request.getParameter("number");

        int favorite = request.getParameter("favorite") != null ? 1 : 0;
        int archive = request.getParameter("archive") != null ? 1 : 0;

        long ct = System.currentTimeMillis() / 1000; 

        // Create the Contact object
        Contact contact = new Contact();
        contact.setUserId(userId);
        contact.setUsername(username);
        contact.setGender(gender);
        contact.setEmail(email);
        contact.setBirthday(dob);
        contact.setPhone(number);
        contact.setArchive(archive);
        contact.setFavorite(favorite);
        contact.setCt(ct); 

   
        ContactDAO contactDAO = new ContactDAOImpl();
        int contactId = contactDAO.addContact(contact);

        if (contactId > 0) {
            contactDAO.addContactEmail(contactId, email);
            contactDAO.addContactPhoneNumber(contactId, number);
            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}
