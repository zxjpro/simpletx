package com.xiaojiezhu.simpletx.util.bean;

/**
 * @author xiaojie.zhu
 * time 2018/12/2 21:45
 */
public class Arrays {

    public static <T> boolean isEmpty(T[] arr){
        if(arr == null || arr.length == 0){
            return true;
        }

        return false;
    }
}
