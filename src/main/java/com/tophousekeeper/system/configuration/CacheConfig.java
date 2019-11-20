package com.tophousekeeper.system.configuration;

import com.tophousekeeper.system.SystemStaticValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NiceBin
 * @description: CacheManager初始化，使用RedisCacheManager
 *               根据SystemStaticValue中的SystemCache枚举内容进行Cache的注册
 * @date 2019/11/13 17:02
 */
@Configuration
public class CacheConfig {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1))
                .computePrefixWith(cacheName -> "Cache"+cacheName);
        // 针对不同cacheName，设置不同的过期时间
        Map<String, RedisCacheConfiguration> initialCacheConfiguration = new HashMap<String, RedisCacheConfiguration>() {{
            SystemStaticValue.SystemCache[] systemCaches = SystemStaticValue.SystemCache.values();
            Arrays.asList(systemCaches).forEach((systemCache)->
                    put(systemCache.getCacheName(),RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(systemCache.getSurviveTime()))));
        }};

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig) // 默认配置（强烈建议配置上）。  比如动态创建出来的都会走此默认配置
                .withInitialCacheConfigurations(initialCacheConfiguration) // 不同cache的个性化配置
                .build();
        return redisCacheManager;
    }
}
