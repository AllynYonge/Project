package com.king.reading.common.utils;

import com.king.reading.exception.ParseResponseException;
import com.qq.tars.protocol.tars.TarsInputStream;
import com.qq.tars.protocol.tars.annotation.TarsStruct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AllynYonge on 02/08/2017.
 */

public class TarsUtils {
    public static Object getBodyRsp(Type type, byte[] body) {
        try {
            Object instance = ((Class) type).newInstance();
            if (body != null) {
                generateObject(instance);
                Method readFrom = ((Class) type).getDeclaredMethod("readFrom", TarsInputStream.class);
                TarsInputStream bodyInputStream = new TarsInputStream(body);
                bodyInputStream.setServerEncoding("UTF-8");
                readFrom.invoke(instance, bodyInputStream);
            }
            return instance;
        } catch (Exception e){
            e.printStackTrace();
            throw new ParseResponseException("use reflect fill tars bodyRsp error");
        }

    }

    private static void generateObject(Object instance) throws Exception{
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++){
            Class type = declaredFields[i].getType();
            if (type.equals(Map.class) ||
                    (type.getAnnotation(TarsStruct.class) != null) && !declaredFields[i].getName().contains("cache")){
                if (type.getAnnotation(TarsStruct.class) != null){
                    declaredFields[i].set(instance, declaredFields[i].getType().newInstance());
                } else if (type.equals(Map.class)){
                    declaredFields[i].set(instance, new HashMap<>());
                }
                generateObject(declaredFields[i].get(instance));
            }
        }
    }
}
