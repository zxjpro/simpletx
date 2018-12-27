package com.xiaojiezhu.simpletx.core.handler;

import com.xiaojiezhu.simpletx.core.exception.SimpletxCommitException;
import com.xiaojiezhu.simpletx.core.exception.SimpletxRollbackException;
import com.xiaojiezhu.simpletx.core.info.SimpletxTransactionUtil;
import com.xiaojiezhu.simpletx.core.info.TransactionMethodAttribute;
import com.xiaojiezhu.simpletx.core.net.OkErrorResult;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;
import com.xiaojiezhu.simpletx.core.transaction.manager.TransactionGroupInvokeStatus;
import com.xiaojiezhu.simpletx.protocol.future.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaojie.zhu
 * time 2018/12/3 22:36
 */
public class TransactionInterceptor extends TransactionAspectSupport {



    public final Logger LOG = LoggerFactory.getLogger(getClass());



    @Override
    protected void runAfterSuccess(TransactionInfo transactionInfo) throws SimpletxCommitException {

        Future<OkErrorResult> future = getTransactionGroupManager().notifyCommit(SimpletxTransactionUtil.getTransactionGroupId());

        //先提交本地事务， 为了防止远程事务失败，如果远程事务失败，则进入补偿模式
        LOG.trace("simpletx begin commit transaction");
        transactionInfo.getTransactionManager().commit(transactionInfo.getStatus());
        LOG.trace("simpletx complete commit transaction ");

        boolean remoteSuccess = false;

        try {
            future.await(transactionTimeout , TimeUnit.MILLISECONDS);
            remoteSuccess = true;
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
            //TODO: 事务超时未处理
        }

        if(remoteSuccess){
            OkErrorResult okErrorResult = future.getNow();
            if(!okErrorResult.isSuccess()){
                throw new SimpletxCommitException("simpletx commit transaction group error , " + okErrorResult.getMessage());
            }else{
                LOG.trace("simpletx commit transaction group success");
            }
        }

    }

    @Override
    protected void runAfterThrowable(TransactionInfo transactionInfo, Throwable throwable) throws SimpletxRollbackException {
        TransactionMethodAttribute methodAttribute = transactionInfo.getMethodAttribute();

        if(throwable == null ||
                isRollbackAble(methodAttribute.getRollbackForClassName() , throwable)){

            Future<OkErrorResult> future = getTransactionGroupManager().notifyRollback(SimpletxTransactionUtil.getTransactionGroupId());


            LOG.trace("simpletx begin rollback local transaction");
            transactionInfo.getTransactionManager().rollback(transactionInfo.getStatus());
            LOG.trace("simpletx complete rollback local transaction");

            boolean remoteSuccess = false;

            try {
                future.await(transactionTimeout , TimeUnit.MILLISECONDS);
                remoteSuccess = true;
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
                //TODO: 事务超时未处理
            }

            if(remoteSuccess){
                OkErrorResult okErrorResult = future.getNow();
                if(!okErrorResult.isSuccess()){
                    throw new SimpletxRollbackException("simpletx rollback transaction group error , " + okErrorResult.getMessage());
                }else{
                    LOG.trace("simpletx rollback transaction group success");
                }
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
