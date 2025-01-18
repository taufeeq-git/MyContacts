package com.taufeeq.web.dao;

import java.util.List;

import com.taufeeq.web.model.Contact;

public interface ContactDAO {
	int addContact(Contact contact);

	void addContactEmail(int contactId, String email);

	void addContactPhoneNumber(int contactId, String number);

	List<Contact> getContactsByUserId(int userId);

	Contact getContactByContactId(int ContactId, String format);

	boolean deleteContactById(int contactId);

	List<Contact> getContactByUserId(int userId);

	boolean updateContact(int contactId, String newUsername, String newGender, String newBirthday, boolean newFavorite,
			boolean newArchive);

	boolean isContactInId(int userId, int contactId);

	boolean doesContactExist(int userId, String string);

	void deleteContactEmail(int contactId, String email);

	void deleteContactPhoneNumber(int contactId, String phoneNumber);

	boolean doesEmailExist(int contactId, String newEmail);

	boolean doesNumberExist(int contactId, String newPhoneNumber);
}
