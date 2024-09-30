package com.taufeeq.web.serv;

import com.taufeeq.web.dao.ContactDAO;
import com.taufeeq.web.dao.ContactDAOImpl;
import com.taufeeq.web.model.Contact;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
//    private ContactDAO contactDAO;
    ContactDAO contactDAO = new ContactDAOImpl();

//    public ProfileServlet() {
//        contactDAO = new ContactDAOImpl();
//    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int contactId = Integer.parseInt(request.getParameter("contactId"));
        Contact contact = contactDAO.getContactByContactId(contactId);
        request.setAttribute("contact", contact);
        request.getRequestDispatcher("viewProfile.jsp").forward(request, response);
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
