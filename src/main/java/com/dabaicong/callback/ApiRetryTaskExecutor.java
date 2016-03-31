/**
 * 
 */
package com.dabaicong.callback;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author 刘冲
 *
 */

public class ApiRetryTaskExecutor {
	
	
	private ExecutorService executor;
	
	/**
	 * 
	 */
	public ApiRetryTaskExecutor() {
		init();
	}
	
	public synchronized void init() {
		System.out.println("初始化线程池");
		if (executor == null) {
			executor = Executors.newCachedThreadPool();
		}
	}
	
	public synchronized void destroy() {
		System.out.println("销毁线程池");
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
			System.out.println("线程池未初始化");
			return null;
		}
		if (executor.isShutdown()) {
			System.out.println("线程池已被关闭");
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
