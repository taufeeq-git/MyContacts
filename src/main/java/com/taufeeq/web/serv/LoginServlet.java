//package com.taufeeq.web.serv;
//
//import com.taufeeq.web.dao.SessionDAO;
//import com.taufeeq.web.dao.SessionDAOImpl;
//import com.taufeeq.web.dao.UserDAO;
//import com.taufeeq.web.dao.UserDAOImpl;
//import de.mkammerer.argon2.Argon2;
//import de.mkammerer.argon2.Argon2Factory;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.Timestamp;
//import java.util.UUID;
//
//@WebServlet("/login")
//public class LoginServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//
//        UserDAO userDAO = new UserDAOImpl();
//        String storedHashedPassword = userDAO.getHashedPasswordByEmail(email);
//        System.out.println("db"+storedHashedPassword);
//
//        if (storedHashedPassword == null) {
//            response.sendRedirect("login.jsp?error=invalid");
//            return;
//        }
//
//        Argon2 argon2 = Argon2Factory.create();
//        
//        boolean isPasswordValid = argon2.verify(storedHashedPassword, password.toCharArray()); 
//
//        if (isPasswordValid) {
//            String sessionId = UUID.randomUUID().toString();
//            Timestamp createdTime = new Timestamp(System.currentTimeMillis());
//            Timestamp expirationTime = new Timestamp(createdTime.getTime() + 30 * 60 * 1000);
//
//            SessionDAO sessionDAO = new SessionDAOImpl();
//            int userId = userDAO.getUserIdByEmail(email); 
//            sessionDAO.addSession(sessionId, userId, createdTime, expirationTime);
//
//            // Set cookie for the session
//            Cookie sessionCookie = new Cookie("sessionId", sessionId);
//            sessionCookie.setMaxAge(60 * 60 * 24); 
//            response.addCookie(sessionCookie);
//
//            response.sendRedirect("dashboard");
//        } else {
//        	System.out.println("worng");
//            response.sendRedirect("login.jsp?error=invalid");
//        }
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.sendRedirect("login.jsp");
//    }
//}


package com.taufeeq.web.serv;

import com.taufeeq.web.dao.SessionDAO;
import com.taufeeq.web.dao.SessionDAOImpl;
import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.dao.UserDAOImpl;
//import com.taufeeq.web.model.User;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	String userIpAddress = request.getRemoteAddr();
//    	request.setAttribute("userIp", userIpAddress);  

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
//        Argon2 argon2 = Argon2Factory.create();
//        String hashedPassword = null;
//        try {
//            hashedPassword = argon2.hash(2, 65536, 1, password); 
//        } finally {
//            argon2.wipeArray(password.toCharArray()); 
//        }
//        System.out.println(hashedPassword);

        UserDAO userDAO = new UserDAOImpl();
        int userId = userDAO.verifyUser(email, password);
//        int userId=34;
//        response.sendRedirect("dashboard");

        if (userId > 0) {
           
            String sessionId = UUID.randomUUID().toString();
            Timestamp createdTime = new Timestamp(System.currentTimeMillis());
            Timestamp expirationTime = new Timestamp(createdTime.getTime() + 30 * 60 * 1000);

           
            SessionDAO sessionDAO = new SessionDAOImpl();
            sessionDAO.addSession(sessionId, userId, createdTime, expirationTime);

          
            Cookie sessionCookie = new Cookie("sessionId", sessionId);
            sessionCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(sessionCookie);
//            System.out.println(sessionId);
            System.out.println(userId);
            response.sendRedirect("dashboard");
        } else {
            response.sendRedirect("login.jsp?error=invalid");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }
}

