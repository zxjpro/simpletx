package com.xiaojiezhu.simpletx.util.http;

import java.util.HashMap;
import java.util.Map;

class DefaultParameterManager implements ParameterManager{
    private Map<String,String> parameters = new HashMap<>();

    @Override
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    @Override
    public void addParameter(String name, String value) {
        this.parameters.put(name , value);
    }
}
