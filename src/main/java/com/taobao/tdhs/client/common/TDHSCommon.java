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

package com.taobao.tdhs.client.common;

import com.taobao.tdhs.client.protocol.TDHSProtocol;
import com.taobao.tdhs.client.protocol.TDHSProtocolBinary;
import com.taobao.tdhs.client.protocol.TDHSProtocolBinaryV2;
import org.apache.commons.lang.StringUtils;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 11-11-1 上午10:51
 */
public final class TDHSCommon {

    public static final int REQUEST_MAX_FIELD_NUM = 256;

    public static final int REQUEST_MAX_KEY_NUM = 10;


    public enum ProtocolVersion {
        V1(new TDHSProtocolBinary()), V2(new TDHSProtocolBinaryV2());
        /**
         * Enum ProtocolVersion ...
         *
         * @author zephyrleaves
         * Created on 12-10-11
         */
        private TDHSProtocol tdhsProtocol;

        private ProtocolVersion(TDHSProtocol tdhsProtocol) {
            this.tdhsProtocol = tdhsProtocol;
        }

        public TDHSProtocol getTdhsProtocol() {
            return tdhsProtocol;
        }

        /**
         * Method fromProp ...
         *
         * @param prop of type String
         *
         * @return ProtocolVersion
         */
        public static ProtocolVersion fromProp(String prop) {
            if (StringUtils.isNotBlank(prop) && (prop.equals("2") || prop.equalsIgnoreCase("v2"))) {
                return V2;
            }
            return V1;
        }
    }

    public enum RequestType {
        GET(0), COUNT(1), UPDATE(10), DELETE(11), INSERT(12), BATCH(20), SHAKE_HAND(0xFFFF);

        private int value;

        RequestType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public enum FindFlag {
        TDHS_EQ(0), TDHS_GE(1), TDHS_LE(2), TDHS_GT(3), TDHS_LT(4), TDHS_IN(5), TDHS_DEQ(6), TDHS_BETWEEN(7);

        private int value;

        FindFlag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public enum FilterFlag {
        TDHS_EQ(0), TDHS_GE(1), TDHS_LE(2), TDHS_GT(3), TDHS_LT(4), TDHS_NOT(5);

        private int value;

        FilterFlag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public enum UpdateFlag {
        TDHS_UPDATE_SET(0), TDHS_UPDATE_ADD(1), TDHS_UPDATE_SUB(2), TDHS_UPDATE_NOW(3);

        private int value;

        UpdateFlag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }


}
