package com.taufeeq.web.dao;

import com.taufeeq.web.model.User;

public interface UserDAO {
    int addUser(User user);
    void addUserEmail(int userId, String email);
    void addUserPhoneNumber(int userId, String number);
    int verifyuser(String email, String password);
    User getUserById(int userId);
    boolean isEmailUnique(String email);
    String getHashedPasswordByEmail(String email);
    int getUserIdByEmail(String email);
	void updateFormat(int userId, String selectedFormat);
	String getFormat(int userId);
    
}
