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

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
/**
 * 
 * @author danchen
 *
 */
public class HintStruct {
	private static Logger logger = Logger.getLogger(HintStruct.class);
	 /*tdhs:idx_ab(a,b)*/
	 private String hintString;
     private String indexName;
     private List<String> listIndexColumns;
     private String errmsg;
     
	public HintStruct(String hintString) {
		this.hintString=hintString.trim().replace(" ", "");
		this.indexName="";
		this.errmsg="";
		this.listIndexColumns=new LinkedList<String>();
	}
	
	public void AnalyzeHint(){
		int k=0;
		if(!this.hintString.substring(0, 2).equals("/*")){
			errmsg="hint systax is not right.";
			return;
		}
		k=k+2;
		
		if((k+5<hintString.length() && hintString.substring(k, k+5).equals("tdhs:"))==false){
			errmsg="hint systax is not right.";
			return;
		}
		k=k+5;
		
		int addr=hintString.indexOf("(");
		if(addr>0 && addr>k){
			this.indexName=hintString.substring(k, addr);
		}else {
			errmsg="hint systax is not right.";
			return;
		}
		
		int addr_right=hintString.indexOf(")");
		if(addr_right>0 && addr_right>addr){
			String allColumns=hintString.substring(addr+1, addr_right);
			String[] array_column=allColumns.split(",");
			for(int i=0;i<array_column.length;i++){
				listIndexColumns.add(array_column[i]);
			}
		}else {
			errmsg="hint systax is not right.";
			return;
		}
		
		if(hintString.substring(addr_right+1).equals("*/")==false){
			errmsg="hint systax is not right.";
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

	@Override
	public String toString() {
		return "HintStruct [hintString=" + hintString + ", indexName="
				+ indexName + ", listIndexColumns=" + listIndexColumns
				+ ", errmsg=" + errmsg + "]";
	}
     
     
}
