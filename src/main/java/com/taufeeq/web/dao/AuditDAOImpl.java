package com.taufeeq.web.dao;

import java.util.*;

import com.taufeeq.web.enums.Column;
import com.taufeeq.web.enums.Enum.*;
import com.taufeeq.web.helpermap.FieldMapperHelper;
import com.taufeeq.web.querybuilder.*;

public class AuditDAOImpl implements AuditDAO {
	QueryBuilder queryBuilder;

	public int insertQuery(Table table, String action, Map<String, Object> newValuesMap, Map<String, Object> oldValuesMap, String pk) {
//	    StringBuilder oldVal = new StringBuilder();
//	    StringBuilder newVal = new StringBuilder();
	    
	    
//	    System.out.println("OVinDAO = "+oldValuesMap);
//	    System.out.println("NVinDAO = "+newValuesMap);
//	    
	    String tableName=table.toString();

	    StringBuilder oldVal = new StringBuilder("{");
	    StringBuilder newVal = new StringBuilder("{");

	    // Handle the case where both maps are populated
	    if ("UPDATE".equals(action)) {
	        for (Map.Entry<String, Object> entry : newValuesMap.entrySet()) {
	            String key = entry.getKey();

	            if (entry.getValue() != null && entry.getValue().getClass() == String.class) {
	                String newValue = entry.getValue().toString();
	                String oldValue = oldValuesMap.get(key) != null ? oldValuesMap.get(key).toString() : "null";

	                // Check if values are different
	                if (!oldValue.equals(newValue)) {
	                    if (!"null".equals(oldValue)) {
	                        oldVal.append("\"").append(key).append("\":\"").append(oldValue).append("\",");
	                    }
	                    if (!"null".equals(newValue)) {
	                        newVal.append("\"").append(key).append("\":\"").append(newValue).append("\",");
	                    }
	                }
	            } else {
	                Object newValue = entry.getValue();
	                Object oldValue = oldValuesMap.get(key);

	                if (key.equals(pk)) {
	                    oldVal.append("\"").append(key).append("\":\"").append(oldValue != null ? oldValue : "null").append("\",");
	                    newVal.append("\"").append(key).append("\":\"").append(newValue != null ? newValue : "null").append("\",");
	                    continue;
	                }

	                // Check if values are different
	                if (!Objects.equals(oldValue, newValue)) {
	                    if (oldValue != null) {
	                        oldVal.append("\"").append(key).append("\":\"").append(oldValue).append("\",");
	                    }
	                    if (newValue != null) {
	                        newVal.append("\"").append(key).append("\":\"").append(newValue).append("\",");
	                    }
	                }
	            }
	        }
	    } else if ("DELETE".equals(action)) {
	        if (oldValuesMap != null) {
	            for (Map.Entry<String, Object> entry : oldValuesMap.entrySet()) {
	                String key = entry.getKey();
	                Object value = entry.getValue();

	                String valueAsString = String.valueOf(value);

	                // Skip null, empty, or zero-like values
	                if (value == null || valueAsString.equals("null") || valueAsString.trim().isEmpty() || valueAsString.equals("0") || valueAsString.equals("[]") || value instanceof java.util.List && ((java.util.List<?>) value).isEmpty()) {
	                    continue;
	                }

	                oldVal.append("\"")
	                      .append(key)
	                      .append("\":\"")
	                      .append(valueAsString)
	                      .append("\",");
	            }
	        }
	    } else if ("INSERT".equals(action)) {
	        if (newValuesMap != null) {
	            for (Map.Entry<String, Object> entry : newValuesMap.entrySet()) {
	                newVal.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue() != null ? entry.getValue() : "null").append("\",");
	            }
	        }
	    }

	    // Remove trailing commas and close the braces
	    if (oldVal.length() > 1) {
	        oldVal.setLength(oldVal.length() - 1);
	    }
	    oldVal.append("}");

	    if (newVal.length() > 1) {
	        newVal.setLength(newVal.length() - 1);
	    }
	    newVal.append("}");

	   		
		queryBuilder = QueryBuilderFactory.getQueryBuilder();

		 return queryBuilder.insert(Table.audit_logs,
				 audit_logs.tableName,
				 audit_logs.action,
				 audit_logs.newValue,
				 audit_logs.oldValue
				 )
				 .values(tableName,action,newVal.toString(),oldVal.toString())
				 .executeInsert();		
	}
	
	public List<?> selectQuery(Table table, Column col, Object updateId){
		Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(table);
//		System.out.println(fieldMapping);

		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		List<?> res=queryBuilder.selectAll()
		.from(table)
		.where(col, updateId)
		.executeSelect(table.getClazz(), fieldMapping);
//		System.out.println(table.getClazz());

//		System.out.println(res);
		
		return res;

		
	}

}
