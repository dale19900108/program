/**
 * Copyright(c)2015 quanmincai Co., Ltd.
 * All right reserved.
 * 创建日期:2015年4月18日
 */

package com.dabaicong.lottery;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import java.util.Set;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dabaicong.jpa.util.DateUtil;

public class GetSpFromJingCaiWang  implements IGetSpData {
	
	private static Logger logger=LoggerFactory.getLogger(GetSpFromJingCaiWang.class);
	
	private static final String BASKETBALL_GUO_SF = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=mnl";

	private static final String BASKETBALL_GUO_RFSF = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=hdc";

	private static final String BASKETBALL_GUO_SFC = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=wnm";

	private static final String BASKETBALL_GUO_DXF = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=hilo";
	
	private static final String FOOTBALL_GUO_SPF = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=hhad&poolcode[]=had";

	private static final String FOOTBALL_GUO_BF = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=crs";

	private static final String FOOTBALL_GUO_JQS = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=ttg";
	
	private static final String FOOTBALL_GUO_BQC = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=hafu";
	
	// 比赛中玩法都不支持的常量,让球胜平负,全场比分,进球总数,半全场胜平负,胜平负,类型:01单关，02过关
//	private static Map<String, String> unsupportZqMap = new HashMap<String, String>();
//	private static Map<String, String> unsupportLqMap = new HashMap<String, String>();
//	private final static String JCLQALLNOTSUPPORT ="3001_01,3001_02,3002_01,3002_02,3003_01,3003_02,3004_01,3004_02,";
//	private final static String JCZQALLNOTSUPPORT ="3006_01,3006_02,3007_01,3007_02,3008_01,3008_02,3009_01,3009_02,3010_01,3010_02,";
//		
//	private static final String UNSALE_KEY = "cbt";
//	private static final String UNSALE_VALUE = "1";	//开售状态
//	private static final String SINGLE_KEY = "single";
//	private static final String SINGLE_VALUE = "1"; //单关不开售状态
	
	private int timeout = 30000;
	
	private static final Map<String, BigDecimal> WEEKID = new HashMap<String, BigDecimal>();

	private static final Map<Integer, Integer> WEEK = new HashMap<Integer, Integer>();
	
	
	
	static {
		WEEKID.put("周一", new BigDecimal(1));
		WEEKID.put("周二", new BigDecimal(2));
		WEEKID.put("周三", new BigDecimal(3));
		WEEKID.put("周四", new BigDecimal(4));
		WEEKID.put("周五", new BigDecimal(5));
		WEEKID.put("周六", new BigDecimal(6));
		WEEKID.put("周日", new BigDecimal(7));

		WEEK.put(1, 2);
		WEEK.put(2, 3);
		WEEK.put(3, 4);
		WEEK.put(4, 5);
		WEEK.put(5, 6);
		WEEK.put(6, 7);
		WEEK.put(7, 1);
	}
	
	public Map<String, JSONObject> getJczqStatic(IVenderConfig config) {
		Map<String, JSONObject> jczqSpMap = new HashMap<String, JSONObject>();
		try {
			getJczqBF(jczqSpMap);
			getJczqSPF(jczqSpMap);
			getJczqBQC(jczqSpMap);
			getJczqJQS(jczqSpMap);
		} catch (Exception e) {
			logger.error("竞彩足球赔率抓取出现异常,异常信息为：",e);
		}
		return jczqSpMap;
		
	}

	public Map<String, JSONObject> getJclqStatic(IVenderConfig config) {
		Map<String, JSONObject> jclqSpMap = new HashMap<String, JSONObject>();
		try {
			getJclqSFC(jclqSpMap);
			getJclqSF(jclqSpMap);
			getJclqDXF(jclqSpMap);
			getJclqRFSF(jclqSpMap);
			dealNotContainsPlayType(jclqSpMap);
			
		} catch (Exception e) {
			logger.error("竞彩篮球赔率抓取出现异常,异常信息为：",e);
		}
		return jclqSpMap;
	}


