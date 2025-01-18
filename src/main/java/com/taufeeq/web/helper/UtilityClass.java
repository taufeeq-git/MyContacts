package com.taufeeq.web.helper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

import com.taufeeq.web.cache.ServerCache;
import com.taufeeq.web.model.Server;

public class UtilityClass {
	public static void notifyOtherServersOfLogout(String sessionId) {
		ServerCache serverCache = ServerCache.getInstance();
		List<Server> otherServers = serverCache.getServerList();
		HttpClient client = HttpClient.newHttpClient();

		for (Server server : otherServers) {

			String notificationUrl = "http://" + server.getIpAddress() + ":" + server.getPortNumber()
					+ "/MyContacts/NotifyServerServlet?sessionId=" + sessionId + "&action=sessionLogout";
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(notificationUrl)).GET().build();

			client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.discarding()).exceptionally(ex -> null);

		}
	}

	public static void notifyOtherServersOfUpdate(int userId) {
		ServerCache serverCache = ServerCache.getInstance();
		List<Server> otherServers = serverCache.getServerList();
		HttpClient client = HttpClient.newHttpClient();

		for (Server server : otherServers) {

			String notificationUrl = "http://" + server.getIpAddress() + ":" + server.getPortNumber()
					+ "/MyContacts/NotifyServerServlet?userId=" + userId + "&action=userUpdate";
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(notificationUrl)).GET().build();

			client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.discarding()).exceptionally(ex -> null);

		}
	}

}
