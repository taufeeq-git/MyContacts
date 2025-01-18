package com.taufeeq.web.dao;

import java.util.List;

import com.taufeeq.web.model.Server;

public interface ServerDAO {

	boolean checkIfSeverExits(String ipAddress, int port);

	int addServer(Server server);

	List<Server> getOtherServers(Server server);

	List<Server> getAllServers();
}