	//	竞彩足球的胜平负，让球胜平负的赔率
	private void getJczqSPF(Map<String, JSONObject> jczqSpMap) throws Exception{
		System.out.println("in #######getJczqSPF########");
		logger.info("开始获取足球胜平负过关赔率信息, url:{}", FOOTBALL_GUO_SPF );
		org.jsoup.nodes.Document doc = getJsoupDocument(FOOTBALL_GUO_SPF);
		System.out.println("#######getJczqSPF########1");
		String data = doc.select("body").first().text();
		System.out.println("#######getJczqSPF########2");
		data = data.substring(8);
		data = data.substring(0, data.length() - 2);
		System.out.println(data);
		System.out.println("#######getJczqSPF########3");
		JSONObject jsonObject = JSONObject.fromObject(data).getJSONObject("data");
		Iterator<String> keys = jsonObject.keys();
		System.out.println("#######getJczqSPF########4");
		while(keys.hasNext()) {
			String key = keys.next();
			JSONObject object = jsonObject.getJSONObject(key);
			//	周三001
			String event = trim(object.getString("num"));
			//	001
			String teamid = event.substring(2);
			//	date : 20150304
			String date = object.getString("b_date").replace("-", "");
			JSONObject array =jczqSpMap.get(date+teamid);
				
			if(object.containsKey("had")){
				//	让球胜平负的赔率json
				JSONObject spfWrqJson = object.getJSONObject("had");
				JSONObject spfWrq = new JSONObject() ;
				//	a是负，d是平，h是胜
				spfWrq.put(LotteryConstant.JCZQ_SPF_WRQ_F_VALUE, spfWrqJson.get("a"));
				spfWrq.put(LotteryConstant.JCZQ_SPF_WRQ_P_VALUE, spfWrqJson.get("d"));
				spfWrq.put(LotteryConstant.JCZQ_SPF_WRQ_S_VALUE, spfWrqJson.get("h"));
				array.put(LotteryType.JCZQ_SPF_WRQ.value+"", spfWrq);
			}else {
				JSONObject spfWrq = new JSONObject() ;
				spfWrq.put(LotteryConstant.JCZQ_SPF_WRQ_F_VALUE, "");
				spfWrq.put(LotteryConstant.JCZQ_SPF_WRQ_P_VALUE, "");
				spfWrq.put(LotteryConstant.JCZQ_SPF_WRQ_S_VALUE, "");
				array.put(LotteryType.JCZQ_SPF_WRQ.value+"", spfWrq);
			}
			
			if(object.containsKey("hhad")){	//让球胜平负的json
				
				JSONObject spfJson = object.getJSONObject("hhad");
				JSONObject spf = new JSONObject() ;
				spf.put(LotteryConstant.JCZQ_SPF_F_VALUE, spfJson.get("a"));
				spf.put(LotteryConstant.JCZQ_SPF_P_VALUE, spfJson.get("d"));
				spf.put(LotteryConstant.JCZQ_SPF_S_VALUE, spfJson.get("h"));
				spf.put(LotteryConstant.JCZQ_MONGO_SP_LETBALL,spfJson.get("fixedodds"));
				array.put(LotteryType.JCZQ_SPF.value+"", spf);
			}else {
				JSONObject spf = new JSONObject() ;
				spf.put(LotteryConstant.JCZQ_SPF_F_VALUE, "");
				spf.put(LotteryConstant.JCZQ_SPF_P_VALUE, "");
				spf.put(LotteryConstant.JCZQ_SPF_S_VALUE, "");
				spf.put(LotteryConstant.JCZQ_MONGO_SP_LETBALL,"");
				array.put(LotteryType.JCZQ_SPF.value+"", spf);
			}
			
			if(array!=null){
				jczqSpMap.put(date+teamid, array);
			}
			

		}
		System.out.println("#######getJczqSPF########");
		System.out.println(data.substring(0, 300));
	}
	//	竞彩足球的比分赔率
	@SuppressWarnings("unchecked")
	private void getJczqBF(Map<String, JSONObject> jczqSpMap) throws Exception{
		logger.info("开始获取足球比分过关赔率信息, url:{}", FOOTBALL_GUO_BF );
		org.jsoup.nodes.Document doc = getJsoupDocument(FOOTBALL_GUO_BF);
		String data = doc.select("body").first().text();
		data = data.substring(8);
		data = data.substring(0, data.length() - 2);
		JSONObject jsonObject = JSONObject.fromObject(data).getJSONObject("data");
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			
			JSONObject object = jsonObject.getJSONObject(key);
			//	周三001
			String event = trim(object.getString("num"));
			//	001
			String teamid = event.substring(2);
			//	date : 20150304
			String date = object.getString("b_date").replace("-", "");
			if(!object.containsKey("crs")){
				continue ; 
			}
			JSONObject crs = object.getJSONObject("crs");
			JSONObject bifenJson = new JSONObject();
			
			// 比分-负其它
			String sp_f_qt = crs.getString("-1-a");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_QT_VALUE, sp_f_qt);
			// 比分-平其它
			String sp_p_qt = crs.getString("-1-d");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZP_QT_VALUE, sp_p_qt);
			// 比分-胜其它
			String sp_s_qt = crs.getString("-1-h");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_QT_VALUE, sp_s_qt);
			// 比分-0:0
			String sp_00 = crs.getString("0000");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZP_0_0_VALUE, sp_00);
			// 比分-0:1
			String sp_01 = crs.getString("0001");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_0_1_VALUE, sp_01);
			// sp_02 比分-0:2
			String sp_02 = crs.getString("0002");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_0_2_VALUE, sp_02);
			// sp_03 比分-0:3
			String sp_03 = crs.getString("0003");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_0_3_VALUE, sp_03);
			// sp_04 比分-0:4
			String sp_04 = crs.getString("0004");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_0_4_VALUE, sp_04);
			// sp_05 比分-0:5
			String sp_05 = crs.getString("0005");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_0_5_VALUE, sp_05);
			// sp_10 比分-1:0
			String sp_10 = crs.getString("0100");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_1_0_VALUE, sp_10);
			// sp_11 比分-1:1
			String sp_11 = crs.getString("0101");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZP_1_1_VALUE, sp_11);
			// sp_12 比分-1:2
			String sp_12 = crs.getString("0102");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_1_2_VALUE, sp_12);
			// sp_13 比分-1:3
			String sp_13 = crs.getString("0103");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_1_3_VALUE, sp_13);
			// sp_14 比分-1:4
			String sp_14 = crs.getString("0104");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_1_4_VALUE, sp_14);
			// sp_15 比分-1:5
			String sp_15 = crs.getString("0105");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_1_5_VALUE, sp_15);
			// sp_20 比分-2:0
			String sp_20 = crs.getString("0200");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_2_0_VALUE, sp_20);
			// sp_21 比分-2:1
			String sp_21 = crs.getString("0201");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_2_1_VALUE, sp_21);
			// sp_22 比分-2:2
			String sp_22 = crs.getString("0202");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZP_2_2_VALUE, sp_22);
			// sp_23 比分-2:3
			String sp_23 = crs.getString("0203");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_2_3_VALUE, sp_23);
			// sp_24 比分-2:4
			String sp_24 = crs.getString("0204");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_2_4_VALUE, sp_24);
			// sp_25 比分-2:5
			String sp_25 = crs.getString("0205");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZF_2_5_VALUE, sp_25);
			// sp_30 比分-3:0
			String sp_30 = crs.getString("0300");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_3_0_VALUE, sp_30);
			// sp_31 比分-3:1
			String sp_31 = crs.getString("0301");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_3_1_VALUE, sp_31);
			// sp_32 比分-3:2
			String sp_32 = crs.getString("0302");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_3_2_VALUE, sp_32);
			// sp_33 比分-3:3
			String sp_33 = crs.getString("0303");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZP_3_3_VALUE, sp_33);
			// sp_40 比分-4:0
			String sp_40 = crs.getString("0400");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_4_0_VALUE, sp_40);
			// sp_41 比分-4:1
			String sp_41 = crs.getString("0401");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_4_1_VALUE, sp_41);
			// sp_42 比分-4:2
			String sp_42 = crs.getString("0402");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_4_2_VALUE, sp_42);
			// sp_50 比分-5:0
			String sp_50 = crs.getString("0500");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_5_0_VALUE, sp_50);
			// sp_51 比分-5:1
			String sp_51 = crs.getString("0501");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_5_1_VALUE, sp_51);
			// sp_52 比分-5:2
			String sp_52 = crs.getString("0502");
			bifenJson.put(LotteryConstant.JCZQ_BF_ZS_5_2_VALUE, sp_52);
			
			
			JSONObject array = new JSONObject();
			array.put("matchNum", date + teamid);
			array.put(LotteryType.JCZQ_BF.value+"", bifenJson);
			jczqSpMap.put(date+teamid, array);
		}
		System.out.println("#######getJczqBF########");
		System.out.println(data.substring(0,200));
	}
	
	//	竞彩足球的半全场赔率
	@SuppressWarnings("unchecked")
	private void getJczqBQC(Map<String, JSONObject> jczqSpMap) throws Exception{
		logger.info("开始获取足球比分过关赔率信息, url:{}", FOOTBALL_GUO_BQC );
		org.jsoup.nodes.Document doc = getJsoupDocument(FOOTBALL_GUO_BQC);
		String data = doc.select("body").first().text();
		data = data.substring(8);
		data = data.substring(0, data.length() - 2);
		JSONObject jsonObject = JSONObject.fromObject(data).getJSONObject("data");
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			
			JSONObject object = jsonObject.getJSONObject(key);
			//	周三001
			String event = trim(object.getString("num"));
			//	001
			String teamid = event.substring(2);
			//	date : 20150304
			String date = object.getString("b_date").replace("-", "");
			if(!object.containsKey("hafu")){
				continue ; 
			}
			JSONObject hafu = object.getJSONObject("hafu");
			JSONObject bqcJson = new JSONObject();
			// 半场胜平负-负负
			String hc_ff = hafu.getString("aa");
			bqcJson.put(LotteryConstant.JCZQ_BQC_FF_VALUE, hc_ff);
			// 半场胜平负-负平
			String hc_fp = hafu.getString("ad");
			bqcJson.put(LotteryConstant.JCZQ_BQC_FP_VALUE, hc_fp);
			// 半场胜平负-负胜
			String hc_fs = hafu.getString("ah");
			bqcJson.put(LotteryConstant.JCZQ_BQC_FS_VALUE, hc_fs);
			// 半场胜平负-平负
			String hc_pf = hafu.getString("da");
			bqcJson.put(LotteryConstant.JCZQ_BQC_PF_VALUE, hc_pf);
			// 半场胜平负-平平
			String hc_pp = hafu.getString("dd");
			bqcJson.put(LotteryConstant.JCZQ_BQC_PP_VALUE, hc_pp);
			// 半场胜平负-平胜
			String hc_ps = hafu.getString("dh");
			bqcJson.put(LotteryConstant.JCZQ_BQC_PS_VALUE, hc_ps);
			// 半场胜平负-胜负
			String hc_sf = hafu.getString("ha");
			bqcJson.put(LotteryConstant.JCZQ_BQC_SF_VALUE, hc_sf);
			// 半场胜平负-胜平
			String hc_sp = hafu.getString("hd");
			bqcJson.put(LotteryConstant.JCZQ_BQC_SP_VALUE, hc_sp);
			// 半场胜平负-胜胜
			String hc_ss = hafu.getString("hh");
			bqcJson.put(LotteryConstant.JCZQ_BQC_SS_VALUE, hc_ss);
			
			JSONObject array =jczqSpMap.get(date+teamid);
					
			if(array!=null){
				array.put(LotteryType.JCZQ_BQC.value+"", bqcJson);
				jczqSpMap.put(date+teamid, array);
			}
			
		}
