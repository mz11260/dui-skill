package com.zm.utils;


/**
 * 类型转换工具
 */
public class TypeUtil {

    /**
     * Object 转 Boolean
     * @param value obj
     * @return boolean
     */
    public static Boolean toBoolean(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("can not cast to boolean, parameter can not be null.");
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }
        if (value instanceof String) {
            String strVal = String.valueOf(value);
            if ("true".equalsIgnoreCase(strVal) //
                    || "1".equals(strVal)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(strVal) //
                    || "0".equals(strVal)) {
                return Boolean.FALSE;
            }
            if ("Y".equalsIgnoreCase(strVal) //
                    || "T".equals(strVal)) {
                return Boolean.TRUE;
            }
            if ("F".equalsIgnoreCase(strVal) //
                    || "N".equals(strVal)) {
                return Boolean.FALSE;
            }
        }
        throw new IllegalArgumentException("can not cast to boolean, value : " + value);
    }

    /**
     * Object转换Integer
     * @param value obj
     * @return integer
     */
    public static Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        throw new NumberFormatException("can not cast to Integer, value : " + value);
    }

    /**
     * Object 转 Long
     * @param value obj
     * @return integer
     */
    public static Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        throw new NumberFormatException("can not cast to Long, value : " + value);
    }

}
