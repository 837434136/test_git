package com.cspebank.www.utils;

import android.widget.LinearLayout;

import com.cspebank.www.app.CspeApplication;

import java.text.SimpleDateFormat;

/**
 * @author yisinian.deng 2015.11.18 定义全局变量 例如： 广播action
 */
public class Constant {

		public static final String API = "http://xiaomi.yisinian.cn/service";

	//是否是第一次启动
	public static final String IS_FIRST = "is_first";
	//是否是游客
	public static final String IS_VISITOR = "is_visitor";
	// 用于Fragment再被意外销毁的时候保存上下文
	public static final String M_CONTEXT = "mContext";

	public static final int SCREENWIDTH = CspeApplication.getContext()
			.getResources().getDisplayMetrics().widthPixels;
	public static final int SCREENHEIGH = CspeApplication.getContext()
			.getResources().getDisplayMetrics().heightPixels;
	public static final int SET_CALENDAR_VIEW_SIZE = 15;
	public static final LinearLayout.LayoutParams EACHVEIWLAYOUTPARAMS =
			new LinearLayout.LayoutParams((SCREENWIDTH / 7 - SET_CALENDAR_VIEW_SIZE),
			(SCREENWIDTH / 7 - SET_CALENDAR_VIEW_SIZE));
	public static final SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyyMMdd");

}
