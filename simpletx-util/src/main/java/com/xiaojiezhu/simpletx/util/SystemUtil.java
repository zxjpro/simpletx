package com.xiaojiezhu.simpletx.util;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 12:52
 */
public class SystemUtil {


    public static void exit(String msg){
        System.err.println("============================================================");
        System.err.println(msg);
        System.err.println("simpletx will exit");
        System.err.println("============================================================");
        System.exit(0);
    }
}
