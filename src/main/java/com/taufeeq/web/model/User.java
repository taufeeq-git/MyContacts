package com.taufeeq.web.model;

import java.util.List;

public class User {
    private int userId;
    private String username;
    private String password;
    private String gender;
    private String birthday;
    private String location;
    private List<String> emails;        
    private List<String> phoneNumbers;  

    // Constructor
    public User(int userId, String username, String password, String gender, String birthday, String location) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.location = location;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
