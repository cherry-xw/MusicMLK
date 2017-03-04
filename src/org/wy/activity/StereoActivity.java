package org.wy.activity;

import java.util.Vector;

import org.wy.entity.PicturePos;
import org.wy.ui.utils.DrawPersonStereoPos;
import org.wy.utils.CommonData;
import org.wy.utils.MyTimeUtils;
import org.wy.utils.PlayMusic;

import com.wy.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class StereoActivity extends Activity {

//	private SlidingMenu ShowMusicList;
	private LinearLayout ctrlearandmusic;
	private DrawPersonStereoPos dpsp;
	private Vector<PicturePos> pps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stereo);

//		ShowMusicList = (SlidingMenu) findViewById(R.id.musiclist);
//		ShowMusicList.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
//		ShowMusicList.setShadowWidthRes(R.dimen.shadow_width);
//		ShowMusicList.setMenu(R.layout.slidingmenulist);
//		ShowMusicList.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (ShowMusicList.isMenuShowing())
//					ShowMusicList.toggle();
//			}
//		});

		initView();
		init();
	}

	private void initView() {
		ctrlearandmusic = (LinearLayout) findViewById(R.id.ctrlearandmusic);
	}

	
	private int changea = 2;
	private int changeb = 2;
	private void init() {
		//画出人的位置和声音的位置
		dpsp = new DrawPersonStereoPos(this);
		dpsp.setLayoutParams(ctrlearandmusic.getLayoutParams());
		ctrlearandmusic.addView(dpsp);
		
		CommonData.handler_sendtouchpos = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case 0:
						calculateSound_SendService(dpsp.pps.elementAt(0).getX(),dpsp.pps.elementAt(0).getY(),dpsp.pps.elementAt(1).getX(),dpsp.pps.elementAt(1).getY());
						break;
					case 1:
						dpsp.pps.elementAt(0).setX(dpsp.pps.elementAt(0).getX()+changea);
						dpsp.pps.elementAt(0).setY(dpsp.pps.elementAt(0).getY()+changeb);
						if(dpsp.pps.elementAt(0).getX() <= 0){
							changea = 2;
						}else if(dpsp.pps.elementAt(0).getX() >= CommonData.ScreenWidth-140){
							changea = -2;
						}
						if(dpsp.pps.elementAt(0).getY() <= 0){
							changeb = 2;
						}else if(dpsp.pps.elementAt(0).getY() >= CommonData.ScreenHeight-140){
							changeb = -2;
						}
						calculateSound_SendService(dpsp.pps.elementAt(0).getX(),dpsp.pps.elementAt(0).getY(),dpsp.pps.elementAt(1).getX(),dpsp.pps.elementAt(1).getY());
						break;
				}
			}
		};
	}
	
	
	//将屏幕坐标位置换算成声音的变化并发送给服务
	private void calculateSound_SendService(int sound_x,int sound_y,int head_x,int head_y){
		
		int ear_y = head_y;//左右耳y位置
		int ear_sound_y = 100-Math.abs(ear_y-sound_y)*100/CommonData.ScreenHeight;//左右耳声音
		
		int leftear_x = head_x-60;//左耳x位置
		int rightear_x = head_x+60;//右耳x位置
		
		int left_ear_sound_x = 0;
		int right_ear_sound_x = 0;
		if(sound_x < rightear_x && sound_x > leftear_x){
			int cha = sound_x-head_x;

			left_ear_sound_x = 80-cha;
			right_ear_sound_x = cha+80;
		}else if(sound_x-rightear_x >= 0){
			left_ear_sound_x = 30-(sound_x-leftear_x)*120/CommonData.ScreenWidth;
			right_ear_sound_x = 100-(sound_x-rightear_x)*120/CommonData.ScreenWidth;
		}else{
			left_ear_sound_x = 100-(leftear_x-sound_x)*120/CommonData.ScreenWidth;
			right_ear_sound_x = 30-(rightear_x-sound_x)*120/CommonData.ScreenWidth;
		}
		if(left_ear_sound_x <= 0){
			left_ear_sound_x = 1;
		}
		if(right_ear_sound_x <= 0){
			right_ear_sound_x = 1;
		}
		
		int les = left_ear_sound_x*ear_sound_y/100;
		int res = right_ear_sound_x*ear_sound_y/100;
		
		Log.e("sss", "les="+les+"  res="+res);
		
		change_lr_sound(les,res);
	}
	
	/** 从StereoActivity传来数据调用的函数，用来改变左右耳音量 ，将此发给service*/
	private void change_lr_sound(int lsd,int rsd) {
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("chglsd", lsd);// 向服务传递数据
		intent.putExtra("chgrsd", rsd);// 向服务传递数据
		startService(intent);
	}
	
	@Override
		protected void onStop() {
			super.onStop();
			dpsp.isPressed = true;
			change_lr_sound(100,100);
		}
	
}
