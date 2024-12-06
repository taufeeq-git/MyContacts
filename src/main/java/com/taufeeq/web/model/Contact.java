package com.taufeeq.web.model;

import java.sql.Date;
import java.util.List;

public class Contact {
    private int userId;
    private int contactId;
    private String username;
    private String gender;
    private Date birthday;  // Stored as a String (e.g., "yyyy-MM-dd")
    private Boolean favorite;
    private Boolean archive;
    private String email;
    private String phone;
    private long ct;  // Created time in epoch format
    private List<String> emails;  // List for multiple emails if applicable
    private List<String> phoneNumbers;  // List for multiple phone numbers if applicable
    private String formattedCreatedTime;
    private String formattedBirthday; // Optional: Formatted String field for display
    // Formatted created time as String

    // Getters and setters
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getFormattedCreatedTime() {
        return formattedCreatedTime;
    }

    public void setFormattedCreatedTime(String formattedCreatedTime) {
        this.formattedCreatedTime = formattedCreatedTime;
    }
    public String getFormattedBirthday() {
        return formattedBirthday;
    }

    public void setFormattedBirthday(String formattedBirthday) {
        this.formattedBirthday = formattedBirthday;
    }
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
        this.ct = ct;
    }
}
