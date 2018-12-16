package com.xiaojiezhu.simpletx.core.exception;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 16:56
 */
public class DuplicateInvokeException extends RuntimeException {

    public DuplicateInvokeException() {
    }

    public DuplicateInvokeException(String message) {
        super(message);
    }

    public DuplicateInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateInvokeException(Throwable cause) {
        super(cause);
    }

    public DuplicateInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
