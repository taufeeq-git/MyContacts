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

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String location = request.getParameter("location");
        String number = request.getParameter("number");

    
        User user = new User(0, username, password, gender, dob, location);

        UserDAO userDAO = new UserDAOImpl();
        int userId = userDAO.addUser(user); 

     
        if (userId > 0) {
       
            userDAO.addUserEmail(userId, email);
            userDAO.addUserPhoneNumber(userId, number);
            response.sendRedirect("login.jsp");
        } else {
            response.sendRedirect("signup.jsp");
        }

        response.getWriter().println("Registration successful!");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect("signup.jsp");
    }

}
