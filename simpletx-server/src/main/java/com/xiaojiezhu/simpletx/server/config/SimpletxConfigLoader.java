package com.xiaojiezhu.simpletx.server.config;

import com.xiaojiezhu.simpletx.util.StringUtils;
import com.xiaojiezhu.simpletx.util.SystemUtil;
import com.xiaojiezhu.simpletx.util.TypeUtil;
import com.xiaojiezhu.simpletx.util.asserts.Asserts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author xiaojie.zhu
 * time 2018/12/22 12:41
 */
public class SimpletxConfigLoader {

    private static final String CONF_PATH_NAME = "simpletx.conf";

    private static final String CONFIG_NAME = "simpletx.properties";



    public static String getConfPath(){
        String confPath = System.getProperty(CONF_PATH_NAME);
        if(confPath == null){
            SystemUtil.exit(StringUtils.str(CONF_PATH_NAME , " property not found"));
        }

        if(!confPath.endsWith("/")){
            confPath += "/";
        }

        return confPath;
    }


    public static SimpletxConfig loadConfig(){
        return new SimpletxConfig();
    }
    public static SimpletxConfig loadConfig(String confPath){
        Asserts.assertNotNull(confPath , "conf path can not be null");
        SimpletxConfig simpletxConfig = new SimpletxConfig();

        if(!confPath.endsWith("/")){
            confPath  = confPath + "/";
        }

        confPath += CONFIG_NAME;

        File file = new File(confPath);

        Properties properties = loadProperties(file);

        reConfig(simpletxConfig , properties);

        return simpletxConfig;

    }



    private static void reConfig(SimpletxConfig simpletxConfig , Properties properties){
        Field[] declaredFields = simpletxConfig.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String name = field.getName();
            String value = properties.getProperty(name);
            if(value != null){
                try {
                    Class<?> type = field.getType();
                    field.set(simpletxConfig , TypeUtil.parseValue(value , type));
                } catch (Throwable e) {
                    e.printStackTrace();
                    SystemUtil.exit("read config error , " + e.getMessage());
                }
            }

            try {
                System.setProperty("simpletx." + name , String.valueOf(field.get(simpletxConfig)));
            } catch (Exception e) {
                e.printStackTrace();
                SystemUtil.exit("read config error , " + e.getMessage());
            }
        }
    }


    private static Properties loadProperties(File file){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);

            inputStream.close();

            return properties;

        } catch (IOException e) {
            e.printStackTrace();
            SystemUtil.exit("init bigsql conf error");
            return null;
        }

    }

}