//		System.out.println("#######getJczqBQC########");
//		System.out.println(data.substring(0,200));
	}
	//	竞彩足球的总进球赔率
	@SuppressWarnings("unchecked")
	private void getJczqJQS(Map<String, JSONObject> jczqSpMap) throws Exception{
		logger.info("开始获取足球比分过关赔率信息, url:{}", FOOTBALL_GUO_JQS );
		org.jsoup.nodes.Document doc = getJsoupDocument(FOOTBALL_GUO_JQS);
		String data = doc.select("body").first().text();
		data = data.substring(8);
		data = data.substring(0, data.length() - 2);
		JSONObject jsonObject = JSONObject.fromObject(data).getJSONObject("data");
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			
			JSONObject object = jsonObject.getJSONObject(key);
			//	周三001
			String event = trim(object.getString("num"));
			//	001
			String teamid = event.substring(2);
			//	date : 20150304
			String date = object.getString("b_date").replace("-", "");
			if(!object.containsKey("ttg")){
				continue ; 
			}
			JSONObject ttg = object.getJSONObject("ttg");
			JSONObject jqsJson = new JSONObject();
			
			// 总进球-进0个
			String tg_0 = ttg.getString("s0");
			jqsJson.put(LotteryConstant.JCZQ_JQS_0_VALUE, tg_0);
			// 总进球-进1个
			String tg_1 = ttg.getString("s1");
			jqsJson.put(LotteryConstant.JCZQ_JQS_1_VALUE, tg_1);
			// 总进球-进2个
			String tg_2 = ttg.getString("s2");
			jqsJson.put(LotteryConstant.JCZQ_JQS_2_VALUE, tg_2);
			// 总进球-进3个
			String tg_3 = ttg.getString("s3");
			jqsJson.put(LotteryConstant.JCZQ_JQS_3_VALUE, tg_3);
			// 总进球-进4个
			String tg_4 = ttg.getString("s4");
			jqsJson.put(LotteryConstant.JCZQ_JQS_4_VALUE, tg_4);
			// 总进球-进5个
			String tg_5 = ttg.getString("s5");
			jqsJson.put(LotteryConstant.JCZQ_JQS_5_VALUE, tg_5);
			// 总进球-进6个
			String tg_6 = ttg.getString("s6");
			jqsJson.put(LotteryConstant.JCZQ_JQS_6_VALUE, tg_6);
			// 总进球-进7+个
			String tg_7 = ttg.getString("s7");
			jqsJson.put(LotteryConstant.JCZQ_JQS_7_VALUE, tg_7);
			
			JSONObject array =jczqSpMap.get(date+teamid);
				
			if(array!=null){
				array.put(LotteryType.JCZQ_JQS.value+"", jqsJson);
				jczqSpMap.put(date+teamid, array);
			}
		}
