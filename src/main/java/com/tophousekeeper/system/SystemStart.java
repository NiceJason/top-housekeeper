package com.tophousekeeper.system;

import com.tophousekeeper.service.system.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author NiceBin
 * @description: 系统启动时，数据加载
 * @date 2019/7/5 9:16
 */
@Component
public class SystemStart implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    SystemService systemService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("系统数据开始加载");
        SystemContext systemContext = SystemContext.getSystemContext();
        //暂时的模拟系统启动加载数据
        String testDB = systemService.selectByResourcesId(1);
        systemContext.setValue("testDB",testDB);
        System.out.println("系统数据加载完毕");
    }
}
