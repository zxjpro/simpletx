package com.xiaojiezhu.simpletx.util.http;

import java.util.HashMap;
import java.util.Map;

class DefaultHeaderManager implements HeaderManager{
    private Map<String,String> headers = new HashMap<>();

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public void addHeader(String name, String value) {
        this.headers.put(name , value);
    }
}
