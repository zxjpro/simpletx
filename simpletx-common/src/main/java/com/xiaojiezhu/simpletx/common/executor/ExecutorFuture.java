package com.xiaojiezhu.simpletx.common.executor;

/**
 * @author xiaojie.zhu
 * time 2018/12/8 17:37
 */
public interface ExecutorFuture {

    /**
     * get task status
     * @return
     */
    Status getStatus();


    public static enum Status {

        /**
         * 未加入,等待中
         */
        WAITING,

        /**
         * 正在执行中
         */
        EXECUTERING,

        /**
         * 执行完成
         */
        COMPLETE,
    }
}
