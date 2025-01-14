
package com.taufeeq.web.serv;

import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.dao.UserDAOImpl;
import com.taufeeq.web.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String dobString = request.getParameter("dob"); 
        String location = request.getParameter("location");
        String number = request.getParameter("number");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        Date dob = null;
            try {
				dob = dateFormat.parse(dobString);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
       

    
        User user = new User(0, username, password, gender, dob, location);
        int userId=0;

        UserDAO userDAO = new UserDAOImpl();
        if(userDAO.isEmailUnique(email)) {
        	userId = userDAO.addUser(user);
//        	System.out.println("unique");
        }else {
        	response.sendRedirect("signup.jsp?error=emailexists");
        	return;
        }
//        userId=userDAO.addUser(user);

        if (userId > 0) {
        		userDAO.addUserEmail(userId, email);
        		userDAO.addUserPhoneNumber(userId, number);
                response.sendRedirect("login.jsp");
        } else {
            response.sendRedirect("signup.jsp?error=invalid");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("signup.jsp");
    }
}
