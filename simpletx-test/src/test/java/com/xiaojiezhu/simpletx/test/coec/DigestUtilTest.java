package com.xiaojiezhu.simpletx.test.coec;

import com.xiaojiezhu.simpletx.util.security.DigestUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 11:28
 */
public class DigestUtilTest {


    @Test
    public void test1(){
        for (int i = 0; i < 100; i++) {
            byte[] bytes = DigestUtil.sha256(UUID.randomUUID().toString());
            String s = DigestUtil.hexToString(bytes);
            //tString(bytes);
            System.out.println(s);
            tString(s.getBytes());
            System.out.println();
        }
    }

    public void tString(byte[] buf){
        for (int i = 0; i < buf.length; i++) {
            byte b = buf[i];
            if(0 == b){
                throw new RuntimeException();
            }
            System.out.printf(String.valueOf(b));
            System.out.printf(",");
        }
        System.out.println();
    }
}
