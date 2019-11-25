package com.tophousekeeper.system.running.cache;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.running.SystemContext;
import com.tophousekeeper.util.Tool;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author NiceBin
 * @description: TODO
 * @date 2019/11/25 9:07
 */
public class RedisCacheMgr extends RedisCacheManager implements I_SystemCacheMgr {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;
    private final Map<String, RedisCacheConfiguration> initialCacheConfiguration;
    private final boolean allowInFlightCacheCreation;

    private ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    public RedisCacheMgr(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);

        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
        this.initialCacheConfiguration = initialCacheConfigurations;
        this.allowInFlightCacheCreation = allowInFlightCacheCreation;
    }

    @Override
    protected RedisCacheEnhance createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
//        BeanDefinition beanDefinition = new RootBeanDefinition(RedisCacheEnhance.class);

//        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
//        constructorArgumentValues.addIndexedArgumentValue(0,name);
//        constructorArgumentValues.addIndexedArgumentValue(1,cacheWriter);
//        constructorArgumentValues.addIndexedArgumentValue(2,cacheConfig);
        ApplicationContext applicationContext = SystemContext.getSystemContext()
                .getApplicationContext();
        DefaultListableBeanFactory defaultListableBeanFactory = applicationContext.getBean(DefaultListableBeanFactory.class);
//        defaultListableBeanFactory.registerBeanDefinition(name,beanDefinition);

        Class<?> cls = Cache.class;
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(cls);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        ConstructorArgumentValues constructorArgumentValues = definition.getConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0,name);
        constructorArgumentValues.addIndexedArgumentValue(1,cacheWriter);
        constructorArgumentValues.addIndexedArgumentValue(2,cacheConfig);
        definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
        definition.setBeanClass(RedisCacheEnhance.class);
        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        defaultListableBeanFactory.registerBeanDefinition(name, definition);


        RedisCacheEnhance redisCacheEnhance = (RedisCacheEnhance)defaultListableBeanFactory.getBean(name);
        caches.put(name, redisCacheEnhance);
        return redisCacheEnhance;
    }

    /**
     * 过期规则为：缓存有效时间-（目前时间-记录时间）<= 随机时间
     * 随机时间是防止同一时刻过期时间太多，造成缓存雪崩，在SystemStaticValue中缓存项里配置
     * true为将要过期（可以刷新了）
     *
     * @param cacheName 缓存名称
     * @param id        数据id
     * @return
     */
    @Override
    public boolean isApproachExpire(String cacheName, String id, ConcurrentHashMap<String, Timestamp> saveTimeMap) throws NoSuchAlgorithmException {
        long ttl = -1;

        RedisCacheConfiguration configuration = this.getCacheConfigurations().get(cacheName);
        ttl = configuration.getTtl().getSeconds();

        if (ttl != -1) {
            int random = Tool.getSecureRandom(SystemStaticValue.CACHE_MIN_EXPIRE, SystemStaticValue.CACHE_MAX_EXPIRE);
            Date date = new Date();
            long nowTime = date.getTime() / 1000;
            long saveTime = saveTimeMap.get(id).getTime() / 1000;
            if (ttl - (nowTime - saveTime) <= random) {
                return true;
            } else {
                return false;
            }
        }
        throw new SystemException(SystemStaticValue.CACHE_EXCEPTION_CODE, "默认缓存管理器不支持TTL");
    }

    @Override
    public void remove(String cacheName, String id) throws Exception {
        Cache cache = this.getCache(cacheName);
        cache.evict(id);
    }


    /**
     * 清除所有缓存内容
     *
     * @throws Exception
     */
    @Override
    public void clearAll() throws Exception {
        Collection<String> cacheNames = this.getCacheNames();
        Iterator<String> iterator = cacheNames.iterator();
        while (iterator.hasNext()) {
            String cacheName = iterator.next();
            Cache redisCache = this.getCache(cacheName);
            redisCache.clear();
        }
    }

    @Override
    public ConcurrentMap<String, Cache> getAllCaches() {
        return caches;
    }
}
