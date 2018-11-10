package com.xiaojiezhu.simpletx.util.asserts;

public class Asserts {

    public static void assertNotNull(Object object , String msg){
        if(object == null){
            throw new NullPointerException(msg);
        }
    }

    public static void assertNotBlank(Object str , String msg){
        if(str == null || "".equals(str.toString().trim())){
            throw new NullPointerException(msg);
        }
    }
}
