package com.tophousekeeper.system.listener;

import com.tophousekeeper.system.management.SystemTimingMgr;
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
    private SystemTimingMgr systemTimingMgr;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        addSessionCount();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }

    /**
     * 统计今日在线人数
     */
    private void addSessionCount(){

        AtomicInteger onlineCount = systemTimingMgr.getOnlineCount();
        onlineCount.incrementAndGet();

        System.out.println("今日访问人数："+onlineCount.get());
    }
}
