package com.tophousekeeper.system.running.cache;

import org.springframework.cache.annotation.Cacheable;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author NiceBin
 * @description: 被 {@link Cacheable} 注解过的方法信息，为了主动更新缓存去调用对应方法
 * @date 2019/11/26 16:28
 */
public class CacheInvocation {
    private Object key;
    private final Object targetBean;
    private final Method targetMethod;
    private Object[] arguments;

    public CacheInvocation(Object key, Object targetBean, Method targetMethod, Object[] arguments) {
        this.key = key;
        this.targetBean = targetBean;
        this.targetMethod = targetMethod;
        if (arguments != null && arguments.length != 0) {
            this.arguments = Arrays.copyOf(arguments, arguments.length);
        }
    }

    public Object[] getArguments() {
        return arguments;
    }

    public Object getTargetBean() {
        return targetBean;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object getKey() {
        return key;
    }
}
