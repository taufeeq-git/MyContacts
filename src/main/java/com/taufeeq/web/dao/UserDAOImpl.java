package com.taufeeq.web.dao;

import com.taufeeq.web.model.User;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mycontacts";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    public int addUser(User user) {
        String sql = "INSERT INTO userdetails (Password, Username, Gender, Birthday, Location) VALUES (?, ?, ?, ?, ?)";
        int userId = -1;

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, user.getPassword());
            pst.setString(2, user.getUsername());
            pst.setString(3, user.getGender());
            pst.setString(4, user.getBirthday());
            pst.setString(5, user.getLocation());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }

    @Override
    public void addUserEmail(int userId, String email) {
        String sql = "INSERT INTO mails (User_ID, Mail) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, userId);
            pst.setString(2, email);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUserPhoneNumber(int userId, String number) {
        String sql = "INSERT INTO phonenumbers (User_ID, Phone_number) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, userId);
            pst.setString(2, number);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean verifyuser(String email,String password) {
    	String sql="SELECT u.Password From userdetails u INNER JOIN mails m ON u.user_ID=m.User_ID WHERE m.Mail=?";
    	try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement pst = con.prepareStatement(sql)) {

               pst.setString(1, email);
               
               try (ResultSet rs = pst.executeQuery()) {
                   if (rs.next()) {
                       String storedPassword = rs.getString("Password");
                       return storedPassword.equals(password); 
                   }
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
    	return false;
    }
}
