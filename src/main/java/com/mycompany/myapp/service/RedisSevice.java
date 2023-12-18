package com.mycompany.myapp.service;

import com.mycompany.myapp.config.ApplicationContextHolder;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisSevice {

    private final RedisTemplate<Object, Object> redisTemplate = (RedisTemplate<Object, Object>) ApplicationContextHolder
        .getContext()
        .getBean("redisTemplate");
    //    @Autowired
    //    private RedisTemplate<Object, Object> redisTemplate;
    private ValueOperations valueOperations;

    public RedisSevice() {
        valueOperations = this.redisTemplate.opsForValue();
    }

    public void setKeyValueWithTimeOut(String key, String value, Long time, TimeUnit timeUnit) {
        valueOperations.set(key, value, time, timeUnit);
    }

    public <T> void setKeyValue(String key, String value) {
        valueOperations.set(key, value);
    }

    public Boolean checkValue(String key, String value) {
        return valueOperations.get(key).equals(value);
    }

    public String getValue(String key) {
        return Optional.ofNullable(valueOperations.get(key)).map(Object::toString).orElse("");
    }

    public void delKey(String key) {
        redisTemplate.delete(key);
    }

    public Boolean isExist(String key) {
        return redisTemplate.hasKey(key);
    }
}
