package com.xiaojiezhu.simpletx.protocol.future;

import java.util.LinkedList;
import java.util.List;

/**
 * to save list value
 * @author xiaojie.zhu
 * time 2018-12-30 22:29
 */
class ListFutureCondition extends CountFutureCondition{

    private final List<Object> listValue = new LinkedList<>();


    public ListFutureCondition(Object id) {
        super(id);
    }

    public ListFutureCondition(Object id, int num) {
        super(id, num);
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);

        this.listValue.add(value);
    }

    @Override
    public Object getValue() {
        return this.listValue;
    }
}
