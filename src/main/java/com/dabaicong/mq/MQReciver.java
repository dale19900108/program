/**
 * Copyright(c)2016 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2016年4月7日
 */

package com.dabaicong.mq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
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
 *          Remark: 这种模式就不对。。。。应该缓存下来session和destination
 */

public class MQReciver {
	
	private static Logger logger = LoggerFactory.getLogger(MQReciver.class.getName());

	// 创建connection
	private Connection connection = null;
	// 创建session
	private Session session;
	// 创建目的地
	private Destination destination;
	// 消费者
	private MessageConsumer consumer;
	private String currentQueueName ;
	
	public void init(String url) throws JMSException {
		// 创建链接
		connection = MQfactory.getMQfactoryConnection(url);
		// 启动
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	}

	public void receive(QueueName name) throws JMSException {
		
		if(destination == null || !name.getValue().equals(currentQueueName))
			destination = session.createQueue(name.getValue()) ;
		if(consumer==null || !name.getValue().equals(currentQueueName))
			consumer = session.createConsumer(destination);
		TextMessage message = (TextMessage) consumer.receive(100);
		if(message != null)
			//logger.error("receive message is :"+ message.getText());
			System.out.println("receive message is :"+ message.getText());
		currentQueueName = name.getValue();
	}

	public void closeConnection() throws JMSException {
		if (null != connection)
			connection.close();
	}

	/**
	 * @return 返回 logger。
	 *
	 */
	public static Logger getLogger() {
		return logger;
	}
}
