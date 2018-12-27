package com.xiaojiezhu.simpletx.util.bean;

/**
 * @author xiaojie.zhu
 * time 2018/12/24 23:22
 */
public class Void {

    private Void(){}

    private static final Void INSTANCE = new Void();

    public static Void getInstance(){
        return INSTANCE;
    }

}
