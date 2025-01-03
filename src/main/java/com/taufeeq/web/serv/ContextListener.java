package com.taufeeq.web.serv;

import com.taufeeq.web.scheduler.SessionScheduler;

import auditLogging.AuditLogProcessor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
	private static AuditLogProcessor auditLogProcessor;  
    private static SessionScheduler sessionScheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sessionScheduler = new SessionScheduler();
        sessionScheduler.startSchedulers(); 
        sce.getServletContext().setAttribute("sessionScheduler", sessionScheduler); 
        
        auditLogProcessor = new AuditLogProcessor();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down AuditLogProcessor...");
            auditLogProcessor.shutdownAndAwaitTermination();  
            sessionScheduler.stopSchedulers();  
        }));
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        sessionScheduler.clearAllSessions();
        sessionScheduler.stopSchedulers(); 
    }

   
//    public SessionScheduler getSessionScheduler() {
//        return sessionScheduler;
//    }
}
