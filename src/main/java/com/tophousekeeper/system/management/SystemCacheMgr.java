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
    //所有缓存的所有数据记录Map
    //外部Map中，key为缓存名称，value为该缓存内的数据储存信息Map
    //内部Map中，key为数据的id，value为记录该数据的储存信息
    private ConcurrentHashMap<String, ConcurrentHashMap<Object, DataInfo>> dataInfoMaps = new ConcurrentHashMap<>();

    /**
     * 储存信息内部类，用于记录
     */
    class DataInfo{
        //记录该数据的时间
        public Timestamp saveTime;
        //获得此数据的方法信息
        public CacheInvocation cacheInvocation;
        //保证只有一个线程提前更新此数据
        public ReentrantLock lock;
    }

    private DataInfo getDataInfo(String cacheName, Object id){
        ConcurrentHashMap<Object, DataInfo> dateInfoMap = dataInfoMaps.get((cacheName));
        DataInfo dataInfo ;
        if (dateInfoMap == null) {
            //简单的锁住了，因为创建这个对象挺快的
            synchronized (this){
                //重新获取一次进行判断，因为dateInfoMap是局部变量
                dateInfoMap = dataInfoMaps.get((cacheName));
                if(dateInfoMap == null){
                    dateInfoMap = new ConcurrentHashMap<>();
                    dataInfo = new DataInfo();
                    dateInfoMap.put(id,dataInfo);
                    dataInfoMaps.put(cacheName, dateInfoMap);
                }
            }
        }
        //这里不能用else，因为多线程同时进入if，后面进的dataInfo会是null
        dataInfo = dateInfoMap.get(id);

        return dataInfo;
    }

    /**
     * 为该数据放入缓存的时间记录
     * @param id 数据id
     */
    public void recordDataSaveTime(String cacheName, Object id) {
        Date date = new Date();
        Timestamp nowtime = new Timestamp(date.getTime());
        DataInfo dataInfo = getDataInfo(cacheName,id);
        dataInfo.saveTime = nowtime;
    }

    /**
     * 记录获得此数据的方法信息，为了主动更新缓存时的调用
     * @param cacheName 缓存名称
     * @param id 数据id
     * @param targetBean 目标类
     * @param targetMethod 目标方法
     * @param arguments 目标方法的参数
     */
    public synchronized void recordCacheInvocation(String cacheName,String id, Object targetBean, Method targetMethod, Object[] arguments){
        CacheInvocation cacheInvocation = new CacheInvocation(id,targetBean,targetMethod,arguments);
        DataInfo dataInfo = getDataInfo(cacheName,id);
        dataInfo.cacheInvocation = cacheInvocation;
    }

    /**
     * 自动刷新，要配合@SystemCache注解一起使用才有效果
     * 原理：先判断数据是否过期，如果数据过期则从缓存删除。
     *
     * @param cacheName 缓存名称
     * @param id        数据id
     * @return
     */
    public synchronized void autoUpdate(String cacheName, Object id) throws Exception {
        DataInfo dataInfo = getDataInfo(cacheName,id);
        if (defaultCacheMgr.isApproachExpire(cacheName, id, dataInfo.saveTime)) {
            if(dataInfo.lock.tryLock())
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

    public ConcurrentHashMap<String, ConcurrentHashMap<Object, DataInfo>> getDataInfoMaps() {
        return dataInfoMaps;
    }

    public void setDataInfoMaps(ConcurrentHashMap<String, ConcurrentHashMap<Object, DataInfo>> dataInfoMaps) {
        this.dataInfoMaps = dataInfoMaps;
    }
}
