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

package com.taobao.tdhs.client.response;

import com.taobao.tdhs.client.common.IMySQLHandlerErrorCodes;
import com.taobao.tdhs.client.common.MySQLHandlerErrorCodes;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.util.ByteOrderUtil;
import com.taobao.tdhs.client.util.ConvertUtil;
import org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-11-1 上午11:16
 */
public class TDHSResponse {

    private TDHSResponseEnum.IClientStatus status;

    private TDHSResponseEnum.IErrorCode errorCode;

    private int dbErrorCode;

    private int fieldNumber;

    private List<TDHSResponseEnum.IFieldType> fieldTypes;

    private List<List<String>> fieldData;

    private List<List<byte[]>> fieldOriData;

    private byte data[];

    private String charsetName;

    private boolean isParsed = false;

    private TDHSMetaData metaData;


    /**
     * Constructor TDHSResponse creates a new TDHSResponse instance.
     *
     * @param status      of type ClientStatus    ,client status from sever-side
     * @param metaData    of type TDHSMetaData    ,meta data from client
     * @param data        of type byte[]          ,data from server-side need to parse
     * @param charsetName of type String          ,charest for decoding
     */
    public TDHSResponse(TDHSResponseEnum.IClientStatus status, TDHSMetaData metaData, byte[] data,
                        String charsetName) {
        this.status = status;
        this.metaData = metaData;
        this.data = data;
        if (StringUtils.isNotBlank(charsetName)) {
            this.charsetName = charsetName;
        }
    }

    /**
     * parse the data received from server
     *
     * @throws TDHSException when has unknow response code
     */
    private synchronized void parse() throws TDHSException {
        if (isParsed) {
            return;
        }
        if (400 <= status.getStatus() && 600 > status.getStatus()) {
            parseFailed(this.data);
        } else if (TDHSResponseEnum.ClientStatus.OK.equals(status)) {
            parseData(this.data);
        } else {
            throw new TDHSException("unknown response code!");
        }
        this.data = null;
        isParsed = true;
    }

    /**
     * parse the data if the client status is not successed
     *
     * @param data of type byte[] ,data from server-side need to parse
     */
    private void parseFailed(final byte data[]) {
        int code = (int) ByteOrderUtil.getUnsignInt(data, 0);
        if (status == TDHSResponseEnum.ClientStatus.DB_ERROR) {
            dbErrorCode = code;
        } else {
            errorCode = TDHSResponseEnum.ErrorCode.valueOf(code);
        }
    }

    /**
     * parse the data if the client status is successed
     *
     * @param data of type byte[] ,data from server-side need to parse
     */
    private void parseData(final byte data[]) {
        int len = data.length;
        int pos = 0;
        fieldOriData = new ArrayList<List<byte[]>>();
        //read field number
        fieldNumber = (int) ByteOrderUtil.getUnsignInt(data, pos);
        pos += 4;
        fieldTypes = new ArrayList<TDHSResponseEnum.IFieldType>(fieldNumber);
        for (int i = 0; i < fieldNumber; i++) {
            fieldTypes.add(TDHSResponseEnum.FieldType.valueOf(data[pos] & 0xFF));
            pos++;
        }
        while (pos < len) {
            List<byte[]> record = new ArrayList<byte[]>(fieldNumber);
            for (int i = 0; i < fieldNumber; i++) {
                int fieldLength = (int) ByteOrderUtil.getUnsignInt(data, pos);
                pos += 4;
                if (fieldLength > 0) {
                    byte f[] = new byte[fieldLength];
                    System.arraycopy(data, pos, f, 0, fieldLength);
                    record.add(f);
                    pos += fieldLength;
                } else {
                    record.add(new byte[0]);
                }
            }
            fieldOriData.add(record);
        }
    }


