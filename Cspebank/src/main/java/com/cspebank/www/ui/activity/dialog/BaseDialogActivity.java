package com.cspebank.www.ui.activity.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;

import com.cspebank.www.R;
import com.cspebank.www.app.CspeManager;
import com.cspebank.www.receiver.NetReceiver;
import com.cspebank.www.utils.LogTrace;
import com.cspebank.www.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

public class BaseDialogActivity extends Activity{

	private Activity activity;
	public Callback callback;
	private CspeManager mManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		mManager = new CspeManager();
		mManager.addActivity(activity);
		
		callback = new Callback() {

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				return false;
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}
		};
//		registerNetReceiver();
	}
	
	/**
	 * 监听网络的广播
	 */
//	private void registerNetReceiver() {
//		netReceiver = new NetStateReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(Constant.CONNECTIVITY_CHANGE_ACTION);
//		registerReceiver(netReceiver, filter);
//	}

	/**
	 * @author deng
	 * 监听网络的广播接收器
	 */
	class NetStateReceiver extends NetReceiver {

		@Override
		public void noNet() {
			ToastUtils.showShort(R.string.network_abnormal_and_check);
		}

		@Override
		public void hasNet() {
			//应用从无网变有网的时候
		}
	}
	
	@Override
    protected void onResume() {
    	super.onResume();
    	MobclickAgent.onResume(this);
    	LogTrace.e("----------> onResume()");
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	MobclickAgent.onPause(this);
    	LogTrace.e("----------> onPause()");
    }
	
	 @Override
	protected void onDestroy() {
		 mManager.removeActivity(activity);
//		unregisterReceiver(netReceiver);
		super.onDestroy();
	}
	
}
