package com.taufeeq.web.model;

public class Token {
	private int id;
	private int userId;
	private String uniqueId;
	private String email;
	private String accessToken;
	private String refreshToken;
	private long createdTime;
	private long modifiedTime;
	private String provider;
	private int syncInterval;
	private long lastSyncTime;

	public long getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(long lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	public Token() {
	}

	public Token(int id, int userId, String accessToken, String refreshToken, long createdTime, long modifiedTime,
			String provider) {
		this.id = id;
		this.userId = userId;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
		this.provider = provider;
	}

	public Token(int userId, String accessToken, String refreshToken, long createdTime, String provider) {
		this.userId = userId;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.createdTime = createdTime;
		this.provider = provider;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public long getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public int getSyncInterval() {
		return syncInterval;
	}

	public void setSyncInterval(int syncInterval) {
		this.syncInterval = syncInterval;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	// Helper method to check if token is expired
	public boolean isTokenExpired() {
		long currentTime = System.currentTimeMillis();
		return (currentTime - createdTime) / 1000 >= 3500;
	}

	// Helper method to check if refresh token exists
	public boolean hasRefreshToken() {
		return refreshToken != null && !refreshToken.isEmpty();
	}

	@Override
	public String toString() {
		return "Token [id=" + id + ", userId=" + userId + ", uniqueId=" + uniqueId + ", email=" + email
				+ ", accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", createdTime=" + createdTime
				+ ", modifiedTime=" + modifiedTime + ", provider=" + provider + ", syncInterval=" + syncInterval + "]";
	}

}