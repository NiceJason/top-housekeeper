package com.tophousekeeper.system.running.cache;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本系统的缓存接口
 * 为了实现数据自动刷新，需要对原来的CacheManager进行增强
 * 对已经实现的缓存管理器进行再次封装
 * 例子如RedisCacheMangerEnhance类，实现此接口，里面持有真正的CacheManger即可
 * SystemCacheMgr会对缓存自动刷新统一管理
 */
public interface I_SystemCache {
    /**
     * 该数据是否过期
     * true为已经过期
     * @param cacheName 缓存名字
     * @param id 数据id
     * @param saveTimeMap 该缓存内的数据和存储时间的Map
     * @return
     * @throws Exception
     */
    boolean isApproachExpire(String cacheName, String id, ConcurrentHashMap<String, Timestamp> saveTimeMap) throws Exception;

    /**
     * 删除指定Cache里的指定数据
     * @param cacheName
     * @param id
     * @throws Exception
     */
    void remove(String cacheName, String id) throws Exception;

    /**
     * 清除所有缓存内容
     * @throws Exception
     */
    void clearAll() throws Exception;

}
