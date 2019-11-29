package com.tophousekeeper.system.running;

import com.tophousekeeper.system.SystemStaticValue;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author NiceBin
 * @description: 系统线程池，对线程进行统一管理
 * @date 2019/11/28 15:25
 */
@Component
public class SystemThreadPool {
    private ThreadPoolExecutor threadPoolExecutor;

    public SystemThreadPool(){
        threadPoolExecutor = new ThreadPoolExecutor(
                SystemStaticValue.THREAD_CORE_POOL_SIZE,
                SystemStaticValue.THREAD_MAXIMUM_POOL_SIZE,
                SystemStaticValue.THREAD_KEEP_ALIVE_TIME,
                SystemStaticValue.THREAD_UNIT,
                new ArrayBlockingQueue<Runnable>(SystemStaticValue.THREAD_MAXIMUM_POOL_SIZE,true)
        );
        //允许核心线程也受KeepAliveTime的影响
        threadPoolExecutor.allowCoreThreadTimeOut(true);
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
}
