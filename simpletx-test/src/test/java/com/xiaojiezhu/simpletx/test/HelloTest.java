package com.xiaojiezhu.simpletx.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
}
