package com.xiaojiezhu.simpletx.test;

import com.xiaojiezhu.simpletx.client.net.packet.input.LoginInputPacket;
import com.xiaojiezhu.simpletx.protocol.context.ConnectionContext;
import com.xiaojiezhu.simpletx.protocol.context.InputPacketManager;
import com.xiaojiezhu.simpletx.protocol.context.SimpleInputPacketManager;
import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.exception.SyntaxRuntimeException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class HelloTest {


    private byte[] buf;

    @Before
    public void init(){
        this.buf = "hello world".getBytes();
    }

    @Test
    public void test() throws InterruptedException {


        ByteBuf byteBuf = Unpooled.wrappedBuffer(buf);

        byteBuf.release();

        System.out.println();
    }


    @Test
    public void test2(){
        ByteBuf buffer = Unpooled.buffer();

        buffer.writeBytes(buf);


        buffer.release();

        System.out.println();
    }


    @Test
    public void test3(){
        class HelloHandler extends Date implements ProtocolHandler<LoginInputPacket>{
            @Override
            public void handler(ConnectionContext connectionContext,int msgId , int code , LoginInputPacket content) {

            }
        }
        class WorldHandler extends HelloHandler{

        }
        class JavaHandler extends WorldHandler{}


        HelloHandler helloHandler = new HelloHandler();

        //findGenericity(helloHandler);

        WorldHandler worldHandler = new WorldHandler();

        InputPacketManager inputPacketManager = new SimpleInputPacketManager();

        Class<?> genericity = inputPacketManager.get(JavaHandler.class);

        System.out.println(genericity);
    }



}
