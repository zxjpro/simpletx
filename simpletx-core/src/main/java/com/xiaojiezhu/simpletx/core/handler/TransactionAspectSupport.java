package com.xiaojiezhu.simpletx.core.handler;

import com.xiaojiezhu.simpletx.common.TransactionGroupFactory;
import com.xiaojiezhu.simpletx.common.annotation.TxTransactional;
import com.xiaojiezhu.simpletx.common.define.Propagation;
import com.xiaojiezhu.simpletx.common.executor.ThreadExecutor;
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
import java.util.concurrent.TimeoutException;

/**
 * @author xiaojie.zhu
 * time 2018/11/26 22:25
 */
public abstract class TransactionAspectSupport implements InitializingBean, BeanFactoryAware {

    /**
     * the thread wait queue logger over size
     */
    private static final int LOGGER_TASK_WAIT_SIZE = 2;

    /**
     * TODO: 需要提取为参数
     * 事务的超时时间
     */
    protected long transactionTimeout = 10000;

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final Map<Method, TransactionMethodAttribute> TRANSACTION_INFO = new ConcurrentHashMap<>(10);


    private BeanFactory beanFactory;

    private TransactionAttributeSource tas;

    /**
     * manage transaction group
     */
    private TransactionGroupManager transactionGroupManager;

    private ThreadExecutor threadExecutor;


    private final ConcurrentReferenceHashMap<Object, PlatformTransactionManager> transactionManagerCache =
            new ConcurrentReferenceHashMap<>(1);

    public static final String DEFAULT_TRANSACTION_MANAGER_NAME = "DEFAULT_TRANSACTION_MANAGER";

    public TransactionAspectSupport() {
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * init and get the thread executor
     *
     * @return
     */
    protected final ThreadExecutor getThreadExecutor() {
        if (this.threadExecutor == null) {
            synchronized (getClass()) {
                if (this.threadExecutor == null) {
                    this.threadExecutor = this.beanFactory.getBean(ThreadExecutor.class);
                }
            }
        }

        return this.threadExecutor;
    }

    /**
     * init and get the transaction group manager
     *
     * @return
     */
    protected final TransactionGroupManager getTransactionGroupManager() {
        if (this.transactionGroupManager == null) {
            synchronized (getClass()) {
                if (this.transactionGroupManager == null) {
                    this.transactionGroupManager = this.beanFactory.getBean(TransactionGroupManager.class);
                }
            }
        }

        return this.transactionGroupManager;
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

        MethodParameter methodParameter = new MethodParameter();
        methodParameter.setArgs(point.getArgs());
        methodParameter.setBeanName(transactionMethodAttribute.getCurrentBeanName());
        methodParameter.setClassName(transactionMethodAttribute.getTarget().getClass().getName());
        methodParameter.setMethodName(transactionMethodAttribute.getMethod().getName());

        if (transactionInfo.isRootTransaction()) {
            transactionGroupManager.createGroup(transactionInfo, methodParameter);

            Object result;
            try {
                result = point.proceed();

            } catch (Throwable throwable) {
                runAfterThrowable(transactionInfo, throwable);
                throw throwable;

            } finally {
                SimpletxTransactionUtil.clearThreadResource();
            }

            runAfterSuccess(transactionInfo);

            return result;
        } else {
            // join transaction
            transactionGroupManager.joinGroup(transactionInfo, methodParameter);

            Object result = null;
            Throwable ex = null;

            try {
                result = point.proceed();
            } catch (Throwable throwable) {
                ex = throwable;
            } finally {
                SimpletxTransactionUtil.clearThreadResource();
            }


            final Runnable commitRunnable = createCommit(transactionInfo);
            final Runnable rollbackRunnable = createRollback(transactionInfo);

            ThreadExecutor threadExecutor = getThreadExecutor();
            threadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    TransactionInvokeFuture invokeFuture = new TransactionInvokeFuture(commitRunnable, rollbackRunnable,
                            transactionInfo.getTransactionGroupId(), transactionMethodAttribute.getTimeout());

                    try {
                        invokeFuture.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //TODO: 等待被中止
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                        //TODO: 等待超时
                    }
                }
            });
            long waitThreadSize = threadExecutor.getWaitThreadSize();
            if(waitThreadSize > LOGGER_TASK_WAIT_SIZE){
                LOG.error("task queue is waiting " + waitThreadSize + " size");
            }

            if (ex != null) {
                throw ex;
            }

            return result;

        }



    }

    protected Runnable createCommit(TransactionInfo transactionInfo) {
        final Runnable commitRunnable = new Runnable() {
            @Override
            public void run() {
                runAfterSuccess(transactionInfo);
            }
        };
        return commitRunnable;
    }

    protected Runnable createRollback(TransactionInfo transactionInfo) {
        final Runnable rollbackRunnable = new Runnable() {
            @Override
            public void run() {
                runAfterThrowable(transactionInfo, null);
            }
        };
        return rollbackRunnable;
    }

    /**
     * run target method with success
     *
     * @param transactionInfo
     */
    protected abstract void runAfterSuccess(TransactionInfo transactionInfo);

    /**
     * run target method throw exception
     *
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
        if (transactionGroupId == null) {
            rootTransaction = true;
            transactionGroupId = TransactionGroupFactory.getInstance().generateGroupId();

            SimpletxTransactionUtil.setTransactionGroupId(transactionGroupId);
        }

        TransactionInfo transactionInfo = new TransactionInfo(methodAttribute, tm, txAttr, transactionStatus, rootTransaction, transactionGroupId);

        return transactionInfo;

    }

    private Object runWithoutTransaction(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();
        return proceed;

    }


    /**
     * find transactionManager , get from cache , if not exist , search from spring and cache
     * @param qualifier
     * @return
     */
    private PlatformTransactionManager determineQualifierTransactionManager(String qualifier) {
        if (StringUtils.isEmpty(qualifier)) {
            qualifier = DEFAULT_TRANSACTION_MANAGER_NAME;
        }

        PlatformTransactionManager platformTransactionManager = this.transactionManagerCache.get(qualifier);
        if (platformTransactionManager == null) {
            if (DEFAULT_TRANSACTION_MANAGER_NAME.equals(qualifier)) {
                platformTransactionManager = this.beanFactory.getBean(PlatformTransactionManager.class);
                if (platformTransactionManager == null) {
                    throw new NullPointerException("not found a " + qualifier + " PlatformTransactionManager bean in spring");
                }
                this.transactionManagerCache.put(qualifier, platformTransactionManager);


            } else {
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
