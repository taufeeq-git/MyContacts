package com.taufeeq.web.Querybuilders;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {
    private List<String> columns;
    private List<String> tables;
    private List<String> values;
    private List<String> conditions;
    private String queryType;  // New field to track query type

    public QueryBuilder() {
        this.columns = new ArrayList<>();
        this.tables = new ArrayList<>();
        this.values = new ArrayList<>();
        this.conditions = new ArrayList<>();
    }

    // To specify whether it's a SELECT or INSERT query
    public QueryBuilder insertInto(String table) {
        this.queryType = "INSERT";
        this.tables.add(table);
        return this;
    }

    // To handle columns for INSERT statement
    public QueryBuilder columns(String... columns) {
        for (String column : columns) {
            this.columns.add(column);
        }
        return this;
    }

    // To handle values for INSERT statement
    public QueryBuilder values(String... values) {
        for (String value : values) {
            this.values.add(value);
        }
        return this;
    }

    // Builds the INSERT query
    public String build() {
        StringBuilder sb = new StringBuilder();
        if ("INSERT".equals(queryType)) {
            // INSERT query
            sb.append("INSERT INTO ");
            sb.append(tables.get(0)).append(" (");
            for (int i = 0; i < columns.size(); i++) {
                sb.append(columns.get(i));
                if (i < columns.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(") VALUES (");
            for (int i = 0; i < values.size(); i++) {
                sb.append(values.get(i));
                if (i < values.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        } else {
            // For other query types like SELECT
            sb.append("SELECT ");
            for (int i = 0; i < columns.size(); i++) {
                sb.append(columns.get(i));
                if (i < columns.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(" FROM ");
            for (int i = 0; i < tables.size(); i++) {
                sb.append(tables.get(i));
                if (i < tables.size() - 1) {
                    sb.append(", ");
                }
            }
            if (!conditions.isEmpty()) {
                sb.append(" WHERE ");
                for (int i = 0; i < conditions.size(); i++) {
                    sb.append(conditions.get(i));
                    if (i < conditions.size() - 1) {
                        sb.append(" AND ");
                    }
                }
            }
        }
        return sb.toString();
    }
}
