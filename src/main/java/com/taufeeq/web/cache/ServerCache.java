package com.taufeeq.web.cache;

import com.taufeeq.web.dao.ServerDAO;
import com.taufeeq.web.dao.ServerDAOImpl;
import com.taufeeq.web.logger.CustomLogger;
import com.taufeeq.web.model.Server;
import java.util.List;

public class ServerCache {

    private static ServerCache instance;
    private List<Server> serverList;
    private final ServerDAO serverDAO = new ServerDAOImpl();
    private static final CustomLogger logger = CustomLogger.getInstance();

    private ServerCache() {
        loadServersFromDB();
        logger.infoApp("ServerCache initialized.");
    }

    public static ServerCache getInstance() {
        if (instance == null) {
            instance = new ServerCache();
        }
        return instance;
    }

    public List<Server> getServerList() {
        return serverList;
    }

    public void reloadCacheFromDB() {
        loadServersFromDB();
        logger.infoApp("ServerCache reloaded from DB.");
    }

    private void loadServersFromDB() {
        this.serverList = serverDAO.getAllServers();
        logger.infoApp("Servers loaded from DB into cache. Server count: " + (serverList != null ? serverList.size() : 0));
    }

    public void updateCache(Server newServer) {
        if (serverList != null && !serverList.contains(newServer)) {
            serverList.add(newServer);
            logger.infoApp("Server added to cache. Server ID: " + newServer.getServerId());
        } else if (serverList == null) {
            reloadCacheFromDB();
            logger.warnApp("Server list was null when attempting to update cache. Reloaded from DB.");
        } else {
            logger.infoApp("Server already exists in cache. Server ID: " + newServer.getServerId());
        }
    }
}