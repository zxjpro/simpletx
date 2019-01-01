package com.xiaojiezhu.simpletx.core.handler;

import com.xiaojiezhu.simpletx.common.TransactionGroupFactory;
import com.xiaojiezhu.simpletx.core.annotation.TxTransactional;
import com.xiaojiezhu.simpletx.common.define.Propagation;
import com.xiaojiezhu.simpletx.common.executor.ThreadExecutor;
import com.xiaojiezhu.simpletx.common.parameter.MethodParameter;
import com.xiaojiezhu.simpletx.core.exception.SimpletxCommitException;
import com.xiaojiezhu.simpletx.core.exception.SimpletxCreateTransactionGroupException;
import com.xiaojiezhu.simpletx.core.exception.SimpletxJoinTransactionGroupException;
import com.xiaojiezhu.simpletx.core.exception.SimpletxRollbackException;
import com.xiaojiezhu.simpletx.core.info.SimpleTransactionMethodAttribute;
import com.xiaojiezhu.simpletx.core.info.SimpletxTransactionUtil;
import com.xiaojiezhu.simpletx.core.info.TransactionMethodAttribute;
import com.xiaojiezhu.simpletx.core.net.packet.input.CommitRollbackInputPacket;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupManager;
import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.future.Future;
import com.xiaojiezhu.simpletx.protocol.future.FutureCondition;
import com.xiaojiezhu.simpletx.protocol.future.Futures;
import com.xiaojiezhu.simpletx.protocol.packet.OkErrorPacket;
import com.xiaojiezhu.simpletx.util.MessageIdGenerator;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaojie.zhu
 * time 2018/11/26 22:25
 */
public abstract class TransactionAspectSupport implements InitializingBean, BeanFactoryAware {


    /**
     * TODO: 需要提取为参数
     * 事务的超时时间
     */
    protected long expireTimeout = 10000;

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final Map<Method, TransactionMethodAttribute> TRANSACTION_INFO = new ConcurrentHashMap<>(10);


    private BeanFactory beanFactory;

    private TransactionAttributeSource tas;

    /**
     * manage transaction group
     */
    private TransactionGroupManager transactionGroupManager;

    private ThreadExecutor threadExecutor;

    private final SimpletxContext simpletxContext;

    private final ConcurrentReferenceHashMap<Object, PlatformTransactionManager> transactionManagerCache =
            new ConcurrentReferenceHashMap<>(1);

    public static final String DEFAULT_TRANSACTION_MANAGER_NAME = "DEFAULT_TRANSACTION_MANAGER";


    public TransactionAspectSupport(SimpletxContext simpletxContext) {
        this.simpletxContext = simpletxContext;
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
            synchronized (this) {
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
            synchronized (this) {
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

        MethodParameter methodParameter = createMethodParameter(point, transactionMethodAttribute);

        if (transactionInfo.isRootTransaction()) {

            return invokeRootTransaction(point, transactionInfo, methodParameter);

        } else {
            // join transaction
            return invokeJoinTransaction(point, transactionInfo, methodParameter);

        }



    }

    /**
     * invoke join transaction
     * @param point
     * @param transactionInfo
     * @param methodParameter
     * @return
     * @throws Throwable
     */
    private Object invokeJoinTransaction(ProceedingJoinPoint point, TransactionInfo transactionInfo, MethodParameter methodParameter) throws Throwable {
        joinTransactionGroup(transactionInfo, methodParameter);



        final int futureId = MessageIdGenerator.getInstance().next();
        final Future<MethodResult> future = Futures.createFuture(this.simpletxContext.getFutureContainer(), futureId);

        ThreadExecutor threadExecutor = getThreadExecutor();
        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                Object result = null;
                Throwable ex = null;
                //init status
                transactionInfo.getStatus();
                try {
                    result = point.proceed();
                } catch (Throwable throwable) {
                    ex = throwable;
                } finally {
                    SimpletxTransactionUtil.clearThreadResource();
                }
                FutureCondition futureCondition = simpletxContext.getFutureContainer().find(futureId);
                futureCondition.setValue(new MethodResult(result , ex));

                futureCondition.signal();
                if(futureCondition.getNum() <= 0){
                    simpletxContext.getFutureContainer().remove(futureId);
                }


                Future<CommitRollbackInputPacket> future = Futures.createFuture(simpletxContext.getFutureContainer(), transactionInfo.getTransactionGroupId());
                try {
                    future.await(expireTimeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException | TimeoutException e) {
                    e.printStackTrace();
                    //TODO: 等待simpletx-server的通知超时
                }

                CommitRollbackInputPacket packet = future.getNow();
                Throwable t = null;
                long startTime = System.currentTimeMillis();
                if(packet.isCommit()){
                    LOG.info("receive simpletx-server command to commit local transaction  , transaction group id : " + transactionInfo.getTransactionGroupId());
                    try {
                        runAfterSuccess(transactionInfo);
                    } catch (Throwable e) {
                        t = e;
                        e.printStackTrace();
                        //TODO: 本地事务提交失败
                    }
                }else{
                    LOG.info("receive simpletx-server command to rollback local transaction  , transaction group id : " + transactionInfo.getTransactionGroupId());
                    try {
                        runAfterThrowable(transactionInfo , null);
                    } catch (Throwable e) {
                        t = e;
                        e.printStackTrace();
                        //TODO: 本地事务回滚失败
                    }
                }

                long endTime = System.currentTimeMillis();
                long useTime = endTime - startTime;
                getTransactionGroupManager().notifyLocalTransactionComplete(packet.isCommit() , t == null , packet.getMessageId() ,useTime);


            }
        });

        MethodResult methodResult = null;
        try {
            methodResult = future.get();
        } catch (InterruptedException e) {
            //这行错误应该不会遇到
            throw new RuntimeException("target method execute be interrupt!" , e);
        }

        if(methodResult.getThrowable() != null){
            throw methodResult.getThrowable();
        }

        return methodResult.getResult();
    }


