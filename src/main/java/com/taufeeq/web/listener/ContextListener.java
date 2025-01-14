package com.taufeeq.web.listener;

import com.taufeeq.web.auditlog.AuditLogProcessor;
import com.taufeeq.web.scheduler.SessionScheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
	private static AuditLogProcessor auditLogProcessor;
	private static SessionScheduler sessionScheduler;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
//		sessionScheduler = new SessionScheduler();
		SessionScheduler.startSchedulers();
		sce.getServletContext().setAttribute("sessionScheduler", sessionScheduler);

		auditLogProcessor = new AuditLogProcessor();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutting down AuditLogProcessor...");
			auditLogProcessor.shutdownAndAwaitTermination();
			SessionScheduler.stopSchedulers();
		}));

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

		SessionScheduler.stopSchedulers();
	}

}
