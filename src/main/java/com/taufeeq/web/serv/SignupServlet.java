package com.taufeeq.web.serv;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user inputs from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        String location = request.getParameter("location");

        Connection con = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;

        try {
        	
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mycontacts", "root", "root");

            String sqlUserDetails = "INSERT INTO userdetails (Password, Username, Gender, Birthday, Location) VALUES (?, ?, ?, ?, ?)";
            pst1 = con.prepareStatement(sqlUserDetails, PreparedStatement.RETURN_GENERATED_KEYS);
            pst1.setString(1, password);
            pst1.setString(2, username);
            pst1.setString(3, gender);
            pst1.setString(4, dob);
            pst1.setString(5, location);
            pst1.executeUpdate();
            
            rs = pst1.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1); 
            }

            
            String sqlUserEmail = "INSERT INTO all_mail (User_ID, Mail) VALUES (?, ?)";
            pst2 = con.prepareStatement(sqlUserEmail);
            pst2.setInt(1, userId);
            pst2.setString(2, email);
            pst2.executeUpdate();
            
            response.getWriter().println("registered successfully");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs!=null) rs.close();
                if(pst1!=null) pst1.close();
                if(pst2!=null) pst2.close();
                if(con!=null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
