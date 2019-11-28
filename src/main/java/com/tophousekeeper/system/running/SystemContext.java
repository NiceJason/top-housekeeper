package com.tophousekeeper.system.running;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author NiceBin
 * @description: 系统的上下文资源
 * @date 2019/7/5 9:30
 */
@Component
public class SystemContext implements ApplicationContextAware {

    //数据库操作类
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //系统线程管理类
    @Autowired
    private SystemThreadPool systemThreadPool;
    //系统动态资源，如在线人数等，必须要线程安全的类存入
    private static Map<String,Object> resources = new HashMap<>();
    //系统启动的时候会加载
    private static ApplicationContext applicationContext;
    private static SystemContext systemContext;

    public static synchronized SystemContext getSystemContext(){

        if(systemContext == null){
            systemContext = (SystemContext)applicationContext.getBean("systemContext");
        }
        return systemContext;
    }

    public ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * 设置资源，必须要放入线程安全类！
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public<T> void setResource(String key,T value){
        resources.put(key,value);
    }

    /**
     * 获取资源
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getResource(String key,Class<T> clazz){
        return (T)resources.get(key);
    }

    /**
     * 根据类的名字获取Spring中的类
     * @param name 没有指定名字的托管，记得首字母要小写 如loginService来获取LoginService类
     * @return
     */
    public Object getAppBean(String name){
        return applicationContext.getBean(name);
    }

    /**
     * 根据class获取Spring中的类
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getAppBean(Class<T> tClass){
        return applicationContext.getBean(tClass);
    }
}
