package com.xiaojiezhu.simpletx.protocol.client;

import com.xiaojiezhu.simpletx.protocol.future.FutureCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/27 11:30
 */
public class ExpireFutureContainer extends SimpleFutureContainer {

    public final Logger LOG = LoggerFactory.getLogger(getClass());

    private final ConcurrentHashMap<Integer, Long> SAVE_TIME = new ConcurrentHashMap<>();

    private int expireTime;

    public ExpireFutureContainer(int expireTime) {
        this.expireTime = expireTime;
    }

    public ExpireFutureContainer() {
        this(2 * 60 * 1000);
        Thread thread = new Thread(new CleanTool());
        thread.setName(this.getClass().getSimpleName() + "#cleanTool");
        thread.start();
    }

    @Override
    public void add(int id, FutureCondition futureCondition) {
        super.add(id, futureCondition);
        this.SAVE_TIME.put(id ,System.currentTimeMillis());
    }

    @Override
    public void remove(int id) {
        super.remove(id);
        this.SAVE_TIME.remove(id);
    }

    class CleanTool implements Runnable{

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(expireTime);
                    Iterator<Map.Entry<Integer, Long>> iterator = SAVE_TIME.entrySet().iterator();
                    int count = 0;
                    while (iterator.hasNext()){
                        Map.Entry<Integer, Long> entry = iterator.next();
                        Long time = entry.getValue();
                        if((System.currentTimeMillis() - time) >= expireTime){
                            iterator.remove();
                            count++;
                        }
                    }

                    LOG.warn("clean expire future condition " + count + " size");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
