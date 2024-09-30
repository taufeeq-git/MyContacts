package com.taufeeq.web.dao;

import java.util.List;

import com.taufeeq.web.model.Contact;
import com.taufeeq.web.model.Group;

public interface GroupDAO {
    void addGroup(Group group);
    List<String> getUserGroups(int userId);
    List<Group> getUserGroupsWithIds(int userId); 
    List<Contact> getContactsInGroup(int groupId);
    void addContactToGroup(int groupId, int contactId);
    String getGroupNameById(int groupId);
    boolean deleteContactFromGroup(int contactId, int groupId);
    
    
}
