package com.taufeeq.web.dao;

import com.taufeeq.cp.DatabaseConnectionPool;
import com.taufeeq.web.enums.Column;
import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.model.ClassInterface;
//import com.taufeeq.web.enums.Enum;
import com.taufeeq.web.query.QueryBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class MySQLQueryBuilder implements QueryBuilder {
    private StringBuilder query;
    private List<Object> parameters;
    private boolean inTransaction = false;
    private Connection transactionConnection;

    public MySQLQueryBuilder() {
        this.query = new StringBuilder();
        this.parameters = new ArrayList<>();
    }
    public void beginTransaction() {
        try {
            transactionConnection = DatabaseConnectionPool.getDataSource().getConnection();
            transactionConnection.setAutoCommit(false);
            inTransaction = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commitTransaction() {
        if (inTransaction && transactionConnection != null) {
            try {
                transactionConnection.commit();
                transactionConnection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeTransactionConnection();
                inTransaction = false;
            }
        }
    }

    @Override
    public void rollbackTransaction() {
        if (inTransaction && transactionConnection != null) {
            try {
                transactionConnection.rollback();
                transactionConnection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeTransactionConnection();
                inTransaction = false;
            }
        }
    }

    private void closeTransactionConnection() {
        try {
            if (transactionConnection != null && !transactionConnection.isClosed()) {
                transactionConnection.setAutoCommit(true); 
                transactionConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public QueryBuilder insert(Table table, Column...columns) {
        String columnNames = Arrays.stream(columns)
        	      .map(Column::getColumnName)
        	      .collect(Collectors.joining(", "));


        query.append("INSERT INTO ").append(table).append(" (")
             .append(columnNames).append(") ");
        return this;
    }
    @Override
    public QueryBuilder delete(Table table) {
        query.append("DELETE FROM ").append(table);
        return this;
    }


    @Override
    public QueryBuilder values(Object... values) {
        query.append("VALUES (");
        
        StringBuilder placeholders = new StringBuilder();
        
        for (int i = 0; i < values.length; i++) {
            placeholders.append("?");
            if (i < values.length - 1) {
                placeholders.append(", ");
            }
        }
        
        query.append(placeholders).append(") ");
        
        for (Object value : values) {
            parameters.add(value);
        }
        
        return this;
    }
    
    @Override
    public QueryBuilder from(Table table) {
        query.append("FROM ").append(table).append(" "); 
        return this;
    }

    @Override
    public QueryBuilder where(Column condition, Object... values) {
        query.append(" WHERE ").append(condition.getColumnName()).append(" = ?");
        for (Object value : values) {
            parameters.add(value);
        }
        return this;
    }

    @Override
    public QueryBuilder whereLessThan(Column condition, Object... values) {
        query.append(" WHERE ").append(condition).append(" < ?");
        for (Object value : values) {
            parameters.add(value);
        }
        return this;
    }

    @Override
    public QueryBuilder and(Column condition, Object value) {
        query.append(" AND ").append(condition.getColumnName()).append(" = ? ");
        parameters.add(value);
        return this;
    }
    
    @Override
    public QueryBuilder limit(int limit) {
        query.append("LIMIT ").append(limit).append(" ");
        return this;
    }
    


    @Override
    public QueryBuilder update(Table table) {
        query.append("UPDATE ").append(table).append(" ");
        return this;
    }

    @Override
    public QueryBuilder set(Column column, Object value) {
        query.append("SET ").append(column).append(" = ? ");
        parameters.add(value);
        return this;
    }

    @Override
    public QueryBuilder deleteFrom(String table) {
        query.append("DELETE FROM ").append(table).append(" ");
        return this;
    }

    @Override
    public QueryBuilder select(Column... columns) {
        String columnNames = Arrays.stream(columns)
                                   .map(Column::getColumnName)  
                                   .collect(Collectors.joining(", "));
        query.append("SELECT ").append(columnNames).append(" ");
        return this;
    }
    
    public QueryBuilder innerJoin(Table table2, Column onLeft,Table table1, Column onRight) {
        query.append("INNER JOIN ").append(table2)
             .append(" ON ").append(table1).append(".").append(onLeft).append(" = ").append(table2).append(".").append(onRight).append(" ");
        return this;
    }
    
    public QueryBuilder leftJoin(Table table2, Column onLeft, Table table1, Column onRight) {
        query.append("LEFT JOIN ")
             .append(table2)
             .append(" ON ")
             .append(table1).append(".").append(onLeft)
             .append(" = ")
             .append(table2).append(".").append(onRight)
             .append(" ");
        return this;
    }


    @Override
    public String build() {
        return query.toString().trim();
    }
    
    @Override
    public int executeInsert() {
        String sql = build(); 
//        System.out.println("Executing SQL: " + sql);
//        System.out.println("With parameters: " + parameters);
        try (Connection con = inTransaction ? transactionConnection : DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            for (int i = 0; i < parameters.size(); i++) {
                pst.setObject(i + 1, parameters.get(i));
            }

            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    clear(); 
                    return rs.getInt(1); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clear(); 
        return -1; 
    }


//    @Override
//    public List<? extends ClassInterface> executeSelect(ClassFieldMapping<? extends ClassInterface>... mappings) {
//        String sql = build();
//        List<ClassInterface> resultList = new ArrayList<>();
//
//        try (Connection con = inTransaction ? transactionConnection : DatabaseConnectionPool.getDataSource().getConnection();
//             PreparedStatement pst = con.prepareStatement(sql)) {
//
//            for (int i = 0; i < parameters.size(); i++) {
//                pst.setObject(i + 1, parameters.get(i));
//            }
//
//            try (ResultSet rs = pst.executeQuery()) {
//                ResultSetMetaData metaData = rs.getMetaData();
//                int columnCount = metaData.getColumnCount();
//
//                while (rs.next()) {
//                    for (ClassFieldMapping<? extends ClassInterface> mapping : mappings) {
//                        Class<? extends ClassInterface> dummyClass = mapping.getDummyClass();
//                        Map<String, String> columnFieldMapping = mapping.getColumnFieldMapping();
//
//                        ClassInterface obj = dummyClass.getDeclaredConstructor().newInstance();
//
//                        for (int i = 1; i <= columnCount; i++) {
//                            String tableName = metaData.getTableName(i);
//                            String columnName = metaData.getColumnName(i);
//                            String fullColumnName = tableName + "." + columnName;
//
//                            if (columnFieldMapping != null) {
//                                String fieldName = columnFieldMapping.get(fullColumnName);
//                                if (fieldName != null) {
//                                    try {
//                                        Field field = dummyClass.getDeclaredField(fieldName);
//                                        field.setAccessible(true);
//                                        Object value = rs.getObject(i);
//                                        field.set(obj, value);
//                                    } catch (NoSuchFieldException e) {
//                                        System.err.println("Field not found in class: " + fieldName);
//                                    }
//                                }
//                            }
//                        }
//
//                        resultList.add(obj);
//                    }
//                }
//            }
//        } catch (SQLException | ReflectiveOperationException e) {
//            e.printStackTrace();
//        } finally {
//            clear();
//        }
//
//        return resultList;
//    }
    @Override
    public <T> List<T> executeSelect(Class<T> dummy, Map<String, String> columnFieldMapping) {
        String sql = build();
        List<T> resultList = new ArrayList<>();
        Map<Object, T> objectIns = new HashMap<>();

        try (Connection con = inTransaction ? transactionConnection : DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Set parameters for the query
            for (int i = 0; i < parameters.size(); i++) {
                pst.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = pst.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                Set<String> resultSetColumns = new HashSet<>();

                // Fetch all column labels from the ResultSet
                for (int i = 1; i <= columnCount; i++) {
                    String tableName = metaData.getTableName(i); 
                    String columnName = metaData.getColumnName(i); 
                    String fullColumnName = tableName + "." + columnName;
                    resultSetColumns.add(fullColumnName);

                }



                while (rs.next()) {
                    if (dummy == String.class || dummy == Integer.class) {
                        // Handle simple types like String or Integer
                        if (columnCount == 1) {
                            Object value = rs.getObject(1);
                            resultList.add(dummy.cast(value));
                        }
                    } else {
                        
                        Object primaryKey = rs.getObject(1);
                        T obj = objectIns.getOrDefault(primaryKey, dummy.getDeclaredConstructor().newInstance());

                        for (Field field : dummy.getDeclaredFields()) {
                            field.setAccessible(true);

                           
                            if (List.class.isAssignableFrom(field.getType())) {
                                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                                Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];

                                List<Object> list = (List<Object>) field.get(obj);
                                if (list == null) {
                                    list = new ArrayList<>();
                                    field.set(obj, list);
                                }

                                Object listElement = listClass.getDeclaredConstructor().newInstance();
                                for (Field listField : listClass.getDeclaredFields()) {
                                    listField.setAccessible(true);
                                    String columnMapping = columnFieldMapping.get(listClass.getSimpleName() + "." + listField.getName());

                                    if (columnMapping != null && resultSetColumns.contains(columnMapping)) {
                                        Object value = rs.getObject(columnMapping);
                                        listField.set(listElement, value);
                                    }
                                }
                                if (!list.contains(listElement)) {
                                    list.add(listElement);
                                }
                            } else {
                                
                                String columnMapping = columnFieldMapping.get(field.getName());
                                

                      
                                if (columnMapping != null && resultSetColumns.contains(columnMapping)) {
                                    Object value = rs.getObject(columnMapping);
                                    field.set(obj, value);
                                }
                            }
                        }
                        objectIns.put(primaryKey, obj);
                    }
                }

                resultList.addAll(objectIns.values());
            }
        } catch (SQLException | ReflectiveOperationException e) {
            e.printStackTrace();
        } finally {
            clear();
        }

        return resultList;
    }




    @Override
    public int executeUpdate() {
        String sql = build(); 
        Connection con = null;
        PreparedStatement pst = null;

        try {
           if (inTransaction) {
                con = transactionConnection;
            } else {
                con = DatabaseConnectionPool.getDataSource().getConnection();
            }
            pst = con.prepareStatement(sql);

            for (int i = 0; i < parameters.size(); i++) {
                pst.setObject(i + 1, parameters.get(i));
            }
            int rowsAffected = pst.executeUpdate(); 
            clear(); 
            return rowsAffected;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clear(); 
        return -1; 
    }

    private void clear() {
        query.setLength(0); 
        parameters.clear(); 
    }
}