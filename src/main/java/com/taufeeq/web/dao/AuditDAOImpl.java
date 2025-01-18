package com.taufeeq.web.dao;

import java.sql.SQLException;
import java.util.*;

import com.taufeeq.web.enums.Column;
import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.helper.FieldMapperHelper;
import com.taufeeq.web.querybuilder.*;
import com.taufeeq.web.helper.JsonValueBuilder; 

public class AuditDAOImpl implements AuditDAO {
	QueryBuilder queryBuilder;
	private JsonValueBuilder jsonValueBuilder; 

	public AuditDAOImpl() {
		this.jsonValueBuilder = new JsonValueBuilder();
		this.queryBuilder = QueryBuilderFactory.getQueryBuilder(); 
	}


	public int insertQuery(Table table, String action, Map<String, Object> newValuesMap,
			Map<String, Object> oldValuesMap, String pk) {

		String tableName = table.toString();
		String oldValJson = "{}";
		String newValJson = "{}";

		if ("UPDATE".equals(action)) {
			oldValJson = jsonValueBuilder.buildJsonStringForUpdate(newValuesMap, oldValuesMap, pk);
			newValJson = jsonValueBuilder.buildJsonStringForInsertOrUpdate(newValuesMap);

		} else if ("DELETE".equals(action)) {
			oldValJson = jsonValueBuilder.buildJsonStringForDelete(oldValuesMap);

		} else if ("INSERT".equals(action)) {
			newValJson = jsonValueBuilder.buildJsonStringForInsertOrUpdate(newValuesMap);
		}


		try {
			return queryBuilder
					.insert(Table.audit_logs, audit_logs.tableName, audit_logs.action, audit_logs.newValue,
							audit_logs.oldValue)
					.values(tableName, action, newValJson, oldValJson).executeInsert();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<?> selectQuery(Table table, Column col, Object updateId) {
		Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(table);

		List<?> res = null;
		try {
			res = queryBuilder.selectAll().from(table).where(col, EnumComparator.EQUAL, updateId)
					.executeSelect(table.getClazz(), fieldMapping);
		} catch (SQLException | ReflectiveOperationException e) {
			e.printStackTrace();
		}

		return res;

	}

}