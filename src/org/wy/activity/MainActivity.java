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
	
	private boolean isChanging=false;//�Ƿ����˽�����

	private int currenttime_0 = -1;//��һ����ʱ��
	private int currenttime_1 = 0;//��ǰ����ʱ��

	private SlidingMenu mRightMenu;
	private SlidingMenu showmusicfunction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mRightMenu = (SlidingMenu) findViewById(R.id.slidingmenulayout);
		mRightMenu.setMode(SlidingMenu.RIGHT);
		// ���ô�����Ļ��ģʽ
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
		// ���ô�����Ļ��ģʽ
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
		//�������ʱ���߿�
		HeadSetHelper.getInstance().setOnHeadSetListener(headSetListener);
		HeadSetHelper.getInstance().open(this);
		scerrnbright();
		initView();
		init();
	}
	/**
	 * ������Ļ�����Ķ���
	 */
	PowerManager pm;
	WakeLock mWakeLock;
	private void scerrnbright(){
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		if(SaveUtils.getScerrnBright())
			alwaysbright();
	}
	/** ����*/
	private void alwaysbright() {
		mWakeLock.acquire();
	}
	/** Ϩ��*/
	private void ligntout() {
		mWakeLock.release();
	}

	//�߿ؼ�����
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
		//��ȡ��Ļ���
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
	
	/**���������汳�� */
	private void initBackImg(){
		if(SaveUtils.getMainBackData() != 0){
			my_main_background.setBackgroundResource(SaveUtils.getMainBackData());
		}
	}
	
	//�Ӻ�̨�򿪵�ǰ̨
	@Override
	protected void onResume() {
		super.onResume();
		CommonData.isbackstage = true;
		getMainData();
		if(PlayMusic.musicInfo != null)
			reOpenScreen();
	}
	
	//��ǰ̨�򿪵���̨
	@Override
	protected void onPause() {
		super.onPause();
		CommonData.isbackstage = false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	/**�ڴ򿪽���ʱ��ʾ�������ϵĸ�����Ϣ��ˢ�� */
	private void reOpenScreen(){
		//��ʾѭ��ģʽ��ͼ��
		if(CommonData.looptype.flag_loop == CommonData.looptype.LOOP_ALL){
			playtypebtn.setImageResource(R.drawable.circleall);
		}else if(CommonData.looptype.flag_loop == CommonData.looptype.LOOP_ONE){
			playtypebtn.setImageResource(R.drawable.circleone);
		}else if(CommonData.looptype.flag_loop == CommonData.looptype.RANDOM){
			playtypebtn.setImageResource(R.drawable.random);
		}
		//��ʾ��ǰ���ų�����ʲô״̬
		if(CommonData.playstatus.flag_play == CommonData.playstatus.PLAY){
			stabtn.setImageResource(R.drawable.btn_tostart);
		}else if(CommonData.playstatus.flag_play == CommonData.playstatus.PAUSE){
			stabtn.setImageResource(R.drawable.btn_topause);
		}
		PlayMusic.musicInfo = CommonData.all_music.elementAt(PlayMusic.itemId);
		showMessage();
		seekbar.setMax(PlayMusic.musicInfo.getDuration());//���ý������ܳ�
	}

	//ȡ�������������״̬����
	private void getMainData(){
		PlayMusic.itemId = SaveUtils.getItemidPlayingData();
		CommonData.looptype.flag_loop = SaveUtils.getLoopData();
		PlayMusic.playing_label = SaveUtils.getLabelPlayingData();
	}

	private void init() {
		//�����򵥴洢
		SaveUtils.sp = getSharedPreferences("myDatas", MODE_PRIVATE);
		//�������ݿ�
		CreateDBinitData();
		//�ڴ����ʱ����������һ�α��������ȡ����
		initBackImg();
		getMainData();
		// �������ʱ�������ݿ����������б����ݼ��ص������б���
		if(PlayMusic.playing_label == 0){
			CommonData.all_music = new MusicListDao(this).findAllMusic();
		}else if(PlayMusic.playing_label == 1){
			CommonData.all_music = new MusicListDao(this).findAllLoveMusic();
		}
		
		if(CommonData.all_music.size() > 0 && PlayMusic.itemId < CommonData.all_music.size()){//������б���ִ��
			//��ʾ������������Ϣ
			PlayMusic.musicInfo = CommonData.all_music.elementAt(PlayMusic.itemId);
//			showMessage();
			reOpenScreen();
		}else{//��ʾ�����б�Ϊ�գ�Ϊ��һ��ִ�иó�������ֻ���û������
			Log.e("sss", "�����б�Ϊ�գ�Ϊ��һ��ִ�иó�������ֻ���û������");
		}
		
		//�ر����
		imgclose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pause();
				stopMyService();
				System.exit(0);
			}
		});
		// ��һ��
		beforebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lastOne();
			}
		});

		// ��һ��
		nextbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextOne();
			}
		});

		// ������ͣ
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
		//�������ı��¼�
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Intent intent = new Intent();
				intent.setAction(CommonData.SERVICE_MUSIC);
				intent.putExtra("lastornext", 2);// ����񴫵�����
				intent.putExtra("seekto", seekbar.getProgress());
				startService(intent);
				isChanging=false;//�ɿ�
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				isChanging=true;//���
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//�������ı�ص�
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
		
		// �������б����
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
		// ����������
		linedialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				stopMyService();
				Intent intent = new Intent(MainActivity.this, StereoActivity.class);
				startActivity(intent);
			}
		});
		
		//����ʾ������ϸ��Ϣ
		showmusicmsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
		        int[] location = new int[2];
				v.getLocationOnScreen(location);
				new MusicMsgDialog(location[0]-CommonData.ScreenWidth/2+showmusicmsg.getWidth()/2, 
						CommonData.ScreenHeight/2-location[1]-showmusicmsg.getHeight()/2*3).show(getFragmentManager(), null);

			}
		});
		// ����ѭ����ģʽ
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
		
		//����
		setup_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SetUpDialog().show(getFragmentManager(), null);
			}
		});
		
		//���ڰ�ť����¼�
		imgabout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AboutDialog().show(getFragmentManager(), null);
			}
		});

		//���б���淢������Ϣ
		CommonData.handler_changetutu = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0: //ͼͼͼ��仯
					try {
						dialogshow();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						Log.e("sss", "show music list dialog is error", e1);
					}
					break;
				case 1://�б�����ʼ�������ֺ�ص�
					play();
					break;
				case 2://��notification����رհ�ť
					pause();
					stopMyService();
					System.exit(0);
					break;
				case 3://��notification�����ͣ��ť
					pause();
					break;
				case 4://��notification�����һ�װ�ť
					lastOne();
					break;
				case 5: //�޸ı���
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
				case 6://��notification�����һ�װ�ť
					nextOne();
					break;
				case 7://������Ļ����
					alwaysbright();
					break;
				case 8://�ر���Ļ����
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
						md.notifyMusicList();//ˢ�������б�
					}
					try {
						seekbar.setMax(PlayMusic.musicInfo.getDuration());//���ý������ܳ�
					} catch (Exception e) {
						Log.e("sss", "list has no music",e);
					}
					break;
				case 1:
					showMessage();//����������Ϣ��������ʾ
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
	
	/**����������������Ϣ��ʾ */
	private void showMessage(){
		showname.setText(PlayMusic.musicInfo.getTilte());
		String strmsgs = " | ";
		if(PlayMusic.musicInfo.getArtist().equals("<unknown>")){
			strmsgs = strmsgs+"δ֪����";
		}else{
			strmsgs = strmsgs+PlayMusic.musicInfo.getArtist();
		}
		if(PlayMusic.musicInfo.getArtist().equals("<unknown>")){
			strmsgs = "δ֪ר��"+strmsgs;
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

	/**�����б�ͼ���л�*/
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

	/** �������ݿ� */
	private void CreateDBinitData() {
		new MusicListDao(this).Create_MusicTable();
//		new CustomListDao(this).Create_MusicTable();
	}

	/** ���� */
	public void play() {
		CommonData.playstatus.flag_play = CommonData.playstatus.PLAY;
		stabtn.setImageResource(R.drawable.btn_tostart);
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("op", CommonData.playstatus.PLAY);// ����񴫵�����
		startService(intent);
	}

	/** ��ͣ */
	public void pause() {
		CommonData.playstatus.flag_play = CommonData.playstatus.PAUSE;
		stabtn.setImageResource(R.drawable.btn_topause);
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("op", CommonData.playstatus.PAUSE);// ����񴫵�����
		startService(intent);
	}

	/** ��һ�� */
	private void lastOne() {
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("lastornext", 0);// ����񴫵�����
		startService(intent);
	}

	/** ��һ�� */
	public void nextOne() {
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("lastornext", 1);// ����񴫵�����
		startService(intent);
	}

	
	/** ֹͣ�������� */
	private void stop() {
		Intent isplay = new Intent("notifi.update");
		sendBroadcast(isplay);// �����̨֧��
		CommonData.playstatus.flag_play = CommonData.playstatus.STOP;
		stabtn.setImageResource(R.drawable.btn_topause);
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("op", CommonData.playstatus.STOP);
		startService(intent);
	}

	// ˫���˳����˳�Ӧ��
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
			//�ر��߿�
			HeadSetHelper.getInstance().close(this);
		}
	}

	/** ������������״̬���� **/
	private void startMyService() {
		Intent intent = new Intent(MainActivity.this, MainService.class);
		startService(intent);
	}

	/** �رռ�������״̬���� **/
	private void stopMyService() {
		Intent intent = new Intent(MainActivity.this, MainService.class);
		stopService(intent);
	}
}
