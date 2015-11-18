package com.baizhi.baseapp.activity;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.androidquery.util.AQUtility;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.baizhi.baseapp.R;
import com.baizhi.baseapp.application.BaseApplication;
import com.baizhi.baseapp.util.DialogUtil;
import com.baizhi.widget.ActionSheet;
import com.baizhi.baseapp.widget.TopBarView;
import com.baizhi.baseapp.widget.WToast;

import java.util.List;


/**
 * activity基类
 *
 * @author sunquan1
 * @date 2015-5-4
 * @since 1.0.0
 */
public abstract class BaseActivity extends FragmentActivity implements ActionSheet.ActionSheetListener {

    protected static final int LEFT_BUTTON = 0;
    protected static final int RIGHT_BUTTON = 1;
    private static final long BACKGROUND_MAX_DURATION = 1000 * 60 * 10L;
    protected static Boolean mIsActive = false;
    private ViewGroup mContainer;
    private TopBarView mTopView;
    private WToast wToast;
    private Dialog mProgressDialog;
    private long backTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isAddToActivityContainer()) {
            ((BaseApplication) getApplication()).addActivity(this);
        }
        wToast = new WToast(this);
        setContentView(R.layout.base_main);
        mContainer = (ViewGroup) findViewById(R.id.container);
        mTopView = (TopBarView) findViewById(R.id.top);
        mTopView.setVisibility(View.VISIBLE);
        if (isShowTitle()) {
            mTopView.setVisibility(View.VISIBLE);
            mTopView.setLeftOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    handleClickEvent(LEFT_BUTTON);
                }
            });
            mTopView.setRightOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        handleClickEvent(RIGHT_BUTTON);
                    }
            });
        } else {
            mTopView.setVisibility(View.GONE);
        }

    }

    protected boolean isAddToActivityContainer() {
        return true;
    }


    protected boolean isShowTitle() {
        return true;
    }

    public void setContentLayout(int resId) {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(resId, mContainer, false);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(layoutParams);
        mContainer.addView(contentView);
    }


    protected void handleClickEvent(int event) {
    }

    protected void setContentLayout(View view) {
        mContainer.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    protected void setCenterContentIcon(int resId) {
        mTopView.setCenterImageContent(resId);
    }

    protected void setRightContentIcon(int resId) {
        mTopView.setRightImageContent(resId);
    }

    protected void setLeftContentIcon(int resId) {
        mTopView.setLeftImageContent(resId);
    }

    protected void setLeftEnable(boolean isEnable) {
        mTopView.setLeftEnable(isEnable);
    }

    protected void setRightEnable(boolean isEnable) {
        mTopView.setRightEnable(isEnable);
    }

    protected void setLeftText(String text, int drawableLeft) {
        mTopView.setLeftTextContent(text, drawableLeft);
    }

    protected void setRightText(String text, int drawableRight) {
        mTopView.setRightTextContent(text, drawableRight);
    }

    protected void setCenterText(String text) {
        mTopView.setCenterTextContent(text);
    }

    protected void showMessage(String message) {
        wToast.showMessage(message);
    }

    protected void showMessage(int message) {
        wToast.showMessage(message);
    }

    protected void showProgress(String message) {
        mProgressDialog = DialogUtil.createProgressDialog(this, message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected Dialog createProgressDialog(String message) {
        return DialogUtil.createProgressDialog(this, message);
    }

    protected void dismissProgress() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception ignored) {
        }
    }

    protected void showActionSheet(String... itemStr) {
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet.createBuilder(this, this.getSupportFragmentManager()).setCancelButtonTitle(R.string.cancel)
                .setOtherButtonTitles(itemStr).setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index, String text) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsActive) {
            mIsActive = true;
            onForegroundRunning();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground() && mIsActive) {
            mIsActive = false;
            onBackgroundRunning();
        }
    }

    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    protected void onForegroundRunning() {
    }

    protected void onBackgroundRunning() {
    }

    protected void clearCache() {
        //clean the file cache with advance option
        long triggerSize = 5000000; //大于5M时候开始清除
        long targetSize = 2000000;      //直到少于2M
        AQUtility.cleanCacheAsync(BaseActivity.this, triggerSize, targetSize);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.scale_hold, R.anim.out_to_right);
    }

    /**
     * 显示对话框
     */
    protected void showErrorDialog(final String msg) {
        SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle(msg)
                .setPositiveButtonText(android.R.string.ok)
                .setTag("errorDialog").showSinglton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isAddToActivityContainer()) {
            ((BaseApplication) getApplication()).deleteActivity(this);
        }
    }
}
