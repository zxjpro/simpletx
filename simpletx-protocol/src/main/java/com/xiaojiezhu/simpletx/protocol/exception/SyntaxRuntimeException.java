package com.xiaojiezhu.simpletx.protocol.exception;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 00:02
 */
public class SyntaxRuntimeException extends RuntimeException {

    public SyntaxRuntimeException() {
    }

    public SyntaxRuntimeException(String message) {
        super(message);
    }

    public SyntaxRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxRuntimeException(Throwable cause) {
        super(cause);
    }

    public SyntaxRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
