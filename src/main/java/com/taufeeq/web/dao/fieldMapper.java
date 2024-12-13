package com.taufeeq.web.dao;

import java.util.*;

public class fieldMapper {
	
	public static Map<String, String> getUserFieldMapping() {
	    Map<String, String> mapping = new HashMap<>();

	   
	    mapping.put("userId", "userdetails.User_ID");
	    mapping.put("username", "userdetails.Username");
	    mapping.put("password", "userdetails.Password");
	    mapping.put("gender", "userdetails.Gender");
	    mapping.put("birthday", "userdetails.Birthday");
	    mapping.put("location", "userdetails.Location");

	 
	    mapping.put("PhoneNumber.phoneNumbers", "phonenumbers.Phone_number");
	    mapping.put("PhoneNumber.createdTime", "phonenumbers.createdTime");
	    
	    mapping.put("Email.emails", "mails.Mail");
	    mapping.put("Email.createdTime", "mails.createdTime");

	    return mapping;
	}

    public static Map<String, String> getEmailFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("mails.Mail", "email");
        mapping.put("mails.createdTime", "createdTime");
        return mapping;
    }
    public static Map<String, String> getPhoneNumberFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("phonenumbers.Phone_number", "phoneNumber");
        mapping.put("phonenumbers.createdTime", "createdTime");
        return mapping;
    }
    public static Map<String, String> getContactFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("userId ", "contactdetails.User_ID");
        mapping.put("contactId", "contactdetails.Contact_ID");
        mapping.put("username", "contactdetails.Name");
        mapping.put("gender", "contactdetails.gender");
        mapping.put("birthday", "contactdetails.birthday");
        mapping.put("favorite", "contactdetails.favorite");
        mapping.put("ct", "contactdetails.created_time");
        mapping.put("archive", "contactdetails.archive");
        return mapping;
    }
    public static Map<String, String> getGroupFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("groupId", "usergroups.Group_ID");
        mapping.put("userId", "usergroups.User_ID");
        mapping.put("groupName", "usergroups.Group_Name");
        return mapping;
    }
}
