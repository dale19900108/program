package com.dabaicong.designModel.proxy;

public interface IProxy {
	
	// 得到对象
	public Object  getObject () ;
	
	// 得到之前的处理方法
	public void beforeGet() ;
	
	// 得到之后的
	public void afterGet();

}
