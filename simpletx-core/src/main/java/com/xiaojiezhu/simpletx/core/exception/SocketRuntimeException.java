package com.xiaojiezhu.simpletx.core.exception;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 17:58
 */
public class SocketRuntimeException extends RuntimeException {


    public SocketRuntimeException() {
    }

    public SocketRuntimeException(String message) {
        super(message);
    }

    public SocketRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketRuntimeException(Throwable cause) {
        super(cause);
    }

    public SocketRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
