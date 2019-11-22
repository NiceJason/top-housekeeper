package com.tophousekeeper.system.AOP;

import com.tophousekeeper.system.management.SystemCacheMgr;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author NiceBin
 * @description: @SystemCache注解处理的地方
 *
 * @date 2019/11/18 14:57
 */
@Aspect
@Component
public class CacheAspect {
    @Autowired
    SystemCacheMgr systemCacheMgr;

    @Pointcut("@annotation(com.tophousekeeper.system.annotation.SystemCache)")
    private void checkSystemCache(){}

    /**
     * 检测该键是否过期
     * @param joinPoint
     */
    @Before(value = "checkSystemCache()")
    public void checkExpire(JoinPoint joinPoint){
        Object[] objects=joinPoint.getArgs();
        System.out.println("进入切面");
    }

    @After(value = "checkSystemCache()")
    public void get(JoinPoint joinPoint){
        Object[] objects=joinPoint.getArgs();
        System.out.println("退出切面");
    }
}
