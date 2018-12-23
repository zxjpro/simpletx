package com.xiaojiezhu.simpletx.server.packet.input;

import com.xiaojiezhu.simpletx.protocol.packet.ByteBuffer;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

import javax.xml.ws.BindingType;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 12:26
 */
public class CreateJoinGroupInputPacket implements InputPacket {

    @Getter
    private boolean compensate;

    @Getter
    private byte[] methodParameter;

    @Getter
    private String transactionGroupId;

    /**
     * 是否把spring bean的名称传过来
     */
    @Getter
    private boolean hasBeanName;

    @Getter
    private String beanName;

    @Getter
    private String className;

    @Getter
    private String methodName;


    @Override
    public void read(ByteBuffer byteBuf) {
        this.compensate = byteBuf.readBoolean();
        this.transactionGroupId = byteBuf.readStringEndZero();

        this.hasBeanName = byteBuf.readBoolean();
        if(this.hasBeanName){
            this.beanName = byteBuf.readStringEndZero();
        }

        this.className = byteBuf.readStringEndZero();
        this.methodName = byteBuf.readStringEndZero();

        boolean hasArgs = byteBuf.readBoolean();

        if(hasArgs){
            this.methodParameter = byteBuf.readBytesToLast();
        }



    }
}
