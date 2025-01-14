package com.taufeeq.web.dao;

import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.helpermap.FieldMapper;
import com.taufeeq.web.model.Contact;
import com.taufeeq.web.querybuilder.QueryBuilder;
import com.taufeeq.web.querybuilder.QueryBuilderFactory;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class ContactDAOImpl implements ContactDAO{
	QueryBuilder queryBuilder;

	@Override
	public int addContact(Contact contact) {
		long ct=System.currentTimeMillis()/1000;
	
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    
	    int contactId = queryBuilder
	        .insert(Table.contactdetails, contactdetails.User_ID, contactdetails.Name, 
	                contactdetails.gender, contactdetails.birthday, 
	                contactdetails.favorite, contactdetails.archive, 
	                contactdetails.created_time,contactdetails.modified_time)
	        .values(contact.getUserId(), contact.getUsername(), contact.getGender(), 
	                contact.getBirthday(), contact.getFavorite(), 
	                contact.getArchive(), ct,ct)
	        .executeInsert();

	    return contactId;
	}

	@Override
	public void addContactEmail(int contactId, String email) {
		long ct=System.currentTimeMillis()/1000;
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    
	    queryBuilder
	        .insert(Table.contactemail, contactemail.Contact_ID, contactemail.Email,contactemail.created_time)
	        .values(contactId, email,ct)
	        .executeInsert();
	}


	@Override
	public void addContactPhoneNumber(int contactId, String phoneNumber) {
		long ct=System.currentTimeMillis()/1000;
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    
	    queryBuilder
	        .insert(Table.contactnumber, contactnumber.Contact_ID, contactnumber.Phone_number,contactnumber.created_time)
	        .values(contactId, phoneNumber,ct)
	        .executeInsert();
	}

	@Override
	public List<Contact> getContactsByUserId(int userId) {
	 
	    Map<String, String> contactFieldMapping = FieldMapper.getContactFieldMapping(); 
	    
	  
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    
	   
	    List<Contact> contacts = queryBuilder.select(
	            contactdetails.Contact_ID, 
	            contactdetails.Name, 
	            contactdetails.gender, 
	            contactdetails.birthday, 
	            contactdetails.favorite, 
	            contactdetails.archive, 
	            contactdetails.created_time,
	            contactdetails.modified_time
	        )
	        .from(Table.contactdetails)
	        .where(contactdetails.User_ID, userId)
	        .executeSelect(Contact.class, contactFieldMapping);

	    

	    return contacts;
	}



	public Contact getContactByContactId(int contactId, String format) {
		
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    Map<String, String> contactFieldMapping = FieldMapper.getContactFieldMapping(); 


	    List<Contact> contacts = queryBuilder.select(
	            contactdetails.Contact_ID,
	            contactdetails.Name,
	            contactdetails.gender,
	            contactdetails.birthday,
	            contactdetails.favorite,
	            contactdetails.archive,
	            contactdetails.created_time,
	            contactdetails.modified_time
	        )
	        .from(Table.contactdetails)
	        .where(contactdetails.Contact_ID, contactId)
	        .executeSelect(Contact.class, contactFieldMapping);
	    
	    if (!contacts.isEmpty()) {
	        Contact contact = contacts.get(0);

	  
	        List<String> emails = queryBuilder.select(contactemail.Email)
	                                          .from(Table.contactemail)
	                                          .where(contactemail.Contact_ID, contactId)
	                                          .executeSelect(String.class, null);
	        contact.setEmails(emails);

	        List<String> phoneNumbers = queryBuilder.select(contactnumber.Phone_number)
	                                                .from(Table.contactnumber)
	                                                .where(contactnumber.Contact_ID, contactId)
	                                                .executeSelect(String.class, null);
	        contact.setPhoneNumbers(phoneNumbers);

	        long createdTimeEpoch = contact.getCt(); 
	        try {
	            SimpleDateFormat formatter = new SimpleDateFormat(format); 
	            String formattedCreatedTime = formatter.format(new Date(createdTimeEpoch * 1000)); 
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
        int res=0;

        try {
            queryBuilder.beginTransaction();

            res+=queryBuilder.delete(Table.groupcontacts)
                .where(groupcontacts.Contact_ID, contactId)
                .executeDelete();


            res+=queryBuilder.delete(Table.contactemail)
                .where(contactemail.Contact_ID, contactId)
                .executeDelete();

            res+=queryBuilder.delete(Table.contactnumber)
                .where(contactnumber.Contact_ID, contactId)
                .executeDelete();


            res+=queryBuilder.delete(Table.contactdetails)
                .where(contactdetails.Contact_ID, contactId)
                .executeDelete();

            queryBuilder.commitTransaction();
        } catch (Exception e) {
            queryBuilder.rollbackTransaction();
            e.printStackTrace();
        }
        System.out.println(res);
        if(res==4) return true;
        else return false;
        
    }


    @Override
    public List<Contact> getContactByUserId(int userId) {
        queryBuilder = QueryBuilderFactory.getQueryBuilder();
        Map<String, String> contactFieldMapping = FieldMapper.getContactFieldMapping(); 

        List<Contact> contactResults = queryBuilder
            .select(contactdetails.Contact_ID, contactdetails.Name)
            .from(Table.contactdetails)
            .where(contactdetails.User_ID, userId)
            .executeSelect(Contact.class,contactFieldMapping);

      if(contactResults.isEmpty()) return null;
      else return contactResults;

    }
    


	@Override
	public boolean updateContact(int contactId, String newUsername, String newGender, String newBirthday,
			boolean newFavorite, boolean newArchive) {
		long mt=System.currentTimeMillis()/1000;
		
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		int res=queryBuilder.update(Table.contactdetails)
		.set(contactdetails.Name,newUsername)
		.set(contactdetails.gender,newGender)
		.set(contactdetails.birthday,newBirthday)
		.set(contactdetails.favorite,newFavorite)
		.set(contactdetails.archive,newArchive)
		.set(contactdetails.modified_time, mt)
		.where(contactdetails.Contact_ID, contactId)
		.executeUpdate();
		if(res==-1) return false;
		else return true;
		
		
	}

	@Override
	public boolean isContactInId(int userId, int contactId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer>res=queryBuilder.select(contactdetails.Contact_ID)
				.from(Table.contactdetails)
				.where(contactdetails.Contact_ID,contactId)
				.and(contactdetails.User_ID, userId)
				.executeSelect(Integer.class, null);
		if(res.isEmpty()) return false;
		else return true;
		
	}





	


}
