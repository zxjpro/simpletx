package com.xiaojiezhu.simpletx.util;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:23
 */
public class Constant {

    /**
     * this info can not delete , it will be system error
     */
    public static final String SIMPLETX_SERVER = "simpletx.server";
    public static final String SIMPLETX_CLIENT = "simpletx.client";
    public static final String AUTHOR = "simpletx.author";
    public static final String AUTHOR_INFO = "zhu.xiaojie";

    /**
     * the request api enter transaction group id
     */
    public static final String SIMPLETX_ENTER_HEADER = "simpletx.transaction.group.id";


    //============================Server start================================
    public static class Server {
        public static class ProtocolCode {

            /**
             * send auth key
             */
            public static final int CODE_AUTH_KEY = 100;

            public static final int CODE_OK_ERROR = 101;

            /**
             * simpletx-server notify all of the transaction group to commit transaction
             */
            public static final int CODE_NOTIFY_COMMIT = 231;
            /**
             * simpletx-server notify all of the transaction group to rollback transaction
             */
            public static final int CODE_NOTIFY_ROLLBACK = 232;

            /**
             * simpletx-server notify root transaction , the transaction group is invoke complete
             */
            public static final int CODE_TRANSACTION_GROUP_COMPLETE = 233;
        }


        public static class ConnectionSession {

            public static final String AUTH_KEY = "AUTH_KEY";
            /**
             * server Login success
             */
            public static final String LOGIN_SUCCESS = "_login_success";
            /**
             * connection id
             */
            public static final String ID = Client.ConnectionSession.ID;
            public static final String APP_NAME = "_appName";
            public static final String APPID = "_appid";

            public static final String EXPIRE_TIME = "simpletx.expireTime";
        }
    }


    //============================Server end================================


    //============================Client start================================

    public static class Client{

        public static class ProtocolCode {

            /**
             * login
             */
            public static final int CODE_LOGIN = 200;
            /**
             * create group
             */
            public static final int CODE_CREATE_GROUP = 231;
            /**
             * join group
             */
            public static final int CODE_JOIN_GROUP = 232;
            /**
             * client send simpletx-server to commit
             */
            public static final int CODE_COMMIT = 233;
            /**
             * client send simpletx-server to rollback
             */
            public static final int CODE_ROLLBACK = 234;
            /**
             * complete local transaction , to notify simpletx-server
             */
            public static final int CODE_COMPLETE_LOCAL_TRANSACTION = 235;
        }


        public static class ConnectionSession {

            public static final String ID = "_id";

            /**
             * server Login success
             */
            public static final String LOGIN_SUCCESS = "_login_success";
        }

    }

    //============================Client end================================

}
