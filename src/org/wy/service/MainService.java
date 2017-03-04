package org.wy.service;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.wy.activity.MainActivity;
import org.wy.utils.CommonData;
import org.wy.utils.MyTimeUtils;
import org.wy.utils.PlayMusic;
import org.wy.utils.SaveUtils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat.Builder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wy.activity.R;

/***
 * 2013/5/25
 * 
 * @author wwj 音乐播放服务
 */
@SuppressLint("NewApi")
public class MainService extends Service implements MediaPlayer.OnCompletionListener {


	private static final int MUSIC_PLAY = 1;
	private static final int MUSIC_PAUSE = 2;
	private static final int MUSIC_STOP = 3;
	private static final int PROGRESS_CHANGE = 4;

	private MediaPlayer mp = null;// Mediaplayer对象
	public static Notification notification;// 通知栏显示当前播放音乐
	public static NotificationManager nm;

	private Timer mTimer;//计时器
	private TimerTask mTimerTask;
	
	private String source = "";//第一次打开资源为空

	private int currentTime;// 当前播放时间
	
	private Timer mtm = null;
	private TimerTask mttk = null;
	
	private int theLastSecond = 0;
	
	private Visualizer mVisualizer;
	
	private Equalizer mEqualizer;

	@Override
	public void onCreate() {
		super.onCreate();

		if (mp != null) {
			mp.reset();
			mp.release();
			mp = null;
		}
		mp = new MediaPlayer();// 实例化mediaplayer
		Log.e("sss", "mp is run");
		mp.setOnCompletionListener(this);// 设置下一首播放监听
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.ANSWER");
		registerReceiver(PhoneListener, filter);
	}

	private int op = -1;
	private int la_or_ne = -1;
	private int show_waveform = -1;
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		setup();
		
