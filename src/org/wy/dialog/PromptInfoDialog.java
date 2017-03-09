package org.wy.dialog;

import org.wy.db.MusicListDao;
import org.wy.utils.CommonData;

import com.wy.activity.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PromptInfoDialog extends DialogFragment {
	
	private View view;
	private TextView promprtmsg,promprtmsgps;
	private String strmsg = "";
	private String strmsgps = "";
	private ImageView cancelbtn,deletebtn;
	private String url = "aa";//删除的音乐的id

	public PromptInfoDialog(String strmsg, String strmsgps){
		this.strmsg = strmsg;
		this.strmsgps = strmsgps;
	}
	//删除音乐接口
	public PromptInfoDialog(String strmsg, String strmsgps, String url){
		this.strmsg = strmsg;
		this.strmsgps = strmsgps;
		this.url = url;
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
		getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogPrompt;

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.dialog_promptinfo, null);
		init();
		return view;
	}
	
	private void init(){
		promprtmsg = (TextView) view.findViewById(R.id.promprtmsg);
		promprtmsgps = (TextView) view.findViewById(R.id.promprtmsgps);
		promprtmsg.setText(strmsg);
		promprtmsgps.setText(strmsgps);
		
		cancelbtn = (ImageView) view.findViewById(R.id.cancelbtn);
		deletebtn = (ImageView) view.findViewById(R.id.deletebtn);
		
		cancelbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(CommonData.handler_deletemusic != null){
//					CommonData.handler_deletemusic.sendEmptyMessage(-1);
//				}
				PromptInfoDialog.this.dismiss();
			}
		});
		
		deletebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(url.endsWith("aa")){
					//非删除单首音乐使用
					//清空列表音乐
//					if(CommonData.handler_deletemusic != null){
//						CommonData.handler_deletemusic.sendEmptyMessage(1);
//					}else{
//						Toast.makeText(view.getContext(), R.string.notify_musiclist, Toast.LENGTH_SHORT).show();
//					}
				}else{
					//删除音乐使用
					
				}
				PromptInfoDialog.this.dismiss();
			}
		});
	}
	
}
