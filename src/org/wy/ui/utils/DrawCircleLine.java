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

public class DrawCircleLine extends View {
	private float[] mBytes;
	private float[] mBytes_copy;

	private float[] mBytes_draw_point;
	
	private Paint mPaint;
	private float[] mPoints;
	private float[] mPoints_link;

	private BigDecimal width, height;
	
	private final float radius = 240;//半径
	private float origin_x;
	private float origin_y;
	
	private RectF oval;
	
	private int mSpectrumNum = 180;//总线条数
	private float angle;
	
	private int rotating_angle = -90;//起始绘线点旋转一定角度
	

	public DrawCircleLine(Context context) {
		super(context);
		init();
	}

	private void init() {
		mBytes = null;
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);//画笔颜色
		mPaint.setStrokeWidth(2);//线条宽度
		mPaint.setAntiAlias(true);//抗锯齿
		width = new BigDecimal(getWidth()/2+"");
		height = new BigDecimal(getHeight()/2+"");

		angle = 360 / mSpectrumNum;
	}

	public void updateVisualizer(byte[] fft, String selectedcolor) {
//		Log.e("st1", fft.length + "");
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
		
		for (int i = 0; i < mSpectrumNum; i+=2) {
			for(int x = 1; x < 2; x++){
				mBytes[i]+=mBytes[i+x];
			}mBytes[i]/=2;

			if(mBytes[i]>18 && mBytes[i]<22){
				mBytes[i]+=19;
			}
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
			
			//画线仅线
			mPoints[j] = origin_x + (float) (Math.cos(Math.PI/180*(i*angle+rotating_angle))*(radius-mBytes[i]*2));
			mPoints[j+1] = origin_y + (float) (Math.sin(Math.PI/180*(i*angle+rotating_angle))*(radius-mBytes[i]*2));
			
			mPoints[j+2] = origin_x + (float) (Math.cos(Math.PI/180*(i*angle+rotating_angle))*(radius+mBytes[i]*2));
			mPoints[j+3] = origin_y + (float) (Math.sin(Math.PI/180*(i*angle+rotating_angle))*(radius+mBytes[i]*2));
			
			j+=4;
		}
		int i;
		for (i = 0; i < mSpectrumNum*2; i+=4) {
			if (mPoints_link == null || mPoints_link.length < mPoints.length * 2) {
				mPoints_link = new float[mPoints.length * 2];
			}
			mPoints_link[k++] = mPoints[i];
			mPoints_link[k++] = mPoints[i+1];
			mPoints_link[k++] = mPoints[i+4];
			mPoints_link[k++] = mPoints[i+5];

			mPoints_link[k++] = mPoints[i+2];
			mPoints_link[k++] = mPoints[i+3];
			mPoints_link[k++] = mPoints[i+6];
			mPoints_link[k++] = mPoints[i+7];
		}
		k-=8;i-=4;
		mPoints_link[k++] = mPoints[i];
		mPoints_link[k++] = mPoints[i+1];
		mPoints_link[k++] = mPoints[0];
		mPoints_link[k++] = mPoints[1];

		mPoints_link[k++] = mPoints[i+2];
		mPoints_link[k++] = mPoints[i+3];
		mPoints_link[k++] = mPoints[2];
		mPoints_link[k++] = mPoints[3];
		
		//画点
//		canvas.drawPoints(mBytes_draw_point, mPaint);
		//画线
		canvas.drawLines(mPoints, mPaint);
		canvas.drawLines(mPoints_link, mPaint);
		//画圆
//		mPaint.setStyle(Paint.Style.STROKE);
//		RectF oval_ = new RectF(origin_x-radius, origin_y-radius, origin_x+radius, origin_y+radius);
//		canvas.drawArc(oval_, 0, 360, false, mPaint);
	}
}
