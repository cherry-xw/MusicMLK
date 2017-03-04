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
	//所有音乐数据
	public static Vector<SdcardMusic> all_music = new Vector<SdcardMusic>();
	//获取屏幕宽，高
	public static int ScreenWidth = 0;
	public static int ScreenHeight = 0;
	
	//判断当前软件是否在后台运行(true表示前台，false表示后台)
	public static boolean isbackstage = false;
	
	//用于将DrawPersonStereoPos的点击数据发给StereoActivity
	public static Handler handler_sendtouchpos;
	
	//判断当前是否为通知栏控制
	public static boolean isNotiControl = false;
	
	public static Handler handler_draw_waveform;
	public static Handler handler_change_lr_sound;
	//创建notification
	public static NotificationManager manager;
	//当前播放的列表
	public static int flag_playing_list = 0;//0表示默认的音乐列表 1表示喜欢的音乐列表
	
	
	// 记录播放状态
	public static class playstatus{
		public static int flag_play = 2;
		public static final int PLAY = 1;// 播放状态
		public static final int PAUSE = 2;// 暂停状态
		public static final int STOP = 3;// 停止
		public static final int PROGRESS_CHANGE = 4;// 进度条改变
	}
	
	// 循环模式
	public static class looptype{
		public static int flag_loop = 2;
		public static final int LOOP_NONE = 0;// 不循环
		public static final int LOOP_ONE = 1;// 单曲循环
		public static final int LOOP_ALL = 2;// 全部循环
		public static final int RANDOM = 3;//随机播放
	}
	
	//音乐列表操作对主界面更新
	public static Handler handler_changetutu = null;
//	//删除后刷新列表内容
//	public static Handler handler_deletemusic = null;
	//service操作对主界面更新
	public static Handler handler_call_main_act_notifylist = null;
	
}
