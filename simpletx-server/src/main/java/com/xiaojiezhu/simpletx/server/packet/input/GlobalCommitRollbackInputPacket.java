package com.xiaojiezhu.simpletx.server.packet.input;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import lombok.Getter;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 12:55
 */
public class GlobalCommitRollbackInputPacket implements InputPacket {

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
    public void read(ByteBuffer byteBuf) {
        this.commit = byteBuf.readBoolean();
        this.transactionGroupId = new String(byteBuf.readBytesToLast());

    }
}
