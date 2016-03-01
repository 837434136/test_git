package com.cspebank.www.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;

import java.io.IOException;

public class Photo {
  public static final Bitmap photoAdapter(String path, Bitmap bitmap) {
    int degree=0;
        if (Build.MANUFACTURER.equals("samsung")) {
//          System.out.println("读取角度:"+readPictureDegree(path));
          degree=readPictureDegree(path);
          bitmap=rotaingImageView(degree, bitmap);  
        }
    return bitmap;
  }
    
    /*
     * 旋转图片 
     * @param angle 
     * @param bitmap 
     * @return Bitmap 
     */  
    public static final Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
        //旋转图片 动作  
        Matrix matrix = new Matrix();;  
        matrix.postRotate(angle);  
        //System.out.println("angle2=" + angle);  
        // 创建新的图片  
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
        return resizedBitmap;  
    }
    
    /**
       * 读取图片属性：旋转的角度
       * @param path 图片绝对路径
       * @return degree旋转的角度
       */
    public static final int readPictureDegree(String path) {
      int degree  = 0;
      try {
              ExifInterface exifInterface = new ExifInterface(path);
              int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
              switch (orientation) {
              case ExifInterface.ORIENTATION_ROTATE_90:
                      degree = 90;
                      break;
              case ExifInterface.ORIENTATION_ROTATE_180:
                      degree = 180;
                      break;
              case ExifInterface.ORIENTATION_ROTATE_270:
                      degree = 270;
                      break;
              }
      } catch (IOException e) {
              e.printStackTrace();
      }
      return degree;
    }
}
