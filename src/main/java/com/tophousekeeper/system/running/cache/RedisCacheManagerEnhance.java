package com.tophousekeeper.system.running.cache;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.util.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/11/21 8:55
 */
@Component
@Primary
public class RedisCacheManagerEnhance implements I_SystemCache {

    @Autowired
    RedisCacheManager redisCacheManager;

    /**
     * 过期规则为：缓存有效时间-（目前时间-记录时间）<= 随机时间
     * 随机时间是防止同一时刻过期时间太多，造成缓存雪崩，在SystemStaticValue中缓存项里配置
     * true为将要过期（可以刷新了）
     * @param cacheName 缓存名称
     * @param id 数据id
     * @return
     */
    @Override
    public boolean isApproachExpire(String cacheName, String id, ConcurrentHashMap<String, Timestamp> saveTimeMap) throws NoSuchAlgorithmException {
        long ttl = -1;

        RedisCacheConfiguration configuration = redisCacheManager.getCacheConfigurations().get(cacheName);
        ttl=configuration.getTtl().getSeconds();

        if(ttl!=-1){
            int random = Tool.getSecureRandom(SystemStaticValue.CACHE_MIN_EXPIRE,SystemStaticValue.CACHE_MAX_EXPIRE);
            Date date = new Date();
            long nowTime = date.getTime()/1000;
            long saveTime = saveTimeMap.get(id).getTime()/1000;
            if(ttl-(nowTime-saveTime)<=random){
                return true;
            }else{
                return false;
            }
        }
        throw new SystemException(SystemStaticValue.CACHE_EXCEPTION_CODE,"默认缓存管理器不支持TTL");
    }

    @Override
    public void remove(String cacheName, String id) throws Exception {
        Cache cache = redisCacheManager.getCache(cacheName);
        cache.evict(id);
    }


    /**
     * 清除所有缓存内容
     * @throws Exception
     */
    @Override
    public void clearAll() throws Exception {
        Collection<String> cacheNames=redisCacheManager.getCacheNames();
        Iterator<String> iterator=cacheNames.iterator();
        while (iterator.hasNext()){
            String cacheName = iterator.next();
            Cache redisCache = redisCacheManager.getCache(cacheName);
            redisCache.clear();
        }
    }

}
