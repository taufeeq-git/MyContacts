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
import java.util.stream.Collectors;

public class GroupDAOImpl implements GroupDAO {

    QueryBuilder queryBuilder;

    @Override
    public void addGroup(Group group) {
    	queryBuilder=QueryBuilderFactory.getQueryBuilder();
    	queryBuilder.insert(Table.usergroups, usergroups.User_ID,usergroups.Group_Name)
    	.values(group.getUserId(),group.getGroupName())
    	.executeInsert();
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
        Map<String, String> groupFieldMapping = fieldMapper.getGroupFieldMapping(); 

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
        Map<String, String> contactFieldMapping = fieldMapper.getContactFieldMapping(); 


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
        queryBuilder = QueryBuilderFactory.getQueryBuilder();
        queryBuilder.insert(Table.groupcontacts,groupcontacts.Group_ID, groupcontacts.Contact_ID)
                .values(groupId, contactId)
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
            .executeUpdate();

        return rowsAffected > 0;
    }

}


