package com.xiaojiezhu.simpletx.util;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/4 20:42
 * 说明 ...
 */
public class TypeUtil {
    private static final List<Class<?>> NUMBER_TYPES = Arrays.asList(Integer.class,int.class,Double.class,double.class,Float.class,float.class,Short.class,short.class,Long.class,long.class,Long.class);
    public static final String YMD = "([\\d]{4}?)-([\\d]+?)-([\\d]+?)";
    public static final String YMDHMS = "([\\d]{4}?)-([\\d]+?)-([\\d]+?) ([\\d]+?):([\\d]+?):([\\d]+)";
    public static final String YMDHMSS = "([\\d]{4}?)-([\\d]+?)-([\\d]+?) ([\\d]+?):([\\d]+?):([\\d]+)\\.([\\d]+)";

    public static boolean isNumber(Object obj){
        Class<?> aClass = obj.getClass();
        return NUMBER_TYPES.contains(aClass);
    }

    public static boolean isLong(Object obj){
        Class<?> aClass = obj.getClass();
        if(aClass == Long.class || aClass == long.class){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isInt(Object obj){
        Class<?> aClass = obj.getClass();
        if(aClass == Integer.class || aClass == int.class){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isDate(Object value) {
        if(value == null){
            return false;
        }else{
            return value instanceof Date;
        }
    }

    /**
     * parse any str to date
     * @param str
     * @return
     */
    public static Date parseDate(String str) throws ParseException {
        if(str.matches(YMD)){
            return DateUtils.parse(str , "yyyy-MM-dd");
        }else if(str.matches(YMDHMS)){
            return DateUtils.parse(str ,"yyyy-MM-dd HH:mm:ss");
        }else if(str.matches(YMDHMSS)){
            return DateUtils.parse(str , "yyyy-MM-dd HH:mm:ss.SSS");
        }else{
            throw new ParseException(str + " can not parse java.util.Date",0);
        }
    }

    public static boolean isInteger(Object val){
        try {
            long v = Long.parseLong(String.valueOf(val));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDouble(Object val) {
        String value = String.valueOf(val);
        try {
            double v = Double.parseDouble(value);
            if(value.contains(".")){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> T parseValue(String value , Class<T> t){
        if(t == int.class || t == Integer.class){
            Integer i = Integer.parseInt(value);
            return (T) i;
        }else if(t == Double.class || t == double.class){
            Double d = Double.parseDouble(value);
            return (T) d;
        }else if(t == String.class){
            return (T) value;
        }else{
            throw new RuntimeException("not support type : " + t);
        }
    }






}
