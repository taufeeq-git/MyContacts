
package com.taufeeq.web.serv;

import com.taufeeq.web.dao.SessionDAO;
import com.taufeeq.web.dao.SessionDAOImpl;
import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.dao.UserDAOImpl;
import com.taufeeq.web.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


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
        		
                String sessionId = UUID.randomUUID().toString();
                Timestamp createdTime = new Timestamp(System.currentTimeMillis());
                Timestamp expirationTime = new Timestamp(createdTime.getTime() + 30 * 60 * 1000);

               
                SessionDAO sessionDAO = new SessionDAOImpl();
                sessionDAO.addSession(sessionId, userId, createdTime, expirationTime);

              
                Cookie sessionCookie = new Cookie("sessionId", sessionId);
                sessionCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(sessionCookie);
                response.sendRedirect("dashboard");
                
                
        } else {
            response.sendRedirect("signup.jsp?error=invalid");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("signup.jsp");
    }
}
