package org.wy.activity;

import org.wy.db.MusicListDao;
import org.wy.dialog.AboutDialog;
import org.wy.dialog.MusicListDialog;
import org.wy.dialog.MusicMsgDialog;
import org.wy.dialog.SelectWaveTypeDialog;
import org.wy.dialog.SetUpDialog;
import org.wy.headset.helper.HeadSetHelper;
import org.wy.headset.helper.HeadSetHelper.OnHeadSetListener;
import org.wy.service.MainService;
import org.wy.utils.CommonData;
import org.wy.utils.MyTimeUtils;
import org.wy.utils.PlayMusic;
import org.wy.utils.SaveUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wy.activity.R;

public class MainActivity extends Activity {

	private LinearLayout my_main_background,imgabout,setup_btn,showwaveform,imgclose,linedialog,showmusicmsg,Layoutshowlist;
	private ImageView showlist;
	private MusicListDialog md = null;
	private ImageView beforebtn, stabtn, nextbtn, playtypebtn;
	private SeekBar seekbar;
	private TextView showname,showmsgs,showtime;
	
	private boolean isChanging=false;//是否点击了进度条

	private int currenttime_0 = -1;//上一播放时间
	private int currenttime_1 = 0;//当前播放时间

	private SlidingMenu mRightMenu;
	private SlidingMenu showmusicfunction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mRightMenu = (SlidingMenu) findViewById(R.id.slidingmenulayout);
		mRightMenu.setMode(SlidingMenu.RIGHT);
		// 设置触摸屏幕的模式
		mRightMenu.setShadowWidthRes(R.dimen.shadow_width);
		mRightMenu.setShadowDrawable(R.drawable.shadow);
		mRightMenu.setMenu(R.layout.rightmenu);
		mRightMenu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if (mRightMenu.isMenuShowing())
					mRightMenu.toggle();
			}
		});
		
		showmusicfunction = (SlidingMenu) findViewById(R.id.showmusicfunction);
		showmusicfunction.setMode(SlidingMenu.RIGHT);
		// 设置触摸屏幕的模式
		showmusicfunction.setShadowWidthRes(R.dimen.shadow_width);
		showmusicfunction.setMenu(R.layout.showmusicfun);
		showmusicfunction.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if (showmusicfunction.isMenuShowing())
					showmusicfunction.toggle();
			}
		});
		
		
