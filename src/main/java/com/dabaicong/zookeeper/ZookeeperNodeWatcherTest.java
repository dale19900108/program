package com.dabaicong.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liuchong on 2018/1/12.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 * <p>
 * node Cache：监视一个结点的创建、更新、删除，并将结点的数据缓存在本地
 * <p>
 * 运行时，先开这个，然后再命令行操作节点
 *
 *  * 但是，无法保证每次都能接收到节点变化的。但是在大量更改的时候，会可能丢掉部分监听
 * 不过，官方文档上说的是，能保证最后一次成功。中间过程可能会忽略。
 */
public class ZookeeperNodeWatcherTest {

    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static final String ZK_PATH = "/zkWatchTest/test1";

    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        final CuratorFramework client1 = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(5, 5000));

        if (client1.getState() != CuratorFrameworkState.STARTED) {
            client1.start();
            System.out.println("zookeeper  client start success");
        }

        // 创建一个watcher 。
        final NodeCache watcher = new NodeCache(client1, ZK_PATH);
        // 最后一个参数不能是true，true没办法监听。
        //final NodeCache watcher = new NodeCache(client1, ZK_PATH, true);

        watcher.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() {
                ChildData data = watcher.getCurrentData();
                if (null != data) {
                    System.out.println("节点数据变化：" + count.get() + "  " + new String(watcher.getCurrentData().getData()));
                    count.addAndGet(1);
                } else {
                    System.out.println("节点被删除!");
                }
            }

        });

        watcher.start();
        System.out.println("register watcher success");
        Thread.sleep(Integer.MAX_VALUE);

    }
}
