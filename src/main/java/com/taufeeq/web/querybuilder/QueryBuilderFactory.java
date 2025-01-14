package com.taufeeq.web.querybuilder;

import com.taufeeq.web.dbconfig.DatabaseConfig;

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
