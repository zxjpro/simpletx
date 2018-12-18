package com.xiaojiezhu.simpletx.server.util;

import java.util.UUID;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:21
 */
public class RandomUtil {

    public static String randomString(){
        return UUID.randomUUID().toString().replace("-" , "");
    }
}
