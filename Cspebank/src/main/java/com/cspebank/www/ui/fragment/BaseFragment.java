package com.cspebank.www.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Fragment 的基类
 * @author yisinian.deng
 * 2015.11.18
 */
public abstract class BaseFragment extends Fragment {

	protected final String TAG = getClass().getSimpleName();
	private Bundle savedState;
	private static final String STATE_KEY = "internalSavedViewState";

	public BaseFragment() {
		super();
		if (getArguments() == null) {
			setArguments(new Bundle());
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!restoreStateFromArguments()) {
			//如果从Arguments获取的state为空的话,表示第一次启动
			onFirstTimeLaunched();
		}
	};
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		saveStateToArguments();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//当程序意外终止的时候，保存fragment 状态或数据或view
		saveStateToArguments();
	}

	private void saveStateToArguments() {
		if (getView() != null) {
			savedState = saveState();
		} 
		
		//当数据保存成功时，再放到bundle包里面，用于下次启动的时候获取
		if (savedState != null) {
			Bundle b = getArguments();
			b.putBundle(STATE_KEY, savedState);
		}
	}

	//从Arguments中获取参数
	private boolean restoreStateFromArguments() {
		Bundle b = getArguments();
		savedState = b.getBundle(STATE_KEY);
		if (savedState != null) {
			restoreState();
			return true;
		}
		return false;
	}

	private void restoreState() {
		if (savedState != null) {
			onRestoreState(savedState);
		}
	}

	private Bundle saveState() {
		Bundle state = new Bundle();
		onSaveState(state);
		return state;
	}

	protected abstract void onRestoreState(Bundle savedInstanceState);
	
	protected abstract void onSaveState(Bundle outState);

	protected abstract void onFirstTimeLaunched();
	
}
