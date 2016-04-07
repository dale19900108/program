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

public class TestMQPriorityBackupSend {

	public static String url = "failover://(tcp://192.168.1.242:61616,tcp://192.168.1.241:61616)?randomize=false&priorityBackup=true";
	public static String url241 = "tcp://192.168.1.241:61616";
	public static String url242 = "tcp://192.168.1.242:61616";
	
	
	
	public static void main(String[] args) {
	
		MQSender sender = new MQSender() ; 
		try {
			sender.Init(url);
			for (int i =0 ;i<2000;i++){
				sender.sendMessage("total send "+i, QueueName.TestQueen);
				Thread.sleep(100);
			}
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
