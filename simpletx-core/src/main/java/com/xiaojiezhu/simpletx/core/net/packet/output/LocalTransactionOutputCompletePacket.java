package com.xiaojiezhu.simpletx.core.net.packet.output;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.OutputPacket;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 19:33
 */
@AllArgsConstructor
public class LocalTransactionOutputCompletePacket implements OutputPacket {

    /**
     * commit or rollback
     * true: commit
     * false: rollback
     */
    private boolean commit;

    /**
     * invoke local transaction is success
     *
     * true: success
     */
    private boolean success;

    /**
     * invoke local transaction use time , ms
     */
    private int useTime;

    private int responseMessageId;

    private String appName;
    private String appid;


    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.writeBoolean(commit);
        byteBuf.writeBoolean(success);
        byteBuf.writeInt(this.useTime);
        byteBuf.writeInt(this.responseMessageId);
        byteBuf.writeStringEndZero(this.appName);
        byteBuf.writeBytes(this.appid.getBytes());
    }
}
