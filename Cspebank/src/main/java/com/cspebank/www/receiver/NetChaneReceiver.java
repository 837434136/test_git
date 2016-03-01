package com.cspebank.www.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.cspebank.www.R;
import com.cspebank.www.utils.Constant;
import com.cspebank.www.utils.LogTrace;
import com.cspebank.www.utils.NetWorkHelper;
import com.cspebank.www.utils.PreferencesUtils;
import com.cspebank.www.utils.ToastUtils;

/**
 * @author yisinian.deng
 * 静态的一个网络监听接收器
 */
public class NetChaneReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if (context == null) {
			return;
		}

		String action = intent.getAction();
		LogTrace.e("网络状态变化 : " + action);
		if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
			//网络变化的时候会发送通知
			if (!NetWorkHelper.isNetAvailable(context)) {
				ToastUtils.showShort(R.string.network_abnormal_and_check);
			}else {
				if (!PreferencesUtils.getBoolean(context, Constant.IS_VISITOR, true)) {
					LogTrace.i("需要刷新");
				}
			}
			return;
		}
	}

}
