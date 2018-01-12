package com.dabaicong.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by liuchong on 2018/1/12.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 * <p>
 * Path Cache：监视一个路径下 子结点的创建、删除、以及结点数据的更新。
 * 产生的事件会传递给注册的PathChildrenCacheListener。
 * <p>
 * 运行时，先开这个，然后再执行之前的simpleApi去操作节点。
 */
public class ZookeeperPathWatcherTest {

    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static final String ZK_PATH = "/zkWatchTest";

    public static void main(String[] args) throws Exception {
        CuratorFramework client1 = CuratorFrameworkFactory.newClient(ZK_ADDRESS, new RetryNTimes(5, 5000));

        if (client1.getState() != CuratorFrameworkState.STARTED) {
            client1.start();
            System.out.println("zookeeper  client start success");
        }

        // 创建一个watcher 。
        PathChildrenCache watcher = new PathChildrenCache(client1, ZK_PATH, true);

        watcher.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
                ChildData data = event.getData();
                if (data == null) {
                    System.out.println("No data in event[" + event + "]");
                } else {
                    System.out.println("Receive event: "
                            + "type=[" + event.getType() + "]"
                            + ", path=[" + data.getPath() + "]"
                            + ", data=[" + new String(data.getData()) + "]"
                            + ", stat=[" + data.getStat() + "]");
                }
            }
        });
         /*//lambda 表达式，跟上面是同样的原理
        watcher.getListenable().addListener((client, event) -> {
            ChildData data = event.getData();
            if (data == null) {
                System.out.println("No data in event[" + event + "]");
            } else {
                System.out.println("Receive event: "
                        + "type=[" + event.getType() + "]"
                        + ", path=[" + data.getPath() + "]"
                        + ", data=[" + new String(data.getData()) + "]"
                        + ", stat=[" + data.getStat() + "]");
            }
        });*/

        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        System.out.println("register watcher success");
        Thread.sleep(Integer.MAX_VALUE);

    }
}
