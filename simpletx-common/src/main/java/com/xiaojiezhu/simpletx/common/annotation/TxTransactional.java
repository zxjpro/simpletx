package com.xiaojiezhu.simpletx.common.annotation;


import com.xiaojiezhu.simpletx.common.define.Propagation;

import java.lang.annotation.*;

/**
 * 分布式事务注解
 * @author xiaojie.zhu
 * time 2018/11/24 17:01
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TxTransactional {


    /**
     * 在同时有多个事务管理器的情况下，使用哪个事务管理器
     * @returnN
     */
    String transactionManager() default "";

    /**
     * 事务的传播级别
     * @return
     */
    Propagation propagation() default Propagation.REQUIRED;

    int timeout() default -1;


    Class<? extends Throwable>[] rollbackFor() default {};


    String[] rollbackForClassName() default {};

}
