package com.cspebank.www.utils;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 类说明： 对话框弹出帮助类
 * 
 * @date Jan 1, 2014 9:27:27 AM
 * @version 1.0
 */
public class DialogUtils {

	public static void dialogBuilder(Activity instance, String title,
			String positiveText, String negativeText, int layoutId,
			final DialogCallBack callBack) {

		Builder builder = new Builder(instance);
		builder.setTitle(title);
		builder.setCancelable(false);

		LayoutInflater inflater = LayoutInflater.from(instance);
		View view = inflater.inflate(layoutId, null);

		builder.setView(view);

		// 确定
		builder.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (callBack != null) {
							callBack.onComplete();
						}
					}
				});

		// 取消
		builder.setNegativeButton(negativeText,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (callBack != null) {
							callBack.onCancel();
						}
					}
				});

		if (!instance.isFinishing()) {
			builder.create().show();
		}
	}

	/**
	 * 弹出询问窗口
	 *
	 * @param
	 * @param
	 */
	public static void dialogBuilder(Activity instance, String title,
			String message, String positiveText, String negativeText,
			final DialogCallBack callBack) {
		Builder builder = new Builder(instance);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setCancelable(false);

		// 确定
		builder.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (callBack != null) {
							callBack.onComplete();
						}
					}
				});

		// 取消
		builder.setNegativeButton(negativeText,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (callBack != null) {
							callBack.onCancel();
						}
					}
				});

		if (!instance.isFinishing()) {
			builder.create().show();
		}
	}

	/**
	 * 弹出询问窗口
	 * 
	 * @param
	 * @param
	 */
	public static void dialogBuilder(Activity instance, String title,
			String message, final DialogCallBack callBack) {
		dialogBuilder(instance, title, message, "确认", "取消", callBack);
	}

	public interface DialogCallBack {

		public void onComplete();

		public void onCancel();
	}
}