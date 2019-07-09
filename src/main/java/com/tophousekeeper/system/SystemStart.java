package com.tophousekeeper.system;

import com.tophousekeeper.service.system.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(SystemStart.class);

    @Autowired
    SystemService systemService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        logger.info("系统数据开始加载");
        SystemContext systemContext = SystemContext.getSystemContext();
        //暂时的模拟系统启动加载数据
        String testDB = systemService.selectByResourcesId(1);
        systemContext.setValue("testDB",testDB);
        logger.info("系统数据加载完毕");
    }
}
