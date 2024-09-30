package com.taufeeq.web.dao;

import com.taufeeq.web.model.Contact;
import com.taufeeq.web.model.Group;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDAOImpl implements GroupDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/mycontacts"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "root"; 

    @Override
    public void addGroup(Group group) {
        String sql = "INSERT INTO usergroups(User_ID, Group_Name) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, group.getUserId());
            stmt.setString(2, group.getGroupName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    @Override
    public List<String> getUserGroups(int userId) {
        List<String> groupNames = new ArrayList<>();
        String sql="SELECT Group_Name from usergroups where User_ID=?;";

        try (Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                groupNames.add(resultSet.getString("Group_Name")); 
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return groupNames; 
    }
    @Override
    public List<Group> getUserGroupsWithIds(int userId) {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT Group_ID, Group_Name FROM usergroups WHERE User_ID = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int groupId = rs.getInt("Group_ID");
                String groupName = rs.getString("Group_Name");
                groups.add(new Group(groupId, userId, groupName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return groups;
    }
    @Override
    public List<Contact> getContactsInGroup(int groupId) {
        String sql = "SELECT c.Contact_ID, c.Name FROM contactdetails c "
                   + "JOIN groupcontacts gc ON c.Contact_ID = gc.Contact_ID "
                   + "WHERE gc.Group_ID = ?";
        List<Contact> contacts = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, groupId);
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

    @Override
    public void addContactToGroup(int groupId, int contactId) {
        String sql = "INSERT INTO groupcontacts (Group_ID, Contact_ID) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, groupId);
            stmt.setInt(2, contactId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getGroupNameById(int groupId) {
        String sql = "SELECT * FROM usergroups WHERE Group_ID = ?";
//        Group group = null;
        String groupname = null;
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, groupId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
//                group = new Group();
//                group.setGroupId(rs.getInt("Group_ID"));
//                group.setGroupName(rs.getString("Group_Name"));
            	groupname=rs.getString("Group_name");
//                group.setUserId(rs.getInt("User_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupname;
    }
    @Override
    public boolean deleteContactFromGroup(int contactId, int groupId) {
        String sql = "DELETE FROM groupcontacts WHERE Contact_ID = ? AND Group_ID = ?";
        boolean isDeleted = false;
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, contactId);
            stmt.setInt(2, groupId);
            
            int rowsAffected = stmt.executeUpdate();
            isDeleted = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }
}


