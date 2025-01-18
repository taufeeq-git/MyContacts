package com.taufeeq.web.serv;

import com.taufeeq.web.dao.GroupDAO;
import com.taufeeq.web.dao.GroupDAOImpl;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddContactToGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GroupDAO groupDAO = new GroupDAOImpl();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		int groupId = Integer.parseInt(request.getParameter("groupId"));
		int contactId = Integer.parseInt(request.getParameter("contactId"));

		if ("add".equalsIgnoreCase(action)) {
			groupDAO.addContactToGroup(groupId, contactId);
		}

		else if ("delete".equalsIgnoreCase(action)) {
			groupDAO.deleteContactFromGroup(contactId, groupId);
		}

		response.sendRedirect("ViewGroupServlet?groupId=" + groupId);
	}
}
