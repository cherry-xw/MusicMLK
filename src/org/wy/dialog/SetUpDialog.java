package org.wy.dialog;


import java.util.Vector;

import org.wy.activity.ShowWaveFormActivity;
import org.wy.utils.CommonData;
import org.wy.utils.SaveUtils;

import android.app.DialogFragment;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;

import com.wy.activity.R;


public class SetUpDialog extends DialogFragment 
{
	private View view;
	private LinearLayout setback,setwaveback,setwavetype;
	private CheckBox setscreenbright;
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }
    

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//设置dialog位置
		final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
		layoutParams.width = (int)(CommonData.ScreenWidth/2);
		layoutParams.height = (int)(CommonData.ScreenHeight/5*3);
		layoutParams.x = (int)(CommonData.ScreenWidth/5);
		layoutParams.y = (int)(CommonData.ScreenHeight/90);
		getDialog().getWindow().setAttributes(layoutParams);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogPrompt;
		getDialog().setCanceledOnTouchOutside(true);
	}
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) 
    {  
        view = inflater.inflate(R.layout.dialog_setup, container, false);  
        init();
        return view;  
    }

    private Vector<Integer> myMainImageIds = new Vector<Integer>();//保存主界面背景图片id数据
    private Vector<Integer> myWaveImageIds = new Vector<Integer>();//保存波形背景图片id数据

    private void init(){
    	setback = (LinearLayout) view.findViewById(R.id.setback);
    	setwaveback = (LinearLayout) view.findViewById(R.id.setwaveback);
    	setwavetype = (LinearLayout) view.findViewById(R.id.setwavetype);
    	setscreenbright = (CheckBox) view.findViewById(R.id.setscreenbright);
    			
    	
    	setback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    /**添加背景数据 */
				myMainImageIds.removeAllElements();
				myMainImageIds.add(R.drawable.mlk);
				myMainImageIds.add(R.drawable.back1);
				myMainImageIds.add(R.drawable.back2);
				myMainImageIds.add(R.drawable.back3);
				new ShowImgListDialog(myMainImageIds,"main").show(getFragmentManager(), null);
			}
		});
    	setwaveback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myWaveImageIds.removeAllElements();
				myWaveImageIds.add(R.drawable.backw1);
				myWaveImageIds.add(R.drawable.backw2);
				myWaveImageIds.add(R.drawable.backw3);
				myWaveImageIds.add(R.drawable.backw4);
				myWaveImageIds.add(R.drawable.backw5);
				myWaveImageIds.add(R.drawable.backw6);
				new ShowImgListDialog(myWaveImageIds,"wave").show(getFragmentManager(), null);
			}
		});
    	
    	setwavetype.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        int[] location = new int[2];
		        v.getLocationOnScreen(location);
				new SelectWaveTypeDialog(location[0]-CommonData.ScreenWidth/8*5, 
						location[1]-CommonData.ScreenHeight/4-setwavetype.getHeight()/2).show(getFragmentManager(), null);
		        Toast.makeText(view.getContext(), R.string.longclickcansee, Toast.LENGTH_SHORT).show();
			}
		});
    	
    	setscreenbright.setChecked(SaveUtils.getScerrnBright());
    	
    	setscreenbright.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(SaveUtils.getScerrnBright()){
					SaveUtils.setScerrnBright(false);
					CommonData.handler_changetutu.sendEmptyMessage(8);
				}else{
					SaveUtils.setScerrnBright(true);
					CommonData.handler_changetutu.sendEmptyMessage(7);
				}
			}
		});
    	
    }
    
}
