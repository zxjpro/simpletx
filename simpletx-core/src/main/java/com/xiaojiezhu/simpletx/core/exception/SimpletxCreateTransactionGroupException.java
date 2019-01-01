package com.xiaojiezhu.simpletx.core.exception;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 13:12
 */
public class SimpletxCreateTransactionGroupException extends SimpletxServerException {

    public SimpletxCreateTransactionGroupException() {
    }

    public SimpletxCreateTransactionGroupException(String message) {
        super(message);
    }

    public SimpletxCreateTransactionGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpletxCreateTransactionGroupException(Throwable cause) {
        super(cause);
    }

    public SimpletxCreateTransactionGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
