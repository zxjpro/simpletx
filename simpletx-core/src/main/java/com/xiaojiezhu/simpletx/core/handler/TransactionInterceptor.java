package com.xiaojiezhu.simpletx.core.handler;

import com.xiaojiezhu.simpletx.core.exception.SimpletxCommitException;
import com.xiaojiezhu.simpletx.core.exception.SimpletxRollbackException;
import com.xiaojiezhu.simpletx.core.info.SimpletxTransactionUtil;
import com.xiaojiezhu.simpletx.core.info.TransactionMethodAttribute;
import com.xiaojiezhu.simpletx.core.net.packet.input.TransactionGroupCompleteInputPacket;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;
import com.xiaojiezhu.simpletx.protocol.client.SimpletxContext;
import com.xiaojiezhu.simpletx.protocol.future.Future;
import com.xiaojiezhu.simpletx.util.StringUtils;
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

    public TransactionInterceptor(SimpletxContext simpletxContext) {
        super(simpletxContext);
    }


    @Override
    protected void runAfterSuccess(TransactionInfo transactionInfo) throws SimpletxCommitException {

        if(transactionInfo.isRootTransaction()){

            Future<TransactionGroupCompleteInputPacket> future = getTransactionGroupManager().notifyGroupCommit(transactionInfo.getTransactionGroupId());

            //先提交本地事务， 为了防止远程事务失败，如果远程事务失败，则进入补偿模式
            commitLocalTransaction(transactionInfo);

            boolean remoteSuccess = false;

            try {
                future.await(expireTimeout, TimeUnit.MILLISECONDS);
                remoteSuccess = true;
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
                //TODO: 事务超时未处理
            }

            if (remoteSuccess) {
                TransactionGroupCompleteInputPacket packet = future.getNow();
                if (!packet.isSuccess()) {
                    // just some node success
                    String errMsg = StringUtils.str("transaction group commit fail , success node { " , packet.getSuccessNode() , " } , error node { " , packet.getErrorNode() , " }");
                    throw new SimpletxCommitException(errMsg);
                } else {
                    //all success
                    LOG.trace("simpletx commit transaction group success");
                }
            }

        }else{
            try {
                commitLocalTransaction(transactionInfo);
            } catch (Throwable e) {
                throw new SimpletxCommitException("commit local transaction error " , e);
            }
        }


    }

    /**
     * commit local transaction
     * @param transactionInfo
     */
    protected void commitLocalTransaction(TransactionInfo transactionInfo) {
        LOG.trace("simpletx begin commit local transaction");
        transactionInfo.getTransactionManager().commit(transactionInfo.getStatus());
        LOG.trace("simpletx complete commit local transaction ");
    }

    @Override
    protected void runAfterThrowable(TransactionInfo transactionInfo, Throwable throwable) throws SimpletxRollbackException {
        TransactionMethodAttribute methodAttribute = transactionInfo.getMethodAttribute();

        if(throwable == null ||
                isRollbackAble(methodAttribute.getRollbackForClassName() , throwable)){

            if(transactionInfo.isRootTransaction()){

                Future<TransactionGroupCompleteInputPacket> future = getTransactionGroupManager().notifyGroupRollback(SimpletxTransactionUtil.getTransactionGroupId());


                this.rollbackLocalTransaction(transactionInfo);

                boolean remoteSuccess = false;

                try {
                    future.await(expireTimeout, TimeUnit.MILLISECONDS);
                    remoteSuccess = true;
                } catch (InterruptedException | TimeoutException e) {
                    e.printStackTrace();
                    //TODO: 事务超时未处理
                }

                if(remoteSuccess){
                    TransactionGroupCompleteInputPacket packet = future.getNow();
                    if(!packet.isSuccess()){
                        //not success , just some node success
                        String errMsg = StringUtils.str("transaction group rollback fail , success node { " , packet.getSuccessNode() , " } , error node { " , packet.getErrorNode() , " }");
                        throw new SimpletxRollbackException(errMsg);
                    }else{
                        //all success
                        LOG.trace("simpletx rollback transaction group success");
                    }
                }

            }else{

                try {
                    this.rollbackLocalTransaction(transactionInfo);
                } catch (Throwable e) {
                    throw new SimpletxRollbackException("rollback local transaction error " , e);
                }

            }




        }

    }


    /**
     * rollback local transaction
     * @param transactionInfo
     */
    private void rollbackLocalTransaction(TransactionInfo transactionInfo) {
        LOG.trace("simpletx begin rollback local transaction");
        transactionInfo.getTransactionManager().rollback(transactionInfo.getStatus());
        LOG.trace("simpletx complete rollback local transaction");
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
