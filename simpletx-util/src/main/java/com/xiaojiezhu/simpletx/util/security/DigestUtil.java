package com.xiaojiezhu.simpletx.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 11:23
 */
public class DigestUtil {

    public static String sha256Hex(String text){
        byte[] bytes = sha256(text);
        return hexToString(bytes);
    }

    public static byte[] sha256(String text){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytes = md.digest(text.getBytes());
        return bytes;
    }

    public static String hexToString(byte[] bytes){
        if(bytes == null){
            return null;
        }
        if(bytes.length == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String s = Integer.toHexString(bytes[i] & 0xff);
            if(s.length() < 2){
                s = "0" + s;
            }
            sb.append(s);
        }

        return sb.toString();
    }
}
