package com.taufeeq.web.model;

import java.util.Objects;

public class PhoneNumber implements ClassInterface {
    private String phoneNumbers;  
    private long createdTime;    
    
    public PhoneNumber() {
    	
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same instance
        if (obj == null || getClass() != obj.getClass()) return false; // Null or different class

        PhoneNumber phoneObj = (PhoneNumber) obj;
        return Objects.equals(this.phoneNumbers, phoneObj.phoneNumbers); // Logical equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumbers); // Consistent with equals
    }

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumbers;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumbers = phoneNumber;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "PhoneNumber{phoneNumber='" + phoneNumbers + "', createdTime=" + createdTime + '}';
    }
}