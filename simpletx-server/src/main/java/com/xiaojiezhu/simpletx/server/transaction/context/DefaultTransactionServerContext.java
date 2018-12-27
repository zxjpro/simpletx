package com.xiaojiezhu.simpletx.server.transaction.context;

import com.xiaojiezhu.simpletx.protocol.server.DefaultServerContext;
import com.xiaojiezhu.simpletx.server.transaction.DefaultTransactionGroup;
import com.xiaojiezhu.simpletx.server.transaction.TransactionBlock;
import com.xiaojiezhu.simpletx.server.transaction.TransactionGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 16:40
 */
public class DefaultTransactionServerContext extends DefaultServerContext implements TransactionServerContext {

    private final ConcurrentHashMap<String , TransactionGroup> groups = new ConcurrentHashMap<>();



    public DefaultTransactionServerContext() {
        Thread thread = new Thread(new CleanTool());
        thread.setName(getClass().getSimpleName() + "#cleanTool");
        thread.start();
    }

    @Override
    public void createTransactionGroup(String groupId, TransactionBlock transactionBlock) {
        TransactionGroup transactionGroup = new DefaultTransactionGroup(groupId , transactionBlock);
        this.groups.put(groupId , transactionGroup);
    }

    @Override
    public void removeTransactionGroup(String groupId) {
        this.groups.remove(groupId);
    }

    @Override
    public TransactionGroup findTransactionGroup(String groupId) {
        return this.groups.get(groupId);
    }

    @Override
    public void joinTransactionGroup(String groupId, TransactionBlock transactionBlock) {

        TransactionGroup transactionGroup = groups.get(groupId);
        if(transactionGroup == null){
            throw new NullPointerException("not found transaction group , groupId:" + groupId);
        }
        transactionGroup.join(transactionBlock);

    }

    @Override
    public int transactionGroupSize() {
        return this.groups.size();
    }



    class CleanTool implements Runnable{
        final Logger logger = LoggerFactory.getLogger(getClass());
        private final int expireTime = 2 * 60 * 1000;

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(expireTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Iterator<Map.Entry<String, TransactionGroup>> iterator = groups.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, TransactionGroup> entry = iterator.next();
                    TransactionGroup group = entry.getValue();

                    if((System.currentTimeMillis() - group.getCreateTime()) > expireTime){
                        iterator.remove();
                        logger.warn("transaction group is expire " + expireTime + "ms , and has be to remove , groupId:" + group.getTransactionId());
                    }
                }
            }
        }
    }
}
