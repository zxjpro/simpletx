package com.xiaojiezhu.simpletx.core.annotation;

import java.lang.annotation.*;

/**
 *
 * 基于tcc方案的分布式事务
 * @author xiaojie.zhu
 * time 2018-12-31 12:18
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TccTransactional {
}
