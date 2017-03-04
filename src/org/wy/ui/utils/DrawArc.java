
package org.wy.ui.utils;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

public class DrawArc extends View {
	private float[] mBytes;
	
	private Paint mPaint;
	private float origin_x;
	private float origin_y;
	private int selectedcolor;
	
	private RectF oval;
	
	private int mSpectrumNum = 90;//总线条数
	

	public DrawArc(Context context) {
		super(context);
		init();
	}

	private void init() {
		mBytes = null;
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);//画笔颜色
		mPaint.setStrokeWidth(2);//线条宽度
		mPaint.setAntiAlias(true);//抗锯齿
	}

	public void updateVisualizer(byte[] fft, String selectedcolor) {
		float[] model = new float[fft.length / 2 + 1];
		
		model[0] = (float) Math.abs(fft[0]);
		int i = 2;
		for (int j = 1; j < mSpectrumNum; j++) {
			model[j] = (float) Math.hypot(fft[i], fft[i + 1]);
			i += 2;
		}
		mBytes = model;
		if(mPaint != null){
			try {
				mPaint.setColor(Color.parseColor(selectedcolor));
			} catch (Exception e) {
				Log.e("sss", "color change is error", e);
			}
			
		}
		invalidate();
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mBytes == null) {
			return;
		}
		int j=0;
		int k=0;
		mPaint.setStrokeWidth(3);
		for (int i = 0; i < mSpectrumNum; i+=2) {
			mBytes[i] = (mBytes[i]+mBytes[i+1])/2;
			
			if(mBytes[i]>15 && mBytes[i]<18){
				mBytes[i]+=15;
			}
			if(mBytes[i]>13 && mBytes[i]<15){
				mBytes[i]+=12;
			}
			if(mBytes[i]>10 && mBytes[i]<13){
				mBytes[i]+=10;
			}
			if(mBytes[i]>8 && mBytes[i]<10){
				mBytes[i]+=7;
			}
			if(mBytes[i]>5 && mBytes[i]<8){
				mBytes[i]+=5;
			}
			origin_x = getWidth()/2;
			origin_y = getHeight()/2;
			
			//画圆形
			mPaint.setStyle(Paint.Style.STROKE);
			oval = new RectF(origin_x-i*4-50, origin_y-i*4-50, origin_x+i*4+50, origin_y+i*4+50);
			canvas.drawArc(oval, 270, mBytes[i]/4*5, false, mPaint);
			canvas.drawArc(oval, 270-mBytes[i]/4*5, mBytes[i]/2*3, false, mPaint);
		}
		
		//画低音节拍
		mPaint.setStrokeWidth(25);
		RectF oval1 = new RectF(origin_x-mSpectrumNum*4-50, origin_y-mSpectrumNum*4-50, origin_x+mSpectrumNum*4+50, origin_y+mSpectrumNum*4+50);

		float bb = (mBytes[0] + mBytes[1])/2;
		canvas.drawArc(oval1, 90, bb/4*5, false, mPaint);
		canvas.drawArc(oval1, 90-bb/4*5, bb/2*3, false, mPaint);
	}
}
