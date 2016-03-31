package com.dabaicong.program;

import java.util.Calendar;
import java.util.Date;

public class Test {	
	
	private static String FIXURL = "http://www.bjlot.com/data/61-63/html/2016/matchinfo_{phase}.xml?{time} (中国标准时间)&_={timestamp}";
	
	
	public static void main(String[] args) {
		Date nowTime = new Date(); // 要转换的时间
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(nowTime.getTime()); 
        FIXURL = FIXURL.replace("{phase}", 150015+"")
        		.replace("{time}", cal.getTime().toString().substring(0, 19))
        		.replace("{timestamp}", cal.getTimeInMillis()+"");
        System.out.println(FIXURL);
	}
	
	
}	
