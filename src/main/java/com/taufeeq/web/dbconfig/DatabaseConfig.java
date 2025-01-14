package com.taufeeq.web.dbconfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final String PROPERTIES_FILE = "db.properties";
    private String dbType;

    public DatabaseConfig() {
        loadProperties();
    }

    private void loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
                dbType = properties.getProperty("db.type");
            } else {
                throw new IOException("Properties file not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDbType() {
        return dbType;
    }
}
