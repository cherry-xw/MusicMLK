package org.wy.receiver;

import org.wy.utils.CommonData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PlayerReceiver extends BroadcastReceiver {

	
	public PlayerReceiver(){
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getStringExtra("action");
		CommonData.isNotiControl = true;
			if(action.equals("close_noti")){
				CommonData.manager.cancelAll();
				CommonData.handler_changetutu.sendEmptyMessage(2);
			}else if(action.equals("playing")){
				CommonData.handler_changetutu.sendEmptyMessage(1);
			}else if(action.equals("pause")){
				CommonData.handler_changetutu.sendEmptyMessage(3);
			}else if(action.equals("last")){
				CommonData.handler_changetutu.sendEmptyMessage(4);
			}else if(action.equals("next")){
				CommonData.handler_changetutu.sendEmptyMessage(6);
			}
	}

}
