
package org.wy.ui.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.wy.entity.PicturePos;
import org.wy.utils.CommonData;
import org.wy.utils.MyTimeUtils;
import org.wy.utils.PlayMusic;

import com.wy.activity.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
/**
 * 画播放音乐的位置
 * @author Administrator
 *
 */
public class DrawPersonStereoPos extends View {
	
	private Paint mPaint;
	public Vector<PicturePos> pps = new Vector<PicturePos>();
	private int chosenIndex = -1;
	private int deltaX = 0;
	private int deltaY = 0;
	private PicturePos chosenPic = null;
	//用于通知界面更新
	private Handler handler_draw_stereo;
	
	public boolean isPressed = false;//手指是否按下


	public DrawPersonStereoPos(Context context) {
		super(context);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		mPaint.setAntiAlias(true);//抗锯齿

		Bitmap miao = ((BitmapDrawable)getResources().getDrawable(R.drawable.miao)).getBitmap();
		Bitmap mai = ((BitmapDrawable)getResources().getDrawable(R.drawable.mai)).getBitmap();

		PicturePos pp_miao = new PicturePos();
		PicturePos pp_mai = new PicturePos();

		pp_miao.setX(CommonData.ScreenWidth/2-60);
		pp_miao.setY(CommonData.ScreenHeight-200);
		
		pp_mai.setX(CommonData.ScreenWidth/2-60);
		pp_mai.setY(CommonData.ScreenHeight-400);

		pp_miao.setBitmap(miao);
		pp_mai.setBitmap(mai);
		
		pps.addElement(pp_miao); 
		pps.addElement(pp_mai);
		
		handler_draw_stereo = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				invalidate();
			}
		};
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for(PicturePos p : pps){
			canvas.drawBitmap(p.getBitmap(), p.getX(), p.getY(), mPaint);
		}
	}
	
	/**
	 * 事件只对状态进行修改
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int)event.getX();
		int y = (int)event.getY();
		
		switch(action)
		{
		case MotionEvent.ACTION_DOWN:
			isPressed = true;
			PicturePos p = choosePic(x, y);
			if(p != null){
				deltaX = x - p.getX();
				deltaY = y - p.getY();
				chosenPic = p;
//				reOrderPs(chosenIndex);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(chosenPic != null){
				chosenPic.setX((int)event.getX() - deltaX);
				chosenPic.setY((int)event.getY() - deltaY);
				handler_draw_stereo.sendEmptyMessage(0);
				CommonData.handler_sendtouchpos.sendEmptyMessage(0);
			}
			break;
		case MotionEvent.ACTION_UP:
			isPressed = false;
			deltaX = -1;
			deltaY = -1;
			chosenPic = null;
			moveMusic();
			break;
		}
		return true;
	}

	private Timer mTimer;//计时器
	private TimerTask mTimerTask;
	
	private void moveMusic(){
		mTimer = new Timer();
		mTimerTask = new TimerTask() {
			public void run() {
				if(!isPressed){
					handler_draw_stereo.sendEmptyMessage(0);
					CommonData.handler_sendtouchpos.sendEmptyMessage(1);
				}else{
					mTimer.cancel();
					mTimerTask.cancel();
					mTimer = null;
					mTimerTask = null;
				}
			}
		};
		mTimer.schedule(mTimerTask, 0, 20);
	}
	
	private PicturePos choosePic(int x, int y)
	{
		for(int i=pps.size()-1;i>=0;i--){
			PicturePos ps = pps.elementAt(i);
			if(x>ps.getX() && x<ps.getX()+ps.getBitmap().getWidth()
					&& y>ps.getY() && y<ps.getY()+ps.getBitmap().getHeight()){
				chosenIndex = i;
				return ps;
			}
		}
		return null;
	}
	//改变绘制图层上下关系
	private boolean reOrderPs(int chosenIndex)
	{
		if(!(chosenIndex==pps.size()-1 || chosenIndex<0)){
			PicturePos ps = pps.elementAt(chosenIndex);
			PicturePos ps_next = pps.elementAt(chosenIndex+1);
			pps.setElementAt(ps, chosenIndex+1);
			pps.setElementAt(ps_next, chosenIndex);
			reOrderPs(chosenIndex+1);
		}
		return true;
	}
}
