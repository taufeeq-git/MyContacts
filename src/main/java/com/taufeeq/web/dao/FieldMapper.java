package com.taufeeq.web.dao;

import java.util.*;

public class FieldMapper {
	
	public static Map<String, String> getUserFieldMapping() {
	    Map<String, String> mapping = new HashMap<>();

	   
	    mapping.put("userId", "userdetails.User_ID");
	    mapping.put("username", "userdetails.Username");
	    mapping.put("password", "userdetails.Password");
	    mapping.put("gender", "userdetails.Gender");
	    mapping.put("birthday", "userdetails.Birthday");
	    mapping.put("location", "userdetails.Location");
	    mapping.put("created_time", "userdetails.created_time");
        mapping.put("modified_time", "userdetails.modified_time");

	 
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
        mapping.put("created_time", "contactdetails.created_time");
        mapping.put("modified_time", "contactdetails.modified_time");
        mapping.put("archive", "contactdetails.archive");
        return mapping;
    }
    public static Map<String, String> getGroupFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("groupId", "usergroups.Group_ID");
        mapping.put("userId", "usergroups.User_ID");
        mapping.put("groupName", "usergroups.Group_Name");
        mapping.put("created_time", "usergroups.created_time");
        mapping.put("modified_time", "usergroups.modified_time");
        return mapping;
    }
    public static Map<String, String> getGroupContactsFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("groupId", "groupcontacts.Group_ID");
        mapping.put("contactId", "groupcontacts.Contact_ID");
        mapping.put("created_time", "groupcontacts.created_time");
        mapping.put("modified_time", "groupcontacts.modified_time");
        return mapping;
    }
    public static Map<String, String> getContactEmailFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("emails", "contactemail.Email");
        mapping.put("emailId", "contactemail.Email_ID");
        mapping.put("createdTime", "contactemail.created_time");
        return mapping;
    }
    public static Map<String, String> getContactPhoneNumberFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("phoneNumbers", "contactnumber.Phone_number");
        mapping.put("numberId", "contactnumber.Number_ID");
        mapping.put("createdTime", "contactnumber.created_time");
        return mapping;
    }
}
