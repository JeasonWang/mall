package com.macro.mall.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Set;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2021/1/6 23:13
 */
@Component
public class RedisDelayQueue extends Queue {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Object get(String key) {
        Set<Object> set=redisTemplate.opsForZSet().rangeByScore(key,0,System.currentTimeMillis(),0,1);
        Object obj=null;
        if (set.iterator().hasNext()){
            obj=set.iterator().next();
        }
        if (!ObjectUtils.isEmpty(obj)) {
            redisTemplate.opsForZSet().remove(key, obj);
        }
        return obj;
    }

    public Boolean put(String key, Object value, Double score) throws Exception {
        return redisTemplate.opsForZSet().add(key, value, score);
    }
}
