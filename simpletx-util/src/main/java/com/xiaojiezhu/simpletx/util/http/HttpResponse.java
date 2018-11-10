package com.xiaojiezhu.simpletx.util.http;

import java.io.Closeable;
import java.util.List;
import java.util.Set;

public interface HttpResponse extends Closeable {

    int getStatus();

    String text();

    byte[] toBytes();

    Set<String> getHeaderKeys();
    List<String> getHeader(String name);
}
