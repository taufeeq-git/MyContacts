package com.taufeeq.web.serv;

import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.dao.UserDAOImpl;
import com.taufeeq.web.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String email=request.getParameter("email");
		String password= request.getParameter("password");
		
		UserDAO userDAO=new UserDAOImpl();
		boolean isvalid=userDAO.verifyuser(email,password);
		
		if(isvalid) {
			response.getWriter().println("Login Successful!!");
		}
		else {
			response.getWriter().println("Login failed!!");
		}
		
	}

}
