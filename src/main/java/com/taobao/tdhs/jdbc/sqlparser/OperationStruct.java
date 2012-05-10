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

package com.taobao.tdhs.jdbc.sqlparser;

import org.apache.commons.lang.StringUtils;

/**
 * @author danchen
 */
public class OperationStruct {
    private String columnName;
    private String oper;
    private String value;

    public OperationStruct() {
        super();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        oper = StringUtils.trim(oper);
        if ("is".equalsIgnoreCase(oper)) {
            this.oper = "=";
        } else {
            this.oper = oper;
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationStruct)) return false;

        OperationStruct that = (OperationStruct) o;

        if (columnName != null ? !columnName.equals(that.columnName) : that.columnName != null) return false;
        if (oper != null ? !oper.equals(that.oper) : that.oper != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = columnName != null ? columnName.hashCode() : 0;
        result = 31 * result + (oper != null ? oper.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OperationStruct [columnName=" + columnName + ", oper=" + oper
                + ", value=" + value + "]";
    }


}
