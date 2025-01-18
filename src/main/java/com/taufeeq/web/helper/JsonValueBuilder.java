package com.taufeeq.web.helper;

import java.util.Map;
import java.util.Objects;

public class JsonValueBuilder {

	public String buildJsonStringForUpdate(Map<String, Object> newValuesMap, Map<String, Object> oldValuesMap,
			String pk) {
		StringBuilder oldVal = new StringBuilder("{");
		StringBuilder newVal = new StringBuilder("{");

		for (Map.Entry<String, Object> entry : newValuesMap.entrySet()) {
			String key = entry.getKey();
			Object newValue = entry.getValue();
			Object oldValue = oldValuesMap.get(key);

			if (key.equals(pk)) {
				appendKeyValueToJson(oldVal, key, oldValue);
				appendKeyValueToJson(newVal, key, newValue);
				continue;
			}

			if (!Objects.equals(oldValue, newValue)) {
				appendKeyValueToJson(oldVal, key, oldValue);
				appendKeyValueToJson(newVal, key, newValue);
			}
		}

		return finalizeJsonString(oldVal).toString();
	}

	public String buildJsonStringForDelete(Map<String, Object> oldValuesMap) {
		StringBuilder oldVal = new StringBuilder("{");
		if (oldValuesMap != null) {
			for (Map.Entry<String, Object> entry : oldValuesMap.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (isEmptyValue(value)) {
					continue;
				}
				appendKeyValueToJson(oldVal, key, value);
			}
		}
		return finalizeJsonString(oldVal).toString();
	}

	public String buildJsonStringForInsertOrUpdate(Map<String, Object> newValuesMap) {
		StringBuilder newVal = new StringBuilder("{");
		if (newValuesMap != null) {
			for (Map.Entry<String, Object> entry : newValuesMap.entrySet()) {
				appendKeyValueToJson(newVal, entry.getKey(), entry.getValue());
			}
		}
		return finalizeJsonString(newVal).toString();
	}

	private void appendKeyValueToJson(StringBuilder jsonBuilder, String key, Object value) {
		if (jsonBuilder.length() > 1 && jsonBuilder.charAt(jsonBuilder.length() - 1) != '{') {
			jsonBuilder.append(",");
		}
		jsonBuilder.append("\"").append(key).append("\":");
		appendValueToJson(jsonBuilder, value);
	}

	private void appendValueToJson(StringBuilder jsonBuilder, Object value) {
		if (value == null) {
			jsonBuilder.append("null");
		} else if (value instanceof Number) {
			jsonBuilder.append(value);
		} else if (value instanceof Boolean) {
			jsonBuilder.append(value);
		} else {
			jsonBuilder.append("\"").append(String.valueOf(value)).append("\"");
		}
	}

	private StringBuilder finalizeJsonString(StringBuilder builder) {
		if (builder.length() > 1 && builder.charAt(builder.length() - 1) == ',') {
			builder.setLength(builder.length() - 1);
		}
		builder.append("}");
		return builder;
	}

	private boolean isEmptyValue(Object value) {
		if (value == null) {
			return true;
		}
		String valueAsString = String.valueOf(value);
		return valueAsString.trim().isEmpty() || valueAsString.equals("null") || valueAsString.equals("0")
				|| valueAsString.equals("[]")
				|| (value instanceof java.util.List && ((java.util.List<?>) value).isEmpty());
	}
}