		try {
//			if(CommonData.isbackstage || CommonData.isNotiControl){
				op = intent.getIntExtra("op", -1);
//			}else{
//				op = -1;
//				Log.e("sss", "op="+op);
//			}
			switch (op) {
			case MUSIC_PLAY:// 播放
				if (!mp.isPlaying()) {
					play();
				}
				break;
			case MUSIC_PAUSE:// 暂停
				if (mp.isPlaying()) {
					pause();
				}
				break;
			case MUSIC_STOP:// 停止
				stop();
				break;
			case PROGRESS_CHANGE:// 进度条改变
				currentTime = intent.getExtras().getInt("progress");
				mp.seekTo(currentTime);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.e("sss", "get op is error", e);
		}
		
		
		try {
//			if(CommonData.isbackstage || CommonData.isNotiControl){
				la_or_ne = intent.getIntExtra("lastornext", -1);
//			}else{
//				la_or_ne = -1;
//			}
			switch (la_or_ne) {
			case 0://上一曲
				lastOne();
				break;
			case 1://下一曲
				nextOne(false);
				break;
			case 2://seekto点击进度条跳转
				theLastSecond = intent.getIntExtra("seekto", 0);
				mp.seekTo(intent.getIntExtra("seekto", 0));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("sss", "get lastornext is error", e);
		}
		
		try {
			if(CommonData.isbackstage){
				show_waveform = intent.getIntExtra("showwaveform", -1);
			}else{
				show_waveform = -1;
			}
			switch (show_waveform) {
			case 0:
				setupVisualizerFxAndUI(true);
				break;
			case 1:
				setupVisualizerFxAndUI(false);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("sss", "get lastornext is error", e);
		}
		//创建通知栏显示
		try {
			CommonData.manager=showCustomView();
		} catch (Exception e) {
			Log.e("sss", "create Notification is error", e);
		}
		
		try {
			int changelsd = intent.getIntExtra("chglsd", 100);
			int changersd = intent.getIntExtra("chgrsd", 100);
			if(mp.isPlaying()){
				mp.setVolume(changelsd/100f, changersd/100f);
			}
		} catch (Exception e) {
			Log.e("sss", "change lr sound is error", e);
		}
	}

	/**
	 * 当开始播放时，通知栏显示当前播放信息
	 */
	private NotificationManager showCustomView() {
		RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.music_notification);
		if(CommonData.all_music.size() > 0){
			remoteViews.setTextViewText(R.id.title_title, CommonData.all_music.elementAt(PlayMusic.itemId).getTilte());
			remoteViews.setTextViewText(R.id.txt_duration, MyTimeUtils.Ms2mmss(CommonData.all_music.elementAt(PlayMusic.itemId).getDuration()));
			remoteViews.setTextViewText(R.id.txt_artist, CommonData.all_music.elementAt(PlayMusic.itemId).getArtist());
		}
		Intent reActivity=new Intent(this,MainActivity.class);
		PendingIntent pIntent=PendingIntent.getActivity(this, 0, reActivity, 0);
		remoteViews.setOnClickPendingIntent(R.id.ll_parent, pIntent);
		//设置按钮事件 
		Intent preintent = new Intent(); 
		preintent.putExtra("action", "last");
		preintent.setAction("org.wy.playerReceiver");
		PendingIntent prepi = PendingIntent.getBroadcast(this, 0, preintent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.last_music, prepi);//----设置对应的按钮ID监控
		
		if(CommonData.playstatus.flag_play == CommonData.playstatus.PLAY){
			Intent pauaseOrStartIntent=new Intent(); 
			pauaseOrStartIntent.putExtra("action", "pause");
			pauaseOrStartIntent.setAction("org.wy.playerReceiver");
			PendingIntent pausepi = PendingIntent.getBroadcast(this, 1, pauaseOrStartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.paly_pause_music, pausepi);//----设置对应的按钮ID监控
			remoteViews.setImageViewResource(R.id.paly_pause_music, R.drawable.btn_tostart);
		}else{
			Intent pauaseOrStartIntent=new Intent(); 
			pauaseOrStartIntent.putExtra("action", "playing");
			pauaseOrStartIntent.setAction("org.wy.playerReceiver");
			PendingIntent pausepi = PendingIntent.getBroadcast(this, 2, pauaseOrStartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.paly_pause_music, pausepi);//----设置对应的按钮ID监控
			remoteViews.setImageViewResource(R.id.paly_pause_music, R.drawable.btn_topause);
		}
		
		Intent nextIntent=new Intent(); 
		nextIntent.putExtra("action", "next");
		nextIntent.setAction("org.wy.playerReceiver");
		PendingIntent nextpi=PendingIntent.getBroadcast(this, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.next_music, nextpi);//----设置对应的按钮ID监控
		
		Intent closeIntent=new Intent(); 
		closeIntent.putExtra("action", "close_noti");
		closeIntent.setAction("org.wy.playerReceiver");
		PendingIntent closepi=PendingIntent.getBroadcast(this, 4, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.close, closepi);
		
		Builder builder = new Builder(MainService.this);
		builder.setContent(remoteViews).setSmallIcon(R.drawable.icon).setOngoing(true)
				.setTicker("music is playing");
		Notification notification=builder.build();
		NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1, notification);
		return manager;
	}

