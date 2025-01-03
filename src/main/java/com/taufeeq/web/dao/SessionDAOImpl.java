package com.taufeeq.web.dao;

import java.sql.*;
import java.util.List;
import java.util.Map;

import com.taufeeq.cp.DatabaseConnectionPool;
import com.taufeeq.web.enums.Enum.Table;
import com.taufeeq.web.enums.Enum.usersessions;
import com.taufeeq.web.model.User;
import com.taufeeq.web.query.QueryBuilder;

public class SessionDAOImpl implements SessionDAO {
	
    @Override
    public String addSession(String sessionId, int userId, Timestamp createdTime, Timestamp expirationTime) {
    	QueryBuilder queryBuilder= QueryBuilderFactory.getQueryBuilder();
    	
        queryBuilder.insert(Table.usersessions,usersessions.Session_ID,usersessions.User_ID,usersessions.Created_Time,usersessions.Expiration_time)
                    .values(sessionId, userId, createdTime, expirationTime)
                    .executeInsert();
        return sessionId;
    }
   
    @Override
    public void deleteExpiredSessions(Timestamp currentTime) {
        QueryBuilder queryBuilder = QueryBuilderFactory.getQueryBuilder();
        int batchSize = 1000; 
        int deletedRows;

        do {
            deletedRows = queryBuilder
                .delete(Table.usersessions)
                .whereLessThan(usersessions.Expiration_time , currentTime) 
                .limit(batchSize) 
                .executeDelete(); 
        } while (deletedRows == batchSize); 
    }


    @Override
    public void updateSessionExpiration(String sessionId, Timestamp newExpirationTime) {
    	QueryBuilder queryBuilder= QueryBuilderFactory.getQueryBuilder();
        queryBuilder.update(Table.usersessions)
                    .set(usersessions.Expiration_time, newExpirationTime)
                    .where(usersessions.Session_ID,sessionId)
                    .executeUpdate();
    }
  
    @Override
    public void deleteAllSessions() {
    	QueryBuilder queryBuilder= QueryBuilderFactory.getQueryBuilder();
        queryBuilder.delete(Table.usersessions)
        			.executeDelete();
    }
    
   
    @Override
    public boolean isValidSession(String sessionId) {
    	QueryBuilder queryBuilder= QueryBuilderFactory.getQueryBuilder();
        List<String>result = queryBuilder
            .select(usersessions.Session_ID)
            .from(Table.usersessions)
            .where(usersessions.Session_ID,sessionId)
            .executeSelect(String.class,null);
        return result!=null;
    }


    @Override
    public User getUserBySessionId(String sessionId) {
    	QueryBuilder queryBuilder= QueryBuilderFactory.getQueryBuilder();
    	Map<String, String> userFieldMapping = FieldMapper.getUserFieldMapping(); 
        List<User> result = queryBuilder
            .select(usersessions.User_ID)
            .from(Table.usersessions)
            .where(usersessions.Session_ID, sessionId)
            .executeSelect(User.class,userFieldMapping);
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }
     


    @Override
    public void deleteSession(String sessionId) {
    	QueryBuilder queryBuilder= QueryBuilderFactory.getQueryBuilder();
        queryBuilder.delete(Table.usersessions)
            		.where(usersessions.Session_ID, sessionId)
            		.executeDelete();
    }

    @Override
    public int getUserIdBySessionId(String sessionId) {
    	QueryBuilder queryBuilder= QueryBuilderFactory.getQueryBuilder();
        List<Integer> result = queryBuilder
            .select(usersessions.User_ID)
            .from(Table.usersessions)
            .where(usersessions.Session_ID, sessionId)
            .executeSelect(Integer.class,null);

        if (!result.isEmpty()) {
            return (int) result.get(0);
        }
        return -1;
    }

    @Override
    public void deleteSessionById(String sessionId) {
    	QueryBuilder queryBuilder= QueryBuilderFactory.getQueryBuilder();
        queryBuilder
            .delete(Table.usersessions)
            .where(usersessions.Session_ID, sessionId)
            .executeDelete();
    }

    @Override
    public Integer getUserIdFromSession(String sessionId) {
    	QueryBuilder queryBuilder= QueryBuilderFactory.getQueryBuilder();
        List<Integer> result = queryBuilder
            .select(usersessions.User_ID)
            .from(Table.usersessions)
            .where(usersessions.Session_ID, sessionId)
            .executeSelect(Integer.class,null);

        if (!result.isEmpty()) {
            return (int) result.get(0);
        }
        return null;
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

