package com.xiaojiezhu.simpletx.core.exception;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 10:30
 */
public class SimpletxTransactionException extends Exception {

    public SimpletxTransactionException() {
    }

    public SimpletxTransactionException(String message) {
        super(message);
    }

    public SimpletxTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpletxTransactionException(Throwable cause) {
        super(cause);
    }

    public SimpletxTransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
