/*
 * Copyright(C) 2011-2012 Alibaba Group Holding Limited
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *
 *  Authors:
 *    wentong <wentong@taobao.com>
 */

package com.taobao.tdhs.jdbc.util;

import org.apache.commons.lang.StringUtils;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-7 上午9:41
 */
public final class StringUtil {

    public static boolean startsWithIgnoreCase(String searchIn, int startAt,
                                               String searchFor) {
        return searchIn.regionMatches(true, startAt, searchFor, 0, searchFor
                .length());
    }

    public static int indexOfIgnoreCaseRespectMarker(int startAt, String src,
                                                     String target, String marker, String markerCloses,
                                                     boolean allowBackslashEscapes) {
        char contextMarker = Character.MIN_VALUE;
        boolean escaped = false;
        int markerTypeFound = 0;
        int srcLength = src.length();
        int ind = 0;

        for (int i = startAt; i < srcLength; i++) {
            char c = src.charAt(i);

            if (allowBackslashEscapes && c == '\\') {
                escaped = !escaped;
            } else if (contextMarker != Character.MIN_VALUE && c == markerCloses.charAt(markerTypeFound) && !escaped) {
                contextMarker = Character.MIN_VALUE;
            } else if ((ind = marker.indexOf(c)) != -1 && !escaped
                    && contextMarker == Character.MIN_VALUE) {
                markerTypeFound = ind;
                contextMarker = c;
            } else if ((Character.toUpperCase(c) == Character.toUpperCase(target.charAt(0)) ||
                    Character.toLowerCase(c) == Character.toLowerCase(target.charAt(0))) && !escaped
                    && contextMarker == Character.MIN_VALUE) {
                if (startsWithIgnoreCase(src, i, target))
                    return i;
            }
        }
        return -1;
    }

    private final static String FIELD_ESCAPE_QUOTATION = "`";

    private final static String VALUE_ESCAPE_QUOTATION_1 = "\"";

    private final static String VALUE_ESCAPE_QUOTATION_2 = "\'";

    public static String escapeField(String field) {
        field = StringUtils.trim(field);
        if (StringUtils.startsWith(field, VALUE_ESCAPE_QUOTATION_1) || StringUtils.endsWith(field,
                VALUE_ESCAPE_QUOTATION_1) ||
                StringUtils.startsWith(field, VALUE_ESCAPE_QUOTATION_2) || StringUtils.endsWith(field,
                VALUE_ESCAPE_QUOTATION_2)) {
            return null;
        }

        if (StringUtils.startsWith(field, FIELD_ESCAPE_QUOTATION) &&
                !StringUtils.endsWith(field, FIELD_ESCAPE_QUOTATION)) {
            return null;
        }

        if (!StringUtils.startsWith(field, FIELD_ESCAPE_QUOTATION) &&
                StringUtils.endsWith(field, FIELD_ESCAPE_QUOTATION)) {
            return null;
        }

        if (StringUtils.startsWith(field, FIELD_ESCAPE_QUOTATION) &&
                StringUtils.endsWith(field, FIELD_ESCAPE_QUOTATION)) {
            if (field.length() > 1)
                return field.substring(1, field.length() - 1);
            else
                return null;
        }


        return field;
    }

    public static String escapeValue(String value) {
        value = StringUtils.trim(value);
        if (StringUtils.startsWith(value, FIELD_ESCAPE_QUOTATION) || StringUtils.endsWith(value,
                FIELD_ESCAPE_QUOTATION)) {
            return null;
        }
        if (StringUtils.startsWith(value, VALUE_ESCAPE_QUOTATION_1) &&
                !StringUtils.endsWith(value, VALUE_ESCAPE_QUOTATION_1)) {
            return null;
        }
        if (!StringUtils.startsWith(value, VALUE_ESCAPE_QUOTATION_1) &&
                StringUtils.endsWith(value, VALUE_ESCAPE_QUOTATION_1)) {
            return null;
        }
        if (StringUtils.startsWith(value, VALUE_ESCAPE_QUOTATION_2) &&
                !StringUtils.endsWith(value, VALUE_ESCAPE_QUOTATION_2)) {
            return null;
        }
        if (!StringUtils.startsWith(value, VALUE_ESCAPE_QUOTATION_2) &&
                StringUtils.endsWith(value, VALUE_ESCAPE_QUOTATION_2)) {
            return null;
        }

        if (StringUtils.startsWith(value, VALUE_ESCAPE_QUOTATION_1) &&
                StringUtils.endsWith(value, VALUE_ESCAPE_QUOTATION_1)) {
            if (value.length() > 1)
                return value.substring(1, value.length() - 1);
            else
                return null;
        }

        if (StringUtils.startsWith(value, VALUE_ESCAPE_QUOTATION_2) &&
                StringUtils.endsWith(value, VALUE_ESCAPE_QUOTATION_2)) {
            if (value.length() > 1)
                return value.substring(1, value.length() - 1);
            else
                return null;
        }

        return value;
    }

    public static String[] escapeIn(String value) {
        value = StringUtils.trim(value);
        if (!StringUtils.startsWith(value, "(") || !StringUtils.endsWith(value,
                ")")) {
            return null;
        }
        value = value.substring(1, value.length() - 1);
        String[] split = StringUtils.split(value, ",");
        String[] result = new String[split.length];
        int i = 0;
        for (String s : split) {
            String str = escapeValue(s);
            if (StringUtils.isBlank(str)) {
                return null;
            }
            result[i++] = str;
        }
        return result;
    }


    public static boolean isLong(String value) {
        try {
            Long.valueOf(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
