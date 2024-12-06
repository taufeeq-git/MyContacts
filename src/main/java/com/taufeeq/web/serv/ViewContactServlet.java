package com.taufeeq.web.serv;
import com.taufeeq.web.dao.*;

import com.taufeeq.web.dao.ContactDAO;
import com.taufeeq.web.dao.ContactDAOImpl;
import com.taufeeq.web.model.Contact;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ViewContactServlet")
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
//        System.out.println(contact.getBirthday());
        System.out.println(contact.getCt());
 
//        System.out.println(contact.getFormattedCreatedTime());
//        String userIpAddress = request.getRemoteAddr();
//    	request.setAttribute("userIp", userIpAddress);  
//        System.out.println(contact.getCt());
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
            contactDAO.deleteContactById(contactId);
            response.sendRedirect("dashboard");
            return;
        }

        request.setAttribute("contactId", contactId);
        doGet(request, response);
    }
}
