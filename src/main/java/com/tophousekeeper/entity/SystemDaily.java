package com.tophousekeeper.entity;

import org.springframework.stereotype.Component;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NiceBin
 * @description: 系统每日的资源，变量必须是线程安全类
 * @date 2019/10/31 16:40
 */
@Component
@Table(name = "t_system_daily")
public class SystemDaily {

    //今日日期
    private Date nowdate;

    //系统每日登录人数
    private AtomicInteger loginCount;

    public SystemDaily(){
        nowdate = new Date();
        loginCount = new AtomicInteger(0);
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
            String setMethodName = "set"+firstLetter+fieldName.substring(1);
            Method getMethod = this.getClass().getMethod(getMethodName);
            Method setMethod = this.getClass().getMethod(setMethodName);

            //获取该属性的实例
            Object obj = getMethod.invoke(this);
            if(obj instanceof AtomicInteger){
                method = theClass.getMethod("set",int.class);
                method.invoke(obj,0);
            }else if(obj instanceof Calendar){
                setMethod.invoke(obj,new Date());
            }
        }
    }

    //以下为set和get

    public AtomicInteger getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(AtomicInteger loginCount) {
        this.loginCount = loginCount;
    }

    public Date getNowdate() {
        return nowdate;
    }

    public void setNowdate(Date nowdate) {
        this.nowdate = nowdate;
    }
}
