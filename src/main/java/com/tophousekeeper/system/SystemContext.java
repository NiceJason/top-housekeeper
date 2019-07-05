package com.tophousekeeper.system;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.HashMap;

/**
 * @author NiceBin
 * @description: 系统的上下文资源
 * @date 2019/7/5 9:30
 */
@Component
public class SystemContext {
    @Autowired
    private static ApplicationContext applicationContext;
    private static SystemContext systemContext;
    //下面这个属性之后会有redis替代
    private HashMap<String,String> valueMap = new HashMap<>();

    public static synchronized SystemContext getSystemContext(){
        WebApplicationContextUtils.getRequiredWebApplicationContext();
        if(systemContext == null){
            systemContext = (SystemContext)applicationContext.getBean("SystemContext");
        }
        return systemContext;
    }

    public ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public void setValue(String key,String value){
        valueMap.put(key,value);
    }

    public String getValue(String key){
        return valueMap.get(key);
    }
}
