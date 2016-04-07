/**
 * Copyright(c)2016 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2016年4月7日
 */

package com.dabaicong.mq;

/**
 * <b>创建日期</b>: 2016年4月7日<br/>
 * <b>作用描述</b>: 队列名
 * @author 刘冲
 * @mender (文件的修改者，文件创建者之外的人)
 * @version 1.0<br/>
 * Remark: 认为有必要的其他信息
 */

public enum QueueName {

	TestQueen("jms:queue:testPriorityBackup", "测试PriorityBackup");
	
	
	private String name;
	private String value;
	
	QueueName(String value,String name){
		this.name = name ;
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getName(){
		return name;
	}
}
