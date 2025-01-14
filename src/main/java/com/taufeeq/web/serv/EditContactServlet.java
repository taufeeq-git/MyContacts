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

    private ContactDAO contactDAO=new ContactDAOImpl();

  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int contactId = Integer.parseInt(request.getParameter("contactId"));
//            System.out.println(contactId);
            int userId=(int) request.getAttribute("userId");
            if(!contactDAO.isContactInId(userId, contactId)) {
//            	System.out.println("illa");
            	response.sendRedirect("dashboard?error=GroupNotFound");
            	return;
            }
            UserDAO userDAO= new UserDAOImpl();
            String format= userDAO.getFormat(userId);
            Contact contact = contactDAO.getContactByContactId(contactId,format);
//            System.out.println(contact.getUsername());

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
            int contactId = Integer.parseInt(request.getParameter("contactId"));
            System.out.println(contactId);
            String newUsername = request.getParameter("username");
            String newGender = request.getParameter("gender");
            String newBirthday = request.getParameter("birthday");
            boolean newFavorite = request.getParameter("favorite") != null;
            boolean newArchive = request.getParameter("archive") != null;

//            Contact updatedContact = new Contact();
//            updatedContact.setContactId(contactId);
//            updatedContact.setUsername(newUsername);
//            updatedContact.setGender(newGender != null && !newGender.isEmpty() ? newGender : null);
//            updatedContact.setFormattedBirthday(newBirthday != null && !newBirthday.isEmpty() ? newBirthday : null);
//            updatedContact.setFavorite(favorite);
//            updatedContact.setArchive(archive);

            boolean success = contactDAO.updateContact(contactId,newUsername,newGender,newBirthday,newFavorite,newArchive); // Update the contact in DB

            if (success) {
                response.sendRedirect("ViewContactServlet?contactId=" + contactId); // Redirect to view contact
            } else {
                request.setAttribute("error", "Failed to update contact.");
                request.getRequestDispatcher("editContact.jsp").forward(request, response); // Forward to edit page with error
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("errorPage.jsp"); // Redirect on error
        }
    }
}

