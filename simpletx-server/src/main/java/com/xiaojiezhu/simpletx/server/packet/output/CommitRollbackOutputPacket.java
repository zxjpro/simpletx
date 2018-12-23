package com.xiaojiezhu.simpletx.server.packet.output;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.OutputPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 22:49
 */
@AllArgsConstructor
public class CommitRollbackOutputPacket implements OutputPacket {

    /**
     * true: commit transaction group
     * false: rollback transaction group
     */
    @Getter
    private boolean commit;

    /**
     * transaction group id
     */
    @Getter
    private String transactionGroupId;


    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.writeBoolean(commit);
        byteBuf.writeBytes(transactionGroupId.getBytes());
    }
}
