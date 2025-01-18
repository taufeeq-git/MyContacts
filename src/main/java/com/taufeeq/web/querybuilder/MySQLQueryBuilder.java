package com.taufeeq.web.querybuilder;

import com.taufeeq.web.auditlog.AuditLogProcessor;
import com.taufeeq.web.dao.AuditDAOImpl;
import com.taufeeq.web.dbcp.DatabaseConnectionPool;
import com.taufeeq.web.enums.Column;
import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.helper.FieldMapperHelper;
import com.taufeeq.web.logger.CustomLogger;

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
	private Column updatedColumn;
	private Object updatedColumValue;
	private Table tableName;
	private Column primaryKeyColumn;
	private String primaryKeyString;
	private List<String> columnNames = new ArrayList<>();
	private Map<String, Object> oldValuesMap = new HashMap<>();
	private Map<String, Object> newValuesMap = new HashMap<>();
	private Boolean foundPrimaryKey = false;
	private boolean inTransaction = false;
	private Connection transactionConnection;
	private static final AuditLogProcessor auditLogProcessor = new AuditLogProcessor();
	private static final CustomLogger logger = CustomLogger.getInstance();

	public MySQLQueryBuilder() {
		this.query = new StringBuilder();
		this.parameters = new ArrayList<>();
	}

		public void beginTransaction() throws SQLException {
			try {
				transactionConnection = DatabaseConnectionPool.getDataSource().getConnection();
				transactionConnection.setAutoCommit(false);
				inTransaction = true;
			} catch (SQLException e) {
				logger.errorApp("Error starting transaction: " + e.getMessage());
				throw new SQLException("Error starting transaction:", e);
			}
		}

		@Override
		public void commitTransaction() throws SQLException {
			if (inTransaction && transactionConnection != null) {
				try {
					transactionConnection.commit();
					transactionConnection.setAutoCommit(true);
				} catch (SQLException e) {
					logger.errorApp("Error committing transaction: " + e.getMessage());
					throw new SQLException("Error committing transaction:", e);
				} finally {
					closeTransactionConnection();
					inTransaction = false;
				}
			} else {
				logger.warnApp("Commit transaction called outside of a transaction context or with null connection.");
			}
		}

		@Override
		public void rollbackTransaction() throws SQLException { 
			if (inTransaction && transactionConnection != null) {
				try {
					transactionConnection.rollback();
					transactionConnection.setAutoCommit(true);
				} catch (SQLException e) {
					logger.errorApp("Error rolling back transaction: " + e.getMessage());
					throw new SQLException("Error rolling back transaction:", e);
				} finally {
					closeTransactionConnection();
					inTransaction = false;
				}
			} else {
				logger.warnApp("Rollback transaction called outside of a transaction context or with null connection.");
			}
		}

		@Override
		public void closeTransactionConnection() {
			try {
				if (transactionConnection != null && !transactionConnection.isClosed()) {
					transactionConnection.setAutoCommit(true);
					transactionConnection.close();
				}
			} catch (SQLException e) {
				logger.warnApp("Error closing transaction connection: " + e.getMessage());
			}
		}

	@Override
	public QueryBuilder insert(Table table, Column... columns) {
		action = "INSERT";
		this.tableName = table;
		primaryKeyColumn = columns[0].getPrimaryKey();
		primaryKeyString = primaryKeyColumn.getSimpleColumnName();

		StringBuilder columnNamesBuilder = new StringBuilder();

		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			columnNames.add(column.getSimpleColumnName());
			if (column.getSimpleColumnName() == primaryKeyString)
				foundPrimaryKey = true;

			columnNamesBuilder.append(column.getColumnName());
			if (i < columns.length - 1) {
				columnNamesBuilder.append(", ");
			}
		}

		String columnNamesString = columnNamesBuilder.toString();

		query.append("INSERT INTO ").append(table).append(" (").append(columnNamesString).append(") ");

		return this;
	}

	@Override
	public QueryBuilder delete(Table table) {
		this.tableName = table;
		action = "DELETE";
		query.append("DELETE FROM ").append(table);
		return this;
	}

	@Override
	public QueryBuilder values(Object... values) {

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
	public QueryBuilder where(Column condition, EnumComparator comparator, Object value) {
		if (action == "UPDATE" || action == "DELETE") {
			updatedColumn = condition;
			updatedColumValue = value;
			newValuesMap.put(condition.getSimpleColumnName(), value);
		}
		query.append(" WHERE ").append(condition.getColumnName()).append(" ").append(comparator.getSymbol())
				.append(" ?");

		if (action == "UPDATE") {

		}
		parameters.add(value);

		return this;
	}

	@Override
	public QueryBuilder whereDiff(long time, Column column1, String operator, long val) {
		query.append(" WHERE (").append(time).append(" - ").append(column1.getColumnName()).append(" ").append(operator)
				.append(" ?)");
		parameters.add(val);
		return this;
	}

	@Override
	public QueryBuilder conjunction(EnumConjunction conjunction, Column condition, EnumComparator comparator,
			Object value) {
		query.append(" " + conjunction.getConjucntion() + " ").append(condition.getColumnName()).append(" ")
				.append(comparator.getSymbol()).append(" ?");
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

		action = "UPDATE";

		this.tableName = table;
		query.append("UPDATE ").append(table).append(" ");
		return this;
	}

	@Override
	public QueryBuilder set(Column column, Object value) {

		primaryKeyColumn = column.getPrimaryKey();
		primaryKeyString = primaryKeyColumn.getSimpleColumnName();

		if (query.indexOf("SET") == -1) {
			newValuesMap.put(column.getSimpleColumnName(), value);
			query.append("SET ");
		} else {
			newValuesMap.put(column.getSimpleColumnName(), value);
			query.append(", ");
		}

		query.append(column).append(" = ? ");

		parameters.add(value);

		return this;
	}

	@Override
	public QueryBuilder select(Column... columns) {
		String columnNames = Arrays.stream(columns).map(Column::getColumnName).collect(Collectors.joining(", "));
		query.append("SELECT ").append(columnNames).append(" ");
		return this;
	}

	@Override
	public QueryBuilder selectAll() {
		query.append("SELECT ").append("* ");
		return this;
	}

	@Override
	public QueryBuilder orderBy(Column columnName, EnumOrder order) {
		query.append(" ORDER BY ").append(columnName).append(" " + order.getOrder() + " ");
		return this;
	}

	@Override
	public QueryBuilder join(EnumJoin joinType, Table table2, Column onLeft, Table table1, Column onRight) {
		query.append(joinType.getJoin() + " ").append(table2).append(" ON ").append(table1).append(".").append(onLeft)
				.append(" = ").append(table2).append(".").append(onRight).append(" ");
		return this;
	}

	@Override
	public String build() {
		String builtQuery = query.toString().trim();
		logger.infoApp("Built SQL Query: " + builtQuery + ", Parameters: " + parameters);
		return builtQuery;
	}

	@Override
	public <T> List<T> executeSelect(Class<T> unknownClass, Map<String, String> columnFieldMapping) throws SQLException, ReflectiveOperationException { // Added throws
		String sql = build();
		List<T> resultList = new ArrayList<>();
		Map<Object, T> objectInstances = new LinkedHashMap<>();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			pst = con.prepareStatement(sql);
			setParameters(pst);
			rs = pst.executeQuery();
			Set<String> resultSetColumns = extractResultSetColumns(rs);
			processResultSet(rs, unknownClass, columnFieldMapping, resultList, objectInstances, resultSetColumns);

		} catch (SQLException e) {
			logger.errorApp("Error executing SELECT query: " + sql + ", Parameters: " + parameters + ", Error: " + e.getMessage());
			throw e; 
		} catch (ReflectiveOperationException e) {
			logger.errorApp("Reflection error during SELECT query processing: " + sql + ", Error: " + e.getMessage());
			throw e; 
		} finally {
			closeResources(con, pst, rs);
			clear();
		}
		resultList.addAll(objectInstances.values());
		return resultList;
	}

	@Override
	public int executeInsert() throws SQLException {
		String sql = build();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rsGeneratedKeys = null;
		int generatedKey = -1;

		try {
			con = getConnection();
			pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setParameters(pst);
			pst.executeUpdate();
			rsGeneratedKeys = pst.getGeneratedKeys();
			if (rsGeneratedKeys.next()) {
				generatedKey = rsGeneratedKeys.getInt(1);
			} else {
				generatedKey = -1; 
			}

			if (!foundPrimaryKey) {
				newValuesMap.put(primaryKeyString, generatedKey);
			}
			if (shouldAudit()) {
				submitAuditLogTask();
			}
			foundPrimaryKey = false;

		} catch (SQLException e) {
			logger.errorApp("Error executing INSERT query: " + sql + ", Parameters: " + parameters + ", Error: " + e.getMessage());
			throw new SQLException("Error executing INSERT query:", e); 
		} finally {
			closeResources(con, pst, rsGeneratedKeys);
			clear();
		}
		return generatedKey;
	}

	@Override
	public int executeUpdate() throws SQLException { 
		String sql = build();
		Connection con = null;
		PreparedStatement pst = null;
		int rowsAffected = -1;

		if (shouldAudit()) {
			selectAuditLog(); 
			updateAuditLog();
		}

		try {
			con = getConnection();
			pst = con.prepareStatement(sql);
			setParameters(pst);
			rowsAffected = pst.executeUpdate();

		} catch (SQLException e) {
			logger.errorApp("Error executing UPDATE query: " + sql + ", Parameters: " + parameters + ", Error: " + e.getMessage());
			throw new SQLException("Error executing UPDATE query:", e); 
		} finally {
			closeResources(con, pst, null);
			clear();
		}
		return rowsAffected;
	}

	@Override
	public int executeDelete() throws SQLException { 
		String sql = build();
		Connection con = null;
		PreparedStatement pst = null;
		int rowsDeleted = -1;

		if (shouldAudit()) {
			AuditDAOImpl auditDAOImpl = new AuditDAOImpl();
			List<?> oldPojos = auditDAOImpl.selectQuery(tableName, updatedColumn, updatedColumValue); 
			if (!oldPojos.isEmpty()) {
				selectAuditLog();
				deleteAuditLog();
			}
		}

		try {
			con = getConnection();
			pst = con.prepareStatement(sql);
			setParameters(pst);
			rowsDeleted = pst.executeUpdate();

		} catch (SQLException e) {
			logger.errorApp("Error executing DELETE query: " + sql + ", Parameters: " + parameters + ", Error: " + e.getMessage());
			throw new SQLException("Error executing DELETE query:", e);
		} finally {
			closeResources(con, pst, null);
			clear();
		}
		return (rowsDeleted > 0) ? 1 : -1;
	}


	private Connection getConnection() throws SQLException { 
		try {
			return inTransaction ? transactionConnection
					: DatabaseConnectionPool.getDataSource().getConnection();
		} catch (SQLException e) {
			logger.errorApp("Error getting database connection: " + e.getMessage());
			throw new SQLException("Error getting database connection:", e);
		}
	}

	private void setParameters(PreparedStatement pst) throws SQLException {
		int size = parameters.size();
		for (int i = 0; i < size; i++) {
			pst.setObject(i + 1, parameters.get(i));
		}
	}

	private Set<String> extractResultSetColumns(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		Set<String> resultSetColumns = new HashSet<>();

		for (int i = 1; i <= columnCount; i++) {
			String tableName = metaData.getTableName(i);
			String columnName = metaData.getColumnName(i);
			String fullColumnName = tableName + "." + columnName;
			resultSetColumns.add(fullColumnName);
		}
		return resultSetColumns;
	}

	private <T> void processResultSet(ResultSet rs, Class<T> unknownClass, Map<String, String> columnFieldMapping,
			List<T> resultList, Map<Object, T> objectInstances, Set<String> resultSetColumns)
			throws SQLException, ReflectiveOperationException {

		while (rs.next()) {
			if (unknownClass == String.class || unknownClass == Integer.class) {
				processPrimitiveRow(rs, unknownClass, resultList, rs.getMetaData().getColumnCount());
			} else {
				processPojoRow(rs, unknownClass, columnFieldMapping, objectInstances, resultSetColumns);
			}
		}
	}

	private <T> void processPrimitiveRow(ResultSet rs, Class<T> unknownClass, List<T> resultList, int columnCount)
			throws SQLException {
		if (columnCount == 1) {
			Object value = rs.getObject(1);
			resultList.add(unknownClass.cast(value));
		} else {
			logger.errorApp("The primitive return type has more than1 column");
		}
	}

	private <T> void processPojoRow(ResultSet rs, Class<T> unknownClass, Map<String, String> columnFieldMapping,
			Map<Object, T> objectInstances, Set<String> resultSetColumns)
			throws SQLException, ReflectiveOperationException {

		Object primaryKey = rs.getObject(1);
		T obj = objectInstances.computeIfAbsent(primaryKey, key -> {
			try {
				return unknownClass.getDeclaredConstructor().newInstance();
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException("Error creating instance of " + unknownClass.getName(), e);
			}
		});

		for (Field field : unknownClass.getDeclaredFields()) {
			field.setAccessible(true);
			if (List.class.isAssignableFrom(field.getType())) {
				populateListField(rs, field, obj, columnFieldMapping, resultSetColumns);
			} else {
				populateRegularField(rs, field, obj, columnFieldMapping, resultSetColumns);
			}
		}
	}

	private <T> void populateListField(ResultSet rs, Field field, T obj, Map<String, String> columnFieldMapping,
			Set<String> resultSetColumns) throws SQLException, ReflectiveOperationException {

		ParameterizedType listType = (ParameterizedType) field.getGenericType();
		Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];

		@SuppressWarnings("unchecked")
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
				if (listField.getType() == long.class && value == null) {
					value = 0L;
				}
				listField.set(listElement, value);
			}
		}
		if (!list.contains(listElement)) {
			list.add(listElement);
		}
	}

	private <T> void populateRegularField(ResultSet rs, Field field, T obj, Map<String, String> columnFieldMapping,
			Set<String> resultSetColumns) throws SQLException, ReflectiveOperationException {

		String columnMapping = columnFieldMapping.get(field.getName());
		if (columnMapping != null && resultSetColumns.contains(columnMapping)) {
			Object value = rs.getObject(columnMapping);
			field.set(obj, value);
		}
	}

	private void closeResources(Connection con, PreparedStatement pst, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.warnApp("Error closing ResultSet: " + e.getMessage());
				e.printStackTrace();
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				logger.warnApp("Error closing PreparedStatement: " + e.getMessage());
				e.printStackTrace();
			}
		}
		if (con != null && !inTransaction) {
			try {
				con.close();
			} catch (SQLException e) {
				logger.warnApp("Error closing Connection: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private void submitAuditLogTask() {
		auditLogProcessor.submitAuditLogTask(() -> {
			try {
				insertAuditLog();
			} catch (Exception e) {
				logger.errorApp("Error in audit log task, retrying: " + e.getMessage());
				e.printStackTrace();

				auditLogProcessor.submitAuditLogTask(() -> insertAuditLog());
			}
		});
	}

	private boolean shouldAudit() {
		return tableName != Table.usersessions && tableName != Table.audit_logs;
	}

	private void insertAuditLog() {
		AuditDAOImpl auditDAOImpl = new AuditDAOImpl();
		auditDAOImpl.insertQuery(tableName, "INSERT", newValuesMap, oldValuesMap, primaryKeyString);
		clearMap();
	}

	private void updateAuditLog() {
		AuditDAOImpl auditDAOImpl = new AuditDAOImpl();
		auditDAOImpl.insertQuery(tableName, "UPDATE", newValuesMap, oldValuesMap, primaryKeyString);
		clearMap();
	}

	private void deleteAuditLog() {
		AuditDAOImpl auditDAOImpl = new AuditDAOImpl();
		auditDAOImpl.insertQuery(tableName, "DELETE", newValuesMap, oldValuesMap, primaryKeyString);
		clearMap();
	}

	private void selectAuditLog() {
		AuditDAOImpl auditDAOImpl = new AuditDAOImpl();
		List<?> oldPojos = auditDAOImpl.selectQuery(tableName, updatedColumn, updatedColumValue);

		Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(tableName);

		for (Object oldPojo : oldPojos) {
			Field[] fields = oldPojo.getClass().getDeclaredFields();
			for (Field field : fields) {
				processPojoFieldForAudit(oldPojo, field, fieldMapping);
			}
		}
	}

	private void processPojoFieldForAudit(Object oldPojo, Field field, Map<String, String> fieldMapping) {
		field.setAccessible(true);
		try {
			Object value = field.get(oldPojo);
			String mappedColumnName = fieldMapping.get(field.getName());
			storeOldValue(field, value, mappedColumnName);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void storeOldValue(Field field, Object value, String mappedColumnName) {
		if (mappedColumnName != null && mappedColumnName.contains(".")) {
			String columnNameWithoutTablePrefix = mappedColumnName.split("\\.")[1];
			oldValuesMap.put(columnNameWithoutTablePrefix, value != null ? value : "null");
		} else {
			oldValuesMap.put(field.getName(), value != null ? value : "null");
		}
	}

	private void clearMap() {
		oldValuesMap.clear();
		newValuesMap.clear();
	}

	private void clear() {
		query.setLength(0);
		parameters.clear();
	}
}