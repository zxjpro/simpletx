package com.xiaojiezhu.simpletx.protocol.packet;

import com.xiaojiezhu.simpletx.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * response ok or error message
 * @author xiaojie.zhu
 * time 2018/12/23 22:09
 */
@AllArgsConstructor
@NoArgsConstructor
public class OkErrorPacket implements OutputPacket ,InputPacket {

    /**
     * 响应请求过来的ID
     */
    @Getter
    private int responseMsgId;

    @Getter
    private String msg;

    @Override
    public void write(ByteBuffer byteBuf) {
        byteBuf.writeInt(this.responseMsgId);
        if(!StringUtils.isEmpty(msg)){
            byteBuf.writeBytes(msg.getBytes());
        }
    }

    @Override
    public void read(ByteBuffer byteBuf) {
        this.responseMsgId = byteBuf.readInt();
        if(byteBuf.readableBytes() > 0){
            this.msg = new String(byteBuf.readBytesToLast());
        }
    }
}
