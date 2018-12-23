package com.xiaojiezhu.simpletx.core.info;

import com.xiaojiezhu.simpletx.common.define.Propagation;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/11/29 11:32
 */
public interface TransactionMethodAttribute {

    Method getMethod();

    /**
     * 方法所在对象
     * @return
     */
    Object getTarget();

    /**
     * get transaction name
     * @return
     */
    String getTransactionManagerName();

    int getTimeout();

    Propagation getPropagation();

    /**
     * 回滚的异常列表
     * @return
     */
    List<String> getRollbackForClassName();

    /**
     * method of bean name
     * @return
     */
    String getCurrentBeanName();


    /**
     * 是否需要补偿
     * @return
     */
    boolean isCompensate();

}
