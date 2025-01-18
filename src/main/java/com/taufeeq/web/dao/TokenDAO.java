package com.taufeeq.web.dao;

import java.util.List;

import com.taufeeq.web.model.Token;

public interface TokenDAO {

	int saveTokens(int userId, String uniqueId, String email, String accessToken, String refreshToken, String provider);

	Boolean tokenChecker(int userId);

	List<Token> showSyncedAccounts(int userId);

	Boolean doesEmailExist(int userId, String email);

	String getAccessToken(int tokenId);

	boolean isTokenExpired(int userId);

	String getRefreshToken(int userId);

	int getTokenId(int userId, String email);

	int updateSyncInterval(int tokenId, int syncInterval);

	int updateAccessToken(int tokenId, String accessToken);

	int updateLastSync(int tokenId);

	List<Token> showAllTokens();
}
