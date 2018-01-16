package com.dabaicong.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liuchong on 2018/1/12.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 * <p>
 * Tree Cache：监视一个路径下 子结点的创建、删除、以及结点数据的更新。
 * <p>
 * <p>
 * 运行时，先开这个，然后再执行之前的simpleApi去操作节点。
 *
 *  * 但是，无法保证每次都能接收到节点变化的。但是在大量更改的时候，会可能丢掉部分监听
 * 不过，官方文档上说的是，能保证最后一次成功。中间过程可能会忽略。
 */
public class ZookeeperTreeWatcherTest {

    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static final String ZK_PATH = "/zkWatchTest";
    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        CuratorFramework client1 = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(5, 5000));

        if (client1.getState() != CuratorFrameworkState.STARTED) {
            client1.start();
            System.out.println("zookeeper  client start success");
        }
        // 创建一个watcher 。
        TreeCache watcher = new TreeCache(client1, ZK_PATH);
        watcher.getListenable().addListener(new TreeCacheListener() {
            public void childEvent(CuratorFramework client, TreeCacheEvent event) {
                System.out.println("事件类型：" + event.getType() + " | 路径：" + (null != event.getData() ? event.getData().getPath() : null));
                count.addAndGet(1);
                System.out.println(count.get());
            }
        });

        // TreeCache在初始化(调用start()方法)的时候会回调TreeCacheListener实例一个事TreeCacheEvent，
        // 而回调的TreeCacheEvent对象的Type为INITIALIZED，ChildData为null，
        // 此时event.getData().getPath()很有可能导致空指针异常，这里应该主动处理并避免这种情况。


        watcher.start();
        System.out.println("register watcher success");
        Thread.sleep(Integer.MAX_VALUE);

    }
}
