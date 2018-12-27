package com.xiaojiezhu.simpletx.protocol.exception;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 09:37
 */
public class ConnectionRuntimeException extends RuntimeException {

    public ConnectionRuntimeException() {
    }

    public ConnectionRuntimeException(String message) {
        super(message);
    }

    public ConnectionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionRuntimeException(Throwable cause) {
        super(cause);
    }

    public ConnectionRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
