package com.taobao.tdhs.client.common;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-7-9 下午1:20
 */
public interface IMySQLHandlerErrorCodes {
    String name();

    int getCode();

    String getDesc();
}
