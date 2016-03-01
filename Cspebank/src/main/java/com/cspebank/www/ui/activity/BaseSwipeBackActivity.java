package com.cspebank.www.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cspebank.www.R;
import com.cspebank.www.app.CspeManager;
import com.cspebank.www.app.SystemStatusActionbar;
import com.cspebank.www.receiver.NetReceiver;
import com.cspebank.www.utils.LogTrace;
import com.cspebank.www.utils.ToastUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityHelper;
import com.umeng.analytics.MobclickAgent;
import com.yisiinian.httputils.RequestManager;

/**
 * @author yisinian.deng
 * time 2015-10-02
 */
public class BaseSwipeBackActivity extends FragmentActivity implements SlidingMenu.OnOpenedListener {

	private Activity activity;
	private SlidingActivityHelper mHelper;
	private SlidingMenu mSlidingMenu;
	public Callback callback;
//	private NetReceiver netReceiver;
	private CspeManager mManager;
	private RequestManager requestManager = new RequestManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//添加activity 到界面列表里面
		mManager = new CspeManager();
		mManager.addActivity(this);
		activity = this;
		SystemStatusActionbar.setTranslucentStatus(activity);
		 mHelper = new SlidingActivityHelper(this);
	     mHelper.onCreate(savedInstanceState);
	   //这里借用了SlidingMenu的setBehindContentView方法来设置一个透明菜单
	    View behindView = new View(this);
	    behindView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
	    		ViewGroup.LayoutParams.MATCH_PARENT));
	    behindView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setBehindContentView(behindView);
        
        
        mSlidingMenu = getSlidingMenu();
       //设置阴影宽度为10个px
        mSlidingMenu.setShadowWidth(10);
        //设置阴影
//        mSlidingMenu.setShadowDrawable(R.drawable.slide_shadow);
        //设置下面的布局，也就是我们上面定义的透明菜单离右边屏幕边缘的距离为0，也就是滑动开以后菜单会全屏幕显示
       // SlidingMenu划出时主页面显示的剩余宽度
        mSlidingMenu.setBehindOffset(0);
       //SlidingMenu滑动时的渐变程度
        mSlidingMenu.setFadeDegree(0.35f);
        //菜单打开监听，因为菜单打开后我们要finish掉当前的Activity
        mSlidingMenu.setOnOpenedListener(this);
        //设置手势滑动方向，因为我们要实现微信那种右滑动的效果，这里设置成SlidingMenu.LEFT模式
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        //因为微信是只有边缘滑动，我们设置成TOUCHMODE_MARGIN模式，如果你想要全屏幕滑动，只需要把这个改成OUCHMODE_FULLSCREEN就OK了
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        
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
//        registerNetReceiver();
        super.onCreate(savedInstanceState);
        
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate(savedInstanceState);
	}
	
//	@Override
//	public boolean onSupportNavigateUp() {
//		return true;
//	}
	
	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null){
            return v;
		}
		return mHelper.findViewById(id);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mHelper.onSaveInstanceState(outState);
	}
	
	@Override 
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, null));
    }

    @Override
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        super.setContentView(v, params);
        mHelper.registerAboveContentView(v, params);
    }
	
	public void setBehindContentView(int id) {
	        setBehindContentView(getLayoutInflater().inflate(id, null));
    }

    public void setBehindContentView(View v) {
	        setBehindContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 		
	        		ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setBehindContentView(View v, ViewGroup.LayoutParams params) {
        mHelper.setBehindContentView(v, params);
    }
	
    public SlidingMenu getSlidingMenu() {
        return mHelper.getSlidingMenu();
    }

    public void toggle() {
        mHelper.toggle();
    }

    public void showContent() {
        mHelper.showContent();
    }

    public void showMenu() {
        mHelper.showMenu();
    }

    public void showSecondaryMenu() {
        mHelper.showSecondaryMenu();
    }

    public void setSlidingActionBarEnabled(boolean b) {
        mHelper.setSlidingActionBarEnabled(b);
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	boolean b = mHelper.onKeyUp(keyCode, event);
    	if (b) {
			return b;
		}
    	return super.onKeyUp(keyCode, event);
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
    
   //滑动完全打开菜单后结束掉当前的Activity
	@Override
	public void onOpened() {
		this.onPause();
		this.onStop();
		this.finish();
	}

    @Override
    public void finish() {
    	super.finish();
    	this.overridePendingTransition(0, R.anim.sliding_out_right);
    }
	
    /**
     * 禁止点击菜单键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_MENU) {
    		//do nothing 独占本次事件
			return true;
		}
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	requestManager.cancelRequest(toString(), true);
    }
    
    @Override
	protected void onDestroy() {
    	mManager.removeActivity(this);
//		unregisterReceiver(netReceiver);
		super.onDestroy();
	}
    
    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestcode, resultcode, data);
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
}
