package com.taobao.tdhs.client.protocol;

import com.taobao.tdhs.client.common.TDHSCommon;
import com.taobao.tdhs.client.packet.BasePacket;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-10-11 下午1:04
 */
public class TDHSProtocolBinaryV2 extends TDHSProtocolBinary {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Method shakeHandPacket ...
     *
     * @param timeOut   of type int
     * @param readCode  of type String
     * @param writeCode of type String
     *
     * @return BasePacket
     */
    @Override
    public BasePacket shakeHandPacket(int timeOut, @Nullable String readCode,
                                      @Nullable String writeCode) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(12 + 2 + 200);
            out.write("TDHS".getBytes());
            writeInt32ToStream(2, out);//版本号
            writeInt32ToStream(timeOut, out);//超时时间
            writeToStream(readCode, out, null); //read code
            writeToStream(writeCode, out, null);//write code
            return new BasePacket(TDHSCommon.RequestType.SHAKE_HAND, 0, out.toByteArray());
        } catch (IOException e) {
            assert false; //不应该发生
            logger.error("shakeHandPacket failed!", e);
            return null;
        }
    }
}
