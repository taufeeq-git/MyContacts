package com.taufeeq.web.serv;

import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.dao.UserDAOImpl;
import com.taufeeq.web.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;



@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String email=request.getParameter("email");
		String password= request.getParameter("password");
		
		UserDAO userDAO=new UserDAOImpl();
		int userId= userDAO.verifyuser(email, password);
		
		if(userId>0) {
			User user = userDAO.getUserById(userId);
			HttpSession session= request.getSession();
			session.setAttribute("userId", userId);
			session.setAttribute("user", user);
			
			response.sendRedirect("dashboard");
		}else {
			response.sendRedirect("login.jsp?error=invalid");
		}
		
	}
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        response.sendRedirect("login.jsp");
	    }

}
