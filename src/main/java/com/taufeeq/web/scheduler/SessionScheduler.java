package com.taufeeq.web.scheduler;

import com.taufeeq.web.dao.SessionDAO;
import com.taufeeq.web.dao.SessionDAOImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionScheduler {
    private final static SessionDAO sessionDAO = new SessionDAOImpl();
    public static final ConcurrentHashMap<String, Timestamp> sessionMap = new ConcurrentHashMap<>();
    
 
    private static final ScheduledExecutorService updateScheduler = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService deleteScheduler = Executors.newScheduledThreadPool(1);

 
    public static void startSchedulers() {
        System.out.println("Schedulers started");	

       
        updateScheduler.scheduleWithFixedDelay(() -> {
          

            List<Map.Entry<String, Timestamp>> sessionsToUpdate = new ArrayList<>(sessionMap.entrySet());
//            
//            if (!sessionsToUpdate.isEmpty()) {
//                sessionsToUpdate.forEach(entry -> sessionMap.remove(entry.getKey()));
//                sessionDAO.batchUpdateSessionExpirations(sessionsToUpdate);
//            }
            if (!sessionsToUpdate.isEmpty()) {
                sessionDAO.batchUpdateSessionExpirations(sessionsToUpdate);
       
                sessionsToUpdate.forEach(entry -> {
                    String sessionId = entry.getKey();
                    Timestamp oldTimestamp = entry.getValue();
                    Timestamp currentTimestamp = sessionMap.get(sessionId);
                    if (currentTimestamp != null && currentTimestamp.equals(oldTimestamp)) {
                        sessionMap.remove(sessionId);
                    }
                });
            }


        }, 1, 1, TimeUnit.MINUTES);  

        deleteScheduler.scheduleWithFixedDelay(() -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            sessionDAO.deleteExpiredSessions(currentTime);

        }, 1, 1, TimeUnit.MINUTES);  
    }


    public static void stopSchedulers() {
        System.out.println("Schedulers are stopping");
        updateScheduler.shutdown();
        deleteScheduler.shutdown();
    }


    public static void addSession(String sessionId, Timestamp currentTime) {
        sessionMap.put(sessionId, currentTime);
    }

    public ConcurrentHashMap<String, Timestamp> getSessionMap() {
        return sessionMap;
    }
}
