package com.taufeeq.web.cache;

import com.taufeeq.web.dao.SessionDAO;
import com.taufeeq.web.dao.SessionDAOImpl;
import com.taufeeq.web.logger.CustomLogger;
import com.taufeeq.web.model.Session;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionCache {
    private static SessionCache instance;
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    public final ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService updateScheduler = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService deleteScheduler = Executors.newScheduledThreadPool(1);
    private static final CustomLogger logger = CustomLogger.getInstance();

    private SessionCache() {
        logger.infoApp("SessionCache initialized.");
    }

    public static SessionCache getInstance() {
        if (instance == null) {
            instance = new SessionCache();
        }
        return instance;
    }

    public void startSchedulers() {
        logger.infoApp("Schedulers starting for SessionCache.");
        updateScheduler.scheduleWithFixedDelay(() -> {
            List<Session> sessionsToUpdate = new ArrayList<>(sessionMap.values());
            if (!sessionsToUpdate.isEmpty()) {
                sessionDAO.batchUpdateSessionExpirations(sessionsToUpdate);
                logger.infoApp("Session expiration batch update task executed for " + sessionsToUpdate.size() + " sessions.");
                sessionMap.clear();
            } else {
                logger.infoApp("Session expiration batch update task executed, no sessions to update.");
            }
        }, 0, 5, TimeUnit.MINUTES);

        deleteScheduler.scheduleWithFixedDelay(() -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            int deletedSessionsCount = sessionDAO.deleteExpiredSessions(currentTime);
            logger.infoApp("Expired session deletion task executed, " + deletedSessionsCount + " sessions deleted from DB.");

            List<String> sessionIdsToDeleteFromCache = new ArrayList<>();
            for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
                Session session = entry.getValue();
                if (session.getExpirationTime() <= currentTime.getTime()) {
                    sessionIdsToDeleteFromCache.add(entry.getKey());
                }
            }
            int cacheDeletions = sessionIdsToDeleteFromCache.size();
            sessionIdsToDeleteFromCache.forEach(sessionMap::remove);
            logger.infoApp("Expired session deletion task executed, " + cacheDeletions + " sessions deleted from cache.");

        }, 0, 5, TimeUnit.MINUTES);
    }

    public void stopSchedulers() {
        logger.infoApp("Schedulers stopping for SessionCache.");
        updateScheduler.shutdown();
        deleteScheduler.shutdown();
        logger.infoApp("Schedulers shutdown initiated.");
    }

    public void addSession(Session session) {
        sessionMap.put(session.getSessionId(), session);
        logger.infoApp("Session added to cache. Session ID: " + session.getSessionId() + ", User ID: " + session.getUserId());
    }

    public void removeSession(String sessionId) {
        Session removedSession = sessionMap.remove(sessionId);
        if (removedSession != null) {
            logger.infoApp("Session removed from cache. Session ID: " + sessionId + ", User ID: " + removedSession.getUserId());
        } else {
            logger.warnApp("Attempted to remove session from cache, but session ID not found: " + sessionId);
        }
    }

    public Session getSession(String sessionId) {
        Session session = sessionMap.get(sessionId);
        if (session != null) {
            logger.infoApp("Cache Hit for Session ID: " + sessionId);
        } else {
            logger.infoApp("Cache Miss for Session ID: " + sessionId);
        }
        return session;
    }

    public ConcurrentHashMap<String, Session> getSessionMap() {
        return sessionMap;
    }
}