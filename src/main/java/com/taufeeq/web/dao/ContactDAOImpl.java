package com.taufeeq.web.dao;

import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.helper.FieldMapperHelper;
import com.taufeeq.web.model.Contact;
import com.taufeeq.web.querybuilder.QueryBuilder;
import com.taufeeq.web.querybuilder.QueryBuilderFactory;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ContactDAOImpl implements ContactDAO {
	QueryBuilder queryBuilder;

	@Override
	public int addContact(Contact contact) {
		long time = System.currentTimeMillis() / 1000;

		queryBuilder = QueryBuilderFactory.getQueryBuilder();

		int contactId = 0;
		try {
			contactId = queryBuilder
					.insert(Table.contactdetails, contactdetails.User_ID, contactdetails.Name, contactdetails.gender,
							contactdetails.birthday, contactdetails.favorite, contactdetails.archive,
							contactdetails.created_time, contactdetails.modified_time)
					.values(contact.getUserId(), contact.getUsername(), contact.getGender(), contact.getBirthday(),
							contact.getFavorite(), contact.getArchive(), time, time)
					.executeInsert();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return contactId;
	}

	@Override
	public void addContactEmail(int contactId, String email) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();

		try {
			queryBuilder.insert(Table.contactemail, contactemail.Contact_ID, contactemail.Email, contactemail.created_time)
					.values(contactId, email, time).executeInsert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addContactPhoneNumber(int contactId, String phoneNumber) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();

		try {
			queryBuilder.insert(Table.contactnumber, contactnumber.Contact_ID, contactnumber.Phone_number,
					contactnumber.created_time).values(contactId, phoneNumber, time).executeInsert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Contact> getContactsByUserId(int userId) {

		Map<String, String> contactFieldMapping = FieldMapperHelper.getFieldMapping(Table.contactdetails);

		queryBuilder = QueryBuilderFactory.getQueryBuilder();

		List<Contact> contacts = null;
		try {
			contacts = queryBuilder
					.select(contactdetails.Contact_ID, contactdetails.Name, contactdetails.gender, contactdetails.birthday,
							contactdetails.favorite, contactdetails.archive, contactdetails.created_time,
							contactdetails.modified_time)
					.from(Table.contactdetails).where(contactdetails.User_ID, EnumComparator.EQUAL, userId)
					.orderBy(contactdetails.Name, EnumOrder.ASC).executeSelect(Contact.class, contactFieldMapping);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}

		return contacts;
	}

	public Contact getContactByContactId(int contactId, String format) {

		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		Map<String, String> contactFieldMapping = FieldMapperHelper.getFieldMapping(Table.contactdetails);

		List<Contact> contacts = null;
		try {
			contacts = queryBuilder
					.select(contactdetails.Contact_ID, contactdetails.Name, contactdetails.gender, contactdetails.birthday,
							contactdetails.favorite, contactdetails.archive, contactdetails.created_time,
							contactdetails.modified_time)
					.from(Table.contactdetails).where(contactdetails.Contact_ID, EnumComparator.EQUAL, contactId)
					.executeSelect(Contact.class, contactFieldMapping);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}

		if (!contacts.isEmpty()) {
			Contact contact = contacts.get(0);

			List<String> emails = null;
			try {
				emails = queryBuilder.select(contactemail.Email).from(Table.contactemail)
						.where(contactemail.Contact_ID, EnumComparator.EQUAL, contactId).executeSelect(String.class, null);
			} catch (SQLException | ReflectiveOperationException e) {
				e.printStackTrace();
			}
			contact.setEmails(emails);

			List<String> phoneNumbers = null;
			try {
				phoneNumbers = queryBuilder.select(contactnumber.Phone_number).from(Table.contactnumber)
						.where(contactnumber.Contact_ID, EnumComparator.EQUAL, contactId).executeSelect(String.class, null);
			} catch (SQLException | ReflectiveOperationException e) {
				e.printStackTrace();
			}
			contact.setPhoneNumbers(phoneNumbers);

			long createdTimeEpoch = contact.getCt();
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(ZoneId.systemDefault());
				String formattedCreatedTime = formatter.format(Instant.ofEpochSecond(createdTimeEpoch));
				contact.setFormattedCreatedTime(formattedCreatedTime);
			} catch (IllegalArgumentException e) {
				System.err.println("Invalid date format: " + format);
			}

			return contact;
		}

		return null;
	}

	@Override
	public boolean deleteContactById(int contactId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();

		int res = 0;

		try {
			queryBuilder.beginTransaction();

			res += queryBuilder.delete(Table.groupcontacts)
					.where(groupcontacts.Contact_ID, EnumComparator.EQUAL, contactId).executeDelete();

			res += queryBuilder.delete(Table.contactemail)
					.where(contactemail.Contact_ID, EnumComparator.EQUAL, contactId).executeDelete();

			res += queryBuilder.delete(Table.contactnumber)
					.where(contactnumber.Contact_ID, EnumComparator.EQUAL, contactId).executeDelete();

			res += queryBuilder.delete(Table.contactdetails)
					.where(contactdetails.Contact_ID, EnumComparator.EQUAL, contactId).executeDelete();

			queryBuilder.commitTransaction();
		} catch (Exception e) {
			try {
				queryBuilder.rollbackTransaction();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		if (res == 4)
			return true;
		else
			return false;

	}

	@Override
	public List<Contact> getContactByUserId(int userId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		Map<String, String> contactFieldMapping = FieldMapperHelper.getFieldMapping(Table.contactdetails);

		List<Contact> contactResults = null;
		try {
			contactResults = queryBuilder.select(contactdetails.Contact_ID, contactdetails.Name)
					.from(Table.contactdetails).where(contactdetails.User_ID, EnumComparator.EQUAL, userId)
					.executeSelect(Contact.class, contactFieldMapping);
		} catch (SQLException | ReflectiveOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (contactResults.isEmpty())
			return null;
		else
			return contactResults;

	}

	@Override
	public boolean updateContact(int contactId, String newUsername, String newGender, String newBirthday,
			boolean newFavorite, boolean newArchive) {
		long mt = System.currentTimeMillis() / 1000;

		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		int res = 0;
		try {
			res = queryBuilder.update(Table.contactdetails).set(contactdetails.Name, newUsername)
					.set(contactdetails.gender, newGender).set(contactdetails.birthday, newBirthday)
					.set(contactdetails.favorite, newFavorite).set(contactdetails.archive, newArchive)
					.set(contactdetails.modified_time, mt).where(contactdetails.Contact_ID, EnumComparator.EQUAL, contactId)
					.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (res == -1)
			return false;
		else
			return true;

	}

	@Override
	public boolean isContactInId(int userId, int contactId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> res = null;
		try {
			res = queryBuilder.select(contactdetails.Contact_ID).from(Table.contactdetails)
					.where(contactdetails.Contact_ID, EnumComparator.EQUAL, contactId)
					.conjunction(EnumConjunction.AND, contactdetails.User_ID, EnumComparator.EQUAL, userId)
					.executeSelect(Integer.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		if (res.isEmpty())
			return false;
		else
			return true;

	}

	@Override
	public boolean doesContactExist(int userId, String number) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> res = null;
		try {
			res = queryBuilder.select(contactdetails.Contact_ID).from(Table.contactdetails)
					.join(EnumJoin.INNER_JOIN, Table.contactnumber, contactdetails.Contact_ID, Table.contactdetails,
							contactnumber.Contact_ID)
					.where(contactdetails.User_ID, EnumComparator.EQUAL, userId)
					.conjunction(EnumConjunction.AND, contactnumber.Phone_number, EnumComparator.EQUAL, number)
					.executeSelect(Integer.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return res.isEmpty() ? false : true;
	}

	public void deleteContactEmail(int contactId, String email) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.delete(Table.contactemail).where(contactemail.Contact_ID, EnumComparator.EQUAL, contactId)
					.conjunction(EnumConjunction.AND, contactemail.Email, EnumComparator.EQUAL, email).executeDelete();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void deleteContactPhoneNumber(int contactId, String phoneNumber) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.delete(Table.contactnumber).where(contactnumber.Contact_ID, EnumComparator.EQUAL, contactId)
					.conjunction(EnumConjunction.AND, contactnumber.Phone_number, EnumComparator.EQUAL, phoneNumber)
					.executeDelete();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean doesEmailExist(int contactId, String newEmail) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<String> res = null;
		try {
			res = queryBuilder.select(contactemail.Email).from(Table.contactemail)
					.where(contactemail.Contact_ID, EnumComparator.EQUAL, contactId)
					.conjunction(EnumConjunction.AND, contactemail.Email, EnumComparator.EQUAL, newEmail)
					.executeSelect(String.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return res.isEmpty() ? false : true;
	}

	@Override
	public boolean doesNumberExist(int contactId, String newPhoneNumber) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<String> res = null;
		try {
			res = queryBuilder.select(contactnumber.Phone_number).from(Table.contactnumber)
					.where(contactnumber.Contact_ID, EnumComparator.EQUAL, contactId)
					.conjunction(EnumConjunction.AND, contactnumber.Phone_number, EnumComparator.EQUAL, newPhoneNumber)
					.executeSelect(String.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return res.isEmpty() ? false : true;
	}

}
