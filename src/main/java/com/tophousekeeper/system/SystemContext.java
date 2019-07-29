package com.tophousekeeper.system;


import com.tophousekeeper.service.system.RedisTemplateService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author NiceBin
 * @description: 系统的上下文资源
 * @date 2019/7/5 9:30
 */
@Component
public class SystemContext implements ApplicationContextAware {

    //Redis缓存
    @Autowired
    private RedisTemplateService redisTemplateService;
    //系统启动的时候会加载
    private static ApplicationContext applicationContext;
    private static SystemContext systemContext;
    //

    public static synchronized SystemContext getSystemContext(){

        if(systemContext == null){
            systemContext = (SystemContext)applicationContext.getBean("systemContext");
        }
        return systemContext;
    }

    public ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    //向redis中存储信息
    public void setValue(String key,Object value){
        redisTemplateService.set(key,value);
    }

    //向redis获取信息
    public<T> T getValue(String key,Class<T> clazz){
        return redisTemplateService.get(key,clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
