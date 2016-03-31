/**
 * Copyright(c)2015 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2015年4月13日
 */

package com.dabaicong.callback;

import java.util.concurrent.Callable;

/**
 * <b>创建日期</b>: 2015年4月13日<br/>
 * <b>作用描述</b>: 对本文件的详细描述，原则上不能少于50字
 * @author 刘冲
 * @mender (文件的修改者，文件创建者之外的人)
 * @version 1.0<br/>
 * Remark: 认为有必要的其他信息
 */

public class testCallback  implements Callable<Object>{
	
	private String guess  ;

	public String getGuess() {
		return guess;
	}

	public void setGuess(String guess) {
		this.guess = guess;
	}




	/**
	 * 功       能: <br/>
	 * 作       者: 刘冲<br/>
	 * 创建日期: 2015年4月13日<br/>
	 * 修  改  者: mender<br/>
	 * 修改日期: modifydate<br/>
	 * @return
	 * @throws Exception
	 * @see java.util.concurrent.Callable#call()
	 * 
	 */
	public Object call() throws Exception {
		System.out.println(guess+this.getClass().toString()+" ready to sleep");
		Thread.sleep(1000);
		System.out.println(guess+this.getClass().toString()+" sleep end");
		return null ;
	}

	
}
