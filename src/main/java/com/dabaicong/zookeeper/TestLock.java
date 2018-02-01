package com.dabaicong.zookeeper;

/**
 * Created by liuchong on 2018/2/1.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 */
public class TestLock {
    private static final String zookeeperAddress = "127.0.0.1:2181";

    public static void main(String[] args) {

        ZookeeperLock lock = new ZookeeperLock("split", zookeeperAddress);
        Thread t1 = new Thread(() -> {

            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " i=" + i + " ready");
                doWithLock(lock, i);
                try {
                    if (lock.isAcquiredInThisProcess()) {
                        lock.unlock();
                        System.out.println(Thread.currentThread().getName() + " release lock");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "T1");
        Thread t2 = new Thread(() -> {
            ZookeeperLock lock1 = new ZookeeperLock("split", zookeeperAddress);
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " i=" + i + " ready");
                doWithLock(lock1, i);
                try {
                    if (lock1.isAcquiredInThisProcess()) {
                        lock1.unlock();
                        System.out.println(Thread.currentThread().getName() + " release lock");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "t2");

        t1.start();
        t2.start();
    }

    private static void doWithLock(ZookeeperLock lock, int i) {
        try {
            lock.tryLock();
            System.out.println(Thread.currentThread().getName() + " " + i + " get lock ,work start!");
            Thread.sleep(3000l);
            System.out.println(Thread.currentThread().getName() + " " + i + " work end!");

        } catch (Exception e) {
            System.out.println("get lock timeout" + Thread.currentThread().getName());
        }

    }
}
