package com.taufeeq.web.model;

import java.util.Objects;

public class Email {
	private int emailId;
	private String emails;
	private long createdTime;

	public Email() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true; 
		if (obj == null || getClass() != obj.getClass())
			return false; 

		Email emailObj = (Email) obj;
		return Objects.equals(this.emails, emailObj.emails); 
	}

	@Override
	public int hashCode() {
		return Objects.hash(emails);
	}

	public String getEmail() {
		return emails;
	}

	public void setEmail(String email) {
		this.emails = email;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public int getEmailId() {
		return emailId;
	}

	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	@Override
	public String toString() {
		return "Email{email='" + emails + "', createdTime=" + createdTime + '}';
	}
}
