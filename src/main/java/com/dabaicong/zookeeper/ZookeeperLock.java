package com.dabaicong.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by liuchong on 2018/2/1.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 * 基于zookeeper实现的分布式锁
 * 实现原理是基于curator的InterProcessMutex，不可重入锁
 * <p>
 * 对于curator的实现，lock = tryLock = lockInterruptibly
 */
public class ZookeeperLock implements Lock {

    protected static final String LOCK_POSITION = "/lock/zookeeper/";
    private static Map<String, CuratorFramework> clientCacheMap = new ConcurrentHashMap<String, CuratorFramework>();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private InterProcessMutex lock = null;


    private String key;     // 缓存的key

    /**
     * 创建一个zookeeper锁
     *
     * @param key 锁的key
     */
    public ZookeeperLock(String key, String ZookeeperAddress) {
        // 得到client。
        String cacheKey = LOCK_POSITION.concat(key);
        CuratorFramework client = clientCacheMap.get(cacheKey);
        if (client == null) {
            client = CuratorFrameworkFactory.newClient(ZookeeperAddress, new RetryNTimes(5, 1000));
            client.start();
            clientCacheMap.put(cacheKey, client);
        } else {
            if (client.getState() != CuratorFrameworkState.STARTED) {
                client.start();
            }
        }
        // 得到锁
        lock = new InterProcessMutex(client, LOCK_POSITION.concat(key));
        this.key = key;
    }

    /**
     * 对当前的锁加锁,在curator中表示的是获得锁
     */
    public void lock() {
        this.tryLock();
    }


    /**
     * 如果锁可用，则获取锁，并立即返回；不可用就一直等到可用。
     * curator 中是没有返回interruptException的
     *
     * @throws InterruptedException
     */
    public void lockInterruptibly() {
        this.tryLock();
    }

    /**
     * 仅在调用时锁为空闲状态才获取该锁。
     * 如果锁可用，则获取锁，并立即返回值 true。如果锁不可用，则此方法将立即返回值 false。
     *
     * @return
     */
    public boolean tryLock() {
        try {
            // 这个方法是curator一直等zookeeper的锁，直到获取锁，有可能抛出异常
            lock.acquire();
            return true;
        } catch (Exception e) {
            logger.error(this.key + " tryLock error", e);
            return false;
        }
    }

    /**
     * 如果锁在给定的等待时间内空闲，并且当前线程未被 中断，则获取锁。
     * 如果锁可用，则此方法将立即返回值 true。如果锁不可用，
     *
     * @param timeOut  超时时间
     * @param timeUnit 超时时间的单位
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(long timeOut, TimeUnit timeUnit) throws InterruptedException {
        try {
            return lock.acquire(timeOut, timeUnit);
        } catch (Exception e) {
            logger.error(this.key + " tryock error", e);
            throw new InterruptedException(e.getMessage());
        }
    }

    /**
     * 释放锁
     */
    public void unlock() {
        try {
            if (isAcquiredInThisProcess())
                lock.release();
            else
                logger.info(this.key + " unlock in error thread:" + Thread.currentThread().getName());
        } catch (Exception e) {
            logger.error(this.key + " unlock exception", e);
        }
    }

    /**
     * 返回TRUE表示当前线程持有锁
     *
     * @return
     */
    public boolean isAcquiredInThisProcess() {
        return lock.isAcquiredInThisProcess();
    }

    /**
     * 返回绑定到此 Lock 实例的新 Condition 实例。
     * 实话是我也不知道jdk为什么提供这么个接口，上面的这句话是jdk中的翻译。
     * 现在我知道什么是condition了，但是不想在注释中告诉你。
     *
     * @return
     */
    public Condition newCondition() {
        return null;
    }

    public String getKey() {
        return key;
    }
}
