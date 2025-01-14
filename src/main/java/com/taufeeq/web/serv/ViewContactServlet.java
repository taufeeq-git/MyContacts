package com.taufeeq.web.serv;
import com.taufeeq.web.dao.*;

import com.taufeeq.web.dao.ContactDAO;
import com.taufeeq.web.dao.ContactDAOImpl;
import com.taufeeq.web.model.Contact;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ViewContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ContactDAO contactDAO;

    public ViewContactServlet() {
        this.contactDAO = new ContactDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int contactId = Integer.parseInt(request.getParameter("contactId"));
        int userId=(int) request.getAttribute("userId");
        UserDAO userDAO= new UserDAOImpl();
        String format= userDAO.getFormat(userId);

        Contact contact = contactDAO.getContactByContactId(contactId, format);

        request.setAttribute("contact", contact);
        request.setAttribute("contactId", contactId);
        request.getRequestDispatcher("viewContact.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String contactIdParam = request.getParameter("contactId");

        if (contactIdParam == null || contactIdParam.isEmpty()) {
            return;
        }

        int contactId = Integer.parseInt(contactIdParam); 
        String action = request.getParameter("action");

     
        if ("addEmail".equals(action)) {
            String newEmail = request.getParameter("newEmail");
            if (newEmail != null && !newEmail.trim().isEmpty()) {
                contactDAO.addContactEmail(contactId, newEmail); 
            }
        } 

        else if ("addPhoneNumber".equals(action)) {
            String newPhoneNumber = request.getParameter("newPhoneNumber");
            if (newPhoneNumber != null && !newPhoneNumber.trim().isEmpty()) {
                contactDAO.addContactPhoneNumber(contactId, newPhoneNumber); 
            }
        } 

        else if ("deleteContact".equals(action)) {
            if(contactDAO.deleteContactById(contactId)) {
            	response.sendRedirect("dashboard?error=ContactNotFound");
                return;
            }
            response.sendRedirect("dashboard");
            return;
        }

        request.setAttribute("contactId", contactId);
        doGet(request, response);
    }
}
