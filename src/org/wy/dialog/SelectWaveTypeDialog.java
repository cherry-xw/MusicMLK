package org.wy.dialog;


import org.wy.utils.CommonData;
import org.wy.utils.SaveUtils;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.wy.activity.R;


public class SelectWaveTypeDialog extends DialogFragment{
	private int x = 0;
	private int y = 0;
	
	private RadioButton rb1,rb2,rb3;
	private ImageView iv1,iv2,iv3;
	
	public SelectWaveTypeDialog() {//设置一个空的构造函数，否则在息屏时会报错弹出
		
	}
	
	/**
	 * 屏幕的（x，y）（0，0）坐标是从屏幕正中心开始计算的
	 * @param x横坐标
	 * @param y纵坐标
	 */
	
	public SelectWaveTypeDialog(int x, int y){
		this.x = x;
		this.y = y;
	}
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }
    
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogSelectWaveType;
		getDialog().setCanceledOnTouchOutside(true);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//设置dialog位置
		final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
		layoutParams.x = x;
		layoutParams.y = y;
		layoutParams.width = (int)(CommonData.ScreenWidth/4);
		layoutParams.height = (int)(CommonData.ScreenHeight/4);
		getDialog().getWindow().setAttributes(layoutParams);
	}

    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState){  
        View view = inflater.inflate(R.layout.dialog_select_wave_type, container, false);

        rb1 = (RadioButton) view.findViewById(R.id.rb1);
        rb2 = (RadioButton) view.findViewById(R.id.rb2);
        rb3 = (RadioButton) view.findViewById(R.id.rb3);
        iv1 = (ImageView) view.findViewById(R.id.iv1);
        iv2 = (ImageView) view.findViewById(R.id.iv2);
        iv3 = (ImageView) view.findViewById(R.id.iv3);
        
        if(SaveUtils.getWaveType() == 1){
			rb1.setChecked(true);
			rb2.setChecked(false);
			rb3.setChecked(false);
        }else if(SaveUtils.getWaveType() == 2){
        	rb1.setChecked(false);
			rb2.setChecked(true);
			rb3.setChecked(false);
        }else if(SaveUtils.getWaveType() == 3){
        	rb1.setChecked(false);
			rb2.setChecked(false);
			rb3.setChecked(true);
        }

        rb1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rb2.setChecked(false);
				rb3.setChecked(false);
			}
		});
        
        rb2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rb1.setChecked(false);
				rb3.setChecked(false);
			}
		});
        
        rb3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rb1.setChecked(false);
				rb2.setChecked(false);
			}
		});

        iv1.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				new SeeSelectImageDialog(R.drawable.type1).show(getFragmentManager(), null);
				return false;
			}
		});
        iv2.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				new SeeSelectImageDialog(R.drawable.type1).show(getFragmentManager(), null);
				return false;
			}
		});
        iv3.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				new SeeSelectImageDialog(R.drawable.type1).show(getFragmentManager(), null);
				return false;
			}
		});
        
        iv1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					rb1.setChecked(true);
					rb2.setChecked(false);
					rb3.setChecked(false);
					SaveUtils.setWaveType(1);
				}
				return false;
			}
		});
        iv2.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					rb1.setChecked(false);
					rb2.setChecked(true);
					rb3.setChecked(false);
					SaveUtils.setWaveType(2);
				}
				return false;
			}
		});
        iv3.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					rb1.setChecked(false);
					rb2.setChecked(false);
					rb3.setChecked(true);
					SaveUtils.setWaveType(3);
				}
				return false;
			}
		});
        return view;  
    }
    
    
}
