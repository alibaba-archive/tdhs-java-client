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

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-11-1 上午11:19
 */
public class TDHSResponseEnum {

    /**
     * Interface IClientStatus ...
     *
     * @author wentong
     *         Created on 12-7-9
     */
    public interface IClientStatus {
        /**
         * Method name ...
         *
         * @return String
         */
        String name();

        /**
         * Method getStatus returns the status of this IClientStatus object.
         *
         * @return the status (type int) of this IClientStatus object.
         */
        int getStatus();
    }

    /**
     * Class DefaultClientStatus ...
     *
     * @author wentong
     *         Created on 12-7-9
     */
    public static class DefaultClientStatus implements IClientStatus {
        private final String name;

        private final int status;

        /**
         * Constructor DefaultClientStatus creates a new DefaultClientStatus instance.
         *
         * @param name   of type String
         * @param status of type int
         */
        public DefaultClientStatus(String name, int status) {
            this.name = name;
            this.status = status;
        }

        /**
         * Method name ...
         *
         * @return String
         */
        public String name() {
            return name;
        }

        /**
         * Method getStatus returns the status of this IClientStatus object.
         *
         * @return the status (type int) of this IClientStatus object.
         */
        public int getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "DefaultClientStatus{" +
                    "name='" + name + '\'' +
                    ", status=" + status +
                    '}';
        }
    }


    public enum ClientStatus implements IClientStatus {
        OK(200), ACCEPT(202), MULTI_STATUS(207), BAD_REQUEST(400), FORBIDDEN(403), NOT_FOUND(404), REQUEST_TIME_OUT(
                408), SERVER_ERROR(500), NOT_IMPLEMENTED(501), DB_ERROR(502), SERVICE_UNAVAILABLE(503);


        private int status;

        ClientStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public static IClientStatus valueOf(final int status) {
            for (ClientStatus s : ClientStatus.values()) {
                if (status == s.getStatus()) {
                    return s;
                }
            }
            return new DefaultClientStatus("UNKNOWN", status);
        }
    }

    /**
     * Interface IErrorCode ...
     *
     * @author wentong
     *         Created on 12-7-9
     */
    public interface IErrorCode {

        /**
         * Method name ...
         *
         * @return String
         */
        String name();

        /**
         * Method getCode returns the code of this IErrorCode object.
         *
         * @return the code (type int) of this IErrorCode object.
         */
        int getCode();

        /**
         * Method getErrorMsg returns the errorMsg of this IErrorCode object.
         *
         * @return the errorMsg (type String) of this IErrorCode object.
         */
        String getErrorMsg();

    }

    public static class DefaultErrorCode implements IErrorCode {

        private final String name;

        private final int code;

        private final String errorMsg;

        public DefaultErrorCode(String name, int code, String errorMsg) {
            this.name = name;
            this.code = code;
            this.errorMsg = errorMsg;
        }

        public String name() {
            return name;
        }

        public int getCode() {
            return code;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        @Override
        public String toString() {
            return "ErrorCode{" +
                    "name=" + name +
                    ", code=" + code +
                    ", errorMsg='" + errorMsg + '\'' +
                    '}';
        }
    }

    public enum ErrorCode implements IErrorCode {
        CLIENT_ERROR_CODE_FAILED_TO_OPEN_TABLE(1, "TDH_SOCKET failed to open table!"),
        CLIENT_ERROR_CODE_FAILED_TO_OPEN_INDEX(2, "TDH_SOCKET failed to open index!"),
        CLIENT_ERROR_CODE_FAILED_TO_MISSING_FIELD(3, "TDH_SOCKET field is missing!"),
        CLIENT_ERROR_CODE_FAILED_TO_MATCH_KEY_NUM(4, "TDH_SOCKET request can't match the key number!"),
        CLIENT_ERROR_CODE_FAILED_TO_LOCK_TABLE(5, "TDH_SOCKET failed to lock table!"),
        CLIENT_ERROR_CODE_NOT_ENOUGH_MEMORY(6, "TDH_SOCKET server don't have enough memory!"),
        CLIENT_ERROR_CODE_DECODE_REQUEST_FAILED(7, "TDH_SOCKET server can't decode your request!"),
        CLIENT_ERROR_CODE_FAILED_TO_MISSING_FIELD_IN_FILTER_OR_USE_BLOB(8,
                "TDH_SOCKET field is missing in filter or use blob!"),
        CLIENT_ERROR_CODE_FAILED_TO_COMMIT(9, "TDH_SOCKET failed to commit, will be rollback!"),
        CLIENT_ERROR_CODE_NOT_IMPLEMENTED(10, "TDH_SOCKET not implemented!"),
        CLIENT_ERROR_CODE_REQUEST_TIME_OUT(11, "TDH_SOCKET request time out!"),
        CLIENT_ERROR_CODE_UNAUTHENTICATION(12, "TDH_SOCKET request is unauthentication!"),
        CLIENT_ERROR_CODE_KILLED(13, "TDH_SOCKET request is killed!"),
        CLIENT_ERROR_CODE_THROTTLED(14, "TDH_SOCKET request is throttled!");

        private int code;

        private String errorMsg;

        ErrorCode(int code, String errorMsg) {
            this.code = code;
            this.errorMsg = errorMsg;
        }

        public int getCode() {
            return code;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public static IErrorCode valueOf(int code) {
            for (ErrorCode c : ErrorCode.values()) {
                if (code == c.getCode()) {
                    return c;
                }
            }
            return new DefaultErrorCode("CLIENT_ERROR_CODE_UNKNOWN", code, "TDH_SOCKET unknown error code!");
        }


        @Override
        public String toString() {
            return "ErrorCode{" +
                    "name=" + name() +
                    ", code=" + code +
                    ", errorMsg='" + errorMsg + '\'' +
                    '}';
        }
    }

    public interface IFieldType {
        String name();

        int getType();
    }

    public static class DefaultFieldType implements IFieldType {
        private final String name;

        private final int type;

        public DefaultFieldType(String name, int type) {
            this.name = name;
            this.type = type;
        }

        public String name() {
            return name;
        }

        public int getType() {
            return type;
        }

        @Override
        public String toString() {
            return "DefaultFieldType{" +
                    "name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }
    }


    public enum FieldType implements IFieldType {

        MYSQL_TYPE_DECIMAL(0),
        MYSQL_TYPE_TINY(1),
        MYSQL_TYPE_SHORT(2),
        MYSQL_TYPE_LONG(3),
        MYSQL_TYPE_FLOAT(4),
        MYSQL_TYPE_DOUBLE(5),
        MYSQL_TYPE_NULL(6),
        MYSQL_TYPE_TIMESTAMP(7),
        MYSQL_TYPE_LONGLONG(8),
        MYSQL_TYPE_INT24(9),
        MYSQL_TYPE_DATE(10),
        MYSQL_TYPE_TIME(11),
        MYSQL_TYPE_DATETIME(12),
        MYSQL_TYPE_YEAR(13),
        MYSQL_TYPE_NEWDATE(14),
        MYSQL_TYPE_VARCHAR(15),
        MYSQL_TYPE_BIT(16),
        MYSQL_TYPE_NEWDECIMAL(246),
        MYSQL_TYPE_ENUM(247),
        MYSQL_TYPE_SET(248),
        MYSQL_TYPE_TINY_BLOB(249),
        MYSQL_TYPE_MEDIUM_BLOB(25),
        MYSQL_TYPE_LONG_BLOB(251),
        MYSQL_TYPE_BLOB(252),
        MYSQL_TYPE_VAR_STRING(253),
        MYSQL_TYPE_STRING(254),
        MYSQL_TYPE_GEOMETRY(255);

        private int type;

        private static FieldType[] cached_type = new FieldType[256];

        FieldType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static IFieldType valueOf(final int type) {
            if (type < 0 || type >= cached_type.length) {
                return new DefaultFieldType("MYSQL_TYPE_UNKNOWN", type);
            }
            if (cached_type[type] != null) {
                return cached_type[type];
            }
            for (FieldType t : FieldType.values()) {
                cached_type[type] = t;
                if (type == t.getType()) {
                    return t;
                }
            }
            return new DefaultFieldType("MYSQL_TYPE_UNKNOWN", type);
        }
    }
}