    /**
     * invoke root transaction
     * @param point
     * @param transactionInfo
     * @param methodParameter
     * @return
     * @throws Throwable
     */
    private Object invokeRootTransaction(ProceedingJoinPoint point, TransactionInfo transactionInfo, MethodParameter methodParameter) throws Throwable {
        createTransactionGroup(transactionInfo, methodParameter);

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
    }



    private MethodParameter createMethodParameter(ProceedingJoinPoint point, TransactionMethodAttribute transactionMethodAttribute) {
        MethodParameter methodParameter = new MethodParameter();
        methodParameter.setArgs(point.getArgs());
        methodParameter.setBeanName(transactionMethodAttribute.getCurrentBeanName());
        methodParameter.setClassName(transactionMethodAttribute.getTarget().getClass().getName());
        methodParameter.setMethodName(transactionMethodAttribute.getMethod().getName());
        return methodParameter;
    }


    /**
     * run target method with success
     *
     * @param transactionInfo
     */
    protected abstract void runAfterSuccess(TransactionInfo transactionInfo) throws SimpletxCommitException;

    /**
     * run target method throw exception
     *
     * @param transactionInfo
     * @param throwable
     */
    protected abstract void runAfterThrowable(TransactionInfo transactionInfo, Throwable throwable) throws SimpletxRollbackException;


    protected TransactionInfo createTransactionIfNecessary(TransactionMethodAttribute methodAttribute) {
        if (methodAttribute == null) {
            return null;
        }
        if (Propagation.NOT_SUPPORTED == methodAttribute.getPropagation()) {
            return null;
        }

        final TransactionAttribute txAttr = (tas != null ? tas.getTransactionAttribute(methodAttribute.getMethod(), methodAttribute.getTarget().getClass()) : null);

        PlatformTransactionManager tm = determineQualifierTransactionManager(methodAttribute.getTransactionManagerName());

//        TransactionStatus transactionStatus = tm.getTransaction(txAttr);

        //TODO: 其它请求进入时，还没有把transactionGroupId加入到线程变量中
        boolean rootTransaction = false;
        String transactionGroupId = SimpletxTransactionUtil.getTransactionGroupId();
        if (transactionGroupId == null) {
            rootTransaction = true;
            transactionGroupId = TransactionGroupFactory.getInstance().generateGroupId();

            SimpletxTransactionUtil.setTransactionGroupId(transactionGroupId);
        }

        TransactionInfo transactionInfo = new TransactionInfo(methodAttribute, tm, txAttr, rootTransaction, transactionGroupId);

        return transactionInfo;

    }

    /**
     * run without transaction
     * @param point
     * @return
     * @throws Throwable
     */
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
     * join transaction group , wait to server response success ,  if join fail , it will throw exception
     * @param transactionInfo
     * @param methodParameter
     */
    private void joinTransactionGroup(TransactionInfo transactionInfo, MethodParameter methodParameter) throws TimeoutException, InterruptedException, SimpletxJoinTransactionGroupException {
        Future<OkErrorPacket> future = getTransactionGroupManager().joinGroup(SimpletxTransactionUtil.getTransactionGroupId(), transactionInfo, methodParameter);

        OkErrorPacket okErrorResult = future.get(this.expireTimeout, TimeUnit.MILLISECONDS);
        if(!okErrorResult.isOk()){
            // join simpletx-server transaction group fail
            throw new SimpletxJoinTransactionGroupException("join simpletx-server transaction group fail : " + okErrorResult.getMessage());
        }


    }

    /**
     * create transaction group , wait to server response success , if create fail , it will throw exception
     * @param transactionInfo
     * @param methodParameter
     */
    private void createTransactionGroup(TransactionInfo transactionInfo, MethodParameter methodParameter) throws TimeoutException, InterruptedException, SimpletxCreateTransactionGroupException {
        Future<OkErrorPacket> future = getTransactionGroupManager().createGroup(SimpletxTransactionUtil.getTransactionGroupId(), transactionInfo, methodParameter);

        OkErrorPacket okErrorResult = future.get(this.expireTimeout, TimeUnit.MILLISECONDS);
        if(!okErrorResult.isOk()){
            // create simpletx-server transaction group fail
            throw new SimpletxCreateTransactionGroupException("create simpletx-server transaction group fail : " + okErrorResult.getMessage());
        }

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
        //TODO : 这里指定，出现异常，是否补偿，还没有抽成配置文件
        attribute.setCompensate(true);

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


    protected final SimpletxContext getSimpletxContext(){
        return this.simpletxContext;
    }
}
