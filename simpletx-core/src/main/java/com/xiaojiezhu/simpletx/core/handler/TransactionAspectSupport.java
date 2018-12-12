package com.xiaojiezhu.simpletx.core.handler;

import com.xiaojiezhu.simpletx.common.TransactionGroupFactory;
import com.xiaojiezhu.simpletx.common.annotation.TxTransactional;
import com.xiaojiezhu.simpletx.common.define.Propagation;
import com.xiaojiezhu.simpletx.common.parameter.MethodParameter;
import com.xiaojiezhu.simpletx.core.info.SimpleTransactionMethodAttribute;
import com.xiaojiezhu.simpletx.core.info.SimpletxTransactionUtil;
import com.xiaojiezhu.simpletx.core.info.TransactionMethodAttribute;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupManager;
import com.xiaojiezhu.simpletx.util.bean.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/11/26 22:25
 */
public abstract class TransactionAspectSupport implements InitializingBean, BeanFactoryAware {


    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final Map<Method, TransactionMethodAttribute> TRANSACTION_INFO = new ConcurrentHashMap<>(10);


    private BeanFactory beanFactory;

    private TransactionAttributeSource tas;

    private TransactionGroupManager transactionGroupManager;


    private final ConcurrentReferenceHashMap<Object, PlatformTransactionManager> transactionManagerCache =
            new ConcurrentReferenceHashMap<>(1);

