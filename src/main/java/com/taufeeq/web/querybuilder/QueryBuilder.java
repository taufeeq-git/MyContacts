package com.taufeeq.web.querybuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.taufeeq.web.enums.*;
import com.taufeeq.web.enums.Enum.*;

public interface QueryBuilder {
	void beginTransaction() throws SQLException;

	void commitTransaction() throws SQLException;

	void rollbackTransaction() throws SQLException;
	
	void closeTransactionConnection();

	QueryBuilder insert(Table table, Column... columns);

	QueryBuilder values(Object... values);

	QueryBuilder where(Column condition, EnumComparator comparator, Object values);
	
	QueryBuilder conjunction(EnumConjunction conjunction, Column condition, EnumComparator comparator, Object value);
	
	QueryBuilder whereDiff(long time, Column column1, String operator, long val);

	QueryBuilder update(Table table);

	QueryBuilder set(Column dtformat, Object value);

	QueryBuilder select(Column... columns);
	
	QueryBuilder selectAll();

	QueryBuilder from(Table table);

	QueryBuilder delete(Table table);
	
	QueryBuilder join(EnumJoin joinType,Table table2, Column onLeft, Table table1, Column onRight);
	
	QueryBuilder orderBy(Column columnName, EnumOrder order);

	String build();

	int executeInsert() throws SQLException;

	int executeUpdate() throws SQLException;

	QueryBuilder limit(int limit);

	<T> List<T> executeSelect(Class<T> clazz, Map<String, String> columnFieldMapping) throws SQLException, ReflectiveOperationException;

	int executeDelete() throws SQLException;
}
