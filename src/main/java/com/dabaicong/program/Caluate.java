package com.dabaicong.program;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dabaicong.jpa.util.RegexUtil;
import com.dabaicong.lottery.LotteryType;
import com.dabaicong.lottery.ZchLotteryDef;



public class Caluate
 {
	
	
	public static final String sfc = "SFC|150202303(0)=1(8.73)/2(6.98),150202304(0)=2(6.22)|2*1";
	public static final String sfd = "SFD|121208306(0)=12(50.0)|1*1";
	public static final String dxf = "DXF|121207309(+192.5)=1(1.63),121207313(+185.5)=2(1.82)|2*1";
	public static final String rfsf = "RSF|121207301(-3.5)=1(1.7),121207304(-3.5)=1(1.7),121207306(+6.5)=2(1.7),121207307(-1.5)=1(1.75),121207310(-7.5)=1(1.65),121207311(+4.5)=2(1.65),121207317(-7.5)=2(1.75),121207318(-3.5)=2(1.75)|8*1";
	public static final String lht = "LHT|130618301:01(0)=1(1.250),130618302:02(-2.50)=1(1.700)|2*1";
	public static final String lhtsfc = "LHT|150202303:02(+9.5)=1(1.84),150202304:03(0)=02(3.4)/04(7.25)|2*1]";
	public static final String dxfreal = "DXF|150202301(192.5)=2(1.75),150202303(203.5)=2(1.76),150202304(202.5)=2(1.83),150202305(198.5)=2(1.72),150202306(208.5)=2(1.67)|5*5";
	public static final String lht3003="LHT|150202303:02(+9.5)=1(1.84),150202304:03(0)=03(4.6)/13(12.5),150202305:03(0)=13(5.5),150202306:02(-13.5)=1(1.75)|4*1";
	public static final String lt3003 = "LHT|150202303:03(0)=01(7.68)/02(8.95)/03(8.76)/05(8.36)/11(4.33),150202304:04(+202.50)=1(3.83)/2(6.74)|2*1";
	public static final String rsf = "RSF|150202301(-16.50)=2(8.08)/1(9.59),150202303(+8.50)=1(4.97),150202304(-6.50)=2(5.34)/1(3.71)|3*4";
	public static final String rsf1 = "RSF|150202302(-7.50)=2(9.73)/1(1.77),150202303(+8.50)=2(6.08),150202304(-6.50)=2(8.77)|3*1";
	
	public static final String error = "LHT|150203302:02(-4.50)=1(2.63),150203304:01(0)=1(8.56)|2*1";
	public static final String error1 = "LHT|150204301:02(-3.50)=2(6.66)/1(1.63),150204302:03(0)=02(6.23)/11(5.46)|2*1";
	public static final String Error = "LHT|150212304:02(-14.50)=2(1.700)/1(1.700),150212305:01(0)=2(1.600)|2*1";


	private static String bfbetregex = "(10|20|21|30|31|32|40|41|42|50|51|52|01|02|12|03|13|23|04|14|24|05|15|25|00|11|22|33|99|09|90)";
	private static String spfregex = "[(]([013]{1}([,][013]){0,2})[)]";
	private static String bfregex = "[(]"+bfbetregex+"{1}"+"([,]"+bfbetregex+"){0,30}"+"[)]";
	private static String zjqregex = "[(][0-7]{1}([,][0-7]){0,7}[)]";
	private static String bqcregex = "[(](00|01|03|11|10|13|33|30|31){1}([,](00|01|03|11|10|13|33|30|31)){0,8}[)]";
	private static String rfspqregex = "[(]([013]{1}([,][013]){0,2})[)]";
	private static String commonregex = "[(][0123456789,]*[)]";
	private static String lotregex = "[*](3006|3007|3008|3009|3010)";
	private static String baseregex = RegexUtil.yyyymmdd+RegexUtil.matchzq+lotregex+commonregex;
	
	private static String regex = baseregex + "([|]" + baseregex+")*"+"[\\^]";
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		
		updateZcSq("15073");
		
		
		
	}	
	
	
	public static  boolean updateZcSq(String phase) {
		
		try {
			final String url = getGrabUrl("",LotteryType.ZC_SFC.value);
			Document doc = Jsoup.connect(url).timeout(4000).get();
			Elements trs = doc.select("#vsTable").select("tr");
			for (Element tr : trs) {
				if (!tr.hasAttr("data-vs")) {
					continue;
				}
				Elements tds = tr.select("td");
				
				
				String matchNum = tds.get(0).text().trim();
				Elements odds = tds.get(5).select("span");
				String oddsString = odds.get(0).text() + "_"+ odds.get(1).text() + "_" + odds.get(2).text();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}

	//	 得到足彩抓取的url
	private static String getGrabUrl(String phase,Integer lotteryType){
		//	14场次跟任选9用的是一样的对阵，所以用一个抓就行了
		if(lotteryType == LotteryType.ZC_SFC.getValue() || lotteryType == LotteryType.ZC_RJC.getValue()){
			if("".equals(phase)){
				return "http://trade.500.com/sfc";
			}
			return MessageFormat
					.format("http://trade.500.com/sfc/project_fq_fsyt.php?lotid=1&playid=1&expect={0}",
							new Object[] { phase });
		}
		return null;
	}
	
	
	
	private static  String ossd(String odds) throws Exception{
//		不包含左右括号一定有问题！
			if(!odds.contains("(")||!odds.contains(")")){
				throw new Exception("检票的时候赔率异常,赔率串为:"+odds);
			}
			
			Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
			//	正常的赔率BF-20150428001:22(50.0),21(80.0);BF-20150428002:41(19.0);BF-20150428001:22(50.0),21(80.0);BF-20150428002:41(19.0)
			String[] matchArray = odds.split(";");
			
			//	混合过关的判定
			boolean isHhgg = false ;
			Map<String, String> playTypeMap = new HashMap<String, String>();
			Set<String> valueSet = new HashSet<String>();
			for(String s:matchArray){
				String type = s.split("-")[0];
				String matchno = s.split("-")[1].substring(0,11);
				valueSet.add(type);
				playTypeMap.put(matchno, type);
			}
			if(valueSet.size()>1){
				isHhgg = true ;
			}
			
			Matcher matcher = null;
			for(String s:matchArray){
				Map<String, String> innerMap  = null ;
				String matchno = s.split("-")[1].substring(0,11);
				if(!map.containsKey(matchno)){
					innerMap = new HashMap<String, String>();
				}
				else 
					innerMap = map.get(matchno);
				String[] values = s.split(":")[1].split(",");
				for(String value : values){
					matcher = Pattern.compile("(\\d{1,2})\\(([0-9\\.]+)\\)").matcher(value);
					if(matcher.find()){
						innerMap.put(matcher.group(1), matcher.group(2));
					}
				}
				map.put(matchno, innerMap);
			}
			
			//	遍历map,然后拼接成串
			StringBuffer sb = new StringBuffer() ;
			for(String key :map.keySet()){
				sb.append(key);
				if(isHhgg){
					String playType = playTypeMap.get(key);
					sb.append(getPlayTypeString(playType));
				}
				sb.append("(");
				Map<String, String> innerMap  = map.get(key);
				//	遍历map
				for (String innerKey : innerMap.keySet()) {
				    sb.append(innerKey);
				    sb.append("_");
				    sb.append(innerMap.get(innerKey));
				    sb.append(",");
				}
				//	移除掉尾巴
				sb.delete(sb.length()-1, sb.length());
				sb.append(")");
				sb.append("|");
			}
			//	移除最后的|
			sb.delete(sb.length()-1, sb.length());
			return sb.toString();
	}
	
	private static String getPlayTypeString(String string) {
		if("BF".equals(string)){
			return "*3007";
		}else if("BQC".equals(string)){
			return "*3009";
		}else if("SPF".equals(string)){
			return "*3010";
		}else if("RQSPF".equals(string)){
			return "*3006";
		}else {
			return "*3008";
		}
	}
	
	private static String getMapperString(int i) {
		if(i==0){
			return "*3007";
		}if(i==1){
			return "*3009";
		}if(i==2){
			return "*3010";
		}if(i==3){
			return "*3006";
		}if(i==4){
			return "*3008";
		}
		return null;
	}
	
	private static Map<String, String> getInnerMap(List<Map<String, String>> playTypeList, String string) throws Exception {
		//	如果map的size为0，那么需要初始化map
		if(playTypeList.size()==0){
			Map<String, String> bfMap = new HashMap<String, String>();
			Map<String, String> bqcMap = new HashMap<String, String>();
			Map<String, String> spfMap = new HashMap<String, String>();
			Map<String, String> rqspfMap = new HashMap<String, String>();
			Map<String, String> zjqMap = new HashMap<String, String>();
			playTypeList.add(bfMap);
			playTypeList.add(bqcMap);
			playTypeList.add(spfMap);
			playTypeList.add(rqspfMap);
			playTypeList.add(zjqMap);
		}
		if("BF".equals(string)){
			return playTypeList.get(0);
		}else if("BQC".equals(string)){
			return playTypeList.get(1);
		}else if("SPF".equals(string)){
			return playTypeList.get(2);
		}else if("RQSPF".equals(string)){
			return playTypeList.get(3);
		}else if("ZJQ".equals(string)){
			return playTypeList.get(4);
		}else {
			throw new Exception("检票的时候赔率异常");
		}
	}
	
	
	
	
	
	
	public static void parseRow(int i, BigDecimal hred) {
		System.out.println("hashcode in arg :"+hred.hashCode());
		if (i==3) {
			System.out.println("-----");
			hred = hred.add(new BigDecimal("1"));
			System.out.println(hred);
			System.out.println("hashcode in function "+hred.hashCode());
		}
	}
	
	protected static boolean isMatchBetcode(String betcode) {
		System.out.println(regex);
		betcode = betcode.split("-")[1];
		System.out.println(betcode);
		if(!betcode.matches(regex)) {
			return false;
		}
		
		String[] bets = betcode.replace("^", "").split("\\|");
		
		for(String bet:bets) {
			if(String.valueOf(LotteryType.JCZQ_BF.getValue()).equals(bet.split("\\*")[1].substring(0, 4))) {
				if(!bet.split("\\*")[1].substring(4).matches(bfregex)) {
					return false;
				}
			}else if(String.valueOf(LotteryType.JCZQ_BQC.getValue()).equals(bet.split("\\*")[1].substring(0, 4))) {
				if(!bet.split("\\*")[1].substring(4).matches(bqcregex)) {
					return false;
				}
			}else if(String.valueOf(LotteryType.JCZQ_JQS.getValue()).equals(bet.split("\\*")[1].substring(0, 4))) {
				if(!bet.split("\\*")[1].substring(4).matches(zjqregex)) {
					return false;
				}
			}else if(String.valueOf(LotteryType.JCZQ_SPF.getValue()).equals(bet.split("\\*")[1].substring(0, 4))) {
				if(!bet.split("\\*")[1].substring(4).matches(rfspqregex)) {
					return false;
				}
			}else if(String.valueOf(LotteryType.JCZQ_SPF_WRQ.getValue()).equals(bet.split("\\*")[1].substring(0, 4))) {
				if(!bet.split("\\*")[1].substring(4).matches(spfregex)) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	public static String dealOdds (String odds){
		String strs[] = StringUtils.split(odds, "|");
		String dd = strs[1].replace("(", "_").replace(",", "|").replace(")", "").replace("/", ",");
		String[] dds = StringUtils.split(dd, "|");
		String result = "";
		int i = 0;
		for (String ds : dds) {
			result += "20" + ds;
			if (i != dds.length - 1) {
				result += ")|";
			}
			i++;
		}
		odds = result.replace("=", "(") + ")";
		StringBuilder stringBuilder = new StringBuilder();

		String numbers[] = StringUtils.split(odds, "|");
		if ("ZHT".equals(strs[0])) {
			int f = 0;
			for (String ss : numbers) {
				String[] s = StringUtils.split(ss.replace("-", ""), "(");
				String num = StringUtils.split(s[0], ":")[1].split("\\_")[0];
				if ("01".equals(num) || "02".equals(num) || "05".equals(num) || "03".equals(num) || "04".equals(num)) {
					stringBuilder.append(StringUtils.split(ss, ":")[0]).append("*").append(ZchLotteryDef.playTypeMapJc.get("jczq" + num)).append("(").append(s[1].replace(":", ""));
				}
				if (f != numbers.length - 1) {
					stringBuilder.append("|");
				}
				f++;
			}
		} else if ("LHT".equals(strs[0])) {
			int f = 0;
			for (String ss : numbers) {
				String[] s = StringUtils.split(ss, "(");
				String num = StringUtils.split(s[0], ":")[1].split("\\_")[0];
				stringBuilder.append(StringUtils.split(ss, ":")[0]).append("*").append(ZchLotteryDef.playTypeMapJc.get("jclq" + num)).append("(");
				if ("03".equals(num) ) {
					stringBuilder.append(s[1]);
				} else if ("01".equals(num)) {
					String odd = getOddString(ss.split("\\(")[1].replace(")", ""));
					stringBuilder.append(odd.replaceFirst("\\(", ""));
				} else if ("04".equals(num)) {
					stringBuilder.append(StringUtils.split(s[0], ":")[1].split("\\_")[1]).append(":").append(s[1]);
				} else if ("02".equals(num)){
					s = StringUtils.split(ss, "(")[0].split("\\_");
					stringBuilder.append(s[1]).append(":");
					String odd = getOddString(ss.split("\\(")[1].replace(")", "")).replace("(", "");
					stringBuilder.append(odd);
				}
				if (f != numbers.length - 1) {
					stringBuilder.append("|");
				}
				f++;
			}
		} else {
			int j = 0;
			for (String ss : numbers) {
				if ("RQS".equals(strs[0]) || "SFD".equals(strs[0]) || "SPF".equals(strs[0])) {
					String[] s = StringUtils.split(ss, "(")[0].split("\\_");
					stringBuilder.append(s[0]).append("(").append(ss.split("\\(")[1]);
				} else if ("SFC".equals(strs[0])) {
					String[] s = StringUtils.split(ss, "(")[0].split("\\_");
					stringBuilder.append(s[0]);
//					 添加赔率串信息
					stringBuilder.append(getOddString(ss.split("\\(")[1].replace(")", "")));
				} else if ("JQS".equals(strs[0]) || "CBF".equals(strs[0])) {
					stringBuilder.append(ss);
				} else if ("BQC".equals(strs[0])) {
					stringBuilder.append(ss.replace("-", ""));
				} else if ("RSF".equals(strs[0])) {
					String[] s = StringUtils.split(ss, "(")[0].split("\\_");
					stringBuilder.append(s[0]).append("(").append(s[1]).append(":");
					String odd = getOddString(ss.split("\\(")[1].replace(")", "")).replace("(", "");
					stringBuilder.append(odd);
				} else if ("DXF".equals(strs[0])) {
					String[] s = StringUtils.split(ss, "(")[0].split("\\_");
					stringBuilder.append(s[0]).append("(").append(s[1]).append(":").append(ss.split("\\(")[1]);
				}
				if (j != numbers.length - 1) {
					stringBuilder.append("|");
				}
				j++;

			}
		}

		odds = stringBuilder.toString();
		if ("CBF".equals(strs[0])) {
			odds = odds.replace(":", "");
		}
		return odds;
	}
	
	
	private static String getOddString(String context ){
		StringBuilder sb = new StringBuilder() ;
		sb.append("(");
		for(String s :context.split(",")){
			sb.append(s.split("\\_")[0].replace("2", "0").replace("1", "3"));
			sb.append("_");
			sb.append(s.split("\\_")[1]);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		return  sb.toString();
	}
	
	public static  boolean returnLong (Date start ,Date end ){
		if((start.getTime()-(end.getTime()))>=0){
			return true ;
		}
		return false ;
	}
	
	
	public static Date fun(Date  matchDate){
		if (matchDate == null) {
			return null;
		}
		Calendar cd = Calendar.getInstance();
    	cd.setTime(matchDate);
    	
    	int matchWeekday = cd.get(Calendar.DAY_OF_WEEK);
		int hourOfDay = cd.get(Calendar.HOUR_OF_DAY);
		int minute = cd.get(Calendar.MINUTE);

		Calendar endSaleCalendar = Calendar.getInstance();
		endSaleCalendar.setTime(matchDate);

		endSaleCalendar.set(Calendar.MILLISECOND, 0);
		
		if (matchWeekday == Calendar.MONDAY) {
			if (hourOfDay >= 1 && (hourOfDay < 9 || (hourOfDay == 9 && minute == 0))) {
				endSaleCalendar.set(Calendar.HOUR_OF_DAY, 1);
				endSaleCalendar.set(Calendar.MINUTE, 0);
				endSaleCalendar.set(Calendar.SECOND, 0);
			}
		} else if (matchWeekday == Calendar.TUESDAY || matchWeekday == Calendar.FRIDAY || matchWeekday == Calendar.WEDNESDAY || matchWeekday == Calendar.THURSDAY) {
			if (hourOfDay < 9 || (hourOfDay == 9 && minute == 0)) {
				endSaleCalendar.set(Calendar.HOUR_OF_DAY, 0);
				endSaleCalendar.set(Calendar.MINUTE, 0);
				endSaleCalendar.set(Calendar.SECOND, 0);
			}
		} else if (matchWeekday == Calendar.SATURDAY) {
			if (hourOfDay < 9 || (hourOfDay == 9 && minute == 0)) {
				endSaleCalendar.set(Calendar.HOUR_OF_DAY, 0);
				endSaleCalendar.set(Calendar.MINUTE, 0);
				endSaleCalendar.set(Calendar.SECOND, 0);
			}
		} else {
			if (hourOfDay >= 1 && (hourOfDay < 9 || (hourOfDay == 9 && minute == 0))) {
				endSaleCalendar.set(Calendar.HOUR_OF_DAY, 1);
				endSaleCalendar.set(Calendar.MINUTE, 0);
				endSaleCalendar.set(Calendar.SECOND, 0);
			}
		}
		return endSaleCalendar.getTime();
	}
}

