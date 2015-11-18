package com.sunquan.chimingfazhou.application;


import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.baizhi.baseapp.application.BaseApplication;
import com.sunquan.chimingfazhou.service.MusicPlayService;

import de.greenrobot.event.EventBus;

/**
 * application
 * <p/>
 * Created by sunquan on 2015/5/4 0020.
 */
public class MyApplication extends BaseApplication {

    MusicPlayService mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            //用绑定方法启动service，就是从这里绑定并得到service，然后就可以操作service了
            mService = ((MusicPlayService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    public static Application getAppContext() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final Intent intent = new Intent(this, MusicPlayService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    public MusicPlayService getService() {
        return mService;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        EventBus.clearCaches();
    }
}
