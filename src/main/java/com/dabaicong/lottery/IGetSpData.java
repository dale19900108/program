/**
 * Copyright(c)2015 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2015年4月18日
 */

package com.dabaicong.lottery;


import java.util.Map;
import net.sf.json.JSONObject;

public interface IGetSpData {
	/**
	 * 竞彩足球
	 * @param url
	 * @return
	 */
	public Map<String, JSONObject> getJczqStatic(IVenderConfig config);
	
	/**
	 * 竞彩足球 动态sp
	 * 单关的赔率已经没有了
	 * @param url
	 * @return
	 */
//	public Map<String, JSONObject> getJczqDynamic(IVenderConfig config);
	
	/**
	 * 竞彩篮球
	 * @param url
	 * @return
	 */
	public Map<String, JSONObject> getJclqStatic(IVenderConfig config);
	
	/**
	 * 竞彩篮球 动态sp
	 * @param url
	 * @return
	 */
//	public Map<String, JSONObject> getJclqDynamic(IVenderConfig config);

}