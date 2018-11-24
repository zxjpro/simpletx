package com.xiaojiezhu.simpletx.common.define;

/**
 * @author xiaojie.zhu
 * time 2018/11/24 21:41
 */
public enum Propagation {

    /**
     * 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中
     */
    REQUIRED(0),

    /**
     * 如果当前已经存在事务，那么加入该事务，否则就不执行事务
     */
    SUPPORTS(1),

    /**
     * 如果当前不存在事务，则抛出异常
     */
    MANDATORY(2),

    /**
     * 创建一个新的事务。如果当前存在着一个事务，就将其挂起，也就是不使用这个事务，额外创建一个新的事务，而之前的事务会保存起来，在这个方法结束后，还会接着用到
     *
     */
    REQUIRES_NEW(3),

    /**
     * 被标记的方法不处理事务
     */
    NOT_SUPPORTED(4),

    /**
     * 如果当前存在事务，则抛出异常
     */
    NEVER(5),

    /**
     * 在执行到该注解标记的方法时，会设置一个save-point
     */
    NESTED(6);

    private int value;

    Propagation(int value) {
        this.value = value;
    }
}
