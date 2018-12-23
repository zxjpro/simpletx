package com.xiaojiezhu.simpletx.core.handler;

import com.xiaojiezhu.simpletx.common.executor.Future;
import com.xiaojiezhu.simpletx.core.info.SimpletxTransactionUtil;
import com.xiaojiezhu.simpletx.core.info.TransactionMethodAttribute;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupInvokeFuture;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupInvokeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaojie.zhu
 * time 2018/12/3 22:36
 */
public class TransactionInterceptor extends TransactionAspectSupport {



    public final Logger LOG = LoggerFactory.getLogger(getClass());



    @Override
    protected void runAfterSuccess(TransactionInfo transactionInfo) {
        TransactionGroupInvokeFuture future = getTransactionGroupManager().notifyCommit(SimpletxTransactionUtil.getTransactionGroupId());

        try {
            TransactionGroupInvokeStatus status = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            //TODO: 事务超时未处理
        }


        LOG.trace("simpletx begin commit transaction");
        transactionInfo.getTransactionManager().commit(transactionInfo.getStatus());
        LOG.trace("simpletx complete commit transaction ");
    }

    @Override
    protected void runAfterThrowable(TransactionInfo transactionInfo, Throwable throwable) {
        TransactionMethodAttribute methodAttribute = transactionInfo.getMethodAttribute();

        if(throwable == null ||
                isRollbackAble(methodAttribute.getRollbackForClassName() , throwable)){

            TransactionGroupInvokeFuture future = getTransactionGroupManager().notifyRollback(SimpletxTransactionUtil.getTransactionGroupId());


            LOG.trace("simpletx begin rollback local transaction");
            transactionInfo.getTransactionManager().rollback(transactionInfo.getStatus());
            LOG.trace("simpletx complete rollback local transaction");

            try {
                TransactionGroupInvokeStatus status = future.get(this.transactionTimeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //TODO: 事务超时未处理
            }


        }

    }

    private boolean isRollbackAble(List<String> rollbackClassNames , Throwable throwable){
        if(rollbackClassNames == null || rollbackClassNames.size() == 0){
            return true;
        }else{
            for (String rollbackClassName : rollbackClassNames) {
                if(rollbackClassName.equals(throwable.getClass().getName())){
                    return true;
                }
            }

            return false;
        }
    }
}