	/*** 来电时监听播放状态 */
	protected BroadcastReceiver PhoneListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_ANSWER)) {
				TelephonyManager telephonymanager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				switch (telephonymanager.getCallState()) {
				case TelephonyManager.CALL_STATE_RINGING:// 当有来电时候，暂停音乐，可我试了试，只是把声音降低而已
					pause();
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					play();
					break;
				default:
					break;
				}
			}
		}
	};
	
	/** 播放 */
	private void play() {
		if (mp != null) {
			if(CommonData.handler_call_main_act_notifylist != null)
				CommonData.handler_call_main_act_notifylist.sendEmptyMessage(0);//通知更新音乐列表
			if(CommonData.handler_draw_waveform != null)
				CommonData.handler_draw_waveform.sendEmptyMessage(6);//通知更新音乐列表
			SaveUtils.setItemidData(PlayMusic.itemId);
			SaveUtils.setLabelData(PlayMusic.playing_label);
//			setupVisualizerFxAndUI(SaveUtils.getShowWaveForm());//在播放前判断是否显示
			mp.start();
		}
	}

	/** 暂停 */
	private void pause() {
		if (mp != null) {
			mVisualizer.setEnabled(false);
			mp.pause();
		}
	}

	/** 停止 */
	private void stop() {
		if (mp != null) {
			mp.stop();
			try {
				mp.prepare();
				mp.seekTo(0);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	private Toast mtoast; 
	/** 初始化mp */
	private void setup() {
		if(CommonData.all_music.size() > 0){
			PlayMusic.musicInfo = CommonData.all_music.elementAt(PlayMusic.itemId);
			if(!source.equals(PlayMusic.musicInfo.getUrl())){
				try {
					mp.reset();
					mp.setDataSource(PlayMusic.musicInfo.getUrl());
					try {
						if (!mp.isPlaying()) {
							mp.prepare();
						}
						if(CommonData.handler_call_main_act_notifylist != null){//通知界面显示音乐信息
							if(mTimer != null){
								mTimer.cancel();
								mTimer = null;
							}
							if(mTimerTask != null){
								mTimerTask.cancel();
								mTimerTask = null;
							}
							mTimer = new Timer();//定时器记录播放进度
							mTimerTask = new TimerTask() {
								public void run() {
									Bundle b = new Bundle();
									if(mp.isPlaying()){
										b.putInt("current", mp.getCurrentPosition());
										theLastSecond = mp.getCurrentPosition();
									}else{
										b.putInt("current", theLastSecond);
									}
									Message msg = new Message();
									msg.what = 1;
									msg.setData(b);
									CommonData.handler_call_main_act_notifylist.sendMessage(msg);

									if(CommonData.handler_draw_waveform != null){
										Message msgs = new Message();
										msgs.setData(b);
										msgs.what = 5;
										CommonData.handler_draw_waveform.sendMessage(msgs);
									}
								}
							};
							mTimer.schedule(mTimerTask, 0, 1000);
						}
							
							
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					Log.e("sss", "onStart mp -> is error", e);
				}
				source = PlayMusic.musicInfo.getUrl();
			}
		}else{
			if(mtoast == null) { 
				mtoast=Toast.makeText(this, R.string.no_music, Toast.LENGTH_LONG);
			}else{
				mtoast.setText(R.string.no_music);  
			}
			mtoast.show();
		}

		if(mVisualizer == null){
			mVisualizer = new Visualizer(mp.getAudioSessionId());
			mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
			mEqualizer = new Equalizer(0, mp.getAudioSessionId());
			mEqualizer.setEnabled(true);
		}
	}
	
	
	
	/*
	 * 下一首 
	 *  aa 表示是否为手动切换
	 */
	private void nextOne(boolean aa) {
		if(aa){
			if (CommonData.all_music.size() == 1 || CommonData.looptype.flag_loop == CommonData.looptype.LOOP_ONE) {
				stop();
				setup();
				if(CommonData.playstatus.flag_play == CommonData.playstatus.PLAY){
					play();
				}
				return;
			}
		}		
		if (CommonData.looptype.flag_loop == CommonData.looptype.RANDOM) {
			PlayMusic.itemId = getRandomNum(0, CommonData.all_music.size() - 1);
		} else {
			if (PlayMusic.itemId == CommonData.all_music.size() - 1) {
				PlayMusic.itemId = 0;
			} else if (PlayMusic.itemId < CommonData.all_music.size() - 1) {
				PlayMusic.itemId++;
			}
		}
		stop();
		setup();
		if(CommonData.playstatus.flag_play == CommonData.playstatus.PLAY){
			play();
		}
	}

	/** 上一首 */
	private void lastOne() {
		if (CommonData.all_music.size() == 1 || CommonData.looptype.flag_loop == CommonData.looptype.LOOP_ONE) {
			stop();
			setup();
			if(CommonData.playstatus.flag_play == CommonData.playstatus.PLAY){
				play();
			}
			return;
		}
		if (CommonData.looptype.flag_loop == CommonData.looptype.RANDOM) {
			PlayMusic.itemId = getRandomNum(0, CommonData.all_music.size() - 1);
		} else {
			if (PlayMusic.itemId == 0) {
				PlayMusic.itemId = CommonData.all_music.size() - 1;
			} else {
				PlayMusic.itemId--;
			}
		}
		stop();
		setup();
		if(CommonData.playstatus.flag_play == CommonData.playstatus.PLAY){
			play();
		}
	}

	
	private void setupVisualizerFxAndUI(boolean show) {
		final int maxCR = Visualizer.getMaxCaptureRate();

		mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
			public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
			}

			public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
				if(CommonData.handler_draw_waveform != null){
					Bundle data = new Bundle();
					data.putByteArray("myfftdata", fft);
					Message msg = new Message();
					msg.setData(data);
					msg.what = SaveUtils.getWaveType();
					CommonData.handler_draw_waveform.sendMessage(msg);
				}
			}
		}, maxCR, false, true);
		mVisualizer.setEnabled(show);//是否要获取fft
	}

	/** 产生随机数 */
	private int getRandomNum(int min, int max) {
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		nextOne(true);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
