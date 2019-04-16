package com.jerry.BasketBaller.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * 将对象转换成json字符串
     */
    public static String objectToJson(Object data){
        try {
            String json = MAPPER.writeValueAsString(data);
            return json;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串转换成对象
     */
    public static <T> T jsonToObject(String json,Class<T> clazz){
        try {
            T t = MAPPER.readValue(json,clazz);
            return t;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
