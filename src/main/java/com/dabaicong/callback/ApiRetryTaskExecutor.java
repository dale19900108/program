/**
 * 
 */
package com.dabaicong.callback;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * api重新尝试执行线程
 * 被执行的线程需要继承AbstractCallableTask抽象类
 */

public class ApiRetryTaskExecutor {
	
	//日志记录器
	private static Logger logger = LoggerFactory.getLogger(ApiRetryTaskExecutor.class.getName());
	
	private ExecutorService executor;
	
	/**
	 * 
	 */
	public ApiRetryTaskExecutor() {
		init();
	}
	
	public synchronized void init() {
		logger.info("初始化线程池");
		if (executor == null) {
			executor = Executors.newCachedThreadPool();
		}
	}
	
	public synchronized void destroy() {
		logger.info("销毁线程池");
		if (executor != null && !executor.isShutdown()) {
			executor.shutdown();
		}
		executor = null;
	}
	
	/**
	 * 异步执行并返回Futrue对象供调用者进行后续操作
	 * @param callback
	 * @return
	 */
	public synchronized Future<Object> invokeAsync(Callable<Object> callback) {
		if (executor == null) {
			logger.info("线程池未初始化");
			return null;
		}
		if (executor.isShutdown()) {
			logger.info("线程池已被关闭");
			return null;
		}
		Future<Object> future = executor.submit(callback);
		return future;
	}
	
	/**
	 * 异步执行并支持获取返回结果
	 * @param callback
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public Object invokeAsyncWithResult(Callable<Object> callback, long timeout) throws Exception {
		Future<Object> future = this.invokeAsync(callback);
		if (future == null) {
			throw new Exception("线程池已关闭");
		}
		try {
			Object result = null;
			if (timeout == 0) {
				result = future.get();
			} else {
				result = future.get(timeout, TimeUnit.MILLISECONDS);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
