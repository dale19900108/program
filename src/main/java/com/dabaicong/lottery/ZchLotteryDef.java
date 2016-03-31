package com.dabaicong.lottery;

import java.util.HashMap;
import java.util.Map;

public class ZchLotteryDef {

//	/** 彩种转换      自己规定的彩种编码    --->  出票商规定的编码 */
//	protected static Map<LotteryType, String> lotteryTypeMap = new HashMap<LotteryType, String>();
//	/** 彩种转换      出票商规定的编码     --->  自己规定的彩种编码  */
//	public static Map<String,LotteryType> toLotteryTypeMap = new HashMap<String,LotteryType>();
//	/** 彩期转换    自己规定的彩期 ---->  出票商的彩期格式*/
//	protected static Map<LotteryType, IPhaseConverter> phaseConverterMap = new HashMap<LotteryType, IPhaseConverter>();
	/** 选号方式转换（单式，复式，胆拖）*/
	protected static Map<Integer, String> playTypeMap = new HashMap<Integer, String>();
	/** 玩法编码转换（任选，和值，直选....）*/
	public static Map<Integer, String> playCodeMap = new HashMap<Integer, String>();
	public static Map<String, Integer> playTypeMapJc = new HashMap<String, Integer>();
	
	static {
		playTypeMapJc.put("jclq01", LotteryType.JCLQ_SF.getValue());
		playTypeMapJc.put("jclq02", LotteryType.JCLQ_RFSF.getValue());
		playTypeMapJc.put("jclq03", LotteryType.JCLQ_SFC.getValue());
		playTypeMapJc.put("jclq04", LotteryType.JCLQ_DXF.getValue());
		
		playTypeMapJc.put("jczq01", LotteryType.JCZQ_SPF.getValue());
		playTypeMapJc.put("jczq02", LotteryType.JCZQ_JQS.getValue());
		playTypeMapJc.put("jczq03", LotteryType.JCZQ_BQC.getValue());
		playTypeMapJc.put("jczq04", LotteryType.JCZQ_BF.getValue());
		playTypeMapJc.put("jczq05", LotteryType.JCZQ_SPF_WRQ.getValue());
	
 		
	}
	
}
