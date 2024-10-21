package com.taufeeq.web.dao;

import com.taufeeq.web.config.DatabaseConfig;

public class QueryLayerFactory {

//    private static final String PROPERTIES_FILE = "db.properties";
    
    public static QueryLayer getQueryLayer() {
        DatabaseConfig config = new DatabaseConfig();
        String dbType = config.getDbType();
//    	String dbType="mysql";
        
        switch (dbType.toLowerCase()) {
            case "mysql":
                return new MySQLQueryLayer();
            case "postgresql":
//                return new PostgreSQLQueryLayer();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}
