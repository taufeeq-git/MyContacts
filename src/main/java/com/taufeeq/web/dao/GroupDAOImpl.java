package com.taufeeq.web.dao;

import com.taufeeq.web.enums.Enum.EnumComparator;
import com.taufeeq.web.enums.Enum.EnumConjunction;
import com.taufeeq.web.enums.Enum.EnumJoin;
import com.taufeeq.web.enums.Enum.EnumOrder;
import com.taufeeq.web.enums.Enum.Table;
import com.taufeeq.web.enums.Enum.contactdetails;
import com.taufeeq.web.enums.Enum.groupcontacts;
import com.taufeeq.web.enums.Enum.usergroups;
import com.taufeeq.web.helper.FieldMapperHelper;
import com.taufeeq.web.model.Contact;
import com.taufeeq.web.model.Group;
import com.taufeeq.web.querybuilder.QueryBuilder;
import com.taufeeq.web.querybuilder.QueryBuilderFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GroupDAOImpl implements GroupDAO {

	QueryBuilder queryBuilder;

	@Override
	public void addGroup(Group group) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder
					.insert(Table.usergroups, usergroups.User_ID, usergroups.Group_Name, usergroups.created_time,
							usergroups.modified_time)
					.values(group.getUserId(), group.getGroupName(), time, time).executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isGroupInId(int userId, int groupId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<Integer> res = queryBuilder.select(usergroups.User_ID).from(Table.usergroups)
					.where(usergroups.User_ID, EnumComparator.EQUAL, userId)
					.conjunction(EnumConjunction.AND, usergroups.Group_ID, EnumComparator.EQUAL, groupId)
					.executeSelect(Integer.class, null);

			if (res.isEmpty())
				return false;
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false; 
		}
	}

	@Override
	public List<String> getUserGroups(int userId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<String> groupResults = queryBuilder.select(usergroups.Group_Name).from(Table.usergroups)
					.where(usergroups.User_ID, EnumComparator.EQUAL, userId).orderBy(usergroups.Group_Name, EnumOrder.ASC)
					.executeSelect(String.class, null);

			return groupResults;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList(); 
		}
	}

	@Override
	public List<Group> getUserGroupsWithIds(int userId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		Map<String, String> groupFieldMapping = FieldMapperHelper.getFieldMapping(Table.usergroups);
		try {
			List<Group> groups = queryBuilder.select(usergroups.Group_ID, usergroups.Group_Name).from(Table.usergroups)
					.where(usergroups.User_ID, EnumComparator.EQUAL, userId).orderBy(usergroups.Group_Name, EnumOrder.ASC)
					.executeSelect(Group.class, groupFieldMapping);

			return groups;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList(); 
		}
	}

	@Override
	public List<Contact> getContactsInGroup(int groupId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		Map<String, String> contactFieldMapping = FieldMapperHelper.getFieldMapping(Table.contactdetails);
		try {
			List<Contact> contacts = queryBuilder.select(contactdetails.Contact_ID, contactdetails.Name)
					.from(Table.contactdetails)
					.join(EnumJoin.INNER_JOIN, Table.groupcontacts, groupcontacts.Contact_ID, Table.contactdetails,
							contactdetails.Contact_ID)
					.where(groupcontacts.Group_ID, EnumComparator.EQUAL, groupId)
					.executeSelect(Contact.class, contactFieldMapping);

			return contacts;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public void addContactToGroup(int groupId, int contactId) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder
					.insert(Table.groupcontacts, groupcontacts.Group_ID, groupcontacts.Contact_ID,
							groupcontacts.created_time, groupcontacts.modified_time)
					.values(groupId, contactId, time, time).executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getGroupNameById(int groupId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<String> groupResult = queryBuilder.select(usergroups.Group_Name).from(Table.usergroups)
					.where(usergroups.Group_ID, EnumComparator.EQUAL, groupId).executeSelect(String.class, null);

			if (groupResult.isEmpty()) {
				return null;
			}
			return (String) groupResult.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null; 
		}
	}

	@Override
	public boolean deleteContactFromGroup(int contactId, int groupId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			int rowsAffected = queryBuilder.delete(Table.groupcontacts)
					.where(groupcontacts.Contact_ID, EnumComparator.EQUAL, contactId)
					.conjunction(EnumConjunction.AND, groupcontacts.Group_ID, EnumComparator.EQUAL, groupId)
					.executeDelete();

			return rowsAffected > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false; 
		}
	}
}