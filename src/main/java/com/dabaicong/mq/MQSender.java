/**
 * Copyright(c)2016 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2016年4月7日
 */

package com.dabaicong.mq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>创建日期</b>: 2016年4月7日<br/>
 * <b>作用描述</b>: 对本文件的详细描述，原则上不能少于50字
 * 
 * @author 刘冲
 * @mender (文件的修改者，文件创建者之外的人)
 * @version 1.0<br/>
 * v0.2版本可以缓存下session和producer，方便后面使用
 */

public class MQSender {

	private static Logger logger = LoggerFactory.getLogger(MQSender.class.getName());

	// 连接
	private Connection conn = null;
	// 创建一个session
	private Session session;
	// 创建目的地
	private Destination dest;
	// 消息提供者
	private MessageProducer producer;

	public void Init(String url) throws JMSException {
		
		conn = MQfactory.getMQfactoryConnection(url) ;
		conn.start();
		session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	public void sendMessage(String messageText,QueueName queuename) throws JMSException {
		
		dest = session.createQueue(queuename.getValue());
		producer = session.createProducer(dest);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		TextMessage message = session.createTextMessage(messageText);
		// 发送消息到服务器
		producer.send(message);
	}

	public void closeConnection() throws JMSException {
		if (null != conn)
			conn.close();
	}
	
	/**
	 * @return 返回 logger。
	 *
	 */
	public static Logger getLogger() {
		return logger;
	}
}
