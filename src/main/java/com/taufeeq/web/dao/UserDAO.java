package com.taufeeq.web.dao;

import com.taufeeq.web.model.User;

public interface UserDAO {
    int addUser(User user);
    void addUserEmail(int userId, String email);
    void addUserPhoneNumber(int userId, String number);
    boolean verifyuser(String email, String password);
}
