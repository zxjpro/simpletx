package com.xiaojiezhu.simpletx.core.info;

import com.xiaojiezhu.simpletx.common.codec.KryoObjectCodec;
import com.xiaojiezhu.simpletx.common.codec.ObjectCodec;
import com.xiaojiezhu.simpletx.util.Constant;
import com.xiaojiezhu.simpletx.util.http.HeaderHandler;
import com.xiaojiezhu.simpletx.util.http.HeaderManager;
import com.xiaojiezhu.simpletx.util.http.HttpClient;

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

    /**
     * default provider usable http client
     */
    private static final HttpClient HTTP_CLIENT;
    static {
        HTTP_CLIENT = new HttpClient();
        HTTP_CLIENT.setHeaderHandler(new HeaderHandler() {
            @Override
            public void handler(HeaderManager headerManager) {
                String transactionGroupId = SimpletxTransactionUtil.getTransactionGroupId();
                if(transactionGroupId != null){

                    headerManager.addHeader(Constant.SIMPLETX_ENTER_HEADER , transactionGroupId);
                }
            }
        });
    }

    public static HttpClient getHttpClient() {
        return HTTP_CLIENT;
    }

    /**
     * object codec
     */
    private static ObjectCodec objectCodec;

    public static void setObjectCodec(ObjectCodec oc) {
        objectCodec = oc;
    }

    public static ObjectCodec getObjectCodec() {
        if(objectCodec == null){
            objectCodec = new KryoObjectCodec();
        }
        return objectCodec;
    }
}
