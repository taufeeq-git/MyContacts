package com.taufeeq.web.serv;

import com.taufeeq.web.dao.GroupDAO;
import com.taufeeq.web.dao.GroupDAOImpl;
import com.taufeeq.web.model.*;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	GroupDAO groupDAO = new GroupDAOImpl();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String groupName = request.getParameter("groupName");
		int userId = (int) request.getAttribute("userId");

		Group newGroup = new Group(userId, groupName);
		groupDAO.addGroup(newGroup);

		response.sendRedirect("dashboard");
	}
}
