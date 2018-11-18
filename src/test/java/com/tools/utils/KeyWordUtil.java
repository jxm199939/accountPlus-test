package com.tools.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;



/**
 * @author wanglin002
 * excel里面读取关键字实现
 */
public class KeyWordUtil {
	
	
	public static int getNum(int start,int end) {
		return (int)(Math.random()*(end-start+1)+start);
		}
	//获取当前方法名
	private static String getMethodName() {  
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();  
		StackTraceElement e = stacktrace[2];  
		String methodName = e.getMethodName();  
		return methodName;  
		}
	
	/**
	 * @函数名称：getCurrentTime
	 * @功能描述：获取当前时间,精确到时分秒
	 * @return
	 */
	public static String get_time() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String timeString = dataFormat.format(date);
		return timeString;
	}
	
	/**
	 * @函数名称：getCurrentTime
	 * @功能描述：生成系统当前时间，精确到日月年
	 * @return
	 */
	public static String get_date() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String timeString = dataFormat.format(date);
		return timeString;
	}

	/**
	 * @函数名称：getRandomString
	 * @功能描述：生成自定义位数的数字字母组合的字符串
	 * @return
	 */
	public static String getRandomString(int length) { 
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * @函数名称：get_id
	 * @功能描述：随机生产18位数字，适用于随机生成订单号
	 * @return
	 */
	public static String get_id() { 
		String base = "0123456789";
		Random random = new Random();	
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 18; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}	
	

	/**
	 * @函数名称：get_mobile
	 * @功能描述：随机生成手机号
	 * @return
	 */
	public static String get_mobile() { 
		String[] telFirst="134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
		int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+third;
		
	}
	    
	/**
	 * @函数名称：get_strX
	 * @功能描述：生成指定长度字符串，其中X用你要生成长度的数字代替，适用于测试no_order的边界值
	 * @return
	 */
	public static String get_strX() { 
		
		String methodLength = getMethodName().substring(getMethodName().length()-1, getMethodName().length());
		int length = Integer.parseInt(methodLength);
		String base = "0123456789";	
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * @函数名称：get_engX
	 * @功能描述：生成指定长度全英文字符，其中X用你要生成长度的数字代替，适用于需要全英文数据场景
	 * @return
	 */
	public static String get_engX() { 
		
		String methodLength = getMethodName().substring(getMethodName().length()-1, getMethodName().length());
		int length = Integer.parseInt(methodLength);

		String base = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}


}