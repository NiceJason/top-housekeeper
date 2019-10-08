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
 * @description: 记录登录在线的人数
 * @date 2019/10/8 8:54
 */
@Aspect
@Component
public class RecordOnlineAspect {

    @Autowired
    private SystemContext systemContext;
    private final Logger logger = LoggerFactory.getLogger(RecordOnlineAspect.class);
    /**
     * 增加在线人数
     */
    @AfterReturning("execution(* login(..)) && target(com.tophousekeeper.service.LoginService)")
    public void addOnline(){
        AtomicInteger online = systemContext.getResource(SystemStaticValue.SY_ONLINE,AtomicInteger.class);
        if(online == null){
            online = new AtomicInteger(0);
        }
        online.incrementAndGet();
        systemContext.setResource(SystemStaticValue.SY_ONLINE,online);
        logger.info("网站当前登录人数："+online.get());
    }

    @AfterReturning("execution(* logout(..)) && target(com.tophousekeeper.service.LoginService)")
    public void reduceOnline(){
        AtomicInteger online = systemContext.getResource(SystemStaticValue.SY_ONLINE,AtomicInteger.class);
        online.decrementAndGet();
        systemContext.setResource(SystemStaticValue.SY_ONLINE,online);
        logger.info("网站当前登录人数："+online.get());
    }
}
