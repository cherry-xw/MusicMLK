package org.wy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MyTimeUtils {
	
	/**
	 *  ������������Ϊ   xx��xx��
	 * @param ms ������
	 * @return ������ʱ��  xx��xx��
	 */
	public static String Ms2mmss(int ms){
		try {
			Date date = new Date(ms);
			SimpleDateFormat sdf = new SimpleDateFormat("HHʱmm��ss��");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
			String hms = sdf.format(date);
			String mtime[] = hms.split("ʱ");
			if(Integer.parseInt(mtime[0]) > 0){
				return hms;
			}else{
				String mtime1[] = mtime[1].split("��");
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
