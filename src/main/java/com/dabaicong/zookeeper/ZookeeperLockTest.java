package com.dabaicong.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by liuchong on 2018/1/10.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 */
public class ZookeeperLockTest {

    private static final String zookeeperAddress = "127.0.0.1:2181";


    public static void main(String[] args) throws Exception {
        // 连接尝试五次，每次间隔5s
        long start = System.currentTimeMillis();
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperAddress, new RetryNTimes(5, 5000));
        client.start();
        System.out.println(client.getState());
        String dataNew = "hello word";
        for (int i = 0; i < 1; i++) {
            System.out.println(" modify  /zkWatchTest/test1" + "    " + dataNew + i);
            System.out.println(client.setData().forPath("/zkWatchTest/test1", (i + dataNew).getBytes()));
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
    }
}
