package com.taufeeq.web.serv;


import com.taufeeq.web.dao.GroupDAO;
import com.taufeeq.web.dao.GroupDAOImpl;
import com.taufeeq.web.dao.ContactDAO;
import com.taufeeq.web.dao.ContactDAOImpl;
import com.taufeeq.web.model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/ViewGroupServlet")
public class ViewGroupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private GroupDAO groupDAO = new GroupDAOImpl();
    private ContactDAO contactDAO = new ContactDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int groupId = Integer.parseInt(request.getParameter("groupId"));
//        HttpSession session = request.getSession();
//        int userId = (Integer) session.getAttribute("userId");
        int userId=(int)request.getAttribute("userId");


        List<Contact> groupContacts = groupDAO.getContactsInGroup(groupId);
        
     
        List<Contact> userContacts = contactDAO.getContactsByUserId(userId);
        
        String groupName = groupDAO.getGroupNameById(groupId);  
        request.setAttribute("groupName", groupName); 
        
        request.setAttribute("groupId", groupId);
        request.setAttribute("groupContacts", groupContacts);
        request.setAttribute("userContacts", userContacts);
        
        request.getRequestDispatcher("viewGroup.jsp").forward(request, response);
    }
}



