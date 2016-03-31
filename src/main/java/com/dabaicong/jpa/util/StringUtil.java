package com.dabaicong.jpa.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	public static String nullValue = "";

	public static boolean isEmpty(String str) {
		if (StringUtils.isEmpty(str)) {
			return true;
		}
		if ("".equals(str.trim())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 去掉开始的0
	 * @param multiple
	 * @return
	 */
	public static String removeStartZero(String string) {
		return StringUtils.removeStart(string, "0");
	}

	public static boolean isNotEmpt(String str) {
		return !isEmpty(str);
	}

	public static String join(String split, String... values) {
		StringBuilder builder = new StringBuilder();
		for (String s : values) {
			builder.append(s).append(split);
		}
		if (!isEmpty(split)) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString().trim();
	}

	public static String join(List<String> list, String split) {
		StringBuilder builder = new StringBuilder();
		for (String s : list) {
			builder.append(s).append(split);
		}
		if (!isEmpty(split)) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString().trim();
	}
	
	/**
	 * 判断是否为空
	 * @param value
	 * @return
	 */
	public static boolean isBlank(String value) {
		if (StringUtils.isBlank(value)||StringUtils.equals(value, "null")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 去掉小数点后多余的0
	 * @param value
	 * @return
	 */
	public static String subZeroAndDot(String value) {  
        if(value.indexOf(".") > 0) {  
        	value = value.replaceAll("0+?$", "");//去掉多余的0  
        	value = value.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }
        return value;  
    }
	
	/**
	 * 将注码转成数组
	 * @param code 注码
	 * @param num (1位还是2位)
	 * @return
	 */
	public static List<String> getStringArrayFromString(String code, Integer num) {
		List<String> list = new ArrayList<String>();
		try {
			int l = code.length();
			int h = l / num;
			int n = 0;
			for (int i = 0; i < h; i++) {
				String ss = StringUtils.substring(code, n, n + num);
				list.add(ss);
				n = n + num;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 将字符串数组转int数组
	 * @param list
	 * @return
	 */
	public static List<Integer> transToIntArray(List<String> list) {
		List<Integer> result = new ArrayList<Integer>();
		if (!list.isEmpty()) {
			for (String str : list) {
				result.add(Integer.parseInt(str));
			}
		}
		return result;
	}
	
	/**
	 * 将int数组拼成字符串
	 * @param list
	 * @return
	 */
	public static String joinIntArray(List<Integer> list) {
		if (!list.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (Integer integer : list) {
				builder.append(integer).append(",");
			}
			return removeEndCharacter(builder.toString(), ",");
		}
		return "";
	}
	
	/**
	 * 去掉字符串结尾的字符
	 * @param string
	 * @param endCharacter
	 * @return
	 */
	public static String removeEndCharacter(String string, String endCharacter) {
		if (StringUtils.endsWith(string, endCharacter)) {
			string = StringUtils.substring(string, 0, string.length()-endCharacter.length());
		}
		return string;
	}
	
	/*public static void main(String[] args) {
		String str = "4,5,4";
		List<Integer> list = transToIntArray(Arrays.asList(StringUtils.splitByWholeSeparator(str, ",")));
		for (Integer integer : list) {
			System.out.println(integer);
		}
		Collections.sort(list);
		System.out.println("***********");
		for (Integer integer : list) {
			System.out.println(integer);
		}
	}*/

}
