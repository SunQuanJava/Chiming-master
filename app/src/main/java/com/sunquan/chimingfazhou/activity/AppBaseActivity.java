package com.sunquan.chimingfazhou.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.baizhi.baseapp.activity.BaseActivity;
import com.baizhi.baseapp.util.PreferencesUtil;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.application.MyApplication;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;


/**
 * activity基类
 *
 * @author sunquan1
 * @date 2015-5-4
 * @since 1.0.0
 */
public abstract class AppBaseActivity extends BaseActivity {

    private static final long BACKGROUND_MAX_DURATION = 1000 * 60 * 10L;
    private long backTime;

    @Override
    protected void onForegroundRunning() {
        if((GlobalDataHolder.getInstance(this).getXiuInfo()==null||TextUtils.isEmpty(GlobalDataHolder.getInstance(this).getUid())) && GlobalDataHolder.getInstance(this).isLogin()) {
            ((MyApplication) getApplication()).finishAllActivity();
            startActivity(new Intent(this, SplashActivity.class));
        }
        else if (backTime != 0 && System.currentTimeMillis() - backTime > BACKGROUND_MAX_DURATION && !((MyApplication) getApplication()).getService().isShowNotification()) {
            ((MyApplication) getApplication()).finishAllActivity();
            startActivity(new Intent(this, SplashActivity.class));
        }
        backTime = 0;
    }

    @Override
    protected void onBackgroundRunning() {
        backTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //清除缓存
                clearCache();
                //保存文件
                GlobalDataHolder.getInstance(AppBaseActivity.this).saveXiuDatasToDB();
            }
        }).start();
    }

    /**
     * 新手引导页
     *
     * @param key
     * @param resId
     * @param dismissId
     */
    protected void checkNewGuidePage(final String key, final int resId, final int dismissId, final OnClickListener buttonClickListener){
        if (!PreferencesUtil.getBooleanByName(this, key, false)) {
            final Dialog dialog = new Dialog(this, R.style.coverDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(resId);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            final WindowManager.LayoutParams lay = dialog.getWindow().getAttributes();
            lay.height = WindowManager.LayoutParams.MATCH_PARENT;
            lay.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface d) {
                    dialog.getWindow().findViewById(dismissId).setOnClickListener(null);
                }
            });
            dialog.getWindow().findViewById(dismissId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        PreferencesUtil.setBooleanByName(AppBaseActivity.this, key, true);
                    }
                    if(buttonClickListener!=null) {
                        buttonClickListener.onClick(v);
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    /**
     * 新手引导页
     *
     * @param key
     * @param resId
     * @param dismissId
     */
    protected void checkNewGuidePage(final String key, final int resId, final int dismissId){
        checkNewGuidePage(key,resId,dismissId,null);
    }

}
