package com.jerry.BasketBaller.dao;

public interface RedisService {

    Boolean expire(String key,long exTime);

    void setEx(String key,String value,long exTime);

    void set(String key ,String value);

    String get(String key);

    String getSet(String key,String value);

    Long del(String key);

    void setnx(String key,String value);
}
