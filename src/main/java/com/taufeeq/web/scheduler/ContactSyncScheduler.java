package com.taufeeq.web.scheduler;

import com.taufeeq.web.dao.TokenDAO;
import com.taufeeq.web.dao.TokenDAOImpl;
import com.taufeeq.web.helper.OauthHelper;
import com.taufeeq.web.logger.CustomLogger;
import com.taufeeq.web.model.Contact;
import com.taufeeq.web.model.Token;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ContactSyncScheduler {

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final TokenDAO tokenDAO = new TokenDAOImpl();
	private static final CustomLogger logger = CustomLogger.getInstance();

	public void startScheduler() {
		Runnable syncTask = () -> {
			try {
				List<Token> tokens = tokenDAO.showAllTokens();
				if (tokens != null) {
					for (Token token : tokens) {
						if (shouldSync(token)) {
							syncContacts(token);
						}
					}
				}
			} catch (Exception e) {
				logger.errorApp("Error during contact sync scheduling: " + e.getMessage());
			}
		};

		logger.infoApp("ContactSyncScheduler started. Sync task scheduled to run every 1 minute.");
		scheduler.scheduleAtFixedRate(syncTask, 0, 1, TimeUnit.MINUTES);
	}

	public void stopScheduler() {
		scheduler.shutdownNow();
		logger.infoApp("ContactSyncScheduler stopped.");
	}

	private boolean shouldSync(Token token) {
		long currentTime = System.currentTimeMillis() / 1000;
		long lastSyncTime = token.getLastSyncTime();
		int syncInterval = token.getSyncInterval();

		if (lastSyncTime == 0) {
			return true;
		}

		long nextSyncTime = lastSyncTime + (syncInterval * 60 * 60);

		return currentTime >= nextSyncTime;
	}

	private void syncContacts(Token token) {
		try {
			String accessToken = OauthHelper.getValidAccessToken(token.getId());
			if (accessToken != null) {
				List<Contact> googleContacts = OauthHelper.fetchGoogleContacts(accessToken);
				if (googleContacts != null) {
					OauthHelper.syncContacts(token.getUserId(), googleContacts);
					tokenDAO.updateLastSync(token.getId());
					logger.infoApp("Contacts synced for user " + token.getUserId() + ", account ID " + token.getId()
							+ ". Synced contacts count: " + googleContacts.size());
				} else {
					logger.warnApp("Failed to fetch Google contacts for user " + token.getUserId() + ", account ID "
							+ token.getId());
				}
			} else {
				logger.warnApp("Invalid or expired access token for user " + token.getUserId() + ", account ID "
						+ token.getId());
			}
		} catch (Exception e) {
			logger.errorApp("Error syncing contacts for user " + token.getUserId() + ", account ID " + token.getId()
					+ ": " + e.getMessage());
		}
	}
}