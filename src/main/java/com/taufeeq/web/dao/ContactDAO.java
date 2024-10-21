package com.taufeeq.web.dao;

import java.util.List;

import com.taufeeq.web.model.Contact;

public interface ContactDAO {
	int addContact(Contact contact);
	void addContactEmail(int contactId, String email);
	void addContactPhoneNumber(int contactId,String number);
	List<Contact> getContactsByUserId(int userId);
	Contact getContactByContactId(int ContactId, String format);
	void deleteContactById(int contactId);
	List<Contact> getContactByUserId(int userId);
	
	
	
}
