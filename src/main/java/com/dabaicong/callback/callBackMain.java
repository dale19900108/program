/**
 * Copyright(c)2015 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2015年4月13日
 */

package com.dabaicong.callback;

/**
 * <b>创建日期</b>: 2015年4月13日<br/>
 * <b>作用描述</b>: 测试回调的主函数
 * @author 刘冲
 * @mender (文件的修改者，文件创建者之外的人)
 * @version 1.0<br/>
 * Remark: 认为有必要的其他信息
 */

public class callBackMain {

	public static void main(String[] args) throws Exception {
			
		testCallback  callback = new testCallback() ; 
		callback.setGuess("test");
		//System.out.println("before call");
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		ApiRetryTaskExecutor exe = new ApiRetryTaskExecutor();
		exe.invokeAsync(callback);
		
		//System.out.println(" ready to sleep");
		Thread.sleep(3000);
		exe.destroy();
		//System.out.println(" sleep end");
			
	}
}
