package com.xiaojiezhu.simpletx.client.annotation;

import com.xiaojiezhu.simpletx.client.spring.SimpletxAutoConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author xiaojie.zhu
 * time 2018/12/2 22:11
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SimpletxAutoConfigure.class)
public @interface EnableSimpletxTransaction {
}
