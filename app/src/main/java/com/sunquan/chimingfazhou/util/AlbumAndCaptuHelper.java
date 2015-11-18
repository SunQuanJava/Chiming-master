package com.sunquan.chimingfazhou.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.sunquan.chimingfazhou.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by sunquan1 on 2015/1/4.
 */
public class AlbumAndCaptuHelper {

    public static final int RESULT_CODE_CROP_BY_CAPTURE = 22; // 通过相机取出的照片裁剪
    public static final int RESULT_CODE_CROP_BY_ALBUM = 23; // 通过相册取出的照片裁剪
    public static final int RESULT_CODE_CAPTURE = 20; // 拍照上传
    public static final int RESULT_CODE_ALBUM = 21; // 相册上传
    /**
     * 相册
     */
    public static void albumSelected(Activity activity) {
        Intent intentAlbum = new Intent(Intent.ACTION_PICK, null);
        intentAlbum.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intentAlbum, RESULT_CODE_ALBUM);
        activity.overridePendingTransition(R.anim.scale_in,R.anim.scale_hold);
    }

    /**
     * 拍照
     */
    public static void captureSeleced(Activity activity,File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intent, RESULT_CODE_CAPTURE);
        activity.overridePendingTransition(R.anim.scale_in,R.anim.scale_hold);
    }

    /**
     * 裁剪图片
     *
     * @param uri
     * @param requestCode
     */
    public static void doCropPhoto(Activity activity,File file, Uri uri, int requestCode) {
        if (requestCode == RESULT_CODE_CROP_BY_ALBUM && Build.VERSION.SDK_INT >= 19) {
            String dataString = Uri.decode(uri.getPath());
            String id;
            try {
                id = dataString.substring(dataString.lastIndexOf("/") + 1, dataString.length());
                if (id.contains(":")) {
                    id = id.substring(id.lastIndexOf(":") + 1, id.length());
                }
            } catch (Exception e) {
                id = "0";
            }

            String path = MediaProviderUtil.getDataColumn(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{id});
            if (TextUtils.isEmpty(path)) {
                path = "";
            }
            File in = new File(path);
            try {
                FileInputStream inFile;
                inFile = new FileInputStream(in);
                FileOutputStream outFile = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int i = 0;
                while ((i = inFile.read(buffer)) != -1) {
                    outFile.write(buffer, 0, i);
                }
                inFile.close();
                outFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.scale_in,R.anim.scale_hold);
    }
}
