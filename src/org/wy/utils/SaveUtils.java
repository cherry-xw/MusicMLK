package org.wy.utils;

import org.wy.activity.MainActivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveUtils {
	public static SharedPreferences sp = null;

	/**
	 * ���������汳����Ϣ
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
	 *  ��ȡ�����汳������
	 * @return ����id
	 */
	public static int getMainBackData(){
		if(sp != null){
			return sp.getInt("backimgresid", 0);
		}
		return 0;
	}
	/**
	 * ���沨�α�����Ϣ
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
	 *  ��ȡ���α�������
	 * @return ����id
	 */
	public static int getWaveBackData(){
		if(sp != null){
			return sp.getInt("wavebackimgresid", 0);
		}
		return 0;
	}
	
	/**
	 * ���浱ǰ���ŵ�itemid
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
	 * ��ȡ��ǰ���ŵ�itemid
	 * @return
	 */
	public static int getItemidPlayingData(){
		if(sp != null){
			return sp.getInt("playingitemid", 2);
		}
		return 2;
	}

	/**
	 * ���浱ǰ���ŵ��б��ǩ
	 * @param label ��ǩ��
	 */
	public static void setLabelData(int label){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putInt("playinglabel", label);
			editor.commit();
		}
	}
	/**
	 * ��ȡ�б��ǩ��
	 * @return �б��ǩ�� 
	 */
	public static int getLabelPlayingData(){
		if(sp != null){
			return sp.getInt("playinglabel", 0);
		}
		return 0;
	}
	

	/**
	 * ���浱ǰ���ŵ�ѭ��ģʽ
	 * @param loop ѭ��ģʽ
	 */
	public static void setLoopData(int loop){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putInt("looptype", loop);
			editor.commit();
		}
	}
	/**
	 * ��ȡ��ǰ���ŵ�ѭ��ģʽ��Ĭ��Ϊȫ��ѭ��
	 * @return ѭ����ʽ
	 */
	public static int getLoopData(){
		if(sp != null){
			return sp.getInt("looptype", 2);
		}
		return 2;
	}


	/**
	 * ���沨��ͼ����
	 * @param type ѭ���������
	 */
	public static void setWaveType(int type){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putInt("wavetype", type);
			editor.commit();
		}
	}
	/**
	 * ��ȡ��ǰѡ�еĲ���ͼ
	 * @return ѭ����ʽ
	 */
	public static int getWaveType(){
		if(sp != null){
			return sp.getInt("wavetype", 1);
		}
		return 1;
	}

	/**
	 * ������Ļ�Ƿ���
	 * @param weather ����
	 */
	public static void setScerrnBright(boolean weather){
		if(sp != null){
			Editor editor = sp.edit();
			editor.putBoolean("weather", weather);
			editor.commit();
		}
	}
	/**
	 * ��ȡ��ǰѡ�еĲ���ͼ
	 * @return �Ƿ�����
	 */
	public static boolean getScerrnBright(){
		if(sp != null){
			return sp.getBoolean("weather", false);
		}
		return false;
	}

	
}
