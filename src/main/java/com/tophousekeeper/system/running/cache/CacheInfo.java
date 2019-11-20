package com.tophousekeeper.system.running.cache;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author NiceBin
 * @description: Cache的详情信息封装，用此类进行缓存的操作
 *               缓存类必须实现Cache接口，并放置com/tophousekeeper/system/running/cache下
 *               会被CacheAspect切
 * @date 2019/11/18 15:01
 */
public class CacheInfo<T>{
    private String key;
    private T value;
    private Timestamp saveTime;
    private long overdue;

    public void updateSaveTime(){
        saveTime = new Timestamp(new Date().getTime());
    }

    //以下为set和get
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Timestamp getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Timestamp saveTime) {
        this.saveTime = saveTime;
    }

    public long getOverdue() {
        return overdue;
    }

    public void setOverdue(long overdue) {
        this.overdue = overdue;
    }
}
