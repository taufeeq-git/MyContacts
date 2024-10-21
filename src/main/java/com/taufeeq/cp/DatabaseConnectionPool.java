package com.taufeeq.cp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnectionPool {

    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/mycontacts");
        config.setUsername("root");
        config.setPassword("root");
        config.setMinimumIdle(5); 
        config.setMaximumPoolSize(50); 
        config.setIdleTimeout(600000);
        config.setConnectionTimeout(30000); 
        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}

