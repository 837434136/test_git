package com.cspebank.www.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cspebank.www.utils.LogTrace;

/**
 * @author hg
 *发送广播，提示注销
 */
public class LogOutReceiver extends BroadcastReceiver {
	
	private Activity mActivity;
	private final String TAG = getClass().getSimpleName();

	public LogOutReceiver(Activity activity){
		this.mActivity=activity;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("studentLogOut")){
			mActivity.finish();
			LogTrace.i(TAG + "finish");
		}
	}
}
