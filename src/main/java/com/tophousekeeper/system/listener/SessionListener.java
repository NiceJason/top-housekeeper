package com.tophousekeeper.system.listener;

import com.tophousekeeper.system.SystemContext;
import com.tophousekeeper.system.SystemStaticValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author NiceBin
 * @description: Session监听器
 * @date 2019/10/8 16:36
 */
@Component
public class SessionListener implements HttpSessionListener {

    @Autowired
    private SystemContext systemContext;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        addSessionCount();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }

    /**
     * 统计今日在线人数（需要任务调度配合）
     */
    private void addSessionCount(){
        AtomicInteger count = systemContext.getResource(SystemStaticValue.SY_ONLINE_COUNT,AtomicInteger.class);
        if(count == null){
            count = new AtomicInteger(0);
        }
        count.incrementAndGet();
        System.out.println("今日在线人数："+count.get());
    }
}
