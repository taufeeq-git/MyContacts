package com.taufeeq.web.dao;

import com.taufeeq.cp.DatabaseConnectionPool;
import com.taufeeq.web.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
 
	    private final QueryLayer queryLayer;

	    public UserDAOImpl(){
	        this.queryLayer =QueryLayerFactory.getQueryLayer(); 
	    }

	    @Override
	    public int addUser(User user) {
	        return queryLayer.addUser(user);
	    }

	    @Override
	    public User getUserById(int userId) {
	        return queryLayer.getUserById(userId);
	    }

	   
	    @Override
	    public void addUserEmail(int userId, String email) {
	        queryLayer.addUserEmail(userId, email);
	    }

	    @Override
	    public void addUserPhoneNumber(int userId, String phoneNumber) {
	        queryLayer.addUserPhoneNumber(userId, phoneNumber);
	    }

	    @Override
	    public boolean isEmailUnique(String email) {
	        return queryLayer.isEmailUnique(email);
	    }

	    @Override
	    public String getHashedPasswordByEmail(String email) {
	        return queryLayer.getHashedPasswordByEmail(email);
	    }

	    @Override
	    public int getUserIdByEmail(String email) {
	        return queryLayer.getUserIdByEmail(email);
	    }

	    @Override
	    public void updateFormat(int userId, String format) {
	        queryLayer.updateFormat(userId, format);
	    }

	    @Override
	    public String getFormat(int userId) {
	        return queryLayer.getFormat(userId);
	    }

		@Override
		public int verifyuser(String email, String password) {
			return queryLayer.verifyuser(email,password);
		}
	}
