package com.taufeeq.web.dao;

import com.taufeeq.web.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public User getUserById(int userId) {
        String userDetailsSql = "SELECT * FROM userdetails WHERE User_ID = ?";
        String emailsSql = "SELECT Mail FROM mails WHERE User_ID = ?";
        String phonesSql = "SELECT Phone_number FROM phonenumbers WHERE User_ID = ?";

        User user = null;

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            
            try (PreparedStatement userDetailsStmt = con.prepareStatement(userDetailsSql)) {
                userDetailsStmt.setInt(1, userId);
                ResultSet rs = userDetailsStmt.executeQuery();
                if (rs.next()) {
                    user = new User(
                        rs.getInt("User_ID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Gender"),
                        rs.getString("Birthday"),
                        rs.getString("Location")
                    );
                }
            }

            if (user != null) {
             
                List<String> emails = new ArrayList<>();
                try (PreparedStatement emailsStmt = con.prepareStatement(emailsSql)) {
                    emailsStmt.setInt(1, userId);
                    ResultSet rsEmails = emailsStmt.executeQuery();
                    while (rsEmails.next()) {
                        emails.add(rsEmails.getString("Mail"));
                    }
                    user.setEmails(emails);
                }

                List<String> phoneNumbers = new ArrayList<>();
                try (PreparedStatement phonesStmt = con.prepareStatement(phonesSql)) {
                    phonesStmt.setInt(1, userId);
                    ResultSet rsPhones = phonesStmt.executeQuery();
                    while (rsPhones.next()) {
                        phoneNumbers.add(rsPhones.getString("Phone_number"));
                    }
                    user.setPhoneNumbers(phoneNumbers);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public int verifyuser(String email, String password){
        String sql = "SELECT u.User_ID FROM userdetails u INNER JOIN mails m ON u.User_ID = m.User_ID WHERE m.Mail = ? AND u.Password = ?";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, email);
            pst.setString(2, password);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("User_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

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

    public void addUserPhoneNumber(int userId, String phoneNumber) {
        String sql = "INSERT INTO phonenumbers (User_ID, Phone_number) VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, userId);
            pst.setString(2, phoneNumber);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
	public boolean isEmailUnique(String email) {
		String sql="SELECT COUNT(*) FROM mails WHERE Mail= ?";
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement pst=con.prepareStatement(sql)){
				pst.setString(1,email);
				ResultSet rs=pst.executeQuery();
				if(rs.next()) {
					return rs.getInt(1)==0;
				}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	


}
