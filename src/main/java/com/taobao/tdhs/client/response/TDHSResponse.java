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

import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.client.util.ByteOrderUtil;
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

    private TDHSResponseEnum.ClientStatus status;

    private TDHSResponseEnum.ErrorCode errorCode;

    private int dbErrorCode;

    private int fieldNumber;

    private List<TDHSResponseEnum.FieldType> fieldTypes;

    private List<List<String>> fieldData;

    private byte data[];

    private String charestName;

    private boolean isParsed = false;

    private TDHSMetaData metaData;


    /**
     * Constructor TDHSResponse creates a new TDHSResponse instance.
     *
     * @param status      of type ClientStatus    ,client status from sever-side
     * @param metaData    of type TDHSMetaData    ,meta data from client
     * @param data        of type byte[]          ,data from server-side need to parse
     * @param charestName of type String          ,charest for decoding
     */
    public TDHSResponse(TDHSResponseEnum.ClientStatus status, TDHSMetaData metaData, byte[] data,
                        String charestName) {
        this.status = status;
        this.metaData = metaData;
        this.data = data;
        if (StringUtils.isNotBlank(charestName)) {
            this.charestName = charestName;
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
            try {
                parseData(this.data);
            } catch (UnsupportedEncodingException e) {
                throw new TDHSException(e);
            }
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
     *
     * @throws UnsupportedEncodingException when charsetName is not supported
     */
    private void parseData(final byte data[]) throws UnsupportedEncodingException {
        int len = data.length;
        int pos = 0;
        fieldData = new ArrayList<List<String>>();
        //read field number
        fieldNumber = (int) ByteOrderUtil.getUnsignInt(data, pos);
        pos += 4;
        fieldTypes = new ArrayList<TDHSResponseEnum.FieldType>(fieldNumber);
        for (int i = 0; i < fieldNumber; i++) {
            fieldTypes.add(TDHSResponseEnum.FieldType.valueOf(data[pos] & 0xFF));
            pos++;
        }
        while (pos < len) {
            List<String> record = new ArrayList<String>(fieldNumber);
            for (int i = 0; i < fieldNumber; i++) {
                int fieldLength = (int) ByteOrderUtil.getUnsignInt(data, pos);
                pos += 4;
                if (fieldLength > 0) {
                    if (fieldLength == 1 && data[pos] == 0) {
                        record.add("");
                    } else {
                        byte f[] = new byte[fieldLength];
                        System.arraycopy(data, pos, f, 0, fieldLength);
                        record.add(
                                StringUtils.isNotBlank(charestName) ? new String(f, this.charestName) : new String(f));
                    }
                    pos += fieldLength;
                } else {
                    record.add(null);
                }
            }
            fieldData.add(record);
        }
    }

    /**
     * Method getCharestName returns the charestName of this TDHSResponse object.
     *
     * @return the charestName (type String) of this TDHSResponse object.
     */
    public String getCharestName() {
        return charestName;
    }

    /**
     * Method setCharestName sets the charestName of this TDHSResponse object.
     *
     * @param charestName the charestName of this TDHSResponse object.
     */
    public void setCharestName(String charestName) {
        if (StringUtils.isNotBlank(charestName)) {
            this.charestName = charestName;
        }
    }


    /**
     * Method getStatus returns the status of this TDHSResponse object.
     *
     * @return the status (type ClientStatus) of this TDHSResponse object.
     */
    public TDHSResponseEnum.ClientStatus getStatus() {
        return status;
    }

    /**
     * Method getErrorCode returns the errorCode of this TDHSResponse object.
     *
     * @return the errorCode (type ErrorCode) of this TDHSResponse object.
     *
     * @throws TDHSException when
     */
    public TDHSResponseEnum.ErrorCode getErrorCode() throws TDHSException {
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
    public List<TDHSResponseEnum.FieldType> getFieldTypes() throws TDHSException {
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
        parse();
        return fieldData;
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
            return new TDHSResutSet(getFieldNames(), metaData, getFieldTypes(), getFieldData());
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
            return new TDHSResutSet(alias, metaData, getFieldTypes(), getFieldData());
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
                    ", dbErrorCode=" + getDbErrorCode();
        } catch (TDHSException e) {
            PrintWriter pw = new PrintWriter(new StringWriter());
            e.printStackTrace(pw);
            return "TDHSResponse parse failed!\n" + pw.toString();
        }
    }
}
