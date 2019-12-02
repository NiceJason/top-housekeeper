package com.tophousekeeper.system.management;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.annotation.UpdateCache;
import com.tophousekeeper.system.running.SystemThreadPool;
import com.tophousekeeper.system.running.cache.CacheInvocation;
import com.tophousekeeper.system.running.cache.I_SystemCacheMgr;
import com.tophousekeeper.system.running.cache.UpdateDataTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author NiceBin
 * @description: 本系统的缓存管理器
 * 数据自动刷新功能，要配合 {@link UpdateCache}才能实现
 *
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
    //系统的线程池类
    @Autowired
    private SystemThreadPool systemThreadPool;
    //所有缓存的所有数据记录Map
    //外部Map中，key为缓存名称，value为该缓存内的数据储存信息Map
    //内部Map中，key为数据的id，value为记录该数据的储存信息
    private ConcurrentHashMap<String, ConcurrentHashMap<Object, DataInfo>> dataInfoMaps = new ConcurrentHashMap<>();

    /**
     * 储存信息内部类，用于记录
     * 获取要调用获取方法，因为加锁了线程才安全
     */
    class DataInfo {
        //记录该数据的时间
        private Timestamp saveTime;
        //获得此数据的方法信息
        private CacheInvocation cacheInvocation;
        //保证只有一个线程提前更新此数据
        private ReentrantLock lock;

        public synchronized void setSaveTime(Timestamp saveTime) {
            this.saveTime = saveTime;
        }


        public synchronized void setCacheInvocation(CacheInvocation cacheInvocation) {
            this.cacheInvocation = cacheInvocation;
        }

        public synchronized void setLock(ReentrantLock lock) {
            this.lock = lock;
        }
    }

    /**
     * 获得DataInfo类，如果为空则创建一个
     * @param cacheName
     * @param id
     * @return
     */
    private DataInfo getDataInfo(String cacheName, Object id) {
        ConcurrentHashMap<Object, DataInfo> dateInfoMap = dataInfoMaps.get((cacheName));
        DataInfo dataInfo;
        if (dateInfoMap == null) {
            //简单的锁住了，因为创建这个对象挺快的
            synchronized (this) {
                //重新获取一次进行判断，因为dateInfoMap是局部变量，不能保证同步
                dateInfoMap = dataInfoMaps.get((cacheName));
                if (dateInfoMap == null) {
                    dateInfoMap = new ConcurrentHashMap<>();
                    dataInfo = new DataInfo();
                    dataInfo.setLock(new ReentrantLock(true));
                    dateInfoMap.put(id, dataInfo);
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
     *
     * @param id 数据id
     */
    public void recordDataSaveTime(String cacheName, Object id) {
        Date date = new Date();
        Timestamp nowtime = new Timestamp(date.getTime());
        DataInfo dataInfo = getDataInfo(cacheName, id);
        dataInfo.setSaveTime(nowtime);
    }

    /**
     * 记录获得此数据的方法信息，为了主动更新缓存时的调用
     *
     * @param cacheName    缓存名称
     * @param id           数据id
     * @param targetBean   目标类
     * @param targetMethod 目标方法
     * @param arguments    目标方法的参数
     */
    public void recordCacheInvocation(String cacheName, String id, Object targetBean, Method targetMethod, Object[] arguments) {
        DataInfo dataInfo = getDataInfo(cacheName, id);
        CacheInvocation cacheInvocation = new CacheInvocation(id, targetBean, targetMethod, arguments);
        //锁在这方法里面有
        dataInfo.setCacheInvocation(cacheInvocation);
    }

    /**
     * 数据自动刷新功能，要配合 {@link UpdateCache}才能实现
     * 原理：先判断数据是否过期，如果数据过期则从缓存删除。
     *
     * @param cacheName 缓存名称
     * @param id        数据id
     * @return
     */
    public void autoUpdate(String cacheName, Object id) throws Exception {
        DataInfo dataInfo = getDataInfo(cacheName, id);
        Cache cache = defaultCacheMgr.getCache(cacheName);


        //如果没有保存的时间，说明该数据还从未载入过
        if (dataInfo.saveTime == null) {
            return;
        }
        if (defaultCacheMgr.isApproachExpire(cacheName, id, dataInfo.saveTime)) {
            if (dataInfo.lock.tryLock()) {
                //获取锁后再次判断数据是否过期
                if (defaultCacheMgr.isApproachExpire(cacheName, id, dataInfo.saveTime)) {
                    ThreadPoolExecutor threadPoolExecutor = systemThreadPool.getThreadPoolExecutor();
                    UpdateDataTask updateDataTask = new UpdateDataTask(dataInfo.cacheInvocation, cache, id);
                    FutureTask futureTask = new FutureTask(updateDataTask);

                    try {
                        threadPoolExecutor.submit(futureTask);
                        futureTask.get(1, TimeUnit.MINUTES);
                        //如果上一步执行完成没报错，那么重新记录保存时间
                        recordDataSaveTime(cacheName,id);
                    } catch (TimeoutException ex) {
                        //如果访问数据库超时
                        throw new SystemException(SystemStaticValue.CACHE_EXCEPTION_CODE, "系统繁忙，稍后再试");
                    } catch (RejectedExecutionException ex) {
                        //如果被线程池拒绝了
                        throw new SystemException(SystemStaticValue.CACHE_EXCEPTION_CODE, "系统繁忙，稍后再试");
                    } finally {
                        dataInfo.lock.unlock();
                    }
                }
            }
        }
    }

    /**
     * 清除所有缓存内容
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
