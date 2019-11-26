package com.tophousekeeper.system.running.cache;

import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.running.SystemContext;
import com.tophousekeeper.util.Tool;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
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
 * @description: RedisCacheManager增强类，为了实现本系统缓存自动更新功能
 * @date 2019/11/25 9:07
 */
public class RedisCacheMgr extends RedisCacheManager implements I_SystemCacheMgr {

    private final RedisCacheWriter cacheWriter;
    private ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    private DefaultListableBeanFactory defaultListableBeanFactory;

    public RedisCacheMgr(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
        this.cacheWriter = cacheWriter;

    }

    /**
     * 重写createRedisCache的方法，生成自己定义的Cache
     * 这里主要要让Spring来生成代理Cache，不然在Cache上的注解是无效的
     * @param name
     * @param cacheConfig
     * @return
     */
    @Override
    protected RedisCacheEnhance createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        //利用Spring生成代理Cache
        BeanDefinition beanDefinition = new RootBeanDefinition(RedisCacheEnhance.class);
        //因为只有有参构造方法，所以要添加参数
        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0,name);
        constructorArgumentValues.addIndexedArgumentValue(1,cacheWriter);
        constructorArgumentValues.addIndexedArgumentValue(2,cacheConfig);

        //如果有属性需要设置，还能这样做，不过需要有对应属性名的set方法
        //definition.getPropertyValues().add("propertyName", beanDefinition.getBeanClassName());

        ApplicationContext applicationContext = SystemContext.getSystemContext()
                .getApplicationContext();
        //需要这样获取的DefaultListableBeanFactory类才能走一遍完整的Bean初始化流程！！
        //像applicationContext.getBean(DefaultListableBeanFactory.class)都不好使！！
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        defaultListableBeanFactory.registerBeanDefinition(name,beanDefinition);

        RedisCacheEnhance redisCacheEnhance = (RedisCacheEnhance)applicationContext.getBean(name);
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
    public boolean isApproachExpire(String cacheName, Object id, ConcurrentHashMap<Object, Timestamp> saveTimeMap) throws NoSuchAlgorithmException {
        long ttl = -1;

        RedisCacheConfiguration configuration = this.getCacheConfigurations().get(cacheName);
        ttl = configuration.getTtl().getSeconds();

        if (ttl != -1 && saveTimeMap!=null) {
            Timestamp saveTimestamp = saveTimeMap.get(id);
            if(saveTimeMap!=null){
                int random = Tool.getSecureRandom(SystemStaticValue.CACHE_MIN_EXPIRE, SystemStaticValue.CACHE_MAX_EXPIRE);
                Date date = new Date();
                long nowTime = date.getTime() / 1000;
                long saveTime = saveTimestamp.getTime() / 1000;
                if (ttl - (nowTime - saveTime) <= random) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void remove(String cacheName, Object id) throws Exception {
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
