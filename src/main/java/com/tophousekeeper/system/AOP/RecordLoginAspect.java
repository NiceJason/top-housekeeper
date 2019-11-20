package com.tophousekeeper.system.AOP;

import com.tophousekeeper.entity.User;
import com.tophousekeeper.system.management.SystemTimingMgr;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NiceBin
 * @description: 记录登录人数（配合计划任务，每天登录次数可以记录），同一用户每天只登记一次
 * @date 2019/10/8 8:54
 */
@Aspect
@Component
public class RecordLoginAspect {

    @Autowired
    private SystemTimingMgr systemTimingMgr;
    private final Logger logger = LoggerFactory.getLogger(RecordLoginAspect.class);


    @AfterReturning(value = "execution(* login(..)) && target(com.tophousekeeper.service.LoginService)",returning = "user")
    public void addOnline(User user){

        if(!systemTimingMgr.isUserFilter(user)){
            AtomicInteger loginCount = systemTimingMgr.getLoginCount();
            loginCount.incrementAndGet();
            logger.info("网站今日登录人数："+loginCount.get());
        }
    }

}
