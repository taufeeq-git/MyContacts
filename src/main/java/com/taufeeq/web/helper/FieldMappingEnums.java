package com.taufeeq.web.helper;

import java.util.HashMap;
import java.util.Map;

public class FieldMappingEnums {

    public enum UserDetailsFields {
        USER_ID("userId", "userdetails.User_ID"),
        USERNAME("username", "userdetails.Username"),
        PASSWORD("password", "userdetails.Password"),
        GENDER("gender", "userdetails.Gender"),
        BIRTHDAY("birthday", "userdetails.Birthday"),
        LOCATION("location", "userdetails.Location"),
        CREATED_TIME("created_time", "userdetails.created_time"),
        MODIFIED_TIME("modified_time", "userdetails.modified_time"),
        PHONE_NUMBER_PHONENUMBERS("PhoneNumber.phoneNumbers", "phonenumbers.Phone_number"), 
        PHONE_NUMBER_CREATED_TIME("PhoneNumber.createdTime", "phonenumbers.created_time"), 
        EMAIL_EMAILS("Email.emails", "mails.Mail"), 
        EMAIL_CREATED_TIME("Email.createdTime", "mails.created_time"); 

        private final String pojoField;
        private final String dbColumn;

        UserDetailsFields(String pojoField, String dbColumn) {
            this.pojoField = pojoField;
            this.dbColumn = dbColumn;
        }

        public String pojoField() {
            return pojoField;
        }

        public String dbColumn() {
            return dbColumn;
        }
    }

    public enum ContactDetailsFields {
        USER_ID("userId", "contactdetails.User_ID"),
        CONTACT_ID("contactId", "contactdetails.Contact_ID"),
        USERNAME("username", "contactdetails.Name"),
        GENDER("gender", "contactdetails.gender"),
        BIRTHDAY("birthday", "contactdetails.birthday"),
        FAVORITE("favorite", "contactdetails.favorite"),
        CREATED_TIME("ct", "contactdetails.created_time"),
        MODIFIED_TIME("modified_time", "contactdetails.modified_time"),
        ARCHIVE("archive", "contactdetails.archive");

        private final String pojoField;
        private final String dbColumn;

        ContactDetailsFields(String pojoField, String dbColumn) {
            this.pojoField = pojoField;
            this.dbColumn = dbColumn;
        }

        public String pojoField() {
            return pojoField;
        }

        public String dbColumn() {
            return dbColumn;
        }
    }


    public enum UserGroupsFields {
        GROUP_ID("groupId", "usergroups.Group_ID"),
        USER_ID("userId", "usergroups.User_ID"),
        GROUP_NAME("groupName", "usergroups.Group_Name"),
        CREATED_TIME("created_time", "usergroups.created_time"),
        MODIFIED_TIME("modified_time", "usergroups.modified_time");

        private final String pojoField;
        private final String dbColumn;

        UserGroupsFields(String pojoField, String dbColumn) {
            this.pojoField = pojoField;
            this.dbColumn = dbColumn;
        }

        public String pojoField() {
            return pojoField;
        }

        public String dbColumn() {
            return dbColumn;
        }
    }

    public enum GroupContactsFields {
        GROUP_ID("groupId", "groupcontacts.Group_ID"),
        CONTACT_ID("contactId", "groupcontacts.Contact_ID"),
        CREATED_TIME("created_time", "groupcontacts.created_time"),
        MODIFIED_TIME("modified_time", "groupcontacts.modified_time");

        private final String pojoField;
        private final String dbColumn;

        GroupContactsFields(String pojoField, String dbColumn) {
            this.pojoField = pojoField;
            this.dbColumn = dbColumn;
        }

        public String pojoField() {
            return pojoField;
        }

        public String dbColumn() {
            return dbColumn;
        }
    }
    
    public enum OAuthTokensFields {
        ID("id", "oauth_tokens.id"),
        USER_ID("userId", "oauth_tokens.user_id"),
        UNIQUE_ID("uniqueId", "oauth_tokens.uniqueId"),
        EMAIL("email", "oauth_tokens.email"),
        ACCESS_TOKEN("accessToken", "oauth_tokens.access_token"),
        REFRESH_TOKEN("refreshToken", "oauth_tokens.refresh_token"),
        CREATED_TIME("createdTime", "oauth_tokens.created_time"), 
        MODIFIED_TIME("modifiedTime", "oauth_tokens.modified_time"), 
        PROVIDER("provider", "oauth_tokens.provider"),
        SYNC_INTERVAL("syncInterval", "oauth_tokens.sync_interval"),
        LAST_SYNC_TIME("lastSyncTime", "oauth_tokens.last_sync_time");

        private final String pojoField;
        private final String dbColumn;

        OAuthTokensFields(String pojoField, String dbColumn) {
            this.pojoField = pojoField;
            this.dbColumn = dbColumn;
        }

        public String pojoField() {
            return pojoField;
        }

        public String dbColumn() {
            return dbColumn;
        }
    }

    public enum ServersFields {
        SERVER_ID("serverId", "servers.server_id"),
        IP_ADDRESS("ipAddress", "servers.ip_address"),
        PORT_NUMBER("portNumber", "servers.port_number"),
        CREATED_TIME("createdTime", "servers.created_time"); 

        private final String pojoField;
        private final String dbColumn;

        ServersFields(String pojoField, String dbColumn) {
            this.pojoField = pojoField;
            this.dbColumn = dbColumn;
        }

        public String pojoField() {
            return pojoField;
        }

        public String dbColumn() {
            return dbColumn;
        }
    }


    public static Map<String, String> getUserDetailsFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        for (UserDetailsFields field : UserDetailsFields.values()) {
            mapping.put(field.pojoField(), field.dbColumn());
        }
        return mapping;
    }

    public static Map<String, String> getContactDetailsFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        for (ContactDetailsFields field : ContactDetailsFields.values()) {
            mapping.put(field.pojoField(), field.dbColumn());
        }
        return mapping;
    }


    public static Map<String, String> getUserGroupsFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        for (UserGroupsFields field : UserGroupsFields.values()) {
            mapping.put(field.pojoField(), field.dbColumn());
        }
        return mapping;
    }

    public static Map<String, String> getGroupContactsFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        for (GroupContactsFields field : GroupContactsFields.values()) {
            mapping.put(field.pojoField(), field.dbColumn());
        }
        return mapping;
    }

    public static Map<String, String> getOAuthTokensFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        for (OAuthTokensFields field : OAuthTokensFields.values()) {
            mapping.put(field.pojoField(), field.dbColumn());
        }
        return mapping;
    }

    public static Map<String, String> getServersFieldMapping() {
        Map<String, String> mapping = new HashMap<>();
        for (ServersFields field : ServersFields.values()) {
            mapping.put(field.pojoField(), field.dbColumn());
        }
        return mapping;
    }
}