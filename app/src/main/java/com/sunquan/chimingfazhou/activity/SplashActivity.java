package com.sunquan.chimingfazhou.activity;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import com.baizhi.baseapp.controller.BaseHandler;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;

import java.lang.ref.WeakReference;

/**
 * 启动页
 *
 * Created by Administrator on 2015/4/29.
 */
public class SplashActivity extends AppBaseActivity {

    private static final long STOP_DURATION = 2000l;

    private static final int LOGIN = 0;
    private static final int MAIN_PAGE = 1;

    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.splash_layout);
        mHandler = new MyHandler(this);
        checkLogin();
    }

    /**
     * 检查是否登录
     */
    private void checkLogin() {
        final UserInfo userInfo = GlobalDataHolder.getInstance(this).getUserInfo();
        //未登录
        if(userInfo == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessageDelayed(LOGIN,STOP_DURATION);
                }
            }).start();
        }
        //已经登录成功，从数据库获取修的功课，并跳转到首页
        else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //初始化该用户的功课记录
                    GlobalDataHolder.getInstance(SplashActivity.this).initXiuDatasFromDB();
                    mHandler.sendEmptyMessageDelayed(MAIN_PAGE,STOP_DURATION);
                }
            }).start();
        }
    }

    @Override
    protected boolean isShowTitle() {
        return false;
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void goToMainPage() {
        IntentUtils.goToMainPage(this);
    }

    private void goToLoginPage() {
        IntentUtils.goToLoginPage(this);
    }

    static class MyHandler extends BaseHandler {

        private WeakReference<SplashActivity> reference;

        public MyHandler(SplashActivity splashActivity) {
            super(Looper.getMainLooper());
            reference = new WeakReference<>(splashActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SplashActivity splashActivity = reference.get();
            if(splashActivity == null) {
                return;
            }
            splashActivity.finish();
            switch (msg.what) {
                //跳转到登录页
                case LOGIN:
                    splashActivity.goToLoginPage();
                    break;
                //跳转到主页
                case MAIN_PAGE:
                    splashActivity.goToMainPage();
                    break;
            }
        }
    }

    @Override
    protected void onBackgroundRunning() {
    }

    @Override
    protected void onForegroundRunning() {
    }
}
