package org.wy.dialog;


import com.wy.activity.R;

import android.app.DialogFragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class LoadingDialog extends DialogFragment 
{
	private ImageView mImage;
	private int x = 0;
	private int y = 0;
	private int resImgId;
	private boolean is_star = false;
	
	

	/**
	 * 屏幕的（x，y）（0，0）坐标是从屏幕正中心开始计算的
	 * @param x横坐标
	 * @param y纵坐标
	 * @param is_star是否是星星
	 */
	
	public LoadingDialog(int x, int y, boolean is_star){
		this.x = x;
		this.y = y;
		this.is_star = is_star;
	}
	
	public LoadingDialog(int x, int y, int resImgId){
		this.x = x;
		this.y = y;
		this.resImgId = resImgId;
	}

	public LoadingDialog(boolean is_star){
		this.is_star = is_star;
	}
	
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
		layoutParams.x = x;
		layoutParams.y = y;
		getDialog().getWindow().setAttributes(layoutParams);
	}

    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) 
    {  
        View view = inflater.inflate(R.layout.dialog_load, container, false);
        mImage = (ImageView) view.findViewById(R.id.load_img_star);
        
        if(is_star){
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_load_cir);
            mImage.startAnimation(hyperspaceJumpAnimation);
            setCancelable(false);
        }else{
            mImage.setImageResource(R.drawable.anim_load_run);
    		AnimationDrawable animationDrawable = (AnimationDrawable) mImage.getDrawable();
    		animationDrawable.start();
        }
        return view;  
    }
    
    
}
