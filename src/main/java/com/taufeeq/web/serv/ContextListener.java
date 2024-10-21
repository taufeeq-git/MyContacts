package com.taufeeq.web.serv;

import com.taufeeq.web.scheduler.SessionScheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    private static SessionScheduler sessionScheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sessionScheduler = new SessionScheduler();
        sessionScheduler.startSchedulers(); 
        sce.getServletContext().setAttribute("sessionScheduler", sessionScheduler); 
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
