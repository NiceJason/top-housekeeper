package com.tophousekeeper.system.AOP;

import com.tophousekeeper.system.management.SystemCacheMgr;
import com.tophousekeeper.system.running.cache.RedisCacheEnhance;
import com.tophousekeeper.util.Tool;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author NiceBin
 * @description: 处理缓存注解的地方：包括@UpdateCache，@Cacheable
 *
 * @date 2019/11/18 14:57
 */
@Aspect
@Component
public class CacheAspect {
    @Autowired
    SystemCacheMgr systemCacheMgr;

    /**
     * 数据注册到SystemCacheMgr
     * 为数据自动更新做准备
     */
    @Before("@annotation(org.springframework.cache.annotation.Cacheable)")
    public void registerCache(JoinPoint joinPoint){
        System.out.println("拦截了@Cacheable");
        //获取到该方法前的@Cacheable注解，来获取CacheName和key的信息
        Method method = Tool.getSpecificMethod(joinPoint);
        Cacheable cacleable = method.getAnnotation(Cacheable.class);
        String[] cacheNames = cacleable.value()!=null?cacleable.value():cacleable.cacheNames();
        String theKey = cacleable.key();
        //取出来的字符串是'key'，需要去掉''
        String key = theKey.substring(1,theKey.length()-1);
        Arrays.stream(cacheNames).forEach(cacheName ->{
            //记录数据保存时间
            systemCacheMgr.recordDataSaveTime(cacheName,key);
            //记录数据对应的方法信息
            systemCacheMgr.recordCacheInvocation(cacheName,key,joinPoint.getTarget(),method,joinPoint.getArgs());
        });
    }

    /**
     * 检测该键是否快过期了
     * 如果快过期则进行自动更新
     * @param joinPoint
     */
    @Before(value = "@annotation(com.tophousekeeper.system.annotation.UpdateCache)&&args(id)")
    public void checkExpire(JoinPoint joinPoint,String id) throws Exception {
        RedisCacheEnhance redisCacheEnhance = (RedisCacheEnhance) joinPoint.getTarget();
        systemCacheMgr.autoUpdate(redisCacheEnhance.getName(),id);
    }
}
