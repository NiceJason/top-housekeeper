package com.tophousekeeper.system.running.cache;

import com.tophousekeeper.system.SystemException;
import com.tophousekeeper.system.SystemStaticValue;
import org.springframework.cache.Cache;
import org.springframework.util.MethodInvoker;

import java.util.concurrent.Callable;

/**
 * @author NiceBin
 * @description: 刷新缓存某个数据的任务
 * @date 2019/11/29 15:29
 */
public class UpdateDataTask implements Callable {
    //将要执行的方法信息
    private CacheInvocation cacheInvocation;
    //对应要操作的缓存
    private Cache cache;
    //对应要更新的数据id
    private Object id;

    /**
     * 初始化任务
     * @param cacheInvocation
     * @param cache
     * @param id
     */
    public UpdateDataTask(CacheInvocation cacheInvocation,Cache cache,Object id){
        this.cacheInvocation = cacheInvocation;
        this.cache = cache;
        this.id = id;
    }

    @Override
    public Object call() throws Exception {
        if(cacheInvocation == null){
            throw new SystemException(SystemStaticValue.CACHE_EXCEPTION_CODE,"更新数据线程方法信息不能为null");
        }
        cache.put(id,methodInvoke());
        return true;
    }

    /**
     * 代理方法的调用
     * @return
     */
    private Object methodInvoke() throws Exception{
        MethodInvoker methodInvoker = new MethodInvoker();
        methodInvoker.setArguments(cacheInvocation.getArguments());
        methodInvoker.setTargetMethod(cacheInvocation.getTargetMethod().getName());
        methodInvoker.setTargetObject(cacheInvocation.getTargetBean());
        methodInvoker.prepare();
        return methodInvoker.invoke();
    }
}