    /**
     * Method parseFieldData ...
     *
     * @throws TDHSException when
     */
    private void parseFieldData() throws TDHSException {
        parse();
        fieldData = new ArrayList<List<String>>();
        if (fieldOriData != null && !fieldOriData.isEmpty()) {
            try {
                for (List<byte[]> record : fieldOriData) {
                    if (record != null) {
                        List<String> strRecord = new ArrayList<String>(record.size());
                        for (byte[] f : record) {
                            strRecord.add(ConvertUtil.getStringFromByte(f, this.charsetName));
                        }
                        fieldData.add(strRecord);
                    } else {
                        fieldData.add(null);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw new TDHSException(e);
            }
        }
    }

    /**
     * Method getCharsetName returns the charsetName of this TDHSResponse object.
     *
     * @return the charsetName (type String) of this TDHSResponse object.
     */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * Method setCharsetName sets the charsetName of this TDHSResponse object.
     *
     * @param charsetName the charsetName of this TDHSResponse object.
     */
    public void setCharsetName(String charsetName) {
        if (StringUtils.isNotBlank(charsetName)) {
            this.charsetName = charsetName;
        }
    }


    /**
     * Method getStatus returns the status of this TDHSResponse object.
     *
     * @return the status (type ClientStatus) of this TDHSResponse object.
     */
    public TDHSResponseEnum.IClientStatus getStatus() {
        return status;
    }

    /**
     * Method getErrorCode returns the errorCode of this TDHSResponse object.
     *
     * @return the errorCode (type ErrorCode) of this TDHSResponse object.
     *
     * @throws TDHSException when
     */
    public TDHSResponseEnum.IErrorCode getErrorCode() throws TDHSException {
        parse();
        return errorCode;
    }

    /**
     * Method getFieldNumber returns the fieldNumber of this TDHSResponse object.
     *
     * @return the fieldNumber (type int) of this TDHSResponse object.
     *
     * @throws TDHSException when
     */
    public int getFieldNumber() throws TDHSException {
        parse();
        return fieldNumber;
    }

    /**
     * Method getFieldTypes returns the fieldTypes of this TDHSResponse object.
     *
     * @return the fieldTypes (type List<FieldType>) of this TDHSResponse object.
     *
     * @throws TDHSException when
     */
    public List<TDHSResponseEnum.IFieldType> getFieldTypes() throws TDHSException {
        parse();
        return fieldTypes;
    }

    /**
     * Method getFieldData returns the fieldData of this TDHSResponse object.
     *
     * @return the fieldData (type List<List<String>>) of this TDHSResponse object.
     *
     * @throws TDHSException when
     */
    public List<List<String>> getFieldData() throws TDHSException {
        if (fieldData != null) {
            return fieldData;
        }
        parseFieldData();
        return fieldData;
    }

    /**
     * Method getFieldOriData returns the fieldOriData of this TDHSResponse object.
     *
     * @return the fieldOriData (type List<List<byte[]>>) of this TDHSResponse object.
     *
     * @throws TDHSException when
     */
    public List<List<byte[]>> getFieldOriData() throws TDHSException {
        parse();
        return fieldOriData;
    }

    /**
     * Method getDbErrorCode returns the dbErrorCode of this TDHSResponse object.
     *
     * @return the dbErrorCode (type int) of this TDHSResponse object.
     *
     * @throws TDHSException when
     */
    public int getDbErrorCode() throws TDHSException {
        parse();
        return dbErrorCode;
    }

    /**
     * Method getMySQLHandlerErrorCode returns the mySQLHandlerErrorCode of this TDHSResponse object.
     *
     * @return the mySQLHandlerErrorCode (type IMySQLHandlerErrorCodes) of this TDHSResponse object.
     *
     * @throws TDHSException when
     */
    public IMySQLHandlerErrorCodes getMySQLHandlerErrorCode() throws TDHSException {
        return MySQLHandlerErrorCodes.valueOf(getDbErrorCode());
    }

    /**
     * Method getFieldNames returns the fieldNames of this TDHSResponse object.
     *
     * @return the fieldNames (type List<String>) of this TDHSResponse object.
     */
    public List<String> getFieldNames() {
        return metaData.getFieldNames();
    }

    /**
     * Method getResultSet returns the resultSet of this TDHSResponse object.
     *
     * @return the resultSet (type ResultSet) of this TDHSResponse object.
     *
     * @throws TDHSException when
     */
    public ResultSet getResultSet() throws TDHSException {
        if (TDHSResponseEnum.ClientStatus.OK.equals(status)) {
            return new TDHSResultSet(getFieldNames(), metaData, getFieldTypes(), getFieldOriData(), charsetName);
        }
        return null;
    }

    /**
     * Method getResultSet ...
     *
     * @param alias of type List<String>
     *
     * @return ResultSet
     *
     * @throws TDHSException when
     */
    public ResultSet getResultSet(List<String> alias) throws TDHSException {
        if (TDHSResponseEnum.ClientStatus.OK.equals(status)) {
            if (alias == null || alias.size() != getFieldNames().size()) {
                throw new TDHSException("alias is wrong! alias:[" + alias + "] fieldNames:[" + getFieldNames() + "]");
            }
            return new TDHSResultSet(alias, metaData, getFieldTypes(), getFieldOriData(), charsetName);
        }
        return null;
    }

    /**
     * Method toString ...
     *
     * @return String
     */
    @Override
    public String toString() {
        try {
            return "TDHSResponse{" +
                    "status=" + getStatus() +
                    ", errorCode=" + getErrorCode() +
                    ", dbErrorCode=" + getDbErrorCode() +
                    ", MySQLHandlerErrorCode=" + getMySQLHandlerErrorCode() +
                    ", fieldNumber=" + getFieldNumber() +
                    ", fieldTypes=" + getFieldTypes() +
                    ", fieldData=" + getFieldData() +
                    '}';
        } catch (TDHSException e) {
            PrintWriter pw = new PrintWriter(new StringWriter());
            e.printStackTrace(pw);
            return "TDHSResponse parse failed!\n" + pw.toString();
        }
    }


    /**
     * Method getErrorMessage returns the errorMessage of this TDHSResponse object.
     *
     * @return the errorMessage (type String) of this TDHSResponse object.
     */
    public String getErrorMessage() {
        try {
            return "status=" + getStatus() +
                    ", errorCode=" + getErrorCode() +
                    ", dbErrorCode=" + getDbErrorCode() +
                    ", MySQLHandlerErrorCode=" + getMySQLHandlerErrorCode();
        } catch (TDHSException e) {
            PrintWriter pw = new PrintWriter(new StringWriter());
            e.printStackTrace(pw);
            return "TDHSResponse parse failed!\n" + pw.toString();
        }
    }
}
