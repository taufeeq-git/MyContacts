package com.taufeeq.web.querybuilder;

import com.taufeeq.web.config.DatabaseConfig;

public class QueryBuilderFactory {

	public static QueryBuilder getQueryBuilder() {
		DatabaseConfig config = new DatabaseConfig();
		String dbType = config.getDbType();

		switch (dbType.toLowerCase()) {
		case "mysql":
			return new MySQLQueryBuilder();
		case "postgresql":
		default:
			throw new IllegalArgumentException("Unsupported database type: " + dbType);
		}
	}
}