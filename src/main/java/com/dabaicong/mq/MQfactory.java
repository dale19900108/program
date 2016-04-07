/**
 * Copyright(c)2016 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2016年4月7日
 */

package com.dabaicong.mq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * <b>创建日期</b>: 2016年4月7日<br/>
 * <b>作用描述</b>: 提供mq的连接工厂
 * 
 * @author 刘冲
 * @mender (文件的修改者，文件创建者之外的人)
 * @version 1.0<br/>
 * Remark: 此版本这是初始化使用 
 * 0.2版本采用连接池的方式管理连接。
 */

public class MQfactory {

	private static Map<String, ActiveMQConnectionFactory> factoryMap = null;

	static {
		factoryMap = new HashMap<String, ActiveMQConnectionFactory>();
	}

	private MQfactory() {

	}

	private static void Init(String url) {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
		if (factoryMap == null) {
			factoryMap = new HashMap<String, ActiveMQConnectionFactory>();
		}
		factoryMap.put(url, factory);
	}
	/*
	 * 根据url得到连接
	 * 这里以后改成用连接池的方式会更高效
	 */
	public static Connection getMQfactoryConnection(String url) throws JMSException {
		if (factoryMap == null || factoryMap.get(url) == null)
			Init(url);
		ActiveMQConnectionFactory factory = factoryMap.get(url);
		if(factory!=null)
			return factory.createConnection(ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD);
		return null;
	}
}
