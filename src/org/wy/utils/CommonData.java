package org.wy.utils;

import java.util.Vector;

import org.wy.activity.MainActivity;
import org.wy.entity.PicturePos;
import org.wy.entity.SdcardMusic;

import android.app.NotificationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

public class CommonData {
	public static final String SERVICE_MUSIC = "org.wy.media.MUSIC_SERVICE";
	
	public static final String IMGRESID = "imgresid";
	//������������
	public static Vector<SdcardMusic> all_music = new Vector<SdcardMusic>();
	//��ȡ��Ļ����
	public static int ScreenWidth = 0;
	public static int ScreenHeight = 0;
	
	//�жϵ�ǰ����Ƿ��ں�̨����(true��ʾǰ̨��false��ʾ��̨)
	public static boolean isbackstage = false;
	
	//���ڽ�DrawPersonStereoPos�ĵ�����ݷ���StereoActivity
	public static Handler handler_sendtouchpos;
	
	//�жϵ�ǰ�Ƿ�Ϊ֪ͨ������
	public static boolean isNotiControl = false;
	
	public static Handler handler_draw_waveform;
	public static Handler handler_change_lr_sound;
	//����notification
	public static NotificationManager manager;
	//��ǰ���ŵ��б�
	public static int flag_playing_list = 0;//0��ʾĬ�ϵ������б� 1��ʾϲ���������б�
	
	
	// ��¼����״̬
	public static class playstatus{
		public static int flag_play = 2;
		public static final int PLAY = 1;// ����״̬
		public static final int PAUSE = 2;// ��ͣ״̬
		public static final int STOP = 3;// ֹͣ
		public static final int PROGRESS_CHANGE = 4;// �������ı�
	}
	
	// ѭ��ģʽ
	public static class looptype{
		public static int flag_loop = 2;
		public static final int LOOP_NONE = 0;// ��ѭ��
		public static final int LOOP_ONE = 1;// ����ѭ��
		public static final int LOOP_ALL = 2;// ȫ��ѭ��
		public static final int RANDOM = 3;//�������
	}
	
	//�����б���������������
	public static Handler handler_changetutu = null;
//	//ɾ����ˢ���б�����
//	public static Handler handler_deletemusic = null;
	//service���������������
	public static Handler handler_call_main_act_notifylist = null;
	
}
