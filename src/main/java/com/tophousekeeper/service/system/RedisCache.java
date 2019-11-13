package com.tophousekeeper.service.system;

import com.tophousekeeper.system.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author NiceBin
 * @description: 对Redis操作进行封装，让任何数据都能方便转换成String格式存储并取出
 * @date 2019/7/4 13:24
 */
@Component
public class RedisCache implements Cache {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private String name;

    //布隆过滤网，对搜索的key值进行过滤，防止缓存击穿
    Map<String,String> filterMap = new HashMap<>();
    /**
     * 设置key,value
     * @param key
     * @param value
     * @param time  有效时间（分钟），最少为10分钟
     * @param <T>
     * @return
     */
    public<T> boolean set(String key,T value,long time){

        final TimeUnit timeUnit = TimeUnit.MINUTES;

        try {
            //任意类型转换成String
            String val = Tool.beanToString(value);

            if(val==null||val.length()<=0){
                return false;
            }
            stringRedisTemplate.opsForValue().set(key,val,time,timeUnit);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 设置key,value 默认有效时间为10分钟
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(String key ,T value){
        final long time = 10;
        return set(key, value,time);
    }

    public static final String TYPE_MIN = "type_min";
    public static final String Type_MID = "type_mid";
    public static final String Type_MAX = "type_max";
    /**
     *
     * @param key
     * @param value
     * @param type 时间随机类型（为了防止缓存同时失效，缓存击穿）
     *             TYPE_MIN 随机为 10-30分钟 (调用格式不对则默认这种)
     *             Type_MID 随机为 30-60分钟
     *             Type_MAX 随机为 60-90分钟
     * @param <T>
     * @return
     */
    public <T> boolean set(String key ,T value,String type) throws NoSuchAlgorithmException {
        long time = 0;
        if(Type_MID.equals(type)){
            time = Tool.getSecureRandom(30,60);
        }else if(Type_MAX.equals(type)){
            time = Tool.getSecureRandom(60,90);
        }else {
            time = Tool.getSecureRandom(10,30);
        }
        return set(key, value,time);
    }


    /**
     * 根据Key值获取缓存内容
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key,Class<T> clazz){
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            return Tool.stringToBean(value,clazz);
        }catch (Exception e){
            return null ;
        }
    }

    /**
     * 清楚系统所有的缓存
     */
    public void clearAll(){
        Set<String> keys = stringRedisTemplate.keys("*");
        for(String key:keys){
            stringRedisTemplate.delete(key);
        }
    }


    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }

    //以下为get和set
    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getFilterMap() {
        return filterMap;
    }

    public void setFilterMap(Map<String, String> filterMap) {
        this.filterMap = filterMap;
    }
}