package com.tophousekeeper.system.running;


import com.tophousekeeper.service.system.RedisTemplateService;
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

    //Redis缓存服务
    @Autowired
    private RedisTemplateService redisTemplateService;
    //数据库操作类
    @Autowired
    private JdbcTemplate jdbcTemplate;
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

    /**
     * 获取redis模板
     * @return
     */
    public RedisTemplateService getRedisTemplateService(){
        return redisTemplateService;
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
}