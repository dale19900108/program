package com.dabaicong.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by liuchong on 2018/1/27.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 * 这个是为了在一个线程得到锁的时候，被kill，其他的线程能不能得到锁 。
 */
public class ZookeeperLockToKilled {

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

        }, "killed - t1");


        t1.start();
    }

    private static void doWithLock(InterProcessLock lock, int i) {
        try {
            lock.acquire();
            System.out.println("----------" + Thread.currentThread().getName() + " " + i + " get lock ,work start!");
            Thread.sleep(20000000l);
            System.out.println("----------" + Thread.currentThread().getName() + " " + i + " work end!");

        } catch (Exception e) {
            System.out.println("----------" + "get lock timeout" + Thread.currentThread().getName());
        }

    }
}
