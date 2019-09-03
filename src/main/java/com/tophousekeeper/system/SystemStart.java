package com.tophousekeeper.system;

import com.tophousekeeper.service.system.RedisTemplateService;
import com.tophousekeeper.service.system.SystemService;
import com.tophousekeeper.system.security.EncrypRSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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
        logger.info("系统启动");
        SystemContext systemContext = SystemContext.getSystemContext();
        logger.info("清除缓存");
        RedisTemplateService redisTemplateService = systemContext.getRedisTemplateService();
        redisTemplateService.clearAll();
        logger.info("系统数据开始加载");

        try {
            //获取欢迎页的导航地址
            String welcomeNavegation = systemService.getNavegationURLs();
            redisTemplateService.set(SystemStaticValue.RE_WELCOMENAVEGATION,welcomeNavegation,RedisTemplateService.TYPE_MIN);

            //生成秘钥对
            KeyPairGenerator keyPairGenRSA = KeyPairGenerator.getInstance("RSA");
            //初始化密钥对生成器，密钥大小为1024位
            keyPairGenRSA.initialize(1024);
            //生成一个密钥对，
            KeyPair keyPairRSA = keyPairGenRSA.generateKeyPair();
            EncrypRSA.privateKey = (RSAPrivateKey) keyPairRSA.getPrivate();
            EncrypRSA.publicKey = (RSAPublicKey) keyPairRSA.getPublic();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        logger.info("系统数据加载完毕");
        logger.info("系统初始化完毕");
    }
}
