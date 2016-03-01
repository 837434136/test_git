package com.cspebank.www.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义控件的基类
 * @author yisinian.deng
 * 2015.11.18
 */
public class BaseView extends View{
	
	public BaseView(Context context) {
		super(context);
	}

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
}
