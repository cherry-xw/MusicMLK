package org.wy.dialog;

import org.wy.db.MusicListDao;
import org.wy.utils.MyTimeUtils;
import org.wy.utils.PlayMusic;

import android.app.DialogFragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wy.activity.R;

public class MusicMsgDialog extends DialogFragment {
	
	private View view;
	private int x = 0;
	private int y = 0;
	private EditText msgmusicname,msgsinger,msgoptical,msgtime,msgroute,msgmusicsize;
	private Button msgmodifycancel,msgmodifydown;
	
	
	public MusicMsgDialog() {
	}

	/**
	 * 屏幕的（x，y）（0，0）坐标是从屏幕正中心开始计算的
	 * @param x横坐标
	 * @param y纵坐标
	 */
	
	public MusicMsgDialog(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, 0);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogMusicMsg;
		getDialog().setCanceledOnTouchOutside(false);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//设置dialog位置
		final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
		layoutParams.x = x;
		layoutParams.y = y;
		getDialog().getWindow().setAttributes(layoutParams);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.dialog_showmusicmsg, null);
		initView();
		init();
		return view;
	}
	
	private void initView(){
		msgmusicname = (EditText) view.findViewById(R.id.msgmusicname);
		msgsinger = (EditText) view.findViewById(R.id.msgsinger);
		msgoptical = (EditText) view.findViewById(R.id.msgoptical);
		msgtime = (EditText) view.findViewById(R.id.msgtime);
		msgroute = (EditText) view.findViewById(R.id.msgroute);
		msgmodifycancel = (Button) view.findViewById(R.id.msgmodifycancel);
		msgmodifydown = (Button) view.findViewById(R.id.msgmodifydown);
		msgmusicsize = (EditText) view.findViewById(R.id.msgmusicsize);
		
	}
	
	
	
	private void init(){
		if(PlayMusic.musicInfo != null){
			msgmusicname.setText(PlayMusic.musicInfo.getTilte());
			msgsinger.setText(PlayMusic.musicInfo.getArtist());
			msgoptical.setText(PlayMusic.musicInfo.getAlbum());
			msgtime.setText(MyTimeUtils.Ms2mmss(PlayMusic.musicInfo.getDuration()));
			msgroute.setText(PlayMusic.musicInfo.getUrl());
			msgmusicsize.setText((PlayMusic.musicInfo.getSize()/1024/1024)+" Mb");
		}else{
			msgmusicname.setText("N/A");
			msgsinger.setText("N/A");
			msgoptical.setText("N/A");
			msgtime.setText("00:00");
			msgroute.setText("N/A");
			msgmusicsize.setText("0 Mb");
		}
		
		
		msgmusicname.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ClipboardManager cmb = (ClipboardManager)view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(msgmusicname.getText());
				Toast.makeText(view.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		msgsinger.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ClipboardManager cmb = (ClipboardManager)view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(msgsinger.getText());
				Toast.makeText(view.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		msgroute.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ClipboardManager cmb = (ClipboardManager)view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(msgroute.getText());
				Toast.makeText(view.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		msgtime.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ClipboardManager cmb = (ClipboardManager)view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(msgtime.getText());
				Toast.makeText(view.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		msgroute.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ClipboardManager cmb = (ClipboardManager)view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(msgroute.getText());
				Toast.makeText(view.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		
		msgmodifycancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MusicMsgDialog.this.dismiss();
			}
		});
		
		msgmodifydown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MusicMsgDialog.this.dismiss();
			}
		});
	}
	
}
