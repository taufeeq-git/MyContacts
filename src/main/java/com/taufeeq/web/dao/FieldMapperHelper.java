package com.taufeeq.web.dao;

import java.util.*;

import com.taufeeq.web.enums.Enum.Table;

public class FieldMapperHelper {
    public static Map<String, String> getFieldMapping(Table tableName) {
        Map<String, String> fieldMapping;
        switch (tableName) {
	    case userdetails:
	        fieldMapping = FieldMapper.getUserFieldMapping();
	        break;
	    case contactdetails:
	        fieldMapping = FieldMapper.getContactFieldMapping();
	        break;
	    case mails:
	        fieldMapping = FieldMapper.getEmailFieldMapping();
	        break;
	    case phonenumbers:
	        fieldMapping = FieldMapper.getPhoneNumberFieldMapping();
	        break;
	    case usergroups:
	        fieldMapping = FieldMapper.getGroupFieldMapping();
	        break;
	    case groupcontacts:
	        fieldMapping = FieldMapper.getGroupContactsFieldMapping();
	        break;
	    case contactemail:
	        fieldMapping = FieldMapper.getContactEmailFieldMapping();
	        break;
	    case contactnumber:
	        fieldMapping = FieldMapper.getContactPhoneNumberFieldMapping();
	        break;
	        
	        
	    default:
	        fieldMapping=null;
//	        System.out.println("noMap");
	}
        return fieldMapping;
    }
}

