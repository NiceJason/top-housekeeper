package com.tophousekeeper.system;

import com.tophousekeeper.service.system.SystemService;
import com.tophousekeeper.system.management.SystemCacheMgr;
import com.tophousekeeper.system.running.SystemContext;
import com.tophousekeeper.system.security.EncrypRSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * @author NiceBin
 * @description: 系统启动时，数据加载
 * @date 2019/7/5 9:16
 */
@Component
public class SystemStart implements ApplicationListener<ContextRefreshedEvent>, ImportBeanDefinitionRegistrar {

    private final Logger logger = LoggerFactory.getLogger(SystemStart.class);

    @Autowired
    private SystemService systemService;
    @Autowired
    private SystemCacheMgr systemCacheMgr;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
            logger.info("系统启动");
            SystemContext systemContext = SystemContext.getSystemContext();
            logger.info("清除缓存");
            systemCacheMgr.clearAll();
            logger.info("系统数据开始加载");
            //获取欢迎页的导航地址
           systemService.getNavegationURLs();
            //生成秘钥
            EncrypRSA.init();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        logger.info("系统数据加载完毕");
        logger.info("系统初始化完毕");
    }

    /**
     * 向Spring手动注册的Bean
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        logger.info("根据配置注册Cache");
        boolean definition = registry.containsBeanDefinition("com.scorpios.bean.Color");
    }
}
