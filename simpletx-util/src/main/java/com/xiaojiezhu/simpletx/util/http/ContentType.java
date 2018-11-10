package com.xiaojiezhu.simpletx.util.http;

public enum ContentType {

    TEXT_HTML("text/html"),
    APPLICATION_JSON("application/json");

    private String value;

    ContentType(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return this.value;
    }
}
