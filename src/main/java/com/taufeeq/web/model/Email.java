package com.taufeeq.web.model;

import java.util.Objects;

public class Email implements ClassInterface {
    private String emails;    
    private long createdTime;    
    
 
    public Email() {
	}
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same instance
        if (obj == null || getClass() != obj.getClass()) return false; // Null or different class

        Email emailObj = (Email) obj;
        return Objects.equals(this.emails, emailObj.emails); // Logical equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(emails); // Consistent with equals
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

    @Override
    public String toString() {
        return "Email{email='" + emails + "', createdTime=" + createdTime + '}';
    }
}
