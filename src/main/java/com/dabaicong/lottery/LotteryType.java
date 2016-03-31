package com.dabaicong.lottery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum LotteryType {
	
	/**
	 * 彩种规则：福彩以10开头，四位数字
	 * 体彩以20开头，四位数字
	 * 竞彩以30开头
	 * */
	SSQ(1001,"双色球"),
	F3D(1002,"福彩3D"),
	QLC(1003,"七乐彩"),
	TJKL10(1004,"天津快乐十分"),
	NXKUAI3(1005,"宁夏快三"),
	JIANGSUKUAI3(1009,"江苏快三"),
	ANHUIKUAI3(1008,"安徽快三"),
	CQSSC(1006,"重庆时时彩"),
	
	CJDLT(2001,"超级大乐透"),
	PL3(2002,"排列三"),
	PL5(2003,"排列五"),
	QXC(2004,"七星彩"),
	
	JX_11X5(2006,"江西11选5"),
	SD_11X5(2007,"山东11选5"),
	SD_PK3(2009,"山东快乐扑克"),
	
	JCLQ_SF(3001,"竞彩篮球胜负"),
	JCLQ_RFSF(3002,"竞彩篮球让分胜负"),
	JCLQ_SFC(3003,"竞彩篮球胜分差"),
	JCLQ_DXF(3004,"竞彩篮球大小分"),
	JCLQ_HHGG(3005,"竞彩篮球混合过关"),
	
	JCZQ_SPF(3006,"竞彩足球让球胜平负"),
	JCZQ_BF(3007,"竞彩足球全场比分"),
	JCZQ_JQS(3008,"竞彩足球进球数"),
	JCZQ_BQC(3009,"竞彩足球半全场胜平负"),
	JCZQ_SPF_WRQ(3010,"竞彩足球胜平负"),
	JCZQ_HHGG(3011,"竞彩足球混合过关"),
	
	ZC_SFC(4001,"足彩胜负彩"),
	ZC_RJC(4002,"足彩任九场"),
	ZC_JQC(4003,"足彩4场进球彩"),
	ZC_BQC(4004,"足彩6场半全场"),
	
	DC_SPF(5001,"北单胜平负"),
	DC_ZJQ(5002,"北单总进球"),
	DC_BQC(5003,"北单半全场"),
	DC_SXDS(5004,"北单上下盘单双"),
	DC_BF(5005,"北单比分"),
	DC_SF(5006,"北单胜负"),
	
	DEFAULT(0,"全部");
	
	public int value;
	
	public String name;
	
	LotteryType(int value,String name){
		this.value=value;
		this.name=name;
	}

	public static LotteryType getLotteryType(int value){
		LotteryType[] lotteryType=LotteryType.values();
		for(LotteryType type:lotteryType){
			if(type.value==value){
				return type;
			}
		}
		return null;
	}

	public int getValue() {
		return value;
	}
	 @Override
	 public String toString(){  //自定义的public方法   
	 return "[value:"+value+",name:"+name+"]";   
	 }   

	public String getName() {
		return name;
	}
	
	public static List<LotteryType> get(){//获取所有的值
		return Arrays.asList(LotteryType.values());
	}
	
	public static List<LotteryType> getGaoPin(){
		List<LotteryType> typeList=new ArrayList<LotteryType>();
		typeList.add(LotteryType.JIANGSUKUAI3);
		typeList.add(LotteryType.ANHUIKUAI3);
		typeList.add(LotteryType.TJKL10);
		typeList.add(LotteryType.JX_11X5);
		typeList.add(LotteryType.SD_11X5);
		typeList.add(LotteryType.SD_PK3);
		return typeList;
	}
	
	public static List<LotteryType> getJclq(){
		List<LotteryType> typeList=new ArrayList<LotteryType>();
		typeList.add(LotteryType.JCLQ_DXF);
		typeList.add(LotteryType.JCLQ_HHGG);
		typeList.add(LotteryType.JCLQ_RFSF);
		typeList.add(LotteryType.JCLQ_SF);
		typeList.add(LotteryType.JCLQ_SFC);
		return typeList;
		
	}
	public static List<LotteryType> getZc(){
		List<LotteryType> typeList=new ArrayList<LotteryType>();
		typeList.add(LotteryType.ZC_SFC);
		typeList.add(LotteryType.ZC_BQC);
		typeList.add(LotteryType.ZC_JQC);
		typeList.add(LotteryType.ZC_RJC);
		return typeList;
		
	}
	
	public static LotteryType getPhaseType(int value){
		LotteryType lotteryType= getLotteryType(value);
		return getPhaseType(lotteryType);
	}
	
	public static int getPhaseTypeValue(int value){
		return getPhaseType(value).getValue();
	}
	public static LotteryType getPhaseType(LotteryType lotteryType){
			if(getJclq().contains(lotteryType)){
				return LotteryType.JCLQ_SF;
			}
			if(getJczq().contains(lotteryType)){
				return LotteryType.JCZQ_SPF;
			}
			if(getDc().contains(lotteryType)){
				return LotteryType.DC_SPF;
			}
			return lotteryType;
		}
	public static List<LotteryType> getJczq(){
		List<LotteryType> typeList=new ArrayList<LotteryType>();
		typeList.add(LotteryType.JCZQ_BF);
		typeList.add(LotteryType.JCZQ_BQC);
		typeList.add(LotteryType.JCZQ_HHGG);
		typeList.add(LotteryType.JCZQ_JQS);
		typeList.add(LotteryType.JCZQ_SPF);
		typeList.add(LotteryType.JCZQ_SPF_WRQ);
		return typeList;
		
	}
	public static List<LotteryType> getDc(){
		List<LotteryType> typeList=new ArrayList<LotteryType>();
		typeList.add(LotteryType.DC_SPF);
		typeList.add(LotteryType.DC_ZJQ);
		typeList.add(LotteryType.DC_BQC);
		typeList.add(LotteryType.DC_SXDS);
		typeList.add(LotteryType.DC_BF);
		typeList.add(LotteryType.DC_SF);
		return typeList;
	}
	/**
	 * 期次时间会动态变化，需要修正的彩期列表
	 * @return
	 */
	public static List<LotteryType> getDynamicPhase(){
		List<LotteryType> typeList=new ArrayList<LotteryType>();
		typeList.add(LotteryType.JIANGSUKUAI3);
		typeList.add(LotteryType.ANHUIKUAI3);
		return typeList;
	}
	/**
	 * 需要生成预开期的彩种
	 * @return
	 */
	public static List<LotteryType> getCheckPhase() {
		List<LotteryType> typeList=new ArrayList<LotteryType>();
		typeList.add(LotteryType.SSQ); //双色球
		typeList.add(LotteryType.JX_11X5); //江西11选5
		typeList.add(LotteryType.SD_11X5); //山东11选5
		typeList.add(LotteryType.JIANGSUKUAI3);//江苏快三
		typeList.add(LotteryType.ANHUIKUAI3);//安徽快三
		typeList.add(LotteryType.CJDLT);//超级大乐透
		typeList.add(LotteryType.SD_PK3);  // 山东快乐扑克
		return typeList;
	}
	
}
