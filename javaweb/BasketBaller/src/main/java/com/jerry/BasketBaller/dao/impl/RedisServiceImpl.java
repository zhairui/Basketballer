package com.jerry.BasketBaller.dao.impl;

import com.jerry.BasketBaller.dao.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Boolean expire(String key, long exTime) {
        return stringRedisTemplate.expire(key,exTime,TimeUnit.MILLISECONDS);
    }

    @Override
    public void setEx(String key, String value, long exTime) {
        stringRedisTemplate.opsForValue().set(key,value,exTime,TimeUnit.MILLISECONDS);
    }

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public String getSet(String key, String value) {
        return stringRedisTemplate.opsForValue().getAndSet(key,value);
    }

    @Override
    public Long del(String key) {
        return stringRedisTemplate.opsForValue().decrement(key);
    }

    @Override
    public void setnx(String key, String value) {
        stringRedisTemplate.opsForValue().setIfAbsent(key,value);
    }
}
