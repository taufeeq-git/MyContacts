package com.taufeeq.web.dao;

import com.taufeeq.cp.DatabaseConnectionPool;
import com.taufeeq.web.enums.Column;
import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.query.QueryBuilder;

import auditLogging.AuditLogProcessor;

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
    
    private String action;
    private Column updateCol;
    private int updateId;
    private Table tableName;
    private Column primaryKey;
    private String pk;
    private List<String> columnNames = new ArrayList<>();
    private Map<String, Object> oldValuesMap = new HashMap<>();
    private Map<String, Object> newValuesMap = new HashMap<>();
    private Boolean foundPkey= false;
    
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
    public QueryBuilder insert(Table table, Column... columns) {
    	action="INSERT";
        
        this.tableName = table; 
        
        
        
        primaryKey = columns[0].getPrimaryKey();
        pk=primaryKey.getSimpleColumnName();
//        if (primaryKey != null) {
//            columnNames.add(primaryKey.getSimpleColumnName());
//        }

        StringBuilder columnNamesBuilder = new StringBuilder();
        
        
        for (int i = 0; i < columns.length; i++) {
            Column column = columns[i];
//            if(column.getSimpleColumnName()!=pk)
            columnNames.add(column.getSimpleColumnName());
            if(column.getSimpleColumnName()==pk)
            	foundPkey=true;
            	

            
            
            columnNamesBuilder.append(column.getColumnName());
            if (i < columns.length - 1) {
                columnNamesBuilder.append(", ");
            }
        }
  
        String columnNamesString = columnNamesBuilder.toString();

      
        query.append("INSERT INTO ").append(table).append(" (")
             .append(columnNamesString).append(") ");
        
        return this;
    }

    @Override
    public QueryBuilder delete(Table table) {
    	this.tableName=table;
    	action="DELETE";
        query.append("DELETE FROM ").append(table);
        return this;
    }


    @Override
    public QueryBuilder values(Object... values) {
//        if (values.length != columnNames.size()+1) {
//            throw new IllegalArgumentException("values doesnt match columns");
//        }
//    	System.out.println(columnNames);
       
        for (int i = 0; i < values.length; i++) {
            newValuesMap.put(columnNames.get(i), values[i]);
        }
        
        
        
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            placeholders.append("?");
            if (i < values.length - 1) {
                placeholders.append(", ");
            }
        }

        query.append("VALUES (").append(placeholders).append(") ");
        
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
    	if(action=="UPDATE"||action=="DELETE") {
    		updateCol=condition;
    		updateId=(int) values[0];
    		newValuesMap.put(condition.getSimpleColumnName(), values[0]);
    	}
        query.append(" WHERE ").append(condition.getColumnName()).append(" = ?");
        
        if(action=="UPDATE") {
        	
        }
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
    	
    	action="UPDATE";
    	
    	this.tableName=table;
        query.append("UPDATE ").append(table).append(" ");
        return this;
    }

    @Override
    public QueryBuilder set(Column column, Object value) {
    	
    	primaryKey = column.getPrimaryKey();
        pk=primaryKey.getSimpleColumnName();
    	
    	
      
        if (query.indexOf("SET") == -1) {
        	newValuesMap.put(column.getSimpleColumnName(), value);
//        	System.out.println(column.getSimpleColumnName()+value);
            query.append("SET ");
        } else {
        	newValuesMap.put(column.getSimpleColumnName(), value);
//        	System.out.println(column.getSimpleColumnName()+value);
//        	System.out.println(column.getColumnName()+column.getColumnName().getClass());
            query.append(", ");  
        }
        

        query.append(column).append(" = ? ");

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
    
    public QueryBuilder selectAll() {
    	 query.append("SELECT ").append("* ");
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
    
    private static final AuditLogProcessor auditLogProcessor = new AuditLogProcessor();

    public int executeInsert() {
        String sql = build();

        try (Connection con = inTransaction ? transactionConnection : DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the parameters for the PreparedStatement
            for (int i = 0; i < parameters.size(); i++) {
                pst.setObject(i + 1, parameters.get(i));
            }

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    clear();
                    int generatedKey = rs.getInt(1);
                    if (!foundPkey) {
                        newValuesMap.put(pk, generatedKey);
                    }

                    if (shouldAudit()) {
                        submitAuditLogTask();
                    }

                    foundPkey = false;
                    return generatedKey;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (shouldAudit()) {
            submitAuditLogTask();
        }

        foundPkey = false;
        clear();
        return -1;
    }

    private void submitAuditLogTask() {
        auditLogProcessor.submitAuditLogTask(() -> {
            try {
                insertAuditLog();
            } catch (Exception e) {
                e.printStackTrace();
              
                auditLogProcessor.submitAuditLogTask(()->insertAuditLog());
            }
        });
    }



    private boolean shouldAudit() {
        return tableName != Table.usersessions && tableName != Table.audit_logs;
    }


    private void insertAuditLog() {
        AuditDAOImpl auditDAOImpl = new AuditDAOImpl();
        auditDAOImpl.insertQuery(tableName, "INSERT", newValuesMap, oldValuesMap, pk);
        oldValuesMap.clear();
        newValuesMap.clear();
    }
    private void updateAuditLog() {
        AuditDAOImpl auditDAOImpl = new AuditDAOImpl();
        auditDAOImpl.insertQuery(tableName, "UPDATE", newValuesMap, oldValuesMap, pk);
        oldValuesMap.clear();
        newValuesMap.clear();
    }
    private void deleteAuditLog() {
        AuditDAOImpl auditDAOImpl = new AuditDAOImpl();
        auditDAOImpl.insertQuery(tableName, "DELETE", newValuesMap, oldValuesMap, pk);
        oldValuesMap.clear();
        newValuesMap.clear();
    }


    
    private void selectAuditLog() {
        AuditDAOImpl auditDAOImpl = new AuditDAOImpl();
        List<?> oldPojoo = auditDAOImpl.selectQuery(tableName, updateCol, updateId);
        if(action=="DELETE" && oldPojoo.isEmpty()) return;
        for (Object oldPojo : oldPojoo) { 
//        	System.out.println(oldPojo.getClass());
            Field[] fields = oldPojo.getClass().getDeclaredFields();

            
            Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(tableName);

            for (Field field : fields) {
                field.setAccessible(true); 
                try {
                    Object value = field.get(oldPojo); 
                    String mappedColumnName = fieldMapping.get(field.getName());
//                    System.out.println(mappedColumnName);

                    if (mappedColumnName != null && mappedColumnName.contains(".")) {
                  
                        String columnNameWithoutTablePrefix = mappedColumnName.split("\\.")[1];
                        oldValuesMap.put(columnNameWithoutTablePrefix, value != null ? value : "null");
                    } else {
                        oldValuesMap.put(field.getName(), value != null ? value : "null");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();  
                }
            }
            if(action=="DELETE")
            	System.out.println(oldValuesMap);
            deleteAuditLog();
        }


        }

    @Override
    public <T> List<T> executeSelect(Class<T> dummy, Map<String, String> columnFieldMapping) {
    	
        String sql = build();
//        System.out.println(sql);
//        System.out.println(parameters);
        List<T> resultList = new ArrayList<>();
        Map<Object, T> objectIns = new HashMap<>();

        try (Connection con = inTransaction ? transactionConnection : DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

         
            for (int i = 0; i < parameters.size(); i++) {
                pst.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = pst.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                Set<String> resultSetColumns = new HashSet<>();

              
                for (int i = 1; i <= columnCount; i++) {
                    String tableName = metaData.getTableName(i); 
                    String columnName = metaData.getColumnName(i); 
                    String fullColumnName = tableName + "." + columnName;
                    resultSetColumns.add(fullColumnName);

                }



                while (rs.next()) {
                    if (dummy == String.class || dummy == Integer.class) {
                        
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
//        System.out.println(resultList.get(0));

        return resultList;
    }


    @Override
    public int executeUpdate() {
        String sql = build(); 
        Connection con = null;
        PreparedStatement pst = null;
        
        if(shouldAudit()) {
        	selectAuditLog();
        	updateAuditLog();
//        	System.out.println(oldValuesMap);
//        	System.out.println(newValuesMap);
        	
        }

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
    @Override
    public int executeDelete() {
        String sql = build(); 
        Connection con = null;
        PreparedStatement pst = null;
        if(shouldAudit()) {
            
        	System.out.println(tableName);
        	System.out.println(updateCol);
        	System.out.println(updateId);
        	selectAuditLog();
//        	deleteAuditLog();
//        	updateAuditLog();
//        	System.out.println("oldVals: "+oldValuesMap);
//        	System.out.println("newVals: "+newValuesMap);
        	
        }

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
            

            int rowsDeleted = pst.executeUpdate();
   
            clear(); 
            if(rowsDeleted>0) return 1;
            
        } catch (SQLException e) {
  
            System.err.println("Error during DELETE operation: " + e.getMessage());
            e.printStackTrace();
        } finally {
      
            if (!inTransaction && con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        clear(); 
        return -1; 
    }


    private void clear() {
        query.setLength(0); 
        parameters.clear(); 
    }
}