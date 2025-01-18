package com.taufeeq.web.dao;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.taufeeq.web.dbcp.DatabaseConnectionPool;
import com.taufeeq.web.enums.Enum.EnumComparator;
import com.taufeeq.web.enums.Enum.Table;
import com.taufeeq.web.enums.Enum.usersessions;
import com.taufeeq.web.helper.FieldMapperHelper;
import com.taufeeq.web.model.Session;
import com.taufeeq.web.model.User;
import com.taufeeq.web.querybuilder.QueryBuilder;
import com.taufeeq.web.querybuilder.QueryBuilderFactory;

public class SessionDAOImpl implements SessionDAO {

	QueryBuilder queryBuilder;

	@Override
	public String addSession(String sessionId, int userId, Timestamp createdTime, Timestamp expirationTime) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder
					.insert(Table.usersessions, usersessions.Session_ID, usersessions.User_ID, usersessions.Created_Time,
							usersessions.Expiration_time)
					.values(sessionId, userId, createdTime, expirationTime).executeInsert();
			return sessionId;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int deleteExpiredSessions(Timestamp currentTime) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		int batchSize = 50;
		int totalDeleted = 0;
		int deletedRows;

		do {
			try {
				deletedRows = queryBuilder.delete(Table.usersessions)
						.where(usersessions.Expiration_time, EnumComparator.LESS_THAN, currentTime).limit(batchSize)
						.executeDelete();
			} catch (SQLException e) {
				e.printStackTrace();
				return totalDeleted;
			}

			totalDeleted += deletedRows;
		} while (deletedRows == batchSize);

		return totalDeleted;
	}

	@Override
	public void updateSessionExpiration(String sessionId, Timestamp newExpirationTime) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.update(Table.usersessions).set(usersessions.Expiration_time, newExpirationTime)
					.where(usersessions.Session_ID, EnumComparator.EQUAL, sessionId).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAllSessions() {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.delete(Table.usersessions).executeDelete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isValidSession(String sessionId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> result = Collections.emptyList();
		try {
			result = queryBuilder.select(usersessions.User_ID).from(Table.usersessions)
					.where(usersessions.Session_ID, EnumComparator.EQUAL, sessionId).executeSelect(Integer.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return result.isEmpty();
	}

	@Override
	public User getUserBySessionId(String sessionId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		Map<String, String> userFieldMapping = FieldMapperHelper.getFieldMapping(Table.usersessions);
		List<User> result = Collections.emptyList();
		try {
			result = queryBuilder.select(usersessions.User_ID).from(Table.usersessions)
					.where(usersessions.Session_ID, EnumComparator.EQUAL, sessionId)
					.executeSelect(User.class, userFieldMapping);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public void deleteSession(String sessionId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.delete(Table.usersessions).where(usersessions.Session_ID, EnumComparator.EQUAL, sessionId)
					.executeDelete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getUserIdBySessionId(String sessionId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> result = Collections.emptyList();
		try {
			result = queryBuilder.select(usersessions.User_ID).from(Table.usersessions)
					.where(usersessions.Session_ID, EnumComparator.EQUAL, sessionId).executeSelect(Integer.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}

		if (!result.isEmpty()) {
			return result.get(0);
		}
		return -1;
	}

	@Override
	public void deleteSessionById(String sessionId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.delete(Table.usersessions).where(usersessions.Session_ID, EnumComparator.EQUAL, sessionId)
					.executeDelete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer getUserIdFromSession(String sessionId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> result = Collections.emptyList();
		try {
			result = queryBuilder.select(usersessions.User_ID).from(Table.usersessions)
					.where(usersessions.Session_ID, EnumComparator.EQUAL, sessionId).executeSelect(Integer.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}

		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public void batchUpdateSessionExpirations(List<Session> sessions) {
		String sql = "UPDATE usersessions SET Expiration_Time = ? WHERE Session_ID = ?";
		int batchSize = 1000;
		int count = 0;

		try (Connection con = DatabaseConnectionPool.getDataSource().getConnection();
				PreparedStatement pst = con.prepareStatement(sql)) {

			for (Session session : sessions) {
				Timestamp newExpirationTime = new Timestamp(session.getExpirationTime());
				pst.setTimestamp(1, newExpirationTime);
				pst.setString(2, session.getSessionId());
				pst.addBatch();

				count++;

				if (count % batchSize == 0) {
					pst.executeBatch();
					count = 0;
				}
			}

			if (count > 0) {
				pst.executeBatch();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getUserIdWithSessionId(String sessionId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> result = Collections.emptyList();
		try {
			result = queryBuilder.select(usersessions.User_ID).from(Table.usersessions)
					.where(usersessions.Session_ID, EnumComparator.EQUAL, sessionId).executeSelect(Integer.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return result.isEmpty() ? -1 : result.get(0);
	}

}