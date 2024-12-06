package com.taufeeq.querylayer;

import com.taufeeq.web.model.User;

public interface QueryLayer {
    int insertUser(User user);
    User selectUserById(int userId);
    int verifyUser(String email, String password);
//    void insertUserEmail(int userId, String email);
//    void insertUserPhoneNumber(int userId, String phoneNumber);
//    boolean checkEmailUnique(String email);
//    String fetchHashedPassword(String email);
//    int fetchUserIdByEmail(String email);
//    void updateUserFormat(int userId, String format);
//    String fetchUserFormat(int userId);
}