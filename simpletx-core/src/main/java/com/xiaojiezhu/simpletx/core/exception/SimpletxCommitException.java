package com.xiaojiezhu.simpletx.core.exception;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 10:25
 */
public class SimpletxCommitException extends SimpletxTransactionException {

    public SimpletxCommitException() {
    }

    public SimpletxCommitException(String message) {
        super(message);
    }

    public SimpletxCommitException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpletxCommitException(Throwable cause) {
        super(cause);
    }

    public SimpletxCommitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
