package com.xiaojiezhu.simpletx.common.node;

/**
 * @author xiaojie.zhu
 * time 2018/12/12 23:20
 */
public interface StatusNodeBlock extends NodeBlock {

    /**
     * the block create time
     * @return
     */
    long getCreateTime();


    /**
     * the block status
     * @return
     */
    State state();

    /**
     * the method is invoke success and not throw any exception
     * @return
     */
    Boolean isInvokeSuccess();



    enum State{
        RUNNING,
        COMPLETE
    }
}