    public static final String DEFAULT_TRANSACTION_MANAGER_NAME = "DEFAULT_TRANSACTION_MANAGER";

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //System.out.println(getClass().getName() + ".afterPropertiesSet");
    }

    /**
     * invoke this aspect method
     *
     * @param point apo around point
     * @return the result of invoke the target method
     */
    public Object invoke(ProceedingJoinPoint point) throws Throwable {
        TransactionMethodAttribute transactionMethodAttribute = findTransactionMethodAttribute(point);
        TransactionInfo transactionInfo = createTransactionIfNecessary(transactionMethodAttribute);


        if (transactionInfo == null) {
            return this.runWithoutTransaction(point);
        }

        if(transactionInfo.isRootTransaction()){
            MethodParameter methodParameter = new MethodParameter();
            methodParameter.setArgs(point.getArgs());
            methodParameter.setBeanName(transactionMethodAttribute.getCurrentBeanName());
            methodParameter.setClassName(this.getClass().getName());
            methodParameter.setMethodName(transactionMethodAttribute.getMethod().getName());

            transactionGroupManager.createGroup(transactionInfo , methodParameter);

            Object result;
            try {
                result = point.proceed();

            } catch (Throwable throwable) {
                runAfterThrowable(transactionInfo , throwable);
                throw throwable;

            } finally {
                SimpletxTransactionUtil.clearThreadResource();
            }

            runAfterSuccess(transactionInfo);

            return result;
        }else{
            // join transaction
        }



        return null;

    }

    /**
     * run target method with success
     * @param transactionInfo
     */
    protected abstract void runAfterSuccess(TransactionInfo transactionInfo);

    /**
     * run target method throw exception
     * @param transactionInfo
     * @param throwable
     */
    protected abstract void runAfterThrowable(TransactionInfo transactionInfo, Throwable throwable);


    protected TransactionInfo createTransactionIfNecessary(TransactionMethodAttribute methodAttribute) {
        if (methodAttribute == null) {
            return null;
        }
        if (Propagation.NOT_SUPPORTED == methodAttribute.getPropagation()) {
            return null;
        }

        final TransactionAttribute txAttr = (tas != null ? tas.getTransactionAttribute(methodAttribute.getMethod(), methodAttribute.getTarget().getClass()) : null);

        PlatformTransactionManager tm = determineQualifierTransactionManager(methodAttribute.getTransactionManagerName());

        TransactionStatus transactionStatus = tm.getTransaction(txAttr);

        boolean rootTransaction = false;
        String transactionGroupId = SimpletxTransactionUtil.getTransactionGroupId();
        if(transactionGroupId == null){
            rootTransaction = true;
            transactionGroupId = TransactionGroupFactory.getInstance().generateGroupId();

            SimpletxTransactionUtil.setTransactionGroupId(transactionGroupId);
        }

        TransactionInfo transactionInfo = new TransactionInfo(methodAttribute , tm , txAttr,transactionStatus , rootTransaction , transactionGroupId);

        return transactionInfo;

    }

    private Object runWithoutTransaction(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();
        return proceed;

    }


    private PlatformTransactionManager determineQualifierTransactionManager(String qualifier) {
        if(StringUtils.isEmpty(qualifier)){
            qualifier = DEFAULT_TRANSACTION_MANAGER_NAME;
        }

        PlatformTransactionManager platformTransactionManager = this.transactionManagerCache.get(qualifier);
        if (platformTransactionManager == null) {
            if(DEFAULT_TRANSACTION_MANAGER_NAME.equals(qualifier)){
                platformTransactionManager = this.beanFactory.getBean(PlatformTransactionManager.class);
                if(platformTransactionManager == null){
                    throw new NullPointerException("not found a " + qualifier + " PlatformTransactionManager bean in spring");
                }
                this.transactionManagerCache.put(qualifier, platformTransactionManager);


            }else{
                platformTransactionManager = BeanFactoryAnnotationUtils.qualifiedBeanOfType(this.beanFactory, PlatformTransactionManager.class, qualifier);
                if (platformTransactionManager == null) {
                    throw new NullPointerException("not found a " + qualifier + " PlatformTransactionManager bean in spring");
                }
                this.transactionManagerCache.put(qualifier, platformTransactionManager);
            }

        }
        return platformTransactionManager;
    }


    /**
     * find from cache , if not exists , load from class
     *
     * @param point
     * @return
     */
    private TransactionMethodAttribute findTransactionMethodAttribute(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        final Method method = signature.getMethod();

        TransactionMethodAttribute transactionMethodAttribute = TRANSACTION_INFO.get(method);
        if (transactionMethodAttribute != null) {
            return transactionMethodAttribute;
        } else {
            synchronized (method) {
                if (transactionMethodAttribute != null) {
                    return transactionMethodAttribute;
                }
                transactionMethodAttribute = this.loadTransactionMethodAttribute(point, method);
                TRANSACTION_INFO.put(method, transactionMethodAttribute);
            }
        }

        return transactionMethodAttribute;
    }

    /**
     * load from clazz
     *
     * @param point
     * @return
     */
    protected TransactionMethodAttribute loadTransactionMethodAttribute(ProceedingJoinPoint point, Method method) {
        SimpleTransactionMethodAttribute attribute = new SimpleTransactionMethodAttribute();
        attribute.setMethod(method);
        attribute.setTargetObject(point.getTarget());

        TxTransactional annotation = method.getAnnotation(TxTransactional.class);

        attribute.setTimeout(annotation.timeout());
        attribute.setTransactionManagerName(annotation.transactionManager());
        attribute.setPropagation(annotation.propagation());
        attribute.setCurrentBeanName(annotation.currentBeanName());

        if (!Arrays.isEmpty(annotation.rollbackFor()) || !Arrays.isEmpty(annotation.rollbackForClassName())) {
            List<String> rollbackFor = new ArrayList<>(annotation.rollbackFor().length + annotation.rollbackForClassName().length);
            if (annotation.rollbackFor().length > 0) {
                for (Class<? extends Throwable> eClass : annotation.rollbackFor()) {
                    rollbackFor.add(eClass.getName());
                }
            }
            if (annotation.rollbackForClassName().length > 0) {
                for (String eClassName : annotation.rollbackForClassName()) {
                    rollbackFor.add(eClassName);
                }
            }

            attribute.setRollbackForClassName(rollbackFor);
        }

        return attribute;
    }

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
        this.tas = transactionAttributeSource;
    }
}
