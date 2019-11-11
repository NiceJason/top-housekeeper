package com.tophousekeeper.system.management;

import com.tophousekeeper.dao.function.system.SystemDailyDao;
import com.tophousekeeper.entity.SystemDaily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NiceBin
 * @description: 系统资源的管理器
 * @date 2019/11/5 11:45
 */
@Component
public class SystemTimingMgr {
    @Autowired
    private SystemDailyDao systemDailyDao;

    //当日登录人数
    private AtomicInteger loginCount;
    //当日在线人数
    private AtomicInteger onlineCount;

    public SystemTimingMgr(){
        loginCount = new AtomicInteger(3);
        onlineCount = new AtomicInteger(4);
    }

    //保存每日数据
    public void saveSystemDaily() throws Exception {
        SystemDaily systemDaily = new SystemDaily();
        systemDaily.setLoginCount(loginCount.get());
        systemDaily.setOnlineCount(onlineCount.get());
        systemDailyDao.insert(systemDaily);
        dailyClear();
    }

    /**
     * 将该类的所有属性重置
     */
    public void dailyClear() throws Exception {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field :fields) {
            field.setAccessible(true);
            //属性名
            String fieldName = field.getName();
            //属性类型
            Class theClass = field.getType();
            //如果属性为对象，那么将要调用的对象方法
            Method method ;

            //获取该属性set和get的方法
            //首字母
            char firstLetter = fieldName.substring(0,1).toUpperCase().charAt(0);
            String getMethodName = "get"+firstLetter+fieldName.substring(1);
            Method getMethod = this.getClass().getMethod(getMethodName);

            //获取该属性的实例
            Object obj = getMethod.invoke(this);
            if(obj instanceof AtomicInteger){
                method = theClass.getMethod("set",int.class);
                method.invoke(obj,0);
            }
        }
    }

    //以下为set和get，必须要有，为了dailyClear方法的正常使用

    public SystemDailyDao getSystemDailyDao() {
        return systemDailyDao;
    }

    public void setSystemDailyDao(SystemDailyDao systemDailyDao) {
        this.systemDailyDao = systemDailyDao;
    }

    public AtomicInteger getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(AtomicInteger loginCount) {
        this.loginCount = loginCount;
    }

    public AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(AtomicInteger onlineCount) {
        this.onlineCount = onlineCount;
    }
}
