package com.taufeeq.web.dao;

import java.util.*;

public class fieldMapper {
	
    public static Map<String, String> getUserFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("User_ID", "userId");
        mapping.put("Username", "username");
        mapping.put("Password", "password");
        mapping.put("Gender", "gender");
        mapping.put("Birthday", "birthday");
        mapping.put("Location", "location");
        mapping.put("Mail", "emails");
        mapping.put("Phone_number", "phoneNumbers");
        return mapping;
    }
    public static Map<String, String> getContactFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("User_ID ", "userId");
        mapping.put("Contact_ID", "contactId");
        mapping.put("Name", "username");
        mapping.put("gender", "gender");
        mapping.put("birthday", "birthday");
        mapping.put("favorite", "favorite");
        mapping.put("created_time", "ct");
        mapping.put("archive", "archive");
        return mapping;
    }
    public static Map<String, String> getGroupFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("Group_ID", "groupId");
        mapping.put("User_ID", "userId");
        mapping.put("Group_Name", "groupName");
        return mapping;
    }
}
