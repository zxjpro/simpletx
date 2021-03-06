package com.xiaojiezhu.simpletx.protocol.packet;

import com.xiaojiezhu.simpletx.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * response ok or error message
 * @author xiaojie.zhu
 * time 2018/12/23 22:09
 */
@AllArgsConstructor
@NoArgsConstructor
public class OkErrorPacket implements OutputPacket ,InputPacket,ResponseInputPacket {

    @Getter
    private boolean ok;
    /**
     * 响应请求过来的ID
     */
    @Getter
    private int responseMsgId;

    @Getter
    private String message;

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.writeBoolean(this.ok);
        byteBuf.writeInt(this.responseMsgId);
        if(!StringUtils.isEmpty(message)){
            byteBuf.writeBytes(message.getBytes());
        }
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        this.ok = byteBuf.readBoolean();
        this.responseMsgId = byteBuf.readInt();
        if(byteBuf.readableBytes() > 0){
            this.message = new String(byteBuf.readBytesToLast());
        }
    }

    @Override
    public int getResponseMessageId() {
        return this.responseMsgId;
    }
}
