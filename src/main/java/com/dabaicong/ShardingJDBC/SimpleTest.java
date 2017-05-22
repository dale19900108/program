package com.dabaicong.ShardingJDBC;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.HostNameIdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.IPIdGenerator;
import com.dangdang.ddframe.rdb.sharding.jdbc.ShardingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuchong on 2017/4/24.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 */
public class SimpleTest {

    private static String databaseName1 = "dbtest0";
    private static String databaseName2 = "dbtest1";
    private static String sql = "SELECT * FROM t_order where user_id ='10';";


    private static Map<String, DataSource> sourceMap = new HashMap<String, DataSource>();


    public static void main(String[] args) throws SQLException {
        // testSelect();
        // testDelete();
        // testIPIdGenerator(20);
        // System.out.println("===========================");
        // testHostNameIDGerenator(20);
        // System.out.println("===========================");
        // testCommonIdGenerator(10);
        // testSelectByShardingDatabase();
        StringBuilder hotIds = new StringBuilder();
        hotIds.append("test1").append(",");
        hotIds.append("test2").append(",");
        hotIds.append("test3").append(",");
        hotIds.append("test4").append(",");
        hotIds.append("test5").append(",");
        hotIds.delete(hotIds.length() - 1, hotIds.length());
        System.out.println(hotIds.toString());
    }

    public static void testIPIdGenerator(int seq) {
        IPIdGenerator idGenerator = new IPIdGenerator();
        for (int i = 0; i < seq; i++) {
            System.out.println(idGenerator.generateId());
        }
    }

    public static void testHostNameIDGerenator(int seq) {
        //Fixme  hostname must be end with number!
        HostNameIdGenerator idGenerator = new HostNameIdGenerator();
        for (int i = 0; i < seq; i++) {
            System.out.println(idGenerator.generateId());
        }
    }

    public static void testCommonIdGenerator(int seq) {
        CommonSelfIdGenerator idGenerator = new CommonSelfIdGenerator();
        // idGenerator.setWorkerId(Long.valueOf("10010"));
        // CommonSelfIdGenerator idGenerator1 = new CommonSelfIdGenerator();
        // idGenerator.setWorkerId(Long.valueOf("11111"));
        for (int i = 0; i < seq; i++) {
            System.out.println(System.currentTimeMillis() + ":  " + idGenerator.generateId());
            // System.out.println("11111:  "+idGenerator1.generateId());
        }
    }


    public static void testSelect() throws SQLException {
        DataSource dataSource = new ShardingDataSource(getShardingRule());
        Connection connection = dataSource.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        int i = 0;
        while (resultSet.next()) {
            System.out.print(resultSet.getString(1) + "\t");
            System.out.print(resultSet.getString(2) + "\t");
            System.out.print(resultSet.getString(3) + "\t");
            System.out.println();
            i++;
        }
        System.out.println("i= " + i);
    }

    public static void testSelectByShardingDatabase() throws SQLException {

        sql = "SELECT * FROM t_order_0 where user_id ='10';";
        DataSource dataSource = new ShardingDataSource(testShardingDatabseRule());
        Connection connection = dataSource.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);
        int i = 0;
        while (resultSet.next()) {
            System.out.print(resultSet.getString(1) + "\t");
            System.out.print(resultSet.getString(2) + "\t");
            System.out.print(resultSet.getString(3) + "\t");
            System.out.println();
            i++;
        }
        System.out.println("i= " + i);
    }

    public static void testDelete() throws SQLException {
        sql = "DELETE FROM t_order where user_id ='11';";
        DataSource dataSource = new ShardingDataSource(getShardingRule());
        Connection connection = dataSource.getConnection();
        Statement stmt = connection.createStatement();
        int i = stmt.executeUpdate(sql);
        System.out.println("i= " + i);

    }

    public static ShardingRule getShardingRule() {

        TableRule orderTableRule = TableRule.builder("t_order").actualTables(Arrays.asList("db1.t_order_1")).dataSourceRule(getDataSourceRule()).build();
        TableRule ticketTableRule = TableRule.builder("ticket").dataSourceRule(getDataSourceRule()).build();
        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(getDataSourceRule())
                .tableRules(Arrays.asList(orderTableRule, ticketTableRule))
                .build();
        return shardingRule;
    }

    // 测试只分库，不分表
    public static ShardingRule testShardingDatabseRule() {

        TableRule orderTableRule = TableRule.builder("t_order_0")
                .actualTables(Arrays.asList("t_order_0"))
                .dataSourceRule(getDataSourceRule()).build();
        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(getDataSourceRule())
                .tableRules(Arrays.asList(orderTableRule))
                // .databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new ModuloDatabaseShardingAlgorithm()))
                // .tableShardingStrategy(new TableShardingStrategy("user_id", new simpleDatabaseShardingStrategy()))
                .build();
        return shardingRule;
    }

    public static DataSourceRule getDataSourceRule() {
        DataSourceRule dataSourceRule = new DataSourceRule(createDataSourceMap());
        return dataSourceRule;
    }

    private static Map<String, DataSource> createDataSourceMap() {
        if (sourceMap == null || sourceMap.size() == 0) {
            sourceMap = new HashMap<String, DataSource>();
            sourceMap.put(databaseName1, createDataSource(databaseName1));
            sourceMap.put(databaseName2, createDataSource(databaseName2));

        }
        return sourceMap;
    }

    private static DataSource createDataSource(String databaseName) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        dataSource.setUrl(String.format("jdbc:mysql://test1.qmcai.com:3306/%s", databaseName));
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }


}

//基本的分库策略
class simpleDatabaseShardingStrategy implements SingleKeyTableShardingAlgorithm<Integer> {

    //根据where条件，选择对应的表名

    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        System.out.println(shardingValue.getValue());
        for (String name : availableTargetNames) {
            System.out.println("name:" + name);
            if (shardingValue.getValue().equals("10")) {
                return name;
            }
        }
        throw new IllegalArgumentException();
    }

    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        return null;
    }

    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<Integer> shardingValue) {
        return null;
    }
}
