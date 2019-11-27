package com.tophousekeeper.system.management;

import com.tophousekeeper.system.running.cache.CacheInvocation;
import com.tophousekeeper.system.running.cache.I_SystemCacheMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author NiceBin
 * @description: 本系统的缓存管理器
 * 数据自动刷新功能，要配合@SystemCache才能实现
 * 目前没办法实现缓存纯自动更新，必须要使用到该缓存拿数据进行触发
 * 纯自动更新没有意义，假设一个数据放了半小时没人访问要过期了，那就过期吧
 * 因为缓存前提是一段时间频繁访问的数据，如果都没人访问了，就不能称之为缓存
 * 不然就是一个系统长期存在的动态变量，不适用于缓存
 * @date 2019/11/14 16:18
 */
@Component
public class SystemCacheMgr {
    //目前系统只考虑一个CacheManager
    //必须有一个I_SystemCache的实现类，多个实现类用@Primary注解，类似于Spring的缓存管理器
    @Autowired
    private I_SystemCacheMgr defaultCacheMgr;
    //所有缓存的时间记录Map，
    //外部Map中，key为缓存名称，value为该缓存内的数据和存储时间的Map
    //内部Map中，key为数据的id，value为记录该数据的时间
    private ConcurrentHashMap<String, ConcurrentHashMap<Object, Timestamp>> saveTimeMaps = new ConcurrentHashMap<>();
    //记录获得此数据的方法信息，为了主动更新缓存时的调用
    //外部Map中，key为cacheName，value为记录方法信息的Map
    //内部Map中，key为数据id，value为方法信息
    private ConcurrentHashMap<String, ConcurrentHashMap<Object, CacheInvocation>> cacheInvocations = new ConcurrentHashMap<>();
    private ReentrantLock refleshLock = new ReentrantLock();
    /**
     * 为该数据放入缓存的时间记录
     * @param id 数据id
     */
    public void recordDataSaveTime(String cacheName, Object id) {
        Date date = new Date();
        Timestamp nowtime = new Timestamp(date.getTime());
        ConcurrentHashMap<Object, Timestamp> saveTimeMap = saveTimeMaps.get((cacheName));
        if (saveTimeMap == null) {
            //简单的锁住了，因为创建这个对象挺快的
            synchronized (this){
                if(saveTimeMap == null){
                    saveTimeMap = new ConcurrentHashMap<>();
                    saveTimeMaps.put(cacheName, saveTimeMap);
                }
            }
        }
        if (!saveTimeMap.containsKey(id)) {
            saveTimeMap.put(id, nowtime);
        } else {
            saveTimeMap.replace(id, nowtime);
        }
    }

    /**
     * 记录获得此数据的方法信息，为了主动更新缓存时的调用
     * @param cacheName 缓存名称
     * @param id 数据id
     * @param targetBean 目标类
     * @param targetMethod 目标方法
     * @param arguments 目标方法的参数
     */
    public void recordCacheInvocation(String cacheName,String id, Object targetBean, Method targetMethod, Object[] arguments){
        ConcurrentHashMap<Object, CacheInvocation> cacheInvocationMap = cacheInvocations.get(cacheName);
        if(cacheInvocationMap == null){
            synchronized (this){
                if(cacheInvocationMap == null){
                    cacheInvocationMap = new ConcurrentHashMap<>();
                    cacheInvocations.put(cacheName,cacheInvocationMap);
                }
            }
        }
        CacheInvocation cacheInvocation = new CacheInvocation(id,targetBean,targetMethod,arguments);
        if(!cacheInvocationMap.containsKey(id)){

            cacheInvocationMap.put(id,cacheInvocation);
        }else{
            cacheInvocationMap.replace(id,cacheInvocation);
        }

    }

    /**
     * 自动刷新，要配合@SystemCache注解一起使用才有效果
     * 原理：先判断数据是否过期，如果数据过期则从缓存删除。
     *
     * @param cacheName 缓存名称
     * @param id        数据id
     * @return
     */
    public void autoUpdate(String cacheName, Object id) throws Exception {
        ConcurrentHashMap<Object, Timestamp> saveTimeMap = saveTimeMaps.get((cacheName));
        if (defaultCacheMgr.isApproachExpire(cacheName, id, saveTimeMap)) {
            defaultCacheMgr.remove(cacheName, id);
        }
    }



    /**
     * 清楚所有缓存内容
     */
    public void clearAll() throws Exception {
        defaultCacheMgr.clearAll();
    }

    //以下为Set和Get
    public I_SystemCacheMgr getDefaultCacheMgr() {
        return defaultCacheMgr;
    }

    public void setDefaultCacheMgr(I_SystemCacheMgr defaultCacheMgr) {
        this.defaultCacheMgr = defaultCacheMgr;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<Object, Timestamp>> getSaveTimeMaps() {
        return saveTimeMaps;
    }

    public void setSaveTimeMaps(ConcurrentHashMap<String, ConcurrentHashMap<Object, Timestamp>> saveTimeMaps) {
        this.saveTimeMaps = saveTimeMaps;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<Object, CacheInvocation>> getCacheInvocations() {
        return cacheInvocations;
    }

    public void setCacheInvocations(ConcurrentHashMap<String, ConcurrentHashMap<Object, CacheInvocation>> cacheInvocations) {
        this.cacheInvocations = cacheInvocations;
    }
}
