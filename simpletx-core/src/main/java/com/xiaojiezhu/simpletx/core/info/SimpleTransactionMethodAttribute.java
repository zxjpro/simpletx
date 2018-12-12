package com.xiaojiezhu.simpletx.core.info;

import com.xiaojiezhu.simpletx.common.define.Propagation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/2 12:53
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleTransactionMethodAttribute implements TransactionMethodAttribute {

    private Method method;
    private Object targetObject;
    private String transactionManagerName;
    private int timeout;
    private Propagation propagation;
    private List<String> rollbackForClassName;
    private String currentBeanName;


    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object getTarget() {
        return this.targetObject;
    }

    @Override
    public String getTransactionManagerName() {
        return this.transactionManagerName;
    }

    @Override
    public int getTimeout() {
        return this.timeout;
    }

    @Override
    public Propagation getPropagation() {
        return this.propagation;
    }

    @Override
    public List<String> getRollbackForClassName() {
        return this.rollbackForClassName;
    }
}
