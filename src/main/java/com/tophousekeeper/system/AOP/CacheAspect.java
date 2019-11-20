package com.tophousekeeper.system.AOP;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author NiceBin
 * @description: @SystemCache注解处理的地方
 *               处理路径com/tophousekeeper/system/running/cache下实现Cache接口的缓存类
 * @date 2019/11/18 14:57
 */
@Aspect
@Component
public class CacheAspect {

//    ThreadLocal<>
//
//    @Before(value = "within(com.tophousekeeper.system.running.cache.*)"+
//            "&&target(org.springframework.cache.Cache)"+"&& args(key,value)")
//    public void put(JoinPoint joinPoint,Object key, Object value){
//        key = "1234";
//        Object[] objects=joinPoint.getArgs();
//        objects[0] = "6666";
//        System.out.println("进入切面");
//    }
//
//    @After(value = "within(com.tophousekeeper.system.running.cache.*)"+
//            "&&target(org.springframework.cache.Cache)"+"&& args(value)")
//    public void get(JoinPoint joinPoint,Object value){
//        System.out.println("退出切面");
//    }
}
