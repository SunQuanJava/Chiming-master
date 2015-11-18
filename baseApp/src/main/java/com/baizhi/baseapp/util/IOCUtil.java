package com.baizhi.baseapp.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/6/18.
 */
public final class IOCUtil {

    /**
     * 从文件中读取开发团队数据信息
     *
     * @param context
     * @param filename
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> getListDataFromAssert(Context context,String filename,Class<T[]> clazz) {
        ArrayList<T> arrayList = null;
        try {
            final InputStream inputStream = context.getResources().getAssets().open(filename);
            final String jsonStr = getStringFromInputStream(inputStream);
            if(!TextUtils.isEmpty(jsonStr)) {
                final T[] arr = new Gson().fromJson(jsonStr, clazz);
                final List<T> list =  Arrays.asList(arr);
                if(!list.isEmpty()) {
                    arrayList = new ArrayList<>(list.size());
                    arrayList.addAll(list);
                }
            }
        } catch (Exception ignored) {
            arrayList = new ArrayList<>();
        }
        return arrayList;
    }

    /**
     * 从文件中读取开发团队数据信息
     *
     * @return
     */
    public static <T> T getDataFromAssert(Context context,String filename,Class<T> clazz) {
        T t = null;
        try {
            final InputStream inputStream = context.getResources().getAssets().open(filename);
            final String jsonStr = getStringFromInputStream(inputStream);

            if(!TextUtils.isEmpty(jsonStr)) {
                if(clazz == JSONObject.class) {
                    t  = (T) new JSONObject(jsonStr);
                } else {
                    final Gson gson = new Gson();
                    t  = gson.fromJson(jsonStr, clazz);
                }

            }
        } catch (Exception e) {
            t = null;
        }
        return t;
    }


    public static Bitmap getImageFromAssetsFile(Context context,String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }



    private static String getStringFromInputStream(InputStream a_is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(a_is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ignored) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }
}
