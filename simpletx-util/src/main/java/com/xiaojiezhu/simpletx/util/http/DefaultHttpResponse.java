package com.xiaojiezhu.simpletx.util.http;

import com.xiaojiezhu.simpletx.util.http.exception.HttpRequestException;
import com.xiaojiezhu.simpletx.util.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * use jdk HttpUrlConnection
 */
class DefaultHttpResponse implements HttpResponse {

    private HttpURLConnection conn;
    private Charset charset;

    public DefaultHttpResponse(HttpURLConnection conn, Charset charset) {
        this.conn = conn;
        this.charset = charset;
    }

    private InputStream inputStream;

    /**
     * 是否读取响应结果
     */
    private boolean read = false;

    @Override
    public int getStatus() {
        try {
            return conn.getResponseCode();
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    @Override
    public String text() {
        this.checkReadable();

        try {
            this.inputStream = this.conn.getInputStream();
            return IOUtils.toString(this.inputStream , this.charset);
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    @Override
    public byte[] toBytes() {
        this.checkReadable();

        try {
            this.inputStream = this.conn.getInputStream();
            byte[] bytes = IOUtils.toByteArray(this.inputStream);
            return bytes;
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }


    @Override
    public Set<String> getHeaderKeys() {
        Map<String, List<String>> headerFields = this.conn.getHeaderFields();
        if(headerFields == null || headerFields.size() == 0){
            return null;
        }
        return headerFields.keySet();
    }

    @Override
    public List<String> getHeader(String name) {
        Map<String, List<String>> headerFields = this.conn.getHeaderFields();
        if(headerFields == null || headerFields.size() == 0){
            return null;
        }
        List<String> values = headerFields.get(name);
        return values;
    }

    @Override
    public void close() throws IOException {
        IOUtils.close(this.conn.getInputStream());
        this.conn.disconnect();
    }

    private void checkReadable(){
        if(this.read){
            throw new HttpRequestException("can not duplicate read http response data , such as text() and boBytes()");
        }
        this.read = true;
    }
}
