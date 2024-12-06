package com.taufeeq.web.dao;

import com.taufeeq.web.enums.Enum.Table;
import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.model.Contact;
import com.taufeeq.web.query.QueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContactDAOImpl implements ContactDAO{
	QueryBuilder queryBuilder;

	@Override
	public int addContact(Contact contact) {
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    
	    int contactId = queryBuilder
	        .insert(Table.contactdetails, contactdetails.User_ID, contactdetails.Name, 
	                contactdetails.gender, contactdetails.birthday, 
	                contactdetails.favorite, contactdetails.archive, 
	                contactdetails.created_time)
	        .values(contact.getUserId(), contact.getUsername(), contact.getGender(), 
	                contact.getBirthday(), contact.getFavorite(), 
	                contact.getArchive(), contact.getCt())
	        .executeInsert();

	    return contactId;
	}

	@Override
	public void addContactEmail(int contactId, String email) {
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    
	    queryBuilder
	        .insert(Table.contactemail, contactemail.Contact_ID, contactemail.Email)
	        .values(contactId, email)
	        .executeInsert();
	}


	@Override
	public void addContactPhoneNumber(int contactId, String phoneNumber) {
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    
	    queryBuilder
	        .insert(Table.contactnumber, contactnumber.Contact_ID, contactnumber.Phone_number)
	        .values(contactId, phoneNumber)
	        .executeInsert();
	}

	@Override
	public List<Contact> getContactsByUserId(int userId) {
	    // Assuming field mapping for Contact class is provided in fieldMapper
	    Map<String, String> contactFieldMapping = fieldMapper.getContactFieldMapping(); 
	    
	    // Initialize the QueryBuilder
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    
	    // Execute the query to get the contacts by user ID
	    List<Contact> contacts = queryBuilder.select(
	            contactdetails.Contact_ID, 
	            contactdetails.Name, 
	            contactdetails.gender, 
	            contactdetails.birthday, 
	            contactdetails.favorite, 
	            contactdetails.archive, 
	            contactdetails.created_time
	        )
	        .from(Table.contactdetails)
	        .where(contactdetails.User_ID, userId)
	        .executeSelect(Contact.class, contactFieldMapping);
//	   for(Contact c:contacts)
//	    System.out.println( ReflectionUtils.objectToString(c));
	    
	    
	    return contacts;
	}



	public Contact getContactByContactId(int contactId, String format) {
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    Map<String, String> contactFieldMapping = fieldMapper.getContactFieldMapping(); 

	    // Fetch the main contact details
	    List<Contact> contacts = queryBuilder.select(
	            contactdetails.Contact_ID,
	            contactdetails.Name,
	            contactdetails.gender,
	            contactdetails.birthday,
	            contactdetails.favorite,
	            contactdetails.archive,
	            contactdetails.created_time
	        )
	        .from(Table.contactdetails)
	        .where(contactdetails.Contact_ID, contactId)
	        .executeSelect(Contact.class, contactFieldMapping);
	    
	    if (!contacts.isEmpty()) {
	        Contact contact = contacts.get(0);

	        // Fetch the emails
	        List<String> emails = queryBuilder.select(contactemail.Email)
	                                          .from(Table.contactemail)
	                                          .where(contactemail.Contact_ID, contactId)
	                                          .executeSelect(String.class, null);
	        contact.setEmails(emails);

	        // Fetch the phone numbers
	        List<String> phoneNumbers = queryBuilder.select(contactnumber.Phone_number)
	                                                .from(Table.contactnumber)
	                                                .where(contactnumber.Contact_ID, contactId)
	                                                .executeSelect(String.class, null);
	        contact.setPhoneNumbers(phoneNumbers);

	        // Format the created time
	        long createdTimeEpoch = contact.getCt(); // Assuming `ct` is stored as epoch time
	        try {
	            SimpleDateFormat formatter = new SimpleDateFormat(format); // Use the provided format
	            String formattedCreatedTime = formatter.format(new Date(createdTimeEpoch * 1000)); // Convert epoch to milliseconds
	            contact.setFormattedCreatedTime(formattedCreatedTime);
	        } catch (IllegalArgumentException e) {
	            System.err.println("Invalid date format: " + format);
	        }

	        return contact;
	    }

	    return null;
	}

  

    
    @Override
    public void deleteContactById(int contactId) {
        queryBuilder = QueryBuilderFactory.getQueryBuilder();

        try {
            queryBuilder.beginTransaction();

            queryBuilder.delete(Table.groupcontacts)
                .where(groupcontacts.Contact_ID, contactId)
                .executeUpdate();


            queryBuilder.delete(Table.contactemail)
                .where(contactemail.Contact_ID, contactId)
                .executeUpdate();

            queryBuilder.delete(Table.contactnumber)
                .where(contactnumber.Contact_ID, contactId)
                .executeUpdate();


            queryBuilder.delete(Table.contactdetails)
                .where(contactdetails.Contact_ID, contactId)
                .executeUpdate();

            queryBuilder.commitTransaction();
        } catch (Exception e) {
            queryBuilder.rollbackTransaction();
            e.printStackTrace();
        }
    }


    @Override
    public List<Contact> getContactByUserId(int userId) {
        queryBuilder = QueryBuilderFactory.getQueryBuilder();
        Map<String, String> contactFieldMapping = fieldMapper.getContactFieldMapping(); 

        List<Contact> contactResults = queryBuilder
            .select(contactdetails.Contact_ID, contactdetails.Name)
            .from(Table.contactdetails)
            .where(contactdetails.User_ID, userId)
            .executeSelect(Contact.class,contactFieldMapping);

      if(contactResults.isEmpty()) return null;
      else return contactResults;

    }





	


}
