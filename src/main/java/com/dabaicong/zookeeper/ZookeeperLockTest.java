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

    private static final String zookeeperPath = "/lockTest";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperAddress, new RetryNTimes(5, 5000));
        System.out.println("client before start status is :" + client.getState().name());
        client.start();
        System.out.println("client start ! now status is :" + client.getState().name());

        getNode(client);

        // createNode
        createNode(client);

        // get node data
        getNodeDate(client);

        // modify data
        modifyNodeDate(client);

        // get node data
        getNodeDate(client);

        // delete node data
        deleteNode(client);

        // get node
        getNode(client);

    }

    // create node
    private static void createNode(CuratorFramework client) throws Exception {
        // createNode
        String dateString = "testString";

        /**
         * Causes any parent nodes to get created using {@link CreateMode#CONTAINER} if they haven't already been.
         *
         * IMPORTANT NOTE: container creation is a new feature in recent versions of ZooKeeper.
         * If the ZooKeeper version you're using does not support containers, the parent nodes
         * are created as ordinary PERSISTENT nodes.
         *
         */
        client.create().creatingParentContainersIfNeeded().forPath(zookeeperPath + "/test1", dateString.getBytes());

        /**
         * Causes any parent nodes to get created if they haven't already been
         */
        client.create().creatingParentsIfNeeded().forPath(zookeeperPath + "/parent/test1", dateString.getBytes());
    }

    // get node info
    private static void getNode(CuratorFramework client) throws Exception {
        System.out.println("------------------getNode--------------------------");
        System.out.println(" ls  / :");
        System.out.println(client.getChildren().forPath("/"));
        System.out.println(" ls  /lockTest :");
        System.out.println(client.getChildren().forPath(zookeeperPath));
        System.out.println(" ls  /lockTest/parent :");
        System.out.println(client.getChildren().forPath("/lockTest/parent"));
        System.out.println("--------------------getNode------------------------");
        System.out.println();
        System.out.println();
    }

    // get node date
    private static void getNodeDate(CuratorFramework client) throws Exception {
        System.out.println("------------------getNodeDate--------------------------");
        System.out.println(" ls  /lockTest :");
        System.out.println(client.getChildren().forPath("/lockTest"));
        System.out.println(" get " + zookeeperPath + "/test1");
        byte[] data = client.getData().forPath(zookeeperPath + "/test1");
        System.out.println(new String(data));
        System.out.println(" get " + zookeeperPath + "/parent/test1");
        data = client.getData().forPath(zookeeperPath + "/parent/test1");
        System.out.println(new String(data));
        System.out.println("--------------------getNodeDate------------------------");
        System.out.println();
        System.out.println();
    }

    // modify node date
    private static void modifyNodeDate(CuratorFramework client) throws Exception {
        System.out.println("--------------------modifyNodeDate------------------------");
        String dataNew = "hello word";
        System.out.println(" ls / :");
        System.out.println(client.getChildren().forPath("/"));
        for (int i = 0; i < 3; i++) {
            System.out.println(" modify  " + zookeeperPath + "/test1" + "    " + dataNew + i);
            System.out.println(client.setData().forPath(zookeeperPath + "/test1", (i + dataNew).getBytes()));
            System.out.println(" modify  " + zookeeperPath + "/parent/test1" + "    " + (i + dataNew));
            System.out.println(client.setData().forPath(zookeeperPath + "/parent/test1", (i + dataNew).getBytes()));
        }
        System.out.println("--------------------modifyNodeDate------------------------");
        System.out.println();
        System.out.println();
    }

    // remove node
    private static void deleteNode(CuratorFramework client) throws Exception {
        System.out.println("--------------------deleteNode------------------------");
        System.out.println(" ls / :");
        System.out.println(client.getChildren().forPath("/"));
        System.out.println(" delete  " + zookeeperPath + "/test1");
        client.delete().forPath(zookeeperPath + "/test1");
        System.out.println(" delete  " + zookeeperPath + "/parent/test1");
        client.delete().forPath(zookeeperPath + "/parent/test1");
        System.out.println("--------------------deleteNode------------------------");
        System.out.println();
        System.out.println();
    }

}