//		if(savedInstanceState != null){
//			CommonData.playstatus.flag_play = savedInstanceState.getInt("playstatus");
//		}
		//软件启动时打开线控
		HeadSetHelper.getInstance().setOnHeadSetListener(headSetListener);
		HeadSetHelper.getInstance().open(this);
		scerrnbright();
		initView();
		init();
	}
	/**
	 * 创建屏幕常亮的对象
	 */
	PowerManager pm;
	WakeLock mWakeLock;
	private void scerrnbright(){
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		if(SaveUtils.getScerrnBright())
			alwaysbright();
	}
	/** 亮起*/
	private void alwaysbright() {
		mWakeLock.acquire();
	}
	/** 熄灭*/
	private void ligntout() {
		mWakeLock.release();
	}

	//线控监听器
	OnHeadSetListener headSetListener = new OnHeadSetListener() {
		
		@Override
		public void onDoubleClick() {
			nextOne();
		}
		
		@Override
		public void onClick() {
			if(CommonData.playstatus.flag_play == CommonData.playstatus.PAUSE && CommonData.all_music.size() > 0){
				play();
			}else{
				pause();
			}
		}
	};
	
	private void initView(){
		//获取屏幕宽高
		CommonData.ScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		CommonData.ScreenHeight = getWindowManager().getDefaultDisplay().getHeight();

		my_main_background = (LinearLayout) findViewById(R.id.my_main_background);
		linedialog = (LinearLayout) findViewById(R.id.linedialog);
		Layoutshowlist = (LinearLayout) findViewById(R.id.Layoutshowlist);
		showlist = (ImageView) findViewById(R.id.showlist);
		beforebtn = (ImageView) findViewById(R.id.beforebtn);
		stabtn = (ImageView) findViewById(R.id.stabtn);
		nextbtn = (ImageView) findViewById(R.id.nextbtn);
		playtypebtn = (ImageView) findViewById(R.id.playtypebtn);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		showname = (TextView) findViewById(R.id.showname);
		showmsgs = (TextView) findViewById(R.id.showmsgs);
		showtime = (TextView) findViewById(R.id.showtime);
		imgabout = (LinearLayout) findViewById(R.id.imgabout);
		setup_btn = (LinearLayout) findViewById(R.id.setup_btn);
		showwaveform = (LinearLayout) findViewById(R.id.showwaveform);
		imgclose = (LinearLayout) findViewById(R.id.imgclose);
		showmusicmsg = (LinearLayout) findViewById(R.id.showmusicmsg);
	}
	
	/**设置主界面背景 */
	private void initBackImg(){
		if(SaveUtils.getMainBackData() != 0){
			my_main_background.setBackgroundResource(SaveUtils.getMainBackData());
		}
	}
	
	//从后台打开到前台
	@Override
	protected void onResume() {
		super.onResume();
		CommonData.isbackstage = true;
		getMainData();
		if(PlayMusic.musicInfo != null)
			reOpenScreen();
	}
	
	//从前台打开到后台
	@Override
	protected void onPause() {
		super.onPause();
		CommonData.isbackstage = false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	/**在打开界面时显示主界面上的各种信息的刷新 */
	private void reOpenScreen(){
		//显示循环模式的图标
		if(CommonData.looptype.flag_loop == CommonData.looptype.LOOP_ALL){
			playtypebtn.setImageResource(R.drawable.circleall);
		}else if(CommonData.looptype.flag_loop == CommonData.looptype.LOOP_ONE){
			playtypebtn.setImageResource(R.drawable.circleone);
		}else if(CommonData.looptype.flag_loop == CommonData.looptype.RANDOM){
			playtypebtn.setImageResource(R.drawable.random);
		}
		//显示当前播放出处于什么状态
		if(CommonData.playstatus.flag_play == CommonData.playstatus.PLAY){
			stabtn.setImageResource(R.drawable.btn_tostart);
		}else if(CommonData.playstatus.flag_play == CommonData.playstatus.PAUSE){
			stabtn.setImageResource(R.drawable.btn_topause);
		}
		PlayMusic.musicInfo = CommonData.all_music.elementAt(PlayMusic.itemId);
		showMessage();
		seekbar.setMax(PlayMusic.musicInfo.getDuration());//设置进度条总长
	}

	//取出保存的主界面状态数据
	private void getMainData(){
		PlayMusic.itemId = SaveUtils.getItemidPlayingData();
		CommonData.looptype.flag_loop = SaveUtils.getLoopData();
		PlayMusic.playing_label = SaveUtils.getLabelPlayingData();
	}

	private void init() {
		//创建简单存储
		SaveUtils.sp = getSharedPreferences("myDatas", MODE_PRIVATE);
		//创建数据库
		CreateDBinitData();
		//在打开软件时，将所有上一次保存的数据取出来
		initBackImg();
		getMainData();
		// 启动软件时，将数据库所有音乐列表数据加载到播放列表中
		if(PlayMusic.playing_label == 0){
			CommonData.all_music = new MusicListDao(this).findAllMusic();
		}else if(PlayMusic.playing_label == 1){
			CommonData.all_music = new MusicListDao(this).findAllLoveMusic();
		}
		
		if(CommonData.all_music.size() > 0 && PlayMusic.itemId < CommonData.all_music.size()){//如果有列表则执行
			//显示主界面音乐信息
			PlayMusic.musicInfo = CommonData.all_music.elementAt(PlayMusic.itemId);
//			showMessage();
			reOpenScreen();
		}else{//表示音乐列表为空，为第一次执行该程序或者手机中没有音乐
			Log.e("sss", "音乐列表为空，为第一次执行该程序或者手机中没有音乐");
		}
		
		//关闭软件
		imgclose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pause();
				stopMyService();
				System.exit(0);
			}
		});
		// 上一曲
		beforebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lastOne();
			}
		});

		// 下一曲
		nextbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextOne();
			}
		});

		// 播放暂停
		stabtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(CommonData.playstatus.flag_play == CommonData.playstatus.PAUSE && CommonData.all_music.size() > 0){
					play();
				}else{
					pause();
				}
			}
		});
		//进度条改变事件
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Intent intent = new Intent();
				intent.setAction(CommonData.SERVICE_MUSIC);
				intent.putExtra("lastornext", 2);// 向服务传递数据
				intent.putExtra("seekto", seekbar.getProgress());
				startService(intent);
				isChanging=false;//松开
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				isChanging=true;//点击
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//进度条改变回调
				if(isChanging){
					showtime.setText(MyTimeUtils.Ms2mmss(seekbar.getProgress())+
							" / "+MyTimeUtils.Ms2mmss(PlayMusic.musicInfo.getDuration()));
				}else{
					showtime.setText(MyTimeUtils.Ms2mmss(currenttime_1)+
							" / "+MyTimeUtils.Ms2mmss(PlayMusic.musicInfo.getDuration()));
				}
			}
		});
		

    	showwaveform.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(CommonData.SERVICE_MUSIC);
				intent.putExtra("showwaveform", 0);
				startService(intent);
				Intent i = new Intent(MainActivity.this, ShowWaveFormActivity.class);
				startActivity(i);
			}
		});
		
		// 打开音乐列表界面
    	Layoutshowlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					dialogshow();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("sss", "show music list dialog is error", e);
				}
			}
		});
		// 打开立体音乐
		linedialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				stopMyService();
				Intent intent = new Intent(MainActivity.this, StereoActivity.class);
				startActivity(intent);
			}
		});
		
		//打开显示音乐详细信息
		showmusicmsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
		        int[] location = new int[2];
				v.getLocationOnScreen(location);
				new MusicMsgDialog(location[0]-CommonData.ScreenWidth/2+showmusicmsg.getWidth()/2, 
						CommonData.ScreenHeight/2-location[1]-showmusicmsg.getHeight()/2*3).show(getFragmentManager(), null);

			}
		});
		// 播放循环的模式
		playtypebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(CommonData.looptype.flag_loop == CommonData.looptype.LOOP_ALL){
					CommonData.looptype.flag_loop = CommonData.looptype.LOOP_ONE;
					playtypebtn.setImageResource(R.drawable.circleone);
				}else if(CommonData.looptype.flag_loop == CommonData.looptype.LOOP_ONE){
					CommonData.looptype.flag_loop = CommonData.looptype.RANDOM;
					playtypebtn.setImageResource(R.drawable.random);
				}else if(CommonData.looptype.flag_loop == CommonData.looptype.RANDOM){
					CommonData.looptype.flag_loop = CommonData.looptype.LOOP_ALL;
					playtypebtn.setImageResource(R.drawable.circleall);
				}
				SaveUtils.setLoopData(CommonData.looptype.flag_loop);
			}
		});
		
		//设置
		setup_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SetUpDialog().show(getFragmentManager(), null);
			}
		});
		
		//关于按钮点击事件
		imgabout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AboutDialog().show(getFragmentManager(), null);
			}
		});

		//由列表界面发来的信息
		CommonData.handler_changetutu = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: //图图图标变化
					try {
						dialogshow();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						Log.e("sss", "show music list dialog is error", e1);
					}
					break;
				case 1://列表点击开始播放音乐后回调
					play();
					break;
				case 2://在notification点击关闭按钮
					pause();
					stopMyService();
					System.exit(0);
					break;
				case 3://在notification点击暂停按钮
					pause();
					break;
				case 4://在notification点击上一首按钮
					lastOne();
					break;
				case 5: //修改背景
					try {
						int resid = msg.getData().getInt(CommonData.IMGRESID);
						SaveUtils.setMainBackData(resid);
						my_main_background.setBackgroundResource(resid);
						Toast.makeText(MainActivity.this, R.string.action_settings_success,Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, R.string.action_settings_failes,Toast.LENGTH_LONG).show();
						Log.e("sss", "set background is error", e);
					}
					break;
				case 6://在notification点击下一首按钮
					nextOne();
					break;
				case 7://保持屏幕常亮
					alwaysbright();
					break;
				case 8://关闭屏幕常亮
					ligntout();
					break;
				default:
					break;
				}
				CommonData.isNotiControl = false;
			};
		};
		
		CommonData.handler_call_main_act_notifylist = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					if(md != null){
						md.notifyMusicList();//刷新音乐列表
					}
					try {
						seekbar.setMax(PlayMusic.musicInfo.getDuration());//设置进度条总长
					} catch (Exception e) {
						Log.e("sss", "list has no music",e);
					}
					break;
				case 1:
					showMessage();//设置音乐信息主界面显示
					currenttime_1 = msg.getData().getInt("current");
					if(!isChanging){
						if(currenttime_1 != currenttime_0){
							seekbar.setProgress(currenttime_1);
							currenttime_0 = currenttime_1;
						}else{
							showtime.setText(MyTimeUtils.Ms2mmss(currenttime_1)+
									" / "+MyTimeUtils.Ms2mmss(PlayMusic.musicInfo.getDuration()));
						}
					}
					break;
				default:
					break;
				}
			}
		};
		
	}
	
	/**设置主界面音乐信息显示 */
	private void showMessage(){
		showname.setText(PlayMusic.musicInfo.getTilte());
		String strmsgs = " | ";
		if(PlayMusic.musicInfo.getArtist().equals("<unknown>")){
			strmsgs = strmsgs+"未知歌手";
		}else{
			strmsgs = strmsgs+PlayMusic.musicInfo.getArtist();
		}
		if(PlayMusic.musicInfo.getArtist().equals("<unknown>")){
			strmsgs = "未知专辑"+strmsgs;
		}else{
			strmsgs = PlayMusic.musicInfo.getAlbum()+strmsgs;
		}
		showmsgs.setText(strmsgs);
		showtime.setText(MyTimeUtils.Ms2mmss(PlayMusic.musicInfo.getDuration()));
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("playstatus", CommonData.playstatus.flag_play);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		CommonData.playstatus.flag_play = savedInstanceState.getInt("playstatus");
	}

	/**音乐列表图标切换*/
	public void dialogshow() throws Exception{
		if (md == null) {
			md = new MusicListDialog();
			md.setCancelable(true);
			md.show(getFragmentManager(), null);
			showlist.setImageResource(R.drawable.liston);
		} else {
			md = null;
			showlist.setImageResource(R.drawable.listoff);
		}
	}

	/** 创建数据库 */
	private void CreateDBinitData() {
		new MusicListDao(this).Create_MusicTable();
//		new CustomListDao(this).Create_MusicTable();
	}

	/** 播放 */
	public void play() {
		CommonData.playstatus.flag_play = CommonData.playstatus.PLAY;
		stabtn.setImageResource(R.drawable.btn_tostart);
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("op", CommonData.playstatus.PLAY);// 向服务传递数据
		startService(intent);
	}

	/** 暂停 */
	public void pause() {
		CommonData.playstatus.flag_play = CommonData.playstatus.PAUSE;
		stabtn.setImageResource(R.drawable.btn_topause);
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("op", CommonData.playstatus.PAUSE);// 向服务传递数据
		startService(intent);
	}

	/** 上一首 */
	private void lastOne() {
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("lastornext", 0);// 向服务传递数据
		startService(intent);
	}

	/** 下一首 */
	public void nextOne() {
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("lastornext", 1);// 向服务传递数据
		startService(intent);
	}

	
	/** 停止播放音乐 */
	private void stop() {
		Intent isplay = new Intent("notifi.update");
		sendBroadcast(isplay);// 发起后台支持
		CommonData.playstatus.flag_play = CommonData.playstatus.STOP;
		stabtn.setImageResource(R.drawable.btn_topause);
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("op", CommonData.playstatus.STOP);
		startService(intent);
	}

	// 双击退出键退出应用
	private long exitTime = 0;

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(this, R.string.close_app, Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			super.onBackPressed();
			stopMyService();
			if(CommonData.manager != null)
				CommonData.manager.cancelAll();
			System.exit(0);
			//关闭线控
			HeadSetHelper.getInstance().close(this);
		}
	}

	/** 启动监听播放状态服务 **/
	private void startMyService() {
		Intent intent = new Intent(MainActivity.this, MainService.class);
		startService(intent);
	}

	/** 关闭监听播放状态服务 **/
	private void stopMyService() {
		Intent intent = new Intent(MainActivity.this, MainService.class);
		stopService(intent);
	}
}
