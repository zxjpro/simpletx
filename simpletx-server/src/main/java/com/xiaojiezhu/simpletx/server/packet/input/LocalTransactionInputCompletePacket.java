package com.xiaojiezhu.simpletx.server.packet.input;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import com.xiaojiezhu.simpletx.protocol.packet.OutputPacket;
import com.xiaojiezhu.simpletx.protocol.packet.ResponseInputPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 19:33
 */
@AllArgsConstructor
public class LocalTransactionInputCompletePacket implements ResponseInputPacket {

    /**
     * commit or rollback
     * true: commit
     * false: rollback
     */
    @Getter
    private boolean commit;

    /**
     * invoke local transaction is success
     *
     * true: success
     */
    @Getter
    private boolean success;

    /**
     * invoke local transaction use time , ms
     */
    @Getter
    private int useTime;


    @Getter
    private int responseMessageId;


    @Getter
    private String appName;

    @Getter
    private String appid;


    @Override
    public void read(ByteBuffer byteBuf) {
        this.commit = byteBuf.readBoolean();
        this.success = byteBuf.readBoolean();
        this.useTime = byteBuf.readInt();
        this.responseMessageId = byteBuf.readInt();
        this.appName = byteBuf.readStringEndZero();
        this.appid = new String(byteBuf.readBytesToLast());
    }
}
