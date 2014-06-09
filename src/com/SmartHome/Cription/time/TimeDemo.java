package com.SmartHome.Cription.time;

import java.util.*;
import java.text.*;
public class TimeDemo {
	
	/*获取现在时间
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
	public static Date getNowdate(){
		Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String dateString = formatter.format(currentTime);
	    ParsePosition pos = new ParsePosition(8);
	    Date currentTime_2 = formatter.parse(dateString, pos);
	    return currentTime_2;
	}
	
	/*获取现在时间
     * @return 返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
	public static String getStringDate() {
	     Date currentTime = new Date();
	     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     String dateString = formatter.format(currentTime);
	     return dateString;
	}
	/* 输入一个时间，获取该时间的时间戳     
	 * @param @param dateString     
	 * @param @return     
	 * @param @throws ParseException     
	 */    
	public long string2Timestamp(String dateString) throws ParseException{        
		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);        
		long temp = date1.getTime();//JAVA的时间戳长度是13位        
		return temp; 
	}
	/*输入一个时间，获取与当前时间的相差秒数     
	 * @param @param dateString     
	 * @param @return     
	 */    
	public long distanceBetweenCurren(String dateString)throws ParseException{        
		Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);     
		long temp = date1.getTime();//JAVA的时间戳长度是13位        
		long current = System.currentTimeMillis();       
		return (current-temp)/1000;             
	}
	
}