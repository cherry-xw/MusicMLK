
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

public class DrawPointCircleLine extends View {
	private float[] mBytes;

	private float[] mBytes_draw_point;
	
	private Paint mPaint;
	private float[] mPoints;

	private BigDecimal width, height;
	
	private final float radius = 250;//半径
	private float origin_x;
	private float origin_y;
	
	private RectF oval;
	
	private int mSpectrumNum = 180;//总线条数
	private float angle;
	

	public DrawPointCircleLine(Context context) {
		super(context);
		init();
	}

	private void init() {
		mBytes = null;
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);//画笔颜色
		mPaint.setStrokeWidth(4);//线条宽度
		mPaint.setAntiAlias(true);//抗锯齿
		width = new BigDecimal(getWidth()/2+"");
		height = new BigDecimal(getHeight()/2+"");

		angle = 360 / mSpectrumNum;
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
		
		for (int i = 0; i < mSpectrumNum; i++) {
			
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
			

			if (mPoints == null || mPoints.length < mBytes.length * 4) {
				mPoints = new float[mBytes.length * 4];
			}
			if (mBytes_draw_point == null || mBytes_draw_point.length < mBytes.length * 2) {
				mBytes_draw_point = new float[mBytes.length * 2];
			}
			
			//画圆点线组合
			double angle_real = Math.PI/180*(i*angle+180);
			mBytes_draw_point[k++] = origin_x + (float) (Math.cos(angle_real)*(radius-mBytes[i]*2));
			mBytes_draw_point[k++] = origin_y + (float) (Math.sin(angle_real)*(radius-mBytes[i]*2));
			mPoints[j++] = (float) (Math.cos(angle_real)*radius) + origin_x;
			mPoints[j++] = (float) (Math.sin(angle_real)*radius) + origin_y;
			
			mPoints[j++] = (float) (Math.cos(angle_real)*(radius+mBytes[i]*2)) + origin_x;
			mPoints[j++] = (float) (Math.sin(angle_real)*(radius+mBytes[i]*2)) + origin_y;
			
		}
		//画点
		canvas.drawPoints(mBytes_draw_point, mPaint);
		//画线
		canvas.drawLines(mPoints, mPaint);
		//画圆
		mPaint.setStyle(Paint.Style.STROKE);
		RectF oval_ = new RectF(origin_x-radius, origin_y-radius, origin_x+radius, origin_y+radius);
		canvas.drawArc(oval_, 0, 360, false, mPaint);
	}
}
