package com.cspebank.www.utils;

import android.content.Context;
import android.widget.Toast;

import com.cspebank.www.app.CspeApplication;

/**
 * @author deng
 * 2015-10-02
 */
public class ToastUtils {

    public static Toast toast = null;

    private ToastUtils() {

    }

    private static void show(Context context, int resId, int duration) {
        if (toast == null){
            toast = Toast.makeText(context, resId, duration);
        }else {
            toast.setText(resId);
        }
        toast.show();
    }

    private static void show(Context context, String message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, message, duration);
        }else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showShort(int resId) {
        if (toast == null){
            toast = Toast.makeText(CspeApplication.getInstance(), resId, Toast.LENGTH_SHORT);
        }else {
            toast.setText(resId);
        }
        toast.show();
    }

    public static void showShort(String message) {
        if (toast == null){
            toast = Toast.makeText(CspeApplication.getInstance(), message, Toast.LENGTH_SHORT);
        }else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showLong(int resId) {
        if(toast == null){
            toast = Toast.makeText(CspeApplication.getInstance(), resId, Toast.LENGTH_LONG);
        }else {
            toast.setText(resId);
        }
        toast.show();
    }

    public static void showLong(String message) {
        if(toast == null){
            toast = Toast.makeText(CspeApplication.getInstance(), message, Toast.LENGTH_LONG);
        }else {
            toast.setText(message);
        }
        toast.show();
    }

}
