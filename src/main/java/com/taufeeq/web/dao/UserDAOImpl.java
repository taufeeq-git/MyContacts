package com.taufeeq.web.dao;



import java.util.*;


import com.taufeeq.web.enums.Enum.*;
//import com.taufeeq.web.enums.Enum;
//import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.model.User;
import com.taufeeq.web.query.QueryBuilder;

public class UserDAOImpl implements UserDAO {

    private QueryBuilder queryBuilder;


    @Override
    public int addUser(User user) {
    	queryBuilder = QueryBuilderFactory.getQueryBuilder();
        return queryBuilder.insert(Table.userdetails,
        				userdetails.Password,
        				userdetails.Username,
        				userdetails.Gender,
        				userdetails.Birthday,
        				userdetails.Location)
                    .values(user.getPassword(), user.getUsername(), user.getGender(), user.getBirthday(), user.getLocation())
                    .executeInsert();
    }

   

    public int verifyUser(String email, String password) {
        queryBuilder = QueryBuilderFactory.getQueryBuilder();

        // Query for User_ID with email and password match
        List<Integer> results = queryBuilder
                .select(userdetails.User_ID)
                .from(Table.userdetails)
                .innerJoin(Table.mails, mails.User_ID, Table.userdetails, userdetails.User_ID) 
                .where(mails.Mail, email)
                .and(userdetails.Password, password) 
                .executeSelect(Integer.class, null); 

        return results.isEmpty() ? 0 : results.get(0);
    }


    @Override
	public void addUserEmail(int userId, String email) {
    	queryBuilder = QueryBuilderFactory.getQueryBuilder();
		queryBuilder.insert(Table.mails,
                mails.User_ID,
                mails.Mail)
        .values(userId, email)
        .executeInsert();
		
	}
    @Override
	public void addUserPhoneNumber(int userId, String number) {
    	queryBuilder = QueryBuilderFactory.getQueryBuilder();
//		queryBuilder.insert("phonenumbers", "User_Id","aj");
		queryBuilder.insert(Table.phonenumbers,
                phonenumbers.User_Id,
                phonenumbers.Phone_number)
        .values(userId, number)
        .executeInsert();
		
	}

	@Override
	public boolean isEmailUnique(String email) {
//		Map<String, String> userFieldMapping = fieldMapper.getUserFieldMapping(); 
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		
		List<Integer> results = queryBuilder.select(mails.User_ID)
				.from(Table.mails)
				.where(mails.Mail,email)
				.executeSelect(Integer.class,null);
//		System.out.println(results.get(0));
	   return results.isEmpty();
	}


