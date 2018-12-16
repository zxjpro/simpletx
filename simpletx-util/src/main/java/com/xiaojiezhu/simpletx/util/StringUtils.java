package com.xiaojiezhu.simpletx.util;

/**
 * @author xiaojie.zhu
 * time 2018/12/16 17:10
 */
public class StringUtils {


    public static String str(Object... str){
        if(str == null || str.length == 0){
            return null;
        }else{
            StringBuilder sb = new StringBuilder();
            for (Object s : str) {
                sb.append(s);
            }
            return sb.toString();
        }
    }

    public static String strConjunction(String conjunction , String ... str){
        if(str == null || str.length == 0){
            return null;
        }else{
            StringBuilder sb = new StringBuilder();
            for (int i = 0 ; i < str.length ; i ++) {
                String s = str[i];
                if(i > 0){
                    sb.append(conjunction);
                }
                sb.append(s);
            }
            return sb.toString();
        }
    }

    public static boolean isEmpty(Object o){
        return o == null || "".equals(String.valueOf(o).trim());
    }
}
