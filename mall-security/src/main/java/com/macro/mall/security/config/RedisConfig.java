package com.macro.mall.security.config;

import com.macro.mall.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类
 * Created by macro on 2020/3/2.
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {
    public static final String MALL_PORTAL_CACHE_KEY="mall_portal:";
    public static final String MALL_ADMIN_CACHE_KEY="mall_admin:";
}
