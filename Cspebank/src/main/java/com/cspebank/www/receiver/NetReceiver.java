package com.cspebank.www.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.cspebank.www.R;
import com.cspebank.www.utils.LogTrace;
import com.cspebank.www.utils.NetWorkHelper;
import com.cspebank.www.utils.ToastUtils;

/**
 * @author yisinian.deng
 * 动态的一个网络监听接收器
 */
public abstract class NetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (context == null) {
			return;
		}

		String action = intent.getAction();
		LogTrace.i("网络状态变化 : " + action);
		if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
			//网络变化的时候会发送通知
			if (!NetWorkHelper.isNetAvailable(context)) {
				ToastUtils.showShort(R.string.network_abnormal_and_check);
				noNet();
			}else {
				hasNet();
			}
			return;
		}
	}

	public abstract void noNet();
	public abstract void hasNet();
	
}
