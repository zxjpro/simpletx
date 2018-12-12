package com.xiaojiezhu.simpletx.core.info;

/**
 * @author xiaojie.zhu
 * time 2018/12/10 21:55
 */
public class SimpletxTransactionUtil {

    private static final ThreadLocal<String> TRANSACTION_GROUP = new ThreadLocal<>();

    public static void setTransactionGroupId(String groupId){
        TRANSACTION_GROUP.set(groupId);
    }

    public static String getTransactionGroupId(){
        return TRANSACTION_GROUP.get();
    }

    public static void removeTransactionGroupId(){
        TRANSACTION_GROUP.remove();
    }


    /**
     * clear thread bind thread local resource
     */
    public static void clearThreadResource(){
        TRANSACTION_GROUP.remove();
    }
}
