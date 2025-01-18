package com.taufeeq.web.helper;

import java.util.Map;

import com.taufeeq.web.enums.Enum.Table;

public class FieldMapperHelper {
    public static Map<String, String> getFieldMapping(Table tableName) {
        Map<String, String> fieldMapping;
        switch (tableName) {
	    case userdetails:
	        fieldMapping = FieldMappingEnums.getUserDetailsFieldMapping();
	        break;
	    case contactdetails:
	        fieldMapping = FieldMappingEnums.getContactDetailsFieldMapping();
	        break;
	    case usergroups:
	        fieldMapping = FieldMappingEnums.getUserGroupsFieldMapping();
	        break;
	    case groupcontacts:
	        fieldMapping = FieldMappingEnums.getGroupContactsFieldMapping();
	        break;
	    case oauth_tokens:
	    	fieldMapping = FieldMappingEnums.getOAuthTokensFieldMapping();
	    	break;
	    case servers:
	    	fieldMapping = FieldMappingEnums.getServersFieldMapping();
	    	break;

	    default:
	        fieldMapping=null;
	}
        return fieldMapping;
    }
}