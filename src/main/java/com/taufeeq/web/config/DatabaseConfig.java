package com.taufeeq.web.config;

import com.taufeeq.web.logger.CustomLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final String PROPERTIES_FILE = "db.properties";
    private String dbType;
    private static final CustomLogger logger = CustomLogger.getInstance();

    public DatabaseConfig() {
        logger.infoApp("DatabaseConfig constructor called. Loading database properties.");
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
                dbType = properties.getProperty("db.type");
                logger.infoApp("Database properties loaded successfully. DB Type: " + dbType);
            } else {
                String errorMessage = "Properties file not found: " + PROPERTIES_FILE;
                logger.errorApp(errorMessage);
                throw new IOException(errorMessage);
            }
        } catch (IOException e) {
            logger.errorApp("IOException occurred while loading database properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getDbType() {
        return dbType;
    }
}