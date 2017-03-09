package org.wy.activity;

import org.wy.ui.utils.DrawArc;
import org.wy.ui.utils.DrawCircleLine;
import org.wy.ui.utils.DrawLine;
import org.wy.ui.utils.DrawPointCircleLine;
import org.wy.utils.CommonData;
import org.wy.utils.MyTimeUtils;
import org.wy.utils.PlayMusic;
import org.wy.utils.SaveUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wy.activity.R;

public class ShowWaveFormActivity extends Activity {
	
	//显示音乐波形画图界面对象
	private DrawCircleLine drawCircleLine;
	private DrawPointCircleLine drawPointCircleLine;
	private DrawArc drawPoint;
	private DrawLine drawLine;
	
	private Spinner selectpaintcolor;
	private ArrayAdapter<CharSequence> adapter;
	private String[] myallcolors;
	private String selectedcolor = "#FFFFFFFF";

	private LinearLayout layout_content,my_wave_background;
	
	private SlidingMenu mLeftMenu;
	
	private TextView leftmusicname,leftmusicauthor,leftmusictime,leftmusiccurrenttime,leftmusicartist;
	private ImageView rightbeforebtn, rightnextbtn;
	private ImageButton leftmusicback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_waveform);
		
		mLeftMenu = (SlidingMenu) findViewById(R.id.showmusicmessage);
		mLeftMenu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		mLeftMenu.setShadowWidthRes(R.dimen.shadow_width);
		mLeftMenu.setMenu(R.layout.leftmenu);
		mLeftMenu.setShadowDrawable(R.drawable.shadow1);
		mLeftMenu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if (mLeftMenu.isMenuShowing())
					mLeftMenu.toggle();
			}
		});
		
		initView();
		init();
	}
	
	private void initView() {
		layout_content = (LinearLayout) findViewById(R.id.layout_content);
		my_wave_background = (LinearLayout) findViewById(R.id.my_wave_background);
		leftmusicback = (ImageButton) findViewById(R.id.leftmusicback);
		leftmusicname = (TextView) findViewById(R.id.leftmusicname);
		leftmusicauthor = (TextView) findViewById(R.id.leftmusicauthor);
		leftmusictime = (TextView) findViewById(R.id.leftmusictime);
		leftmusiccurrenttime = (TextView) findViewById(R.id.leftmusiccurrenttime);
		leftmusicartist = (TextView) findViewById(R.id.leftmusicartist);
		rightbeforebtn = (ImageView) findViewById(R.id.rightbeforebtn);
		rightnextbtn = (ImageView) findViewById(R.id.rightnextbtn);
		selectpaintcolor = (Spinner) findViewById(R.id.selectpaintcolor);
	}
	
	private void init() {
		showMusicMessage();
		leftmusicback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Resources res = getResources () ; 
		myallcolors = res.getStringArray(R.array.myallcolors);
		
		adapter = ArrayAdapter.createFromResource(this,R.array.myallcolors1,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectpaintcolor.setAdapter(adapter);
		selectpaintcolor.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				selectedcolor = myallcolors[position];
//				Log.e("sss", "sss="+selectedcolor);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		selectpaintcolor.setVisibility(View.VISIBLE);
		

		// 上一曲
		rightbeforebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rightlastOne();
			}
		});

		// 下一曲
		rightnextbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rightnextOne();
			}
		});
		
		if(SaveUtils.getMainBackData() != 0){
			my_wave_background.setBackgroundResource(SaveUtils.getWaveBackData());
		}
		
		
		if(SaveUtils.getWaveType() == 1){
			drawPointCircleLine = new DrawPointCircleLine(this);
			drawPointCircleLine.setLayoutParams(layout_content.getLayoutParams());
			layout_content.addView(drawPointCircleLine);
		}else if(SaveUtils.getWaveType() == 2){
			drawCircleLine = new DrawCircleLine(this);
			drawCircleLine.setLayoutParams(layout_content.getLayoutParams());
			layout_content.addView(drawCircleLine);
		}else if(SaveUtils.getWaveType() == 3){
			drawPoint = new DrawArc(this);
			drawPoint.setLayoutParams(layout_content.getLayoutParams());
			layout_content.addView(drawPoint);
		}else if(SaveUtils.getWaveType() == 4){
			drawLine = new DrawLine(this);
			drawLine.setLayoutParams(layout_content.getLayoutParams());
			layout_content.addView(drawLine);
		}
		
		CommonData.handler_draw_waveform = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					byte[] fft1 = msg.getData().getByteArray("myfftdata");
					drawPointCircleLine.updateVisualizer(fft1,selectedcolor);
					break;
				case 2:
					byte[] fft2 = msg.getData().getByteArray("myfftdata");
					drawCircleLine.updateVisualizer(fft2,selectedcolor);
					break;
				case 3:
					byte[] fft3 = msg.getData().getByteArray("myfftdata");
					drawPoint.updateVisualizer(fft3,selectedcolor);
					break;
				case 4:
					byte[] fft4 = msg.getData().getByteArray("myfftdata");
					drawLine.updateVisualizer(fft4,selectedcolor);
					break;
				case 5:
					leftmusiccurrenttime.setText(MyTimeUtils.Ms2mmss(msg.getData().getInt("current")));
					break;
				case 6:
					showMusicMessage();
					break;
				default:
					break;
				}
				
				
			}
		};
		
	}
	
	private void showMusicMessage(){
		if(PlayMusic.musicInfo != null){
			leftmusicname.setText(PlayMusic.musicInfo.getTilte());
			String strmsgs1 = "";
			if(PlayMusic.musicInfo.getArtist().equals("<unknown>")){
				strmsgs1 = "未知歌手";
			}else{
				strmsgs1 = PlayMusic.musicInfo.getArtist();
			}
			leftmusicauthor.setText(strmsgs1);
			
			String strmsgs2 = "";
			if(PlayMusic.musicInfo.getArtist().equals("<unknown>")){
				strmsgs2 = "未知专辑";
			}else{
				strmsgs2 = PlayMusic.musicInfo.getAlbum();
			}
			leftmusicartist.setText(strmsgs2);
			leftmusictime.setText(MyTimeUtils.Ms2mmss(PlayMusic.musicInfo.getDuration()));
		}else{
			leftmusicname.setText("当前列表无音乐");
			leftmusicauthor.setText("无歌手");
			leftmusicartist.setText("无");
			leftmusictime.setText("00:00");
		}
	}

	/** 上一首 */
	private void rightlastOne() {
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("lastornext", 0);// 向服务传递数据
		startService(intent);
	}

	/** 下一首 */
	private void rightnextOne() {
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("lastornext", 1);// 向服务传递数据
		startService(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent();
		intent.setAction(CommonData.SERVICE_MUSIC);
		intent.putExtra("showwaveform", 1);
		startService(intent);
		CommonData.handler_draw_waveform = null;
	}
}
