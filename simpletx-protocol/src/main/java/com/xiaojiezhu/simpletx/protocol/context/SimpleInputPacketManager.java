package com.xiaojiezhu.simpletx.protocol.context;

import com.xiaojiezhu.simpletx.protocol.dispatcher.ProtocolHandler;
import com.xiaojiezhu.simpletx.protocol.exception.SyntaxRuntimeException;
import com.xiaojiezhu.simpletx.protocol.packet.InputPacket;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaojie.zhu
 * time 2018/12/23 10:44
 */
public class SimpleInputPacketManager implements InputPacketManager {

    private final ConcurrentHashMap<Class , Class<? extends InputPacket>> inputPackets = new ConcurrentHashMap<>();

    @Override
    public void register(Class<?> classes, Class<? extends InputPacket> packet) {
        inputPackets.put(classes , packet);
    }

    @Override
    public Class<? extends InputPacket> get(Class<?> classes) {
        Class<? extends InputPacket> aClass = this.inputPackets.get(classes);
        if(aClass != null){
            return aClass;
        }else{
            synchronized (this){
                if(aClass != null){
                    return aClass;
                }else{
                    aClass = (Class<? extends InputPacket>) findGenericity(classes);
                    this.inputPackets.put(classes , aClass);
                    return aClass;
                }
            }
        }
    }


    private Class<?> findGenericity(Class<?> clas){
        List<Type> list = findTypes(clas);

        for (Type type : list) {
            Class<?> clazz = findClassByType(type);


            if(ProtocolHandler.class == clazz){
                Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                if(types == null || types.length == 0){
                    throw new SyntaxRuntimeException(type.toString() + " not has genericity");
                }
                return (Class<?>) types[0];
            }

            return findGenericity(clazz);

        }

        return null;
    }

    private Class<?> findClassByType(Type type) {
        Class<?> clazz = null;
        if(type instanceof Class){
            clazz = (Class<?>) type;
        }else if(type instanceof ParameterizedType){
            ParameterizedType t = (ParameterizedType) type;
            clazz = (Class<?>) t.getRawType();

        }else{
            throw new RuntimeException("not support type : " + type.getClass().getName());
        }
        return clazz;
    }

    private List<Type> findTypes(Class<?> clas) {
        Type[] genericInterfaces = clas.getGenericInterfaces();
        Type genericSuperclass = clas.getGenericSuperclass();

        List<Type> list = new LinkedList<>();
        if(genericInterfaces != null && genericInterfaces.length > 0){
            list.addAll(Arrays.asList(genericInterfaces));
        }
        if(genericSuperclass != null){
            list.add(genericSuperclass);
        }
        return list;
    }
}
