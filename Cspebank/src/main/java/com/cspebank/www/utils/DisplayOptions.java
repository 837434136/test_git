package com.cspebank.www.utils;

import android.graphics.Bitmap;

import com.cspebank.www.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by HandSomeBoy on 2015/8/31.
 */
public class DisplayOptions {

    public static DisplayImageOptions getlistoptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                .showImageOnLoading(R.mipmap.ic_launcher)
                        // 设置图片uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                        // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)
                        // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                        // 设置下载的图片是否缓存在sd卡中
                .cacheOnDisc(true)
                        // 保留exif信息  启动EXIF和JPEG图片模式
                .considerExifParams(true)
                        // 设置图片以如何的编码方式显示
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // .decodingoptions(android.graphics.bitmapfactory.options*
                        // decodingoptions)//设置图片的解码配置
                        // 设置图片下载前的延迟
                .delayBeforeLoading(0)// int
                        // delayinmillis为你设置的延迟时间
                        // 设置图片加入缓存前，对bitmap进行设置
                        // .preprocessor(bitmapprocessor preprocessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                        // .displayer(new roundedbitmapdisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 淡入
                .build();
        return options;
    }



}
