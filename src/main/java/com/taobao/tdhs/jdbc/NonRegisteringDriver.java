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

package com.taobao.tdhs.jdbc;

import com.taobao.tdhs.client.TDHSClient;
import com.taobao.tdhs.client.exception.TDHSException;
import com.taobao.tdhs.jdbc.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-3-6 下午2:58
 */
public class NonRegisteringDriver implements java.sql.Driver {
    private static final String URL_PREFIX = "jdbc:tdhs://";

    private static final String ALLOWED_QUOTES = "\"'";

    public static final String DBNAME_PROPERTY_KEY = "DBNAME";

    public static final String HOST_PROPERTY_KEY = "HOST";

    public static final String PORT_PROPERTY_KEY = "PORT";

    public static final String READ_CODE_PROPERTY_KEY = TDHSClient.READ_CODE;

    public static final String WRITE_CODE_PROPERTY_KEY = TDHSClient.WRITE_CODE;

    public static final String CONNECTION_NUMBER_PROPERTY_KEY = TDHSClient.CONNECTION_NUMBER;

    public static final String TIME_OUT_PROPERTY_KEY = TDHSClient.TIME_OUT;

    public static final String NEED_RECONNECT_PROPERTY_KEY = TDHSClient.NEED_RECONNECT;

    public static final String CONNECT_TIMEOUT_PROPERTY_KEY = TDHSClient.CONNECT_TIMEOUT;

    public static final String CHARSET_NAME_PROPERTY_KEY = TDHSClient.CHARSET_NAME;

    public static final String LOWER_CASE_TABLE_NAMES = TDHSClient.LOWER_CASE_TABLE_NAMES;

    public static final String VERSION = TDHSClient.VERSION;

    public static final String FAKE_TRANSACTION = "fakeTransaction";


    public NonRegisteringDriver() throws SQLException {
        // Required for Class.forName().newInstance()
    }

    public Properties parseURL(String url, Properties defaults)
            throws java.sql.SQLException {
        Properties urlProps = (defaults != null) ? new Properties(defaults)
                : new Properties();

        if (url == null) {
            return null;
        }

        if (!StringUtils.startsWithIgnoreCase(url, URL_PREFIX)) {
            return null;
        }

        int beginningOfSlashes = url.indexOf("//");

        int index = url.indexOf("?");

        if (index != -1) {
            String paramString = url.substring(index + 1, url.length());
            url = url.substring(0, index);

            StringTokenizer queryParams = new StringTokenizer(paramString, "&");

            while (queryParams.hasMoreTokens()) {
                String parameterValuePair = queryParams.nextToken();

                int indexOfEquals = StringUtils.indexOfIgnoreCase(
                        parameterValuePair, "=", 0);

                String parameter = null;
                String value = null;

                if (indexOfEquals != -1) {
                    parameter = parameterValuePair.substring(0, indexOfEquals);

                    if (indexOfEquals + 1 < parameterValuePair.length()) {
                        value = parameterValuePair.substring(indexOfEquals + 1);
                    }
                }

                if ((value != null && value.length() > 0)
                        && (parameter != null && parameter.length() > 0)) {
                    try {
                        urlProps.put(parameter, URLDecoder.decode(value,
                                "UTF-8"));
                    } catch (UnsupportedEncodingException badEncoding) {
                        // punt
                        urlProps.put(parameter, URLDecoder.decode(value));
                    } catch (NoSuchMethodError nsme) {
                        // punt again
                        urlProps.put(parameter, URLDecoder.decode(value));
                    }
                }
            }
        }

        url = url.substring(beginningOfSlashes + 2);

        String hostStuff = null;
        int slashIndex = StringUtil
                .indexOfIgnoreCaseRespectMarker(0, url, "/", ALLOWED_QUOTES, ALLOWED_QUOTES, true);

        if (slashIndex != -1) {
            hostStuff = url.substring(0, slashIndex);

            if ((slashIndex + 1) < url.length()) {
                urlProps.put(DBNAME_PROPERTY_KEY,
                        url.substring((slashIndex + 1), url.length()));
            }
        } else {
            hostStuff = url;
        }

        if ((hostStuff != null) && (hostStuff.trim().length() > 0)) {

            String[] hostPortPair = StringUtils.split(hostStuff, ":");

            if (StringUtils.isNotBlank(hostPortPair[0])) {
                urlProps.setProperty(HOST_PROPERTY_KEY, hostPortPair[0]);
            } else {
                urlProps.setProperty(HOST_PROPERTY_KEY, "localhost");
            }

            if (hostPortPair.length > 1 && StringUtils.isNotBlank(hostPortPair[1])) {
                urlProps.setProperty(PORT_PROPERTY_KEY, hostPortPair[1]);
            } else {
                urlProps.setProperty(PORT_PROPERTY_KEY, "9999");
            }
        } else {
            urlProps.setProperty(HOST_PROPERTY_KEY, "localhost");
            urlProps.setProperty(PORT_PROPERTY_KEY, "9999");
        }
        return urlProps;
    }

    public Connection connect(String url, Properties info) throws SQLException {
        Properties props;
        if ((props = parseURL(url, info)) == null) {
            return null;
        }
        try {
            return new TDHSConnection(TDHSClientInstance.getInstance().createConnection(props), props);
        } catch (TDHSException e) {
            throw new SQLException(e);
        }
    }

    public boolean acceptsURL(String url) throws SQLException {
        return (parseURL(url, null) != null);
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        if (info == null) {
            info = new Properties();
        }

        if ((url != null) && url.startsWith(URL_PREFIX)) {
            info = parseURL(url, info);
        }

        DriverPropertyInfo hostProp = new DriverPropertyInfo(HOST_PROPERTY_KEY,
                info.getProperty(HOST_PROPERTY_KEY));
        hostProp.required = true;
        hostProp.description = "Host name";

        DriverPropertyInfo portProp = new DriverPropertyInfo(PORT_PROPERTY_KEY,
                info.getProperty(PORT_PROPERTY_KEY, "9999"));
        portProp.required = false;
        portProp.description = "port";

        DriverPropertyInfo dbProp = new DriverPropertyInfo(DBNAME_PROPERTY_KEY,
                info.getProperty(DBNAME_PROPERTY_KEY));
        dbProp.required = false;
        dbProp.description = "Database name";

        DriverPropertyInfo readcodeProp = new DriverPropertyInfo(READ_CODE_PROPERTY_KEY,
                info.getProperty(READ_CODE_PROPERTY_KEY));
        readcodeProp.required = false;

        DriverPropertyInfo writeCodeProp = new DriverPropertyInfo(
                WRITE_CODE_PROPERTY_KEY,
                info.getProperty(WRITE_CODE_PROPERTY_KEY));
        writeCodeProp.required = false;
        DriverPropertyInfo[] dpi = new DriverPropertyInfo[5];

        dpi[0] = hostProp;
        dpi[1] = portProp;
        dpi[2] = dbProp;
        dpi[3] = readcodeProp;
        dpi[4] = writeCodeProp;

        return dpi;
    }

    public int getMajorVersion() {
        return 0;
    }

    public int getMinorVersion() {
        return 3;
    }

    public boolean jdbcCompliant() {
        return false;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

}
