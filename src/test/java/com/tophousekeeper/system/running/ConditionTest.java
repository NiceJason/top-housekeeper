package com.tophousekeeper.system.running;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    public static void main(String[] args) throws InterruptedException {
//        System.out.println("1 "+lock.tryLock(1, TimeUnit.SECONDS));
        lock.lock();
//        System.out.println("2 "+lock.tryLock(1, TimeUnit.SECONDS));
        new Thread(new SignalThread()).start();
//        new Thread(new SignalThread()).start();
//        new Thread(new SignalThread()).start();
//        new Thread(new SignalThread()).start();
        System.out.println("主线程等待通知");
        try {
            condition.await();
            Thread.sleep(3000);
        } finally {
//            System.out.println("3 "+lock.tryLock());
            lock.unlock();
//            System.out.println("4 "+lock.tryLock());
        }
        System.out.println("主线程恢复运行");
        Thread.sleep(30000000);
    }
    static class SignalThread implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("5 "+lock.tryLock(1, TimeUnit.SECONDS));
//                lock.lock();
                System.out.println("获取到了");
                System.out.println("6 "+lock.tryLock(1, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName()+"子线程通知");
            } finally {
//                lock.unlock();
            }
        }
    }
}