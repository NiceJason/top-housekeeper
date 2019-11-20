package com.tophousekeeper.system.management;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.system.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author NiceBin
 * @description: 本系统的缓存管理器
 *               为了实现缓存的自动更新，对已经实现的缓存管理器进行再次封装
 *               目前没办法实现缓存纯自动更新，必须要使用到该缓存拿数据进行触发
 *               纯自动更新没有意义，假设一个数据放了半小时没人访问要过期了，那就过期吧
 *               因为缓存前提是一段时间频繁访问的数据，如果都没人访问了，就不能称之为缓存
 *               不然就是一个系统长期存在的动态变量，不适用于缓存
 * @date 2019/11/14 16:18
 */
@Component
public class SystemCacheMgr{
    //spring必须要有一个默认缓存管理器，这个对应那个默认的，目前本系统就用一个缓存管理器，暂不考虑多个
    //且该管理器必须支持TTL
    @Autowired
    private CacheManager defaultCacheMgr;
    //时间记录Map，key为数据的id，value为记录该数据的时间
    private ConcurrentHashMap<String,Timestamp> saveTimeMap = new ConcurrentHashMap<>();

    /**
     * 为该数据增加时间记录
     * @param id 数据id
     */
    public void putSaveTime(String id){
        Date date = new Date();
        Timestamp nowtime = new Timestamp(date.getTime());
        saveTimeMap.put(id,nowtime);
    }

    /**
     * 该数据是否要过期
     * 过期时间为：缓存有效时间-（目前时间-记录时间）<= 随机时间
     * 随机时间是防止同一时刻过期时间太多，造成缓存雪崩，在SystemStaticValue中缓存项里配置
     * true为将要过期（可以刷新了）
     * @param cacheName 缓存名称
     * @param id 数据id
     * @return
     */
    public boolean isApproachExpire(String cacheName,String id) throws NoSuchAlgorithmException {
        long ttl = -1;
        //判断缓存管理器是否是已知支持TTL的类型
        if(defaultCacheMgr instanceof RedisCacheManager){
            RedisCacheConfiguration configuration =((RedisCacheManager) defaultCacheMgr).getCacheConfigurations().get(cacheName);
            ttl=configuration.getTtl().getSeconds();
        }

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

    //以下为Set和Get
    public CacheManager getDefaultCacheMgr() {
        return defaultCacheMgr;
    }

    public void setDefaultCacheMgr(CacheManager defaultCacheMgr) {
        this.defaultCacheMgr = defaultCacheMgr;
    }

    public ConcurrentHashMap<String, Timestamp> getSaveTimeMap() {
        return saveTimeMap;
    }

    public void setSaveTimeMap(ConcurrentHashMap<String, Timestamp> saveTimeMap) {
        this.saveTimeMap = saveTimeMap;
    }
}