	@Override
	public String getHashedPasswordByEmail(String email) {
		Map<String, String> userFieldMapping = fieldMapper.getUserFieldMapping(); 
		String pass="";
		queryBuilder= QueryBuilderFactory.getQueryBuilder();
		List<String> results=queryBuilder.select(userdetails.Password)
				.from(Table.userdetails)
				.innerJoin(Table.mails, mails.User_ID, Table.userdetails, userdetails.User_ID)
				.where(mails.Mail, email)
				.executeSelect(String.class,null);
		if (!results.isEmpty()) {
             pass= (String) results.get(0);
        }
		return pass;
	}
	
//	@Override
//    public User getUserById(int userId) {
//		queryBuilder = QueryBuilderFactory.getQueryBuilder();
//        Map<String, String> userFieldMapping = fieldMapper.getUserFieldMapping(); 
//        List<User> userDetails = queryBuilder
//                .select(userdetails.User_ID, userdetails.Username, userdetails.Password, 
//                        userdetails.Gender, userdetails.Birthday, userdetails.Location)
//                .from(Table.userdetails)
//                .where(userdetails.User_ID, userId)
//                .executeSelect(User.class, userFieldMapping); // Execute and return List<User>
//
//        if (userDetails.isEmpty()) {
//            return null;
//        }
//
//        User user = userDetails.get(0);
//
//        List<String> emails = queryBuilder
//                .select(mails.Mail)
//                .from(Table.mails)
//                .where(mails.User_ID, userId)
//                .executeSelect(String.class, null); 
//
//        user.setEmails(emails);
//
////
////        Map<String, String> phoneMapping = fieldMapper.getPhoneNumberFieldMapping();
//        List<String> phoneNumbers = queryBuilder
//                .select(phonenumbers.Phone_number)
//                .from(Table.phonenumbers)
//                .where(phonenumbers.User_Id, userId)
//                .executeSelect(String.class, null);
//        user.setPhoneNumbers(phoneNumbers);
//        
////        System.out.println("User details: " + ReflectionUtils.objectToString(user));
//
//        return user;
//    }
	
	
//	@Override
//	public User getUserById(int userId) {
//	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
//	    Map<String, String> userFieldMapping = fieldMapper.getUserFieldMapping();
//
//	    // Execute query and retrieve results
//	    List<User> userDetails = queryBuilder
//	    		 .select(
//	    	                userdetails.User_ID,
//	    	                userdetails.Username,
//	    	                userdetails.Password,
//	    	                userdetails.Gender,
//	    	                userdetails.Birthday,
//	    	                userdetails.Location,
//	    	                mails.Mail,
//	    	                phonenumbers.Phone_number
//	    	            )
//	    	            .leftJoin(Table.mails, mails.User_ID, Table.userdetails, userdetails.User_ID)
//	    	            .leftJoin(Table.phonenumbers, phonenumbers.User_Id, Table.userdetails, userdetails.User_ID)
//	    	            .where(userdetails.User_ID, userId)
//	    	            .executeSelect(User.class, userFieldMapping);
//
//	    if (userDetails.isEmpty()) {
//	        return null;
//	    }
//
//	    // Group emails and phone numbers by user
//	    User user = new User(); // Initialize User
//	    Set<String> emails = new HashSet<>();
//	    Set<String> phoneNumbers = new HashSet<>();
//
//	    for (User detail : userDetails) {
//	        if (user.getUserId() == 0) { // Populate only once
//	            user.setUserId(detail.getUserId());
//	            user.setUsername(detail.getUsername());
//	            user.setPassword(detail.getPassword());
//	            user.setGender(detail.getGender());
//	            user.setBirthday(detail.getBirthday());
//	            user.setLocation(detail.getLocation());
//	        }
//	        if (detail.getEmails() != null) emails.addAll(detail.getEmails());
//	        if (detail.getPhoneNumbers() != null) phoneNumbers.addAll(detail.getPhoneNumbers());
//	    }
//
//	    user.setEmails(new ArrayList<>(emails)); // Set unique emails
//	    user.setPhoneNumbers(new ArrayList<>(phoneNumbers)); // Set unique phone numbers
//
//	    return user;
//	}
	
	@Override
    public User getUserById(int userId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
        Map<String, String> userFieldMapping = fieldMapper.getUserFieldMapping(); 
        List<User> userDetails = queryBuilder
                .select(userdetails.User_ID, userdetails.Username, userdetails.Password, 
                        userdetails.Gender, userdetails.Birthday, userdetails.Location)
                .from(Table.userdetails)
                .where(userdetails.User_ID, userId)
                .executeSelect(User.class, userFieldMapping); 

        if (userDetails.isEmpty()) {
            return null;
        }

        User user = userDetails.get(0);

        List<String> emails = queryBuilder
                .select(mails.Mail)
                .from(Table.mails)
                .where(mails.User_ID, userId)
                .executeSelect(String.class, null); 

        user.setEmails(emails);

//
//        Map<String, String> phoneMapping = fieldMapper.getPhoneNumberFieldMapping();
        List<String> phoneNumbers = queryBuilder
                .select(phonenumbers.Phone_number)
                .from(Table.phonenumbers)
                .where(phonenumbers.User_Id, userId)
                .executeSelect(String.class, null);
        user.setPhoneNumbers(phoneNumbers);
        
//        System.out.println("User details: " + ReflectionUtils.objectToString(user));

        return user;
    }

	
	
	@Override
	public int getUserIdByEmail(String email) {
		int userId=0;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> result = queryBuilder.select(mails.Mail)
		.from(Table.userdetails)
		.where(mails.Mail,email)
		.executeSelect(Integer.class,null);
		if (!result.isEmpty()) {
	        userId = (int) result.get(0);
	    }
	    return userId;
	}

	@Override
	public void updateFormat(int userId, String selectedFormat) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		queryBuilder.update(Table.userdetails)
		.set(userdetails.dtformat,selectedFormat)
		.where(userdetails.User_ID,userId)
		.executeUpdate();	
	}

	@Override
	public String getFormat(int userId) {
	    queryBuilder = QueryBuilderFactory.getQueryBuilder();
	    String format = "";

	    
	    List<String> result = queryBuilder
	        .select(userdetails.dtformat)                       
	        .from(Table.userdetails)                 
	        .where(userdetails.User_ID, userId)       
	        .executeSelect(String.class,null);                       

	    if (!result.isEmpty()) {
	        format = (String) result.get(0);
	    }

	    return format;
	}


}