//		System.out.println("#######getJczqJQS########");
//		System.out.println(data.substring(0,200));
	}
	
	//	竞彩篮球胜负
	private void getJclqSF(Map<String, JSONObject> jclqSpMap) throws Exception{
		
		logger.info("开始获取篮球胜负过关赔率赔率信息, url:{}",  BASKETBALL_GUO_SF);
		org.jsoup.nodes.Document doc = getJsoupDocument(BASKETBALL_GUO_SF);
		
		String data = doc.select("body").first().text();
		data = data.substring(8);
		data = data.substring(0, data.length() - 2);
		JSONObject jsonObject = JSONObject.fromObject(data).getJSONObject("data");
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			
			JSONObject object = jsonObject.getJSONObject(key);
			//	周三001
			String event = trim(object.getString("num"));
			//	001
			String teamid = event.substring(2);
			//	date : 20150304
			String date = object.getString("b_date").replace("-", "");
			JSONObject array =jclqSpMap.get(date+teamid);
			if(array == null){
				array = new JSONObject();
			}
			
			if(!object.containsKey("mnl")){
				continue ; 
			}
			JSONObject mnl = object.getJSONObject("mnl");
			//	赔率的字段信息
			JSONObject sf = new JSONObject() ;
			sf.put(LotteryConstant.JCLQ_SF_S_VALUE, mnl.get("h"));
			sf.put(LotteryConstant.JCLQ_SF_F_VALUE, mnl.get("a"));
			
			array.put("matchNum", date + teamid);
			array.put(LotteryType.JCLQ_SF.value+"", sf);
			jclqSpMap.put(date+teamid, array);
			
//			dealUnsupportMap(date+teamid,mnl,LotteryType.JCLQ_SF.value,unsupportLqMap);
		}
		
	}
	

	//	竞彩篮球胜负
	private void getJclqRFSF(Map<String, JSONObject> jclqSpMap) throws Exception{

		logger.info("开始获取篮球让分胜负过关赔率赔率信息, url:{}",  BASKETBALL_GUO_RFSF);
		org.jsoup.nodes.Document doc = getJsoupDocument(BASKETBALL_GUO_RFSF);
		
		String data = doc.select("body").first().text();
		data = data.substring(8);
		data = data.substring(0, data.length() - 2);
		JSONObject jsonObject = JSONObject.fromObject(data).getJSONObject("data");
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			
			JSONObject object = jsonObject.getJSONObject(key);
			//	周三001
			String event = trim(object.getString("num"));
			//	001
			String teamid = event.substring(2);
			//	date : 20150304
			String date = object.getString("b_date").replace("-", "");
			JSONObject array =jclqSpMap.get(date+teamid);
			if(array == null){
				array = new JSONObject();
			}
			if(!object.containsKey("hdc")){
				continue ; 
			}
			JSONObject hdc = object.getJSONObject("hdc");
			//	赔率的字段信息
			JSONObject rfsf = new JSONObject() ;
			rfsf.put(LotteryConstant.JCLQ_RFSF_F_VALUE, hdc.get("a"));
			rfsf.put(LotteryConstant.JCLQ_RFSF_S_VALUE, hdc.get("h"));
			rfsf.put(LotteryConstant.JCLQ_RFSF_HANDICAP,hdc.getString("fixedodds"));
			
			array.put("matchNum", date + teamid);
			array.put(LotteryType.JCLQ_RFSF.value+"", rfsf);
			jclqSpMap.put(date+teamid, array);
			
//			dealUnsupportMap(date+teamid,hdc,LotteryType.JCLQ_RFSF.value,unsupportLqMap);
			
		}
		
		
	}
	
	//	竞彩篮球大小分
	private void getJclqDXF(Map<String, JSONObject> jclqSpMap) throws Exception{

		logger.info("开始获取篮球大小分过关赔率赔率信息, url:{}",  BASKETBALL_GUO_DXF);
		org.jsoup.nodes.Document doc = getJsoupDocument(BASKETBALL_GUO_DXF);
		
		String data = doc.select("body").first().text();
		data = data.substring(8);
		data = data.substring(0, data.length() - 2);
		JSONObject jsonObject = JSONObject.fromObject(data).getJSONObject("data");
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			
			JSONObject object = jsonObject.getJSONObject(key);
			//	周三001
			String event = trim(object.getString("num"));
			//	001
			String teamid = event.substring(2);
			//	date : 20150304
			String date = object.getString("b_date").replace("-", "");
			JSONObject array =jclqSpMap.get(date+teamid);
			if(array == null){
				array = new JSONObject();
			}
			if(!object.containsKey("hilo")){
				continue ; 
			}
			JSONObject hilo = object.getJSONObject("hilo");
			//	赔率的字段信息
			JSONObject dxf = new JSONObject() ;
			dxf.put(LotteryConstant.JCLQ_DXF_LARGE, hilo.getString("h"));
			dxf.put(LotteryConstant.JCLQ_DXF_SMALL, hilo.getString("l"));
			dxf.put(LotteryConstant.JCLQ_DXF_PRESETSCORE, hilo.getString("fixedodds").substring(1));
			
			array.put("matchNum", date + teamid);
			array.put(LotteryType.JCLQ_DXF.value+"", dxf);
			jclqSpMap.put(date+teamid, array);
			
//			dealUnsupportMap(date+teamid,hilo,LotteryType.JCLQ_DXF.value,unsupportLqMap);
			
		}
		
	}
	
	//	竞彩篮球胜分差
	private void getJclqSFC(Map<String, JSONObject> jclqSpMap) throws Exception{
		
		logger.info("开始获取篮球胜分差赔率信息, url:{}", BASKETBALL_GUO_SFC );
		org.jsoup.nodes.Document doc = getJsoupDocument(BASKETBALL_GUO_SFC);
		String data = doc.select("body").first().text();
		data = data.substring(8);
		data = data.substring(0, data.length() - 2);
		JSONObject jsonObject = JSONObject.fromObject(data).getJSONObject("data");
		Iterator<String> keys = jsonObject.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			
			JSONObject object = jsonObject.getJSONObject(key);
			//	周三001
			String event = trim(object.getString("num"));
			//	001
			String teamid = event.substring(2);
			//	date : 20150304
			String date = object.getString("b_date").replace("-", "");
			JSONObject array = new JSONObject() ;
			
			if(!object.containsKey("wnm")){
				continue ; 
			}
			JSONObject wnm = object.getJSONObject("wnm");
			//	赔率的字段信息
			JSONObject sfc = new JSONObject() ;
			//	胜差分-主胜1~5分
			sfc.put(LotteryConstant.JCLQ_SFC_H_1_5_VALUE, wnm.get("w1"));
			//	胜差分-主胜6~10分
			sfc.put(LotteryConstant.JCLQ_SFC_H_6_10_VALUE, wnm.get("w2"));
			//	胜差分-主胜11~15分
			sfc.put(LotteryConstant.JCLQ_SFC_H_11_15_VALUE, wnm.get("w3"));
			//	胜差分-主胜16~20分
			sfc.put(LotteryConstant.JCLQ_SFC_H_16_20_VALUE, wnm.get("w4"));
			//	胜差分-主胜21~25分
			sfc.put(LotteryConstant.JCLQ_SFC_H_21_25_VALUE, wnm.get("w5"));
			//	胜差分-主胜26分以上
			sfc.put(LotteryConstant.JCLQ_SFC_H_26_PLUS_VALUE, wnm.get("w6"));
			//	胜差分-客胜1~5分
			sfc.put(LotteryConstant.JCLQ_SFC_A_1_5_VALUE, wnm.get("l1"));
			//	胜差分-客胜6~10分
			sfc.put(LotteryConstant.JCLQ_SFC_A_6_10_VALUE, wnm.get("l2"));
			//	胜差分-客胜11~15分
			sfc.put(LotteryConstant.JCLQ_SFC_A_11_15_VALUE, wnm.get("l3"));
			//	胜差分-客胜16~20分
			sfc.put(LotteryConstant.JCLQ_SFC_A_16_20_VALUE, wnm.get("l4"));
			//	胜差分-客胜21~25分
			sfc.put(LotteryConstant.JCLQ_SFC_A_21_25_VALUE, wnm.get("l5"));
			//	胜差分-客胜26分以上
			sfc.put(LotteryConstant.JCLQ_SFC_A_26_PLUS_VALUE, wnm.get("l6"));
			
			array.put("matchNum", date + teamid);
			array.put(LotteryType.JCLQ_SFC.value+"", sfc);
			jclqSpMap.put(date+teamid, array);
			
//			dealUnsupportMap(date+teamid,wnm,LotteryType.JCLQ_SFC.value,unsupportLqMap);
			
		}
	
		
	}
	
	private org.jsoup.nodes.Document getJsoupDocument(String url)
			throws Exception {
		return org.jsoup.Jsoup.connect(url).timeout(timeout).get();
	}
	
	public String getDay(int weekid, Date time) {
		while(WEEK.get(weekid) != getWeekid(time)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			calendar.add(Calendar.DAY_OF_WEEK, -1);
			time = calendar.getTime();
		}
		
		return DateUtil.format("yyyyMMdd", time);
	}
	
	public int getWeekid(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	private String trim(String content) {
		content = content.replaceAll("\\s*", "");
		content = content.replaceAll(" *", "");
		return content;
	}

	//	这里处理下没有包含玩法的场次
	private void dealNotContainsPlayType(Map<String, JSONObject> jclqSpMap) {
		for(String id:jclqSpMap.keySet()){
			JSONObject object = jclqSpMap.get(id);
			if(object.get(LotteryType.JCLQ_DXF.value+"")==null){
				JSONObject dxf = new JSONObject() ;
				dxf.put(LotteryConstant.JCLQ_DXF_SMALL, "");
				dxf.put(LotteryConstant.JCLQ_DXF_LARGE, "");
				dxf.put(LotteryConstant.JCLQ_DXF_PRESETSCORE,"");
				object.put(LotteryType.JCLQ_DXF.value+"", dxf);
				jclqSpMap.put(id, object);
			}
			if(object.get(LotteryType.JCLQ_RFSF.value+"")==null){
				JSONObject rfsf = new JSONObject() ;
				rfsf.put(LotteryConstant.JCLQ_RFSF_S_VALUE, "");
				rfsf.put(LotteryConstant.JCLQ_RFSF_F_VALUE, "");
				rfsf.put(LotteryConstant.JCLQ_RFSF_HANDICAP,"");
				object.put(LotteryType.JCLQ_RFSF.value+"", rfsf);
				jclqSpMap.put(id, object);
			}
			if(object.get(LotteryType.JCLQ_SF.value+"")==null){
				JSONObject sf = new JSONObject() ;
				sf.put(LotteryConstant.JCLQ_SF_S_VALUE, "");
				sf.put(LotteryConstant.JCLQ_SF_F_VALUE, "");
				object.put(LotteryType.JCLQ_SF.value+"", sf);
				jclqSpMap.put(id, object);
			}
			if(object.get(LotteryType.JCLQ_SFC.value+"")==null){
				JSONObject sfc = new JSONObject() ;
				//	胜差分-主胜1~5分
				sfc.put(LotteryConstant.JCLQ_SFC_H_1_5_VALUE, "");
				//	胜差分-主胜6~10分
				sfc.put(LotteryConstant.JCLQ_SFC_H_6_10_VALUE, "");
				//	胜差分-主胜11~15分
				sfc.put(LotteryConstant.JCLQ_SFC_H_11_15_VALUE, "");
				//	胜差分-主胜16~20分
				sfc.put(LotteryConstant.JCLQ_SFC_H_16_20_VALUE, "");
				//	胜差分-主胜21~25分
				sfc.put(LotteryConstant.JCLQ_SFC_H_21_25_VALUE, "");
				//	胜差分-主胜26分以上
				sfc.put(LotteryConstant.JCLQ_SFC_H_26_PLUS_VALUE, "");
				//	胜差分-客胜1~5分
				sfc.put(LotteryConstant.JCLQ_SFC_A_1_5_VALUE, "");
				//	胜差分-客胜6~10分
				sfc.put(LotteryConstant.JCLQ_SFC_A_6_10_VALUE, "");
				//	胜差分-客胜11~15分
				sfc.put(LotteryConstant.JCLQ_SFC_A_11_15_VALUE, "");
				//	胜差分-客胜16~20分
				sfc.put(LotteryConstant.JCLQ_SFC_A_16_20_VALUE, "");
				//	胜差分-客胜21~25分
				sfc.put(LotteryConstant.JCLQ_SFC_A_21_25_VALUE, "");
				//	胜差分-客胜26分以上
				sfc.put(LotteryConstant.JCLQ_SFC_A_26_PLUS_VALUE, "");
				object.put(LotteryType.JCLQ_SFC.value+"", sfc);
				jclqSpMap.put(id, object);
			}
			
		}
		
	}
	
//	public void dealUnsupportMap(String matchNo,JSONObject json,int type,Map<String, String> map){
//		String unsupport = unsupportLqMap.get(matchNo);
//		if(unsupport==null){
//			unsupport = JCLQALLNOTSUPPORT;
//			map.put(matchNo, unsupport);
//		}
//		
//		// 如果是正常开售状态，不是置灰色
//		if(json.getString(UNSALE_KEY).equals(UNSALE_VALUE)){
//			unsupport = unsupport.replace(type+"_02,", "");
//			// 如果单关开售
//			if(json.getString(SINGLE_KEY).equals(SINGLE_VALUE)){
//				unsupport = unsupport.replace(type+"_01,", "");
//			}
//		}else{
//			unsupport = unsupport.replace(type+"_02,", "");
//			unsupport = unsupport.replace(type+"_01,", "");
//		}
//		map.put(matchNo, unsupport);
//	}
	
	/**
	 * 功       能: 初始化不支持的玩法map<br/>
	 * 作       者: 刘冲<br/>
	 * 创建日期: 2015年4月13日<br/>
	 * 修  改  者: mender<br/>
	 * 修改日期: modifydate<br/>
	 */
//	private void initLqUnsupportMap() {
//		unsupportLqMap = new HashMap<String, String>();
//	}
	
//	private void fixUnsupport(Map<String, String> map,int type){
//		removeEndChar(map);
//		FixUnsupportRace callback = new FixUnsupportRace();
//		callback.setJclqService(jclqService);
//		callback.setJczqService(jczqService);
//		callback.setUnsupportMap(map);
//		callback.setType(type);
//		callback.setCacheService(cacheService);
//		apiReryTaskExecutor.invokeAsync(callback);
//	}
//	private void removeEndChar(Map<String, String> map){
//		for (String key : map.keySet()) {  
//		    map.put(key, StringUtil.removeEndCharacter(map.get(key), ","));
//		}  
//	}

	public static void main(String[] args) throws Exception {
		GetSpFromJingCaiWang sp = new GetSpFromJingCaiWang() ;
		long start = System.currentTimeMillis();
	
//		Map<String, JSONObject> jczqSpMap = new HashMap<String, JSONObject>();
//		System.out.println("===========================");
//		System.out.println();
//		
//		jczqSpMap = new HashMap<String, JSONObject>();
//		sp.getJczqBF(jczqSpMap);
//		System.out.println(jczqSpMap);
//		
//		System.out.println("===========================");
//		System.out.println();
//		jczqSpMap = new HashMap<String, JSONObject>();
//		sp.getJczqBQC(jczqSpMap);
//		System.out.println(jczqSpMap);
//		
//		System.out.println("===========================");
//		System.out.println();
//		jczqSpMap = new HashMap<String, JSONObject>();
//		sp.getJczqJQS(jczqSpMap);
//		System.out.println(jczqSpMap);
//		
//		System.out.println("===========================");
//		System.out.println();
//		jczqSpMap = new HashMap<String, JSONObject>();
//		sp.getJczqSPF(jczqSpMap);
//		System.out.println(jczqSpMap);
//		
//		System.out.println("===========================");
//		System.out.println();
		System.out.println();System.out.println();
		Map<String, JSONObject> map = sp.getJczqStatic(null);
		System.out.println(map.get("20150418091").getString("3008"));
//		System.out.println();
//
//		System.out.println(map.size());
//		System.out.println();

		Set<String> set  = map.keySet();
//		for(String key :set){
//			System.out.println(map.get(key));
//		}
		long end = System.currentTimeMillis();	
		System.out.println();
		
		System.out.println(map.size());
		System.out.println("haoshi:"+(end-start)+"ms");
		
	}

}
