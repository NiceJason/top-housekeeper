package com.tophousekeeper.system.listener;

import com.tophousekeeper.system.running.SystemContext;
import com.tophousekeeper.system.SystemStaticValue;
import com.tophousekeeper.entity.SystemDaily;
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
        SystemDaily systemDaily = systemContext.getResource(SystemStaticValue.SY_DAILY, SystemDaily.class);
        AtomicInteger loginCount = systemDaily.getLoginCount();
        loginCount.incrementAndGet();

        System.out.println("今日访问人数："+loginCount.get());
    }
}