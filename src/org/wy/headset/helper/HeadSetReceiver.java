package org.wy.headset.helper;

import java.util.Timer;
import java.util.TimerTask;

import org.wy.headset.helper.HeadSetHelper.OnHeadSetListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

public class HeadSetReceiver extends BroadcastReceiver{

	Timer timer = null;
	OnHeadSetListener headSetListener = null;
	private static boolean isTimerStart = false;
	private static MyTimer myTimer = null;
	//��д���췽�������ӿڰ󶨡���Ϊ����ĳ�ʼ���������ԡ�
	public HeadSetReceiver(){
		timer = new Timer(true);
		this.headSetListener = HeadSetHelper.getInstance().getOnHeadSetListener();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 String intentAction = intent.getAction() ;
	        if(Intent.ACTION_MEDIA_BUTTON.equals(intentAction)){
	        	//���KeyEvent����  
	        	KeyEvent keyEvent = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT); 
	        	if(headSetListener != null){
	        		try {
	        			if(keyEvent.getAction() == KeyEvent.ACTION_UP){
	        				if(isTimerStart){
	        					myTimer.cancel();
	        					isTimerStart = false;
	        					headSetListener.onDoubleClick();
	        				}else{
	        					myTimer = new MyTimer();
	        					timer.schedule(myTimer,1000);
	        					isTimerStart = true;
	        				}
	        			}
					} catch (Exception e) {
						// TODO: handle exception
					}
	        	}	
	        }
	        //��ֹ�㲥(���ñ�ĳ����յ��˹㲥�����ܸ���)
//	        abortBroadcast();
	}
	/*
	 * ��ʱ���������ӳ�1�룬�����޲�����Ϊ����
	 */
	class MyTimer extends TimerTask{
			
			@Override
			public void run() {
				try {
					myHandle.sendEmptyMessage(0);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
	};
	/*
	 * ��handle��Ŀ����Ҫ��Ϊ�˽��ӿ������߳��д���
	 * ��Ϊ�˰�ȫ����ѽӿڷŵ����̴߳���
	 */
	Handler myHandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			headSetListener.onClick();
			isTimerStart = false;
		}
		
	};
		
}
