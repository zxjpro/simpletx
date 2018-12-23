package com.xiaojiezhu.simpletx.util;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:23
 */
public class Constant {

    /**
     * this info can not delete , it will be system error
     */
    public static final String AUTHOR = "simpletx.author";
    public static final String AUTHOR_INFO = "zhu.xiaojie";


    //============================Server start================================
    public static class Server {
        public static class ProtocolCode {

            /**
             * send auth key
             */
            public static final int CODE_AUTH_KEY = 100;

            public static final int CODE_OK = 101;
            public static final int CODE_ERROR = 102;

            /**
             * simpletx-server notify all of the transaction group to commit transaction
             */
            public static final int CODE_NOTIFY_COMMIT = 231;
            /**
             * simpletx-server notify all of the transaction group to rollback transaction
             */
            public static final int CODE_NOTIFY_ROLLBACK = 231;
        }


        public static class ConnectionSession {

            public static final String AUTH_KEY = "AUTH_KEY";
            /**
             * Login success
             */
            public static final String LOGIN_SUCCESS = "_login_success";
            /**
             * connection id
             */
            public static final String ID = Client.ConnectionSession.ID;
            public static final String APP_NAME = "_appName";
            public static final String APPID = "_appid";
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
        }


        public static class ConnectionSession {

            public static final String ID = "_id";
        }

    }

    //============================Client end================================

}
