package com.xiaojiezhu.simpletx.core;

import com.xiaojiezhu.simpletx.common.annotation.TxTransactional;
import com.xiaojiezhu.simpletx.core.handler.TransactionAspectSupport;
import com.xiaojiezhu.simpletx.util.bean.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/11/26 22:09
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class TransactionAspectConfigure implements InitializingBean {

    private TransactionAspectSupport transactionAspectSupport;

    public TransactionAspectConfigure(TransactionAspectSupport transactionAspectSupport) {
        this.transactionAspectSupport = transactionAspectSupport;
    }

    @Pointcut("@annotation(com.xiaojiezhu.simpletx.common.annotation.TxTransactional)")
    public void txPoint(){}


    @Around("txPoint()")
    public Object invokeTargetMethod(ProceedingJoinPoint point) throws Throwable {

        Object result = transactionAspectSupport.invoke(point);

        return result;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
