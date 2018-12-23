package com.xiaojiezhu.simpletx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 17:00
 */
public class DateUtils {

    public static Date parse(String text , String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = df.parse(text);
        return date;
    }
}
