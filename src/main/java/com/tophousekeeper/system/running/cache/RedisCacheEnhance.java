package com.tophousekeeper.system.running.cache;

import com.tophousekeeper.system.annotation.UpdateCache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;

/**
 * @author NiceBin
 * @description:    增强RedisCache
 *                  为了能写上@Update注解，实现自动刷新
 * @date 2019/7/4 13:24
 */

public class RedisCacheEnhance extends RedisCache {

    /**
     * Create new {@link RedisCacheEnhance}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected RedisCacheEnhance(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }

    @UpdateCache
    public ValueWrapper get(Object key){
        System.out.println("进入get方法");
        return super.get(key);
    }

    @UpdateCache
    public <T> T get(Object key, @Nullable Class<T> type){
        return super.get(key,type);
    }

    @UpdateCache
    public <T> T get(Object key, Callable<T> valueLoader){
        return super.get(key,valueLoader);
    }
}