package com.taufeeq.web.dao;

import java.sql.*;
import java.util.List;
import java.util.Map;

import com.taufeeq.cp.DatabaseConnectionPool;
import com.taufeeq.web.model.User;

public class SessionDAOImpl implements SessionDAO {

    @Override
    public String addSession(String sessionId, int userId, Timestamp createdTime, Timestamp expirationTime) {
        String sql = "INSERT INTO usersessions (Session_ID, User_ID, Created_Time, Expiration_Time) VALUES (?, ?, ?, ?)";
        
        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, sessionId);
            pst.setInt(2, userId);
            pst.setTimestamp(3, createdTime);
            pst.setTimestamp(4, expirationTime);
//            pst.setString(5, timezone);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessionId;
    }
    
    public void deleteExpiredSessions(Timestamp currentTime) {
        int batchSize = 1000;  
        int deletedRows;
        
        do {
            String sql = "DELETE FROM usersessions WHERE Expiration_Time < ? LIMIT ?";
            
            try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();	
                 PreparedStatement pst = con.prepareStatement(sql)) {
                
                pst.setTimestamp(1, currentTime);
                pst.setInt(2, batchSize);
                
                deletedRows = pst.executeUpdate();  
                
            } catch (SQLException e) {
                e.printStackTrace();
                break;
            }

        } while (deletedRows == batchSize);  
    }

    @Override
    public void updateSessionExpiration(String sessionId, Timestamp newExpirationTime) {
        String sql = "UPDATE usersessions SET Expiration_Time = ? WHERE Session_ID = ?";
        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setTimestamp(1, newExpirationTime);
            pst.setString(2, sessionId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  
    @Override
    public void deleteAllSessions() {
        String sql = "DELETE FROM usersessions";
        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean isValidSession(String sessionId) {
        String sql = "SELECT COUNT(*) FROM usersessions WHERE session_id = ?";
        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, sessionId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User getUserBySessionId(String sessionId) {
        String sql = "SELECT user_id FROM session WHERE Session_ID = ?";
        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, sessionId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    UserDAOImpl userDAO = new UserDAOImpl();
                    return userDAO.getUserById(userId); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteSession(String sessionId) {
        String sql = "DELETE FROM usersessions WHERE Session_ID = ?";
        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, sessionId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getUserIdBySessionId(String sessionId) {
        String sql = "SELECT User_ID FROM usersessions WHERE Session_ID = ?";
        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, sessionId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    @Override
    public void deleteSessionById(String sessionId) {
        String sql = "DELETE FROM usersessions WHERE Session_ID = ?";
        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, sessionId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Integer getUserIdFromSession(String sessionId) {
        String sql = "SELECT User_ID FROM usersessions WHERE Session_ID = ?";
        Integer userId = null;

        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, sessionId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("User_ID"); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId; 
    }
  
    
    @Override
    public void batchUpdateSessionExpirations(List<Map.Entry<String, Timestamp>> sessions) {
        String sql = "UPDATE usersessions SET Expiration_Time = ? WHERE Session_ID = ?";
        int batchSize = 1000; 
        int count = 0;

        try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            for (Map.Entry<String, Timestamp> entry : sessions) {
             
                Timestamp newExpirationTime = new Timestamp(entry.getValue().getTime() + 30 * 60 * 1000);
                pst.setTimestamp(1, newExpirationTime);
                pst.setString(2, entry.getKey()); 
                pst.addBatch(); 

                count++; 

              
                if (count % batchSize == 0) {
                    pst.executeBatch(); 
                    count = 0; 
                }
            }

         
            if (count > 0) {
                pst.executeBatch(); 
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }


}

