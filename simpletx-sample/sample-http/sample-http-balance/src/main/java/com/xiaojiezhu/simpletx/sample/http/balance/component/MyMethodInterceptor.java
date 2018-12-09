package com.xiaojiezhu.simpletx.sample.http.balance.component;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author xiaojie.zhu
 * time 2018/12/3 21:32
 */
@Component
public class MyMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("invoe");
        return invocation.proceed();
    }
}
