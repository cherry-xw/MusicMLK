package org.wy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MyTimeUtils {
	
	/**
	 *  将毫秒数处理为   xx分xx秒
	 * @param ms 毫秒数
	 * @return 处理后的时间  xx分xx秒
	 */
	public static String Ms2mmss(int ms){
		try {
			Date date = new Date(ms);
			SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分ss秒");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
			String hms = sdf.format(date);
			String mtime[] = hms.split("时");
			if(Integer.parseInt(mtime[0]) > 0){
				return hms;
			}else{
				String mtime1[] = mtime[1].split("分");
				if(Integer.parseInt(mtime1[0]) > 0){
					return mtime[1];
				}else{
					return mtime1[1];
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "error";
		}
	}
}
