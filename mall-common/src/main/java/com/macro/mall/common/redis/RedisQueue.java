package com.macro.mall.common.redis;

import com.macro.mall.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2021/1/6 23:12
 */
@Component
public class RedisQueue extends Queue {
    @Autowired
    private RedisService redisService;

    @Override
    public Object get(String key) throws Exception {
        //移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
        return redisService.rPop(key, 1000L, TimeUnit.MILLISECONDS);
    }

    public Long put(String key, Object value) throws Exception {
        return redisService.lPush(key, value);
    }
}
