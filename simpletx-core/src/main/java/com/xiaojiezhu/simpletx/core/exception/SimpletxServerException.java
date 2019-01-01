package com.xiaojiezhu.simpletx.core.exception;

/**
 * @author xiaojie.zhu
 * time 2018-12-31 13:11
 */
public class SimpletxServerException extends Exception {

    public SimpletxServerException() {
    }

    public SimpletxServerException(String message) {
        super(message);
    }

    public SimpletxServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpletxServerException(Throwable cause) {
        super(cause);
    }

    public SimpletxServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
