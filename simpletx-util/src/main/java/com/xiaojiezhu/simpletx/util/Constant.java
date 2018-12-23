package com.xiaojiezhu.simpletx.util;

/**
 * @author xiaojie.zhu
 * time 2018/12/18 21:23
 */
public class Constant {


    //============================Server start================================
    public static class Server {
        public static class ProtocolCode {

            /**
             * send auth key
             */
            public static final int CODE_AUTH_KEY = 100;
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
            public static final String ID = "_id";
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
        }


        public static class ConnectionSession {

            public static final String ID = "_id";
        }

    }

    //============================Client end================================

}
