/**
 * Copyright(c)2016 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2016年4月4日
 */

package com.dabaicong.callback;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>创建日期</b>: 2016年4月4日<br/>
 * <b>作用描述</b>: 对本文件的详细描述，原则上不能少于50字
 * @author 刘冲
 * @mender (文件的修改者，文件创建者之外的人)
 * @version 1.0<br/>
 * Remark: 认为有必要的其他信息
 */

public abstract class AbstractCallableTask<V> implements Callable<V>{

	//日志记录器
	private static Logger logger = LoggerFactory.getLogger("AbstractCallableTask");
	
	//任务名
	private String taskName ;
	
	//任务出错后执行尝试次数
	private int retrycount = 5;
	
	//实际执行的尝试次数。若是一次执行成功，则是0
	private int realExecutecount = 0;
	
	//
	private int retryInterval = 1000 ;
	
	
	
	public V call() throws Exception {
		while (realExecutecount<retrycount) {
			try {
				V result = execute() ;
				return result ;
			} catch (Exception e) {
				logger.error("error ",e);
			}
			realExecutecount++ ;
			//进入到等待。
			synchronized (this) {
				try {
					this.wait(retryInterval);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		logger.error("[Error] task :"+getTaskName()+"has retry"+ getRetrycount()+" times and fail to execute it");
		return null ;
	}
	
	protected abstract V execute() throws Exception;
	
	

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getRetrycount() {
		return retrycount;
	}

	public void setRetrycount(int retrycount) {
		this.retrycount = retrycount;
	}
	
	
	public int getRealExecutecount() {
		return realExecutecount;
	}

	public int getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	/**
	 * @return 返回 logger。
	 *
	 */
	public static Logger getLogger() {
		return logger;
	}
}
