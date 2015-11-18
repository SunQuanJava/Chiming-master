package com.baizhi.baseapp.application;


import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.util.HashSet;

/**
 * application
 * <p/>
 * Created by sunquan on 2015/5/4 0020.
 */
public class BaseApplication extends Application {
    private static final int MEMORY_CACHE_SIZE = 2 * 1024 * 1024;
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final int DISK_CACHE_FILE_COUNT = 100;
    private static final int THREAD_POOL_SIZE = 3;
    public static Application sInstance;
    private static AQuery aQuery;
    private boolean mIsCheckedUpgrade = false;
    private HashSet<Activity> activityContainer; // 一个Activity类


    public static Application getAppContext() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initImageLoader(getApplicationContext());
    }

    public static synchronized AQuery getAQuery(Context context) {
        if(aQuery == null) {
            aQuery = new AQuery(context.getApplicationContext());
        }
        return aQuery;
    }

    public static Application getInstance() {
        return sInstance;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //clear all memory cached images when system is in low memory
        //note that you can configure the max image cache count, see CONFIGURATION
        BitmapAjaxCallback.clearCache();
        ImageLoader.getInstance().clearMemoryCache();
    }


    /**
     * 添加Activity到LinkList
     */
    public void addActivity(Activity activity) {
        if (activityContainer == null) {
            activityContainer = new HashSet<>();
        }
        if (!activity.isFinishing()) {
            activityContainer.add(activity);
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityContainer == null) {
            return;
        }
        if (activityContainer.isEmpty()) {
            activityContainer = null;
            return;
        }
        for (Activity activity : activityContainer) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        activityContainer.clear();
        activityContainer = null;
    }

    /**
     * 从LinkList中删除Activity
     */
    public void deleteActivity(Activity activity) {
        if (activityContainer == null || activityContainer.isEmpty()) {
            return;
        }
        if (activity != null && activity.isFinishing() && activityContainer.contains(activity)) {
            activityContainer.remove(activity);
        }
    }

    private void initImageLoader(Context context) {

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(THREAD_POOL_SIZE)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(MEMORY_CACHE_SIZE)
                .diskCacheSize(DISK_CACHE_SIZE)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(DISK_CACHE_FILE_COUNT)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs() // Remove for release app
                .build();//开始构建

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
