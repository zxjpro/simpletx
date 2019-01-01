package com.xiaojiezhu.simpletx.core.exception;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 13:12
 */
public class SimpletxJoinTransactionGroupException extends SimpletxServerException {

    public SimpletxJoinTransactionGroupException() {
    }

    public SimpletxJoinTransactionGroupException(String message) {
        super(message);
    }

    public SimpletxJoinTransactionGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpletxJoinTransactionGroupException(Throwable cause) {
        super(cause);
    }

    public SimpletxJoinTransactionGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
