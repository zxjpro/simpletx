package com.xiaojiezhu.simpletx.core.handler;

import com.xiaojiezhu.simpletx.core.info.TransactionMethodAttribute;
import com.xiaojiezhu.simpletx.core.transaction.TransactionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xiaojie.zhu
 * time 2018/12/3 22:36
 */
public class TransactionInterceptor extends TransactionAspectSupport {

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    protected void runAfterSuccess(TransactionInfo transactionInfo) {
        LOG.trace("simpletx begin commit transaction");
        transactionInfo.getTransactionManager().commit(transactionInfo.getStatus());
        LOG.trace("simpletx complete commit transaction ");
    }

    @Override
    protected void runAfterThrowable(TransactionInfo transactionInfo, Throwable throwable) {
        TransactionMethodAttribute methodAttribute = transactionInfo.getMethodAttribute();
        if(isRollbackAble(methodAttribute.getRollbackForClassName() , throwable)){
            LOG.trace("simpletx begin rollback transaction");
            transactionInfo.getTransactionManager().rollback(transactionInfo.getStatus());
            LOG.trace("simpletx complete rollback transaction");
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
