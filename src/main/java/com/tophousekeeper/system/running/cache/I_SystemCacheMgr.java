package com.tophousekeeper.system.running.cache;

import com.tophousekeeper.system.annotation.UpdateCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentMap;
/**
 * 本系统的缓存接口，SystemCacheMgr统一保存数据记录的时间和控制缓存自动刷新流程
 *
 * 为了实现数据快过期前的自动刷新，需要以下操作：
 * 1.实现此接口
 *   如果用如RedisCacheManager这种写好的类，需要子类继承再实现此接口
 *   如果Cache是CacheManager内部生成的，还需要重写createCache方法
 *   使生成的Cache走一遍Spring初始化Bean的过程，交给Spring管理
 *   这里主要为了Spring帮忙生成代理类，让注解生效
 * 2.实现了 {@link Cache} 接口的类在get方法上加上注解 {@link UpdateCache} 才有更新效果，所以如果要用如RedisCache
 *   这种写好的类，需要子类继承，并重写get方法
 *   然后在get方法上加@UpdateCache
 */
public interface I_SystemCacheMgr extends CacheManager{
    /**
     * 该数据是否过期
     * true为已经过期
     * @param cacheName 缓存名字
     * @param id 数据id
     * @param saveTime 该缓存内该数据的存储时间
     * @return
     * @throws Exception
     */
    boolean isApproachExpire(String cacheName, Object id, Timestamp saveTime) throws Exception;

    /**
     * 删除指定Cache里的指定数据
     * @param cacheName
     * @param id
     * @throws Exception
     */
    void remove(String cacheName, Object id) throws Exception;

    /**
     * 清除所有缓存内容
     * @throws Exception
     */
    void clearAll() throws Exception;

    /**
     * 获得所有的Cache
     * @return
     */
    ConcurrentMap<String, Cache> getAllCaches();
}
