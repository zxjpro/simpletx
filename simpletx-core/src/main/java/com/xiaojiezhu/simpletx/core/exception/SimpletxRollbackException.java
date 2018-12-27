package com.xiaojiezhu.simpletx.core.exception;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 10:26
 */
public class SimpletxRollbackException extends SimpletxTransactionException {

    public SimpletxRollbackException() {
    }

    public SimpletxRollbackException(String message) {
        super(message);
    }

    public SimpletxRollbackException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpletxRollbackException(Throwable cause) {
        super(cause);
    }

    public SimpletxRollbackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
