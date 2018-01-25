package com.dabaicong.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by liuchong on 2018/1/10.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 *
 * 这种锁的模型是两个线程都在运行，谁拿到锁谁就执行。
 * 同一时间只有一个线程在运行。
 * 优点是两个节点一起运行。
 */
public class ZookeeperLockTest {

    private static final String zookeeperAddress = "127.0.0.1:2181";
    private static final String ZK_LOCK_PATH = "/lottery-order/lock/split";


    public static void main(String[] args) {
        final CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperAddress, new RetryNTimes(10, 4000));
        client.start();
        System.out.println("zk client start success!");


        Thread t1 = new Thread(() -> {
            InterProcessMutex lock = new InterProcessMutex(client, ZK_LOCK_PATH);
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " i=" + i + " ready");
                doWithLock(lock, i);
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                lock.release();
                System.out.println(Thread.currentThread().getName() + " release lock");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, "t1");
        Thread t2 = new Thread(() -> {
            InterProcessMutex lock = new InterProcessMutex(client, ZK_LOCK_PATH);
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " i=" + i + " ready");
                doWithLock(lock, i);
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 3) {
                    // 或者可以考虑换成毒药的标记位来判断是否执行。
                    Thread.currentThread().stop();
                    if (lock.isAcquiredInThisProcess()) {
                        try {
                            lock.release();
                            System.out.println(Thread.currentThread().getName() + " release lock");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                lock.release();
                System.out.println(Thread.currentThread().getName() + " release lock");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t2");

        t1.start();
        t2.start();
    }

    private static void doWithLock(InterProcessLock lock, int i) {
        try {
            lock.acquire();
            System.out.println(Thread.currentThread().getName() + " " + i + " get lock ,work start!");
            Thread.sleep(2000l);
            System.out.println(Thread.currentThread().getName() + " " + i + " work end!");

        } catch (Exception e) {
            System.out.println("get lock timeout" + Thread.currentThread().getName());
        }

    }
}
