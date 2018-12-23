package com.xiaojiezhu.simpletx.core.net.packet.output;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.OutputPacket;
import lombok.AllArgsConstructor;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 12:10
 */
@AllArgsConstructor
public class CommitRollbackOutputPacket implements OutputPacket {
    private boolean commit;
    private String transactionId;


    /**
     * is commit 1
     * transactionId StringToLast
     * @param byteBuf
     */
    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.writeBoolean(this.commit);
        byteBuf.writeBytes(this.transactionId.getBytes());

    }
}
