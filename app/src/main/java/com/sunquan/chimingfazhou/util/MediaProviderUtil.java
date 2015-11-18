package com.sunquan.chimingfazhou.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;

import com.sunquan.chimingfazhou.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MediaProviderUtil {

  public static final int  REQUEST_PHOTO_FROM_CAMERA = 0x1;
  public static final int  REQUEST_PHOTO_FROM_ALBUM = 0x2;
  public static final int  REQUEST_VIDEO_FROM_CAMERA = 0x3;
  public static final int  REQUEST_VIDEO_FROM_ALBUM = 0x4;


  public static final int QUALITY_HIGH_VIDEO = 0x1;
  public static final int QUALITY_LOW_VIDEO = 0x2;

  private static MediaProviderUtil mediaProviderUtil = new MediaProviderUtil();
  private File file = null;

  public static MediaProviderUtil getInstance(){
    if (mediaProviderUtil == null) {
      mediaProviderUtil = new MediaProviderUtil();
    }
    return mediaProviderUtil;
  }

  public File getPhotoFile(){
    return file;
  }

  public void photoFromAlbum(Context context) {
    Intent intent = new Intent(Intent.ACTION_PICK, null);
    intent.setDataAndType(
      Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    ((Activity) context).startActivityForResult(intent, REQUEST_PHOTO_FROM_ALBUM);
      ((Activity) context).overridePendingTransition(R.anim.scale_in,R.anim.scale_hold);
  }

  public void videoFromAlbum(Context context) {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI, "video/*");
    Intent wrapperIntent = Intent.createChooser(intent, null);
    ((Activity) context).startActivityForResult(wrapperIntent, REQUEST_VIDEO_FROM_ALBUM);
    ((Activity) context).overridePendingTransition(R.anim.scale_in,R.anim.scale_hold);
  }


  /**
   * @param path
   * @return 是否是用了旋转修正
   */
  public static boolean rotaingImageView(String path) {  
    
    if (TextUtils.isEmpty(path)) {
      return false;
    }
    
    int angle = readPictureDegree(path);
    
    if (angle == 0) {
      return false;
    }
    //旋转图片 动作  
    Matrix matrix = new Matrix();;  
    matrix.postRotate(angle);  
    // 创建新的图片  
    Bitmap bitmap = null;

    try {
      bitmap = setThumbail(path, 320*480);
      if (bitmap == null) {
        return false;
      }
      Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
        bitmap.getWidth(), bitmap.getHeight(), matrix, true); 
      File file = new File(path); 
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)); 
      resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
      bos.flush();//输出 
      bos.close();//关闭 
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        bitmap.recycle();
      } catch (Exception e2) {
      }
    }
    return true;
  }
  
  public static int readPictureDegree(String path) {  
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
    } catch (Exception e) {  
      e.printStackTrace();  
    }  
    return degree;  
  }  

  
  public static int getDurationColumn(Context context, String[] selectionArgs) {  

    Cursor cursor = null;  
    final String column = "duration";  
    final String[] projection = {column};  

    try {  
      cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, "_id=?", selectionArgs, null);  
      if (cursor != null && cursor.moveToFirst()) {  
        return cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
      }  
    } finally {  
      if (cursor != null)  
        cursor.close();  
    }  
    return 60000;  
  } 
  
  public static String getDataColumn(Context context, Uri uri, String[] selectionArgs) {  

    Cursor cursor = null;  
    final String column = "_data";  
    final String[] projection = {column};  

    try {  
      cursor = context.getContentResolver().query(uri, projection, "_id=?", selectionArgs, null);  
      if (cursor != null && cursor.moveToFirst()) {  
        final int index = cursor.getColumnIndexOrThrow(column);  
        return cursor.getString(index);  
      }  
    } finally {  
      if (cursor != null)  
        cursor.close();  
    }  
    return null;  
  }  

  public static long durationByUri(Context context, Uri uri) {
    long l = 0L;
    if (context == null || uri == null){
      return l;
    }
    try {
      Cursor localCursor = context.getContentResolver().query(uri, null, null, null, null);
      if ((localCursor != null) && (localCursor.getCount() > 0)) {
        localCursor.moveToFirst();
        l = localCursor.getInt(localCursor.getColumnIndexOrThrow("duration"));
        localCursor.close();
      }
    } catch (Exception e) {
      l = 60000;
      e.printStackTrace();
    }
    return l;
  }

  public static String getMediaPath(Context context, Uri uri){
    if (context == null || uri == null) {
      return null;
    }
    try {
      ContentResolver cr = context.getContentResolver();
      Cursor cursor = cr.query(uri, null, null, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
        return cursor.getString(1);
      }
    } catch (Exception e) {
    }
    return null;
  }

  public static Bitmap setThumbail(String path, int area){
    BitmapFactory.Options opts = new BitmapFactory.Options();  
    opts.inJustDecodeBounds = true;  
    BitmapFactory.decodeFile(path, opts);
    opts.inSampleSize = computeSampleSize(opts, -1, area);  
    opts.inJustDecodeBounds = false;  

    try {  
      return BitmapFactory.decodeFile(path, opts);  
    }catch (Exception e) {  
      return null;
    }  

  }

  public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {  
    int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);  
    int roundedSize;  
    if (initialSize <= 8) {  
      roundedSize = 1;  
      while (roundedSize < initialSize) {  
        roundedSize <<= 1;  
      }  
    } else {  
      roundedSize = (initialSize + 7) / 8 * 8;  
    }  
    return roundedSize;  
  }  

  private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
    double w = options.outWidth;  
    double h = options.outHeight;  
    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
    int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
    if (upperBound < lowerBound) {  
      // return the larger one when there is no overlapping zone.  
      return lowerBound;  
    }  
    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
      return 1;  
    } else if (minSideLength == -1) {  
      return lowerBound;  
    } else {  
      return upperBound;  
    }  
  }  
}
