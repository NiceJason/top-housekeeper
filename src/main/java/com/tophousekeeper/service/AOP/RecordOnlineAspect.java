package com.tophousekeeper.service.AOP;

import com.tophousekeeper.system.SystemContext;
import com.tophousekeeper.system.SystemStaticValue;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NiceBin
 * @description: 记录登录人数（配合计划任务，每天登录次数可以记录）
 * @date 2019/10/8 8:54
 */
@Aspect
@Component
public class RecordOnlineAspect {

    @Autowired
    private SystemContext systemContext;
    private final Logger logger = LoggerFactory.getLogger(RecordOnlineAspect.class);

    @AfterReturning("execution(* login(..)) && target(com.tophousekeeper.service.LoginService)")
    public void addOnline(){
        AtomicInteger online = systemContext.getResource(SystemStaticValue.SY_LOGIN_COUNT,AtomicInteger.class);
        if(online == null){
            online = new AtomicInteger(0);
        }
        online.incrementAndGet();
        systemContext.setResource(SystemStaticValue.SY_LOGIN_COUNT,online);
        logger.info("网站今日登录人数："+online.get());
    }

}
