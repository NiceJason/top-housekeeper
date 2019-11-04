package com.tophousekeeper.system;

import com.tophousekeeper.service.system.RedisTemplateService;
import com.tophousekeeper.service.system.SystemService;
import com.tophousekeeper.system.running.SystemContext;
import com.tophousekeeper.system.security.EncrypRSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

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

            //生成秘钥
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(56);
            // 产生密钥
            SecretKey secretKey = keyGenerator.generateKey();
            // 获取密钥
            byte[] bytesKey = secretKey.getEncoded();

            // KEY转换
            DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            Key convertSecretKey = factory.generateSecret(desKeySpec);

            EncrypRSA.convertSecretKey = convertSecretKey;

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        logger.info("系统数据加载完毕");
        logger.info("系统初始化完毕");
    }
}
