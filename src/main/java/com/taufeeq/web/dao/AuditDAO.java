package com.taufeeq.web.dao;

import java.util.*;

import com.taufeeq.web.enums.Column;
import com.taufeeq.web.enums.Enum.Table;

public interface AuditDAO {
//	int insertQuery(Table table,String action, Map<String,Object> valuesMap);

	int insertQuery(Table table, String action, Map<String, Object> newValuesMap, Map<String, Object> oldValuesMap, String pk);
	public List<?> selectQuery(Table table, Column col, int id);
}
