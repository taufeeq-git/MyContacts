package com.taufeeq.web.dao;
import com.taufeeq.web.model.Contact;
import com.taufeeq.web.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAOImpl implements ContactDAO{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mycontacts";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

	public int addContact(Contact contact) {
        String sql = "INSERT INTO contactdetails (User_ID ,Name, gender, birthday, favorite, archive  ) VALUES (?,?, ?, ?, ?, ?)";
        int ContactId = -1;

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, contact.getUserId());
            pst.setString(2, contact.getUsername());
            pst.setString(3, contact.getGender());
            pst.setString(4, contact.getBirthday());
            pst.setInt(5, contact.getFavorite());
            pst.setInt(6, contact.getArchive());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                ContactId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ContactId;
    }
	public void addContactEmail(int contactId, String email) {
//		System.out.println(contactId+" : "+email);
        String sql = "INSERT INTO contactemail (Contact_ID, Email) VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, contactId);
            pst.setString(2, email);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addContactPhoneNumber(int contactId, String phoneNumber) {
        String sql = "INSERT INTO contactnumber (Contact_ID, Phone_number) VALUES (?, ?)";
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, contactId);
            pst.setString(2, phoneNumber);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Contact> getContactsByUserId(int userId) {
    	String sql="Select * from contactdetails where User_ID =?;";
        List<Contact> contacts = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = connection.prepareStatement(sql)) {
             
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String name = rs.getString("Name");
                String gender = rs.getString("gender");
                String birthday = rs.getString("birthday");
                int favorite = rs.getInt("favorite");
                int archive = rs.getInt("archive");

                Contact contact = new Contact();
                contact.setContactId(contactId);
                contact.setUserId(userId);
                contact.setUsername(name);
                contact.setGender(gender);
                contact.setBirthday(birthday);
                contact.setFavorite(favorite);
                contact.setArchive(archive);

                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }
    @Override
    public Contact getContactByContactId(int contactId) {
        String contactSql = "SELECT * FROM contactdetails WHERE Contact_ID = ?";
        String emailSql = "SELECT Email FROM contactemail WHERE Contact_ID = ?";
        String phoneSql = "SELECT Phone_number FROM contactnumber WHERE Contact_ID = ?";

        Contact contact = new Contact();
        List<String> emails = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement contactStmt = connection.prepareStatement(contactSql);
             PreparedStatement emailStmt = connection.prepareStatement(emailSql);
             PreparedStatement phoneStmt = connection.prepareStatement(phoneSql)) {

            // Fetch contact details
            contactStmt.setInt(1, contactId);
            ResultSet contactRs = contactStmt.executeQuery();
            
            if (contactRs.next()) {
                int userId = contactRs.getInt("User_ID");
                String name = contactRs.getString("Name");
                String gender = contactRs.getString("gender");
                String birthday = contactRs.getString("birthday");
                int favorite = contactRs.getInt("favorite");
                int archive = contactRs.getInt("archive");
                
    	        contact.setUserId(userId);
    	        contact.setGender(gender);
    	        contact.setUsername(name);
    	        contact.setBirthday(birthday);
    	        contact.setArchive(archive);
    	        contact.setFavorite(favorite);
            }

            emailStmt.setInt(1, contactId);
            ResultSet emailRs = emailStmt.executeQuery();
            while (emailRs.next()) {
                emails.add(emailRs.getString("Email"));
            }

            phoneStmt.setInt(1, contactId);
            ResultSet phoneRs = phoneStmt.executeQuery();
            while (phoneRs.next()) {
                phoneNumbers.add(phoneRs.getString("Phone_number"));
            }

            if (contact != null) {
                contact.setEmails(emails);
                contact.setPhoneNumbers(phoneNumbers);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return contact;
    }
    
    @Override
    public void deleteContactById(int contactId) {
    	String deleteCGSql = "DELETE FROM groupcontacts WHERE Contact_ID = ?";
        String deleteEmailsSql = "DELETE FROM contactemail WHERE Contact_ID = ?";
        String deleteNumbersSql = "DELETE FROM contactnumber WHERE Contact_ID = ?";
        String deleteContactSql = "DELETE FROM contactdetails WHERE Contact_ID = ?";
        
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);
            try (PreparedStatement contactStmt = conn.prepareStatement(deleteCGSql)) {
                contactStmt.setInt(1, contactId);
                contactStmt.executeUpdate();
            }

            try (PreparedStatement emailStmt = conn.prepareStatement(deleteEmailsSql)) {
                emailStmt.setInt(1, contactId);
                emailStmt.executeUpdate();
            }

            try (PreparedStatement numberStmt = conn.prepareStatement(deleteNumbersSql)) {
                numberStmt.setInt(1, contactId);
                numberStmt.executeUpdate();
            }

            try (PreparedStatement contactStmt = conn.prepareStatement(deleteContactSql)) {
                contactStmt.setInt(1, contactId);
                contactStmt.executeUpdate();
            }
           
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
//    public List<String> getAllContactNames(int userId) {
//        List<String> contactNames = new ArrayList<>();
//        String sql = "SELECT Name FROM contactdetails WHERE User_ID = ?";
//        
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            
//            stmt.setInt(1, userId);
//            ResultSet rs = stmt.executeQuery();
//            
//            while (rs.next()) {
//                contactNames.add(rs.getString("Name"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        
//        return contactNames;
//    }
    public List<Contact> getContactByUserId(int userId) {
        String sql = "SELECT Contact_ID, Name FROM contactdetails WHERE User_ID = ?";
        List<Contact> contacts = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Contact contact = new Contact();
                contact.setContactId(rs.getInt("Contact_ID"));
                contact.setUsername(rs.getString("Name"));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }


	


}
