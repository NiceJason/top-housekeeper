package com.tophousekeeper.system.management;

import com.tophousekeeper.system.running.cache.I_SystemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author NiceBin
 * @description: 本系统的缓存管理器
 *               数据自动刷新功能，要配合@SystemCache才能实现
 *               目前没办法实现缓存纯自动更新，必须要使用到该缓存拿数据进行触发
 *               纯自动更新没有意义，假设一个数据放了半小时没人访问要过期了，那就过期吧
 *               因为缓存前提是一段时间频繁访问的数据，如果都没人访问了，就不能称之为缓存
 *               不然就是一个系统长期存在的动态变量，不适用于缓存
 * @date 2019/11/14 16:18
 */
@Component
public class SystemCacheMgr{
    //目前系统只考虑一个CacheManager，多个也能实现，只需要参数多加个CacheManagerName，配合注解将此参数传入即可
    //必须有一个I_SystemCache的实现类，多个实现类用@Primary注解，类似于Spring的缓存管理器
    @Autowired
    private I_SystemCache defaultCacheMgr;
    //所有缓存的时间记录Map，
    //外部Map中，key为缓存名称，value为该缓存内的数据和存储时间的Map
    //内部Map中，key为数据的id，value为记录该数据的时间
    private ConcurrentHashMap<String,ConcurrentHashMap<String,Timestamp>> saveTimeMaps = new ConcurrentHashMap<>();

    /**
     * 为该数据在对应的缓存里增加时间记录
     * @param id 数据id
     */
    public void putSaveTime(String cacheName,String id){
        Date date = new Date();
        Timestamp nowtime = new Timestamp(date.getTime());
        ConcurrentHashMap<String,Timestamp> saveTimeMap = saveTimeMaps.get((cacheName));
        saveTimeMap.put(id,nowtime);
    }

    /**
     * 自动刷新，要配合@SystemCache注解一起使用才有效果
     * 原理：先判断数据是否过期，如果数据过期则从缓存删除。
     * 然后@SystemCache包含@Cacheable，所以会重新访问数据库将缓存写入
     * @param cacheName 缓存名称
     * @param id 数据id
     * @return
     */
    public void autoUpdate(String cacheName,String id) throws Exception {
        ConcurrentHashMap<String,Timestamp> saveTimeMap = saveTimeMaps.get((cacheName));
        if(defaultCacheMgr.isApproachExpire(cacheName,id,saveTimeMap)){
           defaultCacheMgr.remove(cacheName,id);
        }
    }

    /**
     * 清楚所有缓存内容
     */
    public void clearAll() throws Exception {
        defaultCacheMgr.clearAll();
    }

    //以下为Set和Get
    public I_SystemCache getDefaultCacheMgr() {
        return defaultCacheMgr;
    }

    public void setDefaultCacheMgr(I_SystemCache defaultCacheMgr) {
        this.defaultCacheMgr = defaultCacheMgr;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<String, Timestamp>> getSaveTimeMaps() {
        return saveTimeMaps;
    }

    public void setSaveTimeMaps(ConcurrentHashMap<String, ConcurrentHashMap<String, Timestamp>> saveTimeMaps) {
        this.saveTimeMaps = saveTimeMaps;
    }
}
