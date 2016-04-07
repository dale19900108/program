/**
 * Copyright(c)2016 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2016年4月7日
 */

package com.dabaicong.mq;

import javax.jms.JMSException;


/**
 * <b>创建日期</b>: 2016年4月7日<br/>
 * <b>作用描述</b>: mqd的Priority Backup属性测试
 * @author 刘冲
 * @mender (文件的修改者，文件创建者之外的人)
 * @version 1.0<br/>
 * Remark: 认为有必要的其他信息
 */

public class TestMQPriorityBackupRecive {

	public static String url = "failover://(tcp://192.168.1.242:61616,tcp://192.168.1.241:61616)?randomize=false&priorityBackup=true";
	public static String url241 = "tcp://192.168.1.241:61616";
	public static String url242 = "tcp://192.168.1.242:61616";
	
	
	
	public static void main(String[] args) {
		
		System.out.println("reciving start !");
		MQReciver mrr = new MQReciver();
		try {
			mrr.init(url);
			while (true) {
				mrr.receive(QueueName.TestQueen);
			}
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
}
