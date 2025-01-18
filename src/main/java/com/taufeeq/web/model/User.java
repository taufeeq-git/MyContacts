package com.taufeeq.web.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class User {
	private int userId;
	private String username;
	private String password;
	private String gender;
	private Date birthday;
	private String location;
	private long created_time;
	private long modified_time;
	private List<Email> emails;
	private List<PhoneNumber> phoneNumbers;
	private String loginMethod;
	private SimpleDateFormat dateFormat;
	

	public User() {

	}

	public long getCreated_time() {
		return created_time;
	}

	public void setCreated_time(long created_time) {
		this.created_time = created_time;
	}

	public long getModified_time() {
		return modified_time;
	}

	public void setModified_time(long modified_time) {
		this.modified_time = modified_time;
	}

	// Constructor
	public User(int userId, String username, String password, String gender, Date birthday, String location, String loginMethod) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.birthday = birthday;
		this.location = location;
		this.loginMethod = loginMethod;
	}

	public User(int userId, String username, String password, String gender, String location, String loginMethod) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.location = location;
		this.loginMethod = loginMethod;
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLoginMethod() {
		return loginMethod;
	}

	public void setLoginMethod(String loginMethod) {
		this.loginMethod = loginMethod;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}



	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", gender=" + gender
				+ ", birthday=" + birthday + ", location=" + location + ", created_time=" + created_time
				+ ", modified_time=" + modified_time + ", emails=" + emails + ", phoneNumbers=" + phoneNumbers
				+ ", loginMethod=" + loginMethod + "]";
	}
}
