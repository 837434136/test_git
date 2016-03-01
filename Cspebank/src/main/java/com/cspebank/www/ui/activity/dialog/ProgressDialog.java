package com.cspebank.www.ui.activity.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cspebank.www.R;

/*
 * 通用不确定进度条弹窗
 */
public class ProgressDialog extends Activity{
	
	public static final String FINISH_PROGRESS_DIALOG = "finish_progress_dialog";
	
	
	/** 
     * 得到自定义的progressDialog 不明确进度条
     */  
    public static Dialog createLoadingDialog(Context context, String msg) {  
  
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(R.layout.dialog_progress, null);// 得到加载view  
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.common_ll_progress_view);// 加载布局
        // main.xml中的ImageView  
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.common_progress_dialog);  
        TextView tipTextView = (TextView) v.findViewById(R.id.common_tv_progress_dialog);// 提示文字  
//        // 加载动画  
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.progress_loading_anim);  
//         使用ImageView显示动画  
        
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
        tipTextView.setText(msg);// 设置加载信息  
  
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog  
  
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局  
        return loadingDialog;
  
    }
}
