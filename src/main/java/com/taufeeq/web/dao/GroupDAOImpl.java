package com.taufeeq.web.dao;

import com.taufeeq.web.enums.Enum.Table;
import com.taufeeq.web.enums.Enum.contactdetails;
import com.taufeeq.web.enums.Enum.groupcontacts;
import com.taufeeq.web.enums.Enum.usergroups;
import com.taufeeq.web.model.Contact;
import com.taufeeq.web.model.Group;
import com.taufeeq.web.query.QueryBuilder;
import java.util.List;
import java.util.Map;


public class GroupDAOImpl implements GroupDAO {

    QueryBuilder queryBuilder;

    @Override
    public void addGroup(Group group) {
    	long ct=System.currentTimeMillis()/1000;
    	queryBuilder=QueryBuilderFactory.getQueryBuilder();
    	queryBuilder.insert(Table.usergroups, usergroups.User_ID,usergroups.Group_Name,usergroups.created_time,usergroups.modified_time)
    	.values(group.getUserId(),group.getGroupName(),ct,ct)
    	.executeInsert();
    }
    
    public boolean isGroupInId(int userId,int groupId) {
    	queryBuilder=QueryBuilderFactory.getQueryBuilder();
    	List<Integer> res=queryBuilder.select(usergroups.User_ID)
    	.from(Table.usergroups)
    	.where(usergroups.User_ID, userId)
    	.and(usergroups.Group_ID, groupId)
    	.executeSelect(Integer.class, null);
    	
    
		if(res.isEmpty()) 
			return false;

		
		else return true;
		
    }
    
    @Override
    public List<String> getUserGroups(int userId) {
        queryBuilder = QueryBuilderFactory.getQueryBuilder();

        List<String> groupResults = queryBuilder
            .select(usergroups.Group_Name)
            .from(Table.usergroups)
            .where(usergroups.User_ID, userId)
            .executeSelect(String.class,null);


        return groupResults;
    }

    @Override
    public List<Group> getUserGroupsWithIds(int userId) {
        queryBuilder = QueryBuilderFactory.getQueryBuilder();
        Map<String, String> groupFieldMapping = FieldMapper.getGroupFieldMapping(); 

        List<Group> groups = queryBuilder
            .select(usergroups.Group_ID, usergroups.Group_Name)
            .from(Table.usergroups)
            .where(usergroups.User_ID, userId)
            .executeSelect(Group.class,groupFieldMapping);

        return groups;
	    }

    @Override
    public List<Contact> getContactsInGroup(int groupId) {
        queryBuilder = QueryBuilderFactory.getQueryBuilder();
        Map<String, String> contactFieldMapping = FieldMapper.getContactFieldMapping(); 


        List<Contact> contacts = queryBuilder
            .select(contactdetails.Contact_ID, contactdetails.Name)
            .from(Table.contactdetails)
            .innerJoin(Table.groupcontacts, groupcontacts.Contact_ID,Table.contactdetails,contactdetails.Contact_ID)
            .where(groupcontacts.Group_ID, groupId)
            .executeSelect(Contact.class,contactFieldMapping);


        return contacts;
    }

    @Override
    public void addContactToGroup(int groupId, int contactId) {
    	long ct=System.currentTimeMillis()/1000;
        queryBuilder = QueryBuilderFactory.getQueryBuilder();
        queryBuilder.insert(Table.groupcontacts,groupcontacts.Group_ID, groupcontacts.Contact_ID,groupcontacts.created_time,groupcontacts.modified_time)
                .values(groupId, contactId, ct, ct)
                .executeInsert();
    }

    @Override
    public String getGroupNameById(int groupId) {
        queryBuilder = QueryBuilderFactory.getQueryBuilder();

        List<String> groupResult = queryBuilder
            .select(usergroups.Group_Name)
            .from(Table.usergroups)
            .where(usergroups.Group_ID, groupId)
            .executeSelect(String.class,null);

        if (groupResult.isEmpty()) {
            return null;
        }

        return (String) groupResult.get(0);
    }
    
    @Override
    public boolean deleteContactFromGroup(int contactId, int groupId) {
        queryBuilder = QueryBuilderFactory.getQueryBuilder();

        int rowsAffected = queryBuilder
            .delete(Table.groupcontacts)
            .where(groupcontacts.Contact_ID, contactId)
            .and(groupcontacts.Group_ID, groupId)
            .executeDelete();

        return rowsAffected > 0;
    }

}


