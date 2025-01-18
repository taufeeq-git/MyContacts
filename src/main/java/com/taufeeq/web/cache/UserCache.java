package com.taufeeq.web.cache;

import com.taufeeq.web.dao.UserDAO;
import com.taufeeq.web.dao.UserDAOImpl;
import com.taufeeq.web.logger.CustomLogger;
import com.taufeeq.web.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserCache {

	private static UserCache instance;
	private static final int CACHE_CAPACITY = 2;
	private static final CustomLogger logger = CustomLogger.getInstance();

	private final int cacheCapacity;
	private final ConcurrentHashMap<Integer, User> userCache;
	private final ConcurrentHashMap<Integer, Integer> accessFrequency;
	private final UserDAO userDAO;

	private UserCache() {
		this.cacheCapacity = CACHE_CAPACITY;
		this.userCache = new ConcurrentHashMap<>(CACHE_CAPACITY);
		this.accessFrequency = new ConcurrentHashMap<>(CACHE_CAPACITY);
		this.userDAO = new UserDAOImpl();
		logger.infoApp("UserCache initialized with capacity: " + CACHE_CAPACITY);
	}

	public static UserCache getInstance() {
		if (instance == null) {
			instance = new UserCache();
		}
		return instance;
	}

	public User getUser(int userId) {
		User user = userCache.get(userId);
		if (user != null) {
			logger.infoApp("Cache Hit for User ID: " + userId);
			return user;
		}

		user = userDAO.getUserById(userId);
		if (user != null) {
			addUserToCache(user);
			logger.infoApp("Cache Miss for User ID: " + userId + ", fetched from DB and added to cache.");
			return user;
		} else {
			logger.warnApp("Cache Miss for User ID: " + userId + ", User not found in DB.");
			return null;
		}
	}

	public ConcurrentHashMap<Integer, User> getUserCache() {
		return userCache;
	}

	public void hitFrequency(int userId) {
		User user = userCache.get(userId);
		if (user != null) {
			incrementFrequency(userId);
			logger.infoApp("Cache Hit for User ID: " + userId + ", incremented access frequency.");
			return;
		}

		user = userDAO.getUserById(userId);
		if (user != null) {
			addUserToCache(user);
			incrementFrequency(userId);
			logger.infoApp("Cache Miss for User ID: " + userId
					+ ", fetched from DB, added to cache, and incremented frequency.");
		} else {
			logger.warnApp("Cache Miss for User ID: " + userId + " during frequency hit, User not found in DB.");
		}
	}

	public void addUserToCache(User user) {
		if (userCache.size() >= cacheCapacity) {
			evictLFU();
		}
		userCache.put(user.getUserId(), user);
		accessFrequency.put(user.getUserId(), 0);
		logger.infoApp("User added to cache: User ID: " + user.getUserId() + ", Cache Size: " + userCache.size() + "/"
				+ cacheCapacity);
	}

	public void removeUserFromCache(int userId) {
		User removedUser = userCache.remove(userId);
		accessFrequency.remove(userId);
		if (removedUser != null) {
			logger.infoApp("User removed from cache: User ID: " + userId + ", User: " + removedUser + ", Cache Size: "
					+ userCache.size() + "/" + cacheCapacity);
		} else {
			logger.warnApp("Attempted to remove User ID: " + userId + " from cache, but user was not found.");
		}
	}

	private void evictLFU() {
		if (userCache.isEmpty()) {
			logger.warnApp("Cache is empty, no eviction needed.");
			return;
		}

		int lfuUserId = -1;
		int minFrequency = Integer.MAX_VALUE;

		for (Map.Entry<Integer, Integer> entry : accessFrequency.entrySet()) {
			int userId = entry.getKey();
			int frequency = entry.getValue();

			if (frequency < minFrequency) {
				minFrequency = frequency;
				lfuUserId = userId;
			}
		}

		if (lfuUserId != -1) {
			User evictedUser = userCache.remove(lfuUserId);
			accessFrequency.remove(lfuUserId);
			logger.infoApp("LFU Eviction - User ID: " + lfuUserId + ", User: " + evictedUser + ", Frequency: "
					+ minFrequency + ", Cache Size after Eviction: " + userCache.size() + "/" + cacheCapacity);
		} else {
			logger.warnApp("LFU Eviction process failed to identify a user for eviction.");
		}
	}

	private void incrementFrequency(int userId) {
		Integer currentFrequency = accessFrequency.get(userId);
		if (currentFrequency != null) {
			accessFrequency.put(userId, currentFrequency + 1);
		} else {
			accessFrequency.put(userId, 1);
			logger.warnApp(
					"Frequency increment for User ID: " + userId + " failed to find existing frequency, setting to 1.");
		}
	}

	public int getCacheSize() {
		int size = userCache.size();
		logger.infoApp("Current Cache Size: " + size + "/" + cacheCapacity);
		return size;
	}
}