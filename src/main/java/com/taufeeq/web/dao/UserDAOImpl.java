package com.taufeeq.web.dao;

import java.util.*;

import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.helper.FieldMapperHelper;
import com.taufeeq.web.model.User;
import com.taufeeq.web.querybuilder.QueryBuilder;
import com.taufeeq.web.querybuilder.QueryBuilderFactory;

public class UserDAOImpl implements UserDAO {

	private QueryBuilder queryBuilder;

	@Override
	public int addUser(User user) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		long time = System.currentTimeMillis() / 1000;

		try {
			return queryBuilder
					.insert(Table.userdetails, userdetails.Password, userdetails.Username, userdetails.Gender,
							userdetails.Birthday, userdetails.Location, userdetails.created_time,
							userdetails.modified_time, userdetails.login_method)
					.values(user.getPassword(), user.getUsername(), user.getGender(), user.getBirthday(),
							user.getLocation(), time, time, user.getLoginMethod())
					.executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public int verifyUser(String email, String password) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();

		try {
			List<Integer> results = queryBuilder.select(userdetails.User_ID).from(Table.userdetails)
					.join(EnumJoin.INNER_JOIN, Table.mails, mails.User_ID, Table.userdetails, userdetails.User_ID)
					.where(mails.Mail, EnumComparator.EQUAL, email)
					.conjunction(EnumConjunction.AND, userdetails.Password, EnumComparator.EQUAL, password)
					.conjunction(EnumConjunction.AND, userdetails.login_method, EnumComparator.EQUAL, "manual")
					.executeSelect(Integer.class, null);

			return results.isEmpty() ? 0 : results.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void addUserEmail(int userId, String email) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.insert(Table.mails, mails.User_ID, mails.Mail, mails.created_time).values(userId, email, time)
					.executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addUserPhoneNumber(int userId, String number) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.insert(Table.phonenumbers, phonenumbers.User_Id, phonenumbers.Phone_number,
					phonenumbers.created_time).values(userId, number, time).executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean isEmailUnique(String email) {

		queryBuilder = QueryBuilderFactory.getQueryBuilder();

		try {
			List<Integer> results = queryBuilder.select(mails.User_ID).from(Table.mails)
					.where(mails.Mail, EnumComparator.EQUAL, email).executeSelect(Integer.class, null);

			return results.isEmpty();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getHashedPasswordByEmail(String email) {
		String pass = "";
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<String> results = queryBuilder.select(userdetails.Password).from(Table.userdetails)
					.join(EnumJoin.INNER_JOIN, Table.mails, mails.User_ID, Table.userdetails, userdetails.User_ID)
					.where(mails.Mail, EnumComparator.EQUAL, email).executeSelect(String.class, null);
			if (!results.isEmpty()) {
				pass = (String) results.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pass;
	}

	@Override
	public User getUserById(int userId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(Table.userdetails);

		try {
			List<User> results = queryBuilder
					.select(userdetails.User_ID, userdetails.Username, userdetails.Password, userdetails.Gender,
							userdetails.Birthday, userdetails.Location, userdetails.created_time,
							userdetails.modified_time, mails.Mail, mails.created_time, phonenumbers.Phone_number,
							phonenumbers.created_time)
					.from(Table.userdetails)
					.join(EnumJoin.LEFT_JOIN, Table.mails, mails.User_ID, Table.userdetails, userdetails.User_ID)
					.join(EnumJoin.LEFT_JOIN, Table.phonenumbers, phonenumbers.User_Id, Table.userdetails,
							userdetails.User_ID)
					.where(userdetails.User_ID, EnumComparator.EQUAL, userId).executeSelect(User.class, fieldMapping);

			return results.isEmpty() ? null : results.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getUserIdByEmail(String email) {
		int userId = 0;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<Integer> result = queryBuilder.select(mails.User_ID).from(Table.mails)
					.where(mails.Mail, EnumComparator.EQUAL, email).executeSelect(Integer.class, null);
			if (!result.isEmpty()) {
				userId = (int) result.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId;
	}

	@Override
	public void updateFormat(int userId, String selectedFormat) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.update(Table.userdetails).set(userdetails.dtformat, selectedFormat)
					.where(userdetails.User_ID, EnumComparator.EQUAL, userId).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getFormat(int userId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		String format = "";

		try {
			List<String> result = queryBuilder.select(userdetails.dtformat).from(Table.userdetails)
					.where(userdetails.User_ID, EnumComparator.EQUAL, userId).executeSelect(String.class, null);

			if (!result.isEmpty()) {
				format = (String) result.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return format;
	}

	public void updateUserProfile(int userId, String username, String gender, String birthday, String location) {
		long currentTime = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.update(Table.userdetails).set(userdetails.Username, username).set(userdetails.Gender, gender)
					.set(userdetails.Birthday, birthday).set(userdetails.Location, location)
					.set(userdetails.modified_time, currentTime)
					.where(userdetails.User_ID, EnumComparator.EQUAL, userId).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteEmail(int userId, String email) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.delete(Table.mails).where(mails.User_ID, EnumComparator.EQUAL, userId)
					.conjunction(EnumConjunction.AND, mails.Mail, EnumComparator.EQUAL, email).executeDelete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deletePhoneNumber(int userId, String phoneNumber) {

		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.delete(Table.phonenumbers).where(phonenumbers.User_Id, EnumComparator.EQUAL, userId)
					.conjunction(EnumConjunction.AND, phonenumbers.Phone_number, EnumComparator.EQUAL, phoneNumber)
					.executeDelete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getUserIdByGoogleId(String uid) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<Integer> res = queryBuilder.select(oauth_identity.user_id).from(Table.oauth_identity)
					.where(oauth_identity.unique_identifier, EnumComparator.EQUAL, uid)
					.executeSelect(Integer.class, null);
			return res.isEmpty() ? 0 : res.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public void setUniqueId(int userId, String uniqueId, String provider) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder
					.insert(Table.oauth_identity, oauth_identity.user_id, oauth_identity.unique_identifier,
							oauth_identity.provider, oauth_identity.created_time, oauth_identity.modified_time)
					.values(userId, uniqueId, provider, time, time).executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean doesEmailExist(int userId, String newEmail) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<String> res = queryBuilder.select(mails.Mail).from(Table.mails)
					.where(mails.User_ID, EnumComparator.EQUAL, userId)
					.conjunction(EnumConjunction.AND, mails.Mail, EnumComparator.EQUAL, newEmail)
					.executeSelect(String.class, null);
			return res.isEmpty() ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean doesNumberExist(int userId, String newPhoneNumber) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<String> res = queryBuilder.select(phonenumbers.Phone_number).from(Table.phonenumbers)
					.where(phonenumbers.User_Id, EnumComparator.EQUAL, userId)
					.conjunction(EnumConjunction.AND, phonenumbers.Phone_number, EnumComparator.EQUAL, newPhoneNumber)
					.executeSelect(String.class, null);
			return res.isEmpty() ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}