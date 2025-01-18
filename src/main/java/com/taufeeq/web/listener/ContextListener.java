package com.taufeeq.web.listener;

import com.taufeeq.web.auditlog.AuditLogProcessor;
import com.taufeeq.web.dao.*;
import com.taufeeq.web.model.Server;
import com.taufeeq.web.scheduler.ContactSyncScheduler;
import com.taufeeq.web.cache.SessionCache;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
	private static AuditLogProcessor auditLogProcessor;
	private static SessionCache sessionScheduler;

	private static final SessionCache sessionCache = SessionCache.getInstance();
	private static final ContactSyncScheduler contactSyncScheduler = new ContactSyncScheduler();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sessionCache.startSchedulers();
		contactSyncScheduler.startScheduler();
		sce.getServletContext().setAttribute("sessionScheduler", sessionScheduler);

		auditLogProcessor = new AuditLogProcessor();

		String ipAddress = getServerIp();
		int port = getServerPort(sce);

		Server server = new Server();
		server.setIpAddress(ipAddress);
		server.setPortNumber(port);

		registerServer(server);
		notifyServers(server);

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (auditLogProcessor != null) {
			auditLogProcessor.shutdownAndAwaitTermination();
		}
		if (contactSyncScheduler != null) {
			contactSyncScheduler.stopScheduler();
		}
		if (sessionCache != null) {
			sessionCache.stopSchedulers();
		}
	}

	private void registerServer(Server server) {

		ServerDAO serverDAO = new ServerDAOImpl();

		Boolean serverExists = serverDAO.checkIfSeverExits(server.getIpAddress(), server.getPortNumber());
		if (!serverExists) {
			serverDAO.addServer(server);
		}

	}

	private String getServerIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "UNKNOWN";
		}
	}

	private int getServerPort(ServletContextEvent sce) {
		try {
			return (Integer) new InitialContext().lookup("java:comp/env/server.port");
		} catch (NamingException e) {
			e.printStackTrace();
			return 8080;
		}
	}

	private void notifyServers(Server server) {
		ServerDAO serverDAO = new ServerDAOImpl();
		List<Server> servers = serverDAO.getOtherServers(server);

		HttpClient client = HttpClient.newHttpClient();

		for (Server otherServer : servers) {
			String notificationUrl = "http://" + otherServer.getIpAddress() + ":" + otherServer.getPortNumber()
					+ "/MyContacts/NotifyServerServlet?action=serverJoin";
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(notificationUrl)).GET().build();
			client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.discarding()).exceptionally(ex -> null);
		}

	}

}
