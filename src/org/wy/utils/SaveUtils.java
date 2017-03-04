package org.wy.utils;

import org.wy.activity.MainActivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveUtils {
	public static SharedPreferences sp = null;

	/**
	 * 保存主界面背景信息
	 * @param backimgresid
	 */
	public static void setMainBackData(int backimgresid){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putInt("backimgresid", backimgresid);
			editor.commit();
		}
	}
	/**
	 *  获取主界面背景界面
	 * @return 背景id
	 */
	public static int getMainBackData(){
		if(sp != null){
			return sp.getInt("backimgresid", 0);
		}
		return 0;
	}
	/**
	 * 保存波形背景信息
	 * @param backimgresid
	 */
	public static void setWaveBackData(int backimgresid){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putInt("wavebackimgresid", backimgresid);
			editor.commit();
		}
	}
	/**
	 *  获取波形背景界面
	 * @return 背景id
	 */
	public static int getWaveBackData(){
		if(sp != null){
			return sp.getInt("wavebackimgresid", 0);
		}
		return 0;
	}
	
	/**
	 * 保存当前播放的itemid
	 * @param itemid
	 */
	public static void setItemidData(int itemid){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putInt("playingitemid", itemid);
			editor.commit();
		}
	}
	/**
	 * 获取当前播放的itemid
	 * @return
	 */
	public static int getItemidPlayingData(){
		if(sp != null){
			return sp.getInt("playingitemid", 2);
		}
		return 2;
	}

	/**
	 * 保存当前播放的列表标签
	 * @param label 标签号
	 */
	public static void setLabelData(int label){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putInt("playinglabel", label);
			editor.commit();
		}
	}
	/**
	 * 获取列表标签号
	 * @return 列表标签号 
	 */
	public static int getLabelPlayingData(){
		if(sp != null){
			return sp.getInt("playinglabel", 0);
		}
		return 0;
	}
	

	/**
	 * 保存当前播放的循环模式
	 * @param loop 循环模式
	 */
	public static void setLoopData(int loop){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putInt("looptype", loop);
			editor.commit();
		}
	}
	/**
	 * 获取当前播放的循环模式，默认为全部循环
	 * @return 循环方式
	 */
	public static int getLoopData(){
		if(sp != null){
			return sp.getInt("looptype", 2);
		}
		return 2;
	}


	/**
	 * 保存波形图类型
	 * @param type 循环类型序号
	 */
	public static void setWaveType(int type){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putInt("wavetype", type);
			editor.commit();
		}
	}
	/**
	 * 获取当前选中的波形图
	 * @return 循环方式
	 */
	public static int getWaveType(){
		if(sp != null){
			return sp.getInt("wavetype", 1);
		}
		return 1;
	}

	/**
	 * 保存屏幕是否常亮
	 * @param weather 亮起
	 */
	public static void setScerrnBright(boolean weather){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putBoolean("weather", weather);
			editor.commit();
		}
	}
	/**
	 * 获取当前选中的波形图
	 * @return 是否亮起
	 */
	public static boolean getScerrnBright(){
		if(sp != null){
			return sp.getBoolean("weather", false);
		}
		return false;
	}

	
}
