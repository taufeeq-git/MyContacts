package com.taufeeq.web.dao;

import com.taufeeq.web.config.DatabaseConfig;
import com.taufeeq.web.query.QueryBuilder;

public class QueryBuilderFactory {

//    private static final String PROPERTIES_FILE = "db.properties";
    
    public static QueryBuilder getQueryBuilder() {
        DatabaseConfig config = new DatabaseConfig();
        String dbType = config.getDbType();
//    	String dbType="mysql";
        
        switch (dbType.toLowerCase()) {
            case "mysql":
                return new MySQLQueryBuilder();
            case "postgresql":
//                return new PostgreSQLQueryLayer();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}
