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

import com.taobao.tdhs.jdbc.util.ConvertUtil;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author danchen
 */
public class HintStruct {
    private static Logger logger = Logger.getLogger(HintStruct.class);
    /*tdhs:<hash>[idx_ab(a,b)]*/
    private final String hintString;
    private String indexName;
    private List<String> listIndexColumns;
    private String errmsg;
    private int hash;

    public HintStruct(String hintString) {
        this.hintString = hintString.trim().replace(" ", "");
        this.indexName = "";
        this.errmsg = "";
        this.listIndexColumns = new LinkedList<String>();
    }

    public void AnalyzeHint() {
        int k = 0;
        if (!this.hintString.substring(0, 2).equals("/*")) {
            errmsg = "hint syntax is not right.";
            return;
        }
        k = k + 2;

        if (!(k + 5 < hintString.length() && hintString.substring(k, k + 5).equals("tdhs:"))) {
            errmsg = "hint syntax is not right.";
            return;
        }
        k = k + 5;

        int hash_left = hintString.indexOf("<");
        int hash_right = hintString.indexOf(">");
        if (hash_left > 0 && hash_left >= k && hash_right > 0 && hash_right > hash_left) {
            String hashCode = hintString.substring(hash_left + 1, hash_right);
            k += (hashCode.length() + 2);
            this.hash = ConvertUtil.safeConvertInt(hashCode, 0);
        }

        int idx_left = hintString.indexOf("[");
        int idx_right = hintString.indexOf("]");

        if (idx_left > 0 && idx_left >= k && idx_right > 0 && idx_right > idx_left) {
            String indexHint = hintString.substring(idx_left + 1, idx_right);
            k += (indexHint.length() + 2);
            int addr = indexHint.indexOf("(");
            if (addr > 0) {
                this.indexName = indexHint.substring(0, addr);
            } else {
                errmsg = "hint syntax is not right.";
                return;
            }

            int addr_right = indexHint.indexOf(")");
            if (addr_right > 0 && addr_right > addr) {
                String allColumns = indexHint.substring(addr + 1, addr_right);
                String[] array_column = allColumns.split(",");
                Collections.addAll(listIndexColumns, array_column);
            } else {
                errmsg = "hint syntax is not right.";
                return;
            }

        }

        if (!hintString.substring(k).equals("*/")) {
            errmsg = "hint syntax is not right.";
            return;
        }
    }

    public String getHintString() {
        return hintString;
    }

    public String getIndexName() {
        return indexName;
    }

    public List<String> getListIndexColumns() {
        return listIndexColumns;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public int getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "HintStruct{" +
                "hintString='" + hintString + '\'' +
                ", indexName='" + indexName + '\'' +
                ", listIndexColumns=" + listIndexColumns +
                ", errmsg='" + errmsg + '\'' +
                ", hash=" + hash +
                '}';
    }
}
