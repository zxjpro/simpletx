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

    /**
     * 如果当前类，在spring中，同时注入了多个实例，那么需要通过设置这个属性，来指定该类，在spring中bean的name。
     * 这是因为simple在超时情况下，会触发重试机制，simpletx需要通过bean名称，来定位方法入口
     * @return
     */
    String currentBeanName() default "";

}
