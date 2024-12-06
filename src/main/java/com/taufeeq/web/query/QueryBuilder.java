package com.taufeeq.web.query;

import java.util.List;
import java.util.Map;

import com.taufeeq.web.enums.*;
//import com.taufeeq.web.enums.Table;
import com.taufeeq.web.enums.Enum.*;

public interface QueryBuilder {
	void beginTransaction();
    void commitTransaction();
    void rollbackTransaction();
    QueryBuilder insert(Table table, Column... columns);
    QueryBuilder values(Object... values);
    QueryBuilder where(Column condition,Object...value);
	QueryBuilder update(Table table);
    QueryBuilder set(Column dtformat, Object value);
    QueryBuilder deleteFrom(String table);
    QueryBuilder select(Column... columns);
    QueryBuilder from(Table table);
	QueryBuilder delete(Table table);
    QueryBuilder and(Column condition, Object value);
    QueryBuilder innerJoin(Table table2, Column onLeft,Table table1, Column onRight);
    QueryBuilder leftJoin(Table table2, Column onLeft, Table table1, Column onRight);
    String build(); 
    int executeInsert(); 
//    List<Map<String, Object>> executeSelect();
    int executeUpdate();
	QueryBuilder whereLessThan(Column condition, Object... values);
	QueryBuilder limit(int limit);
	<T> List<T> executeSelect(Class<T> clazz, Map<String, String> columnFieldMapping);
}
