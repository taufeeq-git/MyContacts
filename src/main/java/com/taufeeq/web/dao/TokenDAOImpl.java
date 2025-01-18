package com.taufeeq.web.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.taufeeq.web.enums.Enum.EnumComparator;
import com.taufeeq.web.enums.Enum.EnumConjunction;
import com.taufeeq.web.enums.Enum.Table;
import com.taufeeq.web.enums.Enum.oauth_tokens;
import com.taufeeq.web.helper.FieldMapperHelper;
import com.taufeeq.web.model.Token;
import com.taufeeq.web.querybuilder.QueryBuilder;
import com.taufeeq.web.querybuilder.QueryBuilderFactory;

import java.sql.SQLException;
import java.lang.ReflectiveOperationException;

public class TokenDAOImpl implements TokenDAO {

	private QueryBuilder queryBuilder;

	@Override
	public int saveTokens(int userId, String uniqueId, String email, String accessToken, String refreshToken,
			String provider) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			return queryBuilder
					.insert(Table.oauth_tokens, oauth_tokens.user_id, oauth_tokens.unique_id, oauth_tokens.email,
							oauth_tokens.access_token, oauth_tokens.refresh_token, oauth_tokens.provider,
							oauth_tokens.created_time, oauth_tokens.modified_time)
					.values(userId, uniqueId, email, accessToken, refreshToken, provider, time, time).executeInsert();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	@Override
	public Boolean tokenChecker(int userId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> res = Collections.emptyList();
		try {
			res = queryBuilder.select(oauth_tokens.id).from(Table.oauth_tokens)
					.where(oauth_tokens.user_id, EnumComparator.EQUAL, userId).executeSelect(Integer.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return !res.isEmpty();

	}

	@Override
	public List<Token> showSyncedAccounts(int userId) {
		Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(Table.oauth_tokens);
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Token> res = Collections.emptyList();
		try {
			res = queryBuilder.selectAll().from(Table.oauth_tokens)
					.where(oauth_tokens.user_id, EnumComparator.EQUAL, userId).executeSelect(Token.class, fieldMapping);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public Boolean doesEmailExist(int userId, String email) {
		Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(Table.oauth_tokens);
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Token> res = Collections.emptyList();
		try {
			res = queryBuilder.selectAll().from(Table.oauth_tokens)
					.where(oauth_tokens.user_id, EnumComparator.EQUAL, userId)
					.conjunction(EnumConjunction.AND, oauth_tokens.email, EnumComparator.EQUAL, email)
					.executeSelect(Token.class, fieldMapping);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}

		return !res.isEmpty();
	}

	@Override
	public String getAccessToken(int tokenId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<String> res = Collections.emptyList();
		try {
			res = queryBuilder.select(oauth_tokens.access_token).from(Table.oauth_tokens)
					.where(oauth_tokens.id, EnumComparator.EQUAL, tokenId).executeSelect(String.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return res.isEmpty() ? null : res.get(0);
	}

	@Override
	public String getRefreshToken(int tokenId) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<String> res = Collections.emptyList();
		try {
			res = queryBuilder.select(oauth_tokens.refresh_token).from(Table.oauth_tokens)
					.where(oauth_tokens.id, EnumComparator.EQUAL, tokenId).executeSelect(String.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return res.isEmpty() ? null : res.get(0);
	}

	@Override
	public int updateAccessToken(int tokenId, String accessToken) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			return queryBuilder.update(Table.oauth_tokens).set(oauth_tokens.access_token, accessToken)
					.where(oauth_tokens.id, EnumComparator.EQUAL, tokenId).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}

	}

	@Override
	public int getTokenId(int userId, String email) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> res = Collections.emptyList();
		try {
			res = queryBuilder.select(oauth_tokens.id).from(Table.oauth_tokens)
					.where(oauth_tokens.user_id, EnumComparator.EQUAL, userId)
					.conjunction(EnumConjunction.AND, oauth_tokens.email, EnumComparator.EQUAL, email)
					.executeSelect(Integer.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
			return -1;
		}
		return res.isEmpty() ? -1 : res.get(0);
	}

	@Override
	public int updateSyncInterval(int tokenId, int syncInterval) {
		try {
			queryBuilder = QueryBuilderFactory.getQueryBuilder();
			return queryBuilder.update(Table.oauth_tokens).set(oauth_tokens.sync_interval, syncInterval)
					.where(oauth_tokens.id, EnumComparator.EQUAL, tokenId).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int updateLastSync(int tokenId) {
		long time = System.currentTimeMillis() / 1000;
		try {
			queryBuilder = QueryBuilderFactory.getQueryBuilder();
			return queryBuilder.update(Table.oauth_tokens).set(oauth_tokens.last_sync_time, time)
					.where(oauth_tokens.id, EnumComparator.EQUAL, tokenId).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public List<Token> showAllTokens() {
		List<Token> tokens = new ArrayList<>();
		Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(Table.oauth_tokens);
		try {
			queryBuilder = QueryBuilderFactory.getQueryBuilder();
			tokens = queryBuilder.selectAll().from(Table.oauth_tokens).executeSelect(Token.class, fieldMapping);
			return tokens;
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public boolean isTokenExpired(int tokenId) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<Integer> res = Collections.emptyList();
		try {
			res = queryBuilder.select(oauth_tokens.id).from(Table.oauth_tokens)
					.whereDiff(time, oauth_tokens.created_time, "<", 3500)
					.conjunction(EnumConjunction.AND, oauth_tokens.id, EnumComparator.EQUAL, tokenId)
					.executeSelect(Integer.class, null);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return res.isEmpty();

	}

}