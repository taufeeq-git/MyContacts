package com.taufeeq.web.dao;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static String objectToString(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append(obj.getClass().getSimpleName()).append("{");
        
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            try {
                sb.append(field.getName())
                  .append("=")
                  .append(field.get(obj))
                  .append(", ");
            } catch (IllegalAccessException e) {
                sb.append(field.getName()).append("=N/A, ");
            }
        }

        if (fields.length > 0) {
            sb.setLength(sb.length() - 2); // Remove the trailing ", "
        }
        sb.append("}");
        return sb.toString();
    }
}
