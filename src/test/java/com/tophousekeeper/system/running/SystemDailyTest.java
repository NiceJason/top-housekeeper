package com.tophousekeeper.system.running;

import com.tophousekeeper.entity.SystemDaily;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class SystemDailyTest {


    @Test
    public void dailyClear() throws Exception {
        SystemDaily systemDaily = new SystemDaily();
        AtomicInteger atomicInteger=systemDaily.getLoginCount();
        System.out.println("初始值 = "+atomicInteger.get());
        atomicInteger.incrementAndGet();
        System.out.println("加一后 = "+atomicInteger.get());
        atomicInteger.set(0);
        System.out.println("手动清空后 = "+atomicInteger.get());

        atomicInteger.incrementAndGet();
        System.out.println("加一后 = "+atomicInteger.get());
        systemDaily.dailyClear();
        System.out.println("清空后 = "+atomicInteger.get());
    }

    @Test
    public void getLoginCount() {
    }

    @Test
    public void setLoginCount() {
    }
}