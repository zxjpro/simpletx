package com.xiaojiezhu.simpletx.common.executor;

/**
 * @author xiaojie.zhu
 * time 2018/12/9 15:33
 */
public class WorkerException extends RuntimeException {

    public WorkerException() {
    }

    public WorkerException(String message) {
        super(message);
    }

    public WorkerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkerException(Throwable cause) {
        super(cause);
    }

    public WorkerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
