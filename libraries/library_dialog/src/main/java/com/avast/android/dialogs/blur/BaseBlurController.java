package com.avast.android.dialogs.blur;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.avast.android.dialogs.R;
import com.avast.android.dialogs.core.BaseDialogFragment;

/**
 * Created by tiantong on 2015/2/25.
 */
public class BaseBlurController {
    private static final String TAG = BaseBlurController.class.getSimpleName();
    /**
     * Engine used to blur.
     */
    private BlurDialogEngine mBlurDialogEngine;

    /**
     * Allow to set a Toolbar which isn't set as actionbar.
     */
    private Toolbar mToolbar;
    protected boolean mUseBlur = false;
    private static final String USE_BLUR = "BaseDialogFragment_USE_BLUR";

    public void doOnCreate(BaseDialogFragment fragment, Bundle savedInstanceState) {
        Log.i(TAG, "onCreate mUseBlur:" + mUseBlur);
        if (savedInstanceState != null) {
            //在savednstanceState中如果有值，可能界面发生过变化
            mUseBlur = savedInstanceState.getBoolean(USE_BLUR, true);
        }
        Resources resources = fragment.getResources();
        if (mUseBlur) {
            //如果bulder中开启blur，则根据value中的参数设置,这样可以配置一些情况不能使用blur(当前默认hdpi不开启blur)
            mUseBlur = resources.getBoolean(R.bool.dialog_blur);
        }
        if (!mUseBlur) {
            //如果获取本地的设置未开启，则不设置
            Log.i(TAG, "!mUseBlur return");
            return;
        }
        mBlurDialogEngine = new BlurDialogEngine(fragment.getActivity());
        if (mToolbar != null) {
            mBlurDialogEngine.setToolbar(mToolbar);
        }
        final boolean blurDilaogDebug = resources.getBoolean(R.bool.blur_dialog_debug);
        mBlurDialogEngine.debug(blurDilaogDebug);
        final int blurRadius = resources.getInteger(R.integer.dialog_blur_radius);
        mBlurDialogEngine.setBlurRadius(blurRadius);
        final float downScale = (float) resources.getInteger(R.integer.dialog_blur_downgrade);
        mBlurDialogEngine.setDownScaleFactor(downScale);
        final boolean useRenderScript = resources.getBoolean(R.bool.blur_dialog_use_render_script);
        mBlurDialogEngine.setUseRenderScript(useRenderScript);
        final boolean blurActionBar = resources.getBoolean(R.bool.blur_dialog_blur_actionbar);
        mBlurDialogEngine.setBlurActionBar(blurActionBar);
    }

    public void doOnResume(BaseDialogFragment fragment) {
        Log.i(TAG, "onResume mUseBlur:" + mUseBlur);
        if (mUseBlur) {
            mBlurDialogEngine.onResume(fragment.getRetainInstance());
        }
    }

    public void doOnDismiss() {
        Log.i(TAG, "onDismiss mUseBlur:" + mUseBlur);
        if (mUseBlur) {
            mBlurDialogEngine.onDismiss();
        }
    }

    public void doOnDestory() {
        Log.i(TAG, "onDestroy mUseBlur:" + mUseBlur);
        if (mUseBlur) {
            mBlurDialogEngine.onDestroy();
        }
    }

    /**
     * 默认UseBlur为true，直接调用xml中的参数
     *
     * @param useBlur
     */
    public void setUseBlur(boolean useBlur) {
        this.mUseBlur = useBlur;
    }

    public void doOnSaveInstanceState(Bundle outState) {
        if (!mUseBlur) {
            outState.putBoolean(USE_BLUR, mUseBlur);
        }
    }

    /**
     * Allow to set a Toolbar which isn't set as ActionBar.
     * <p/>
     * Must be called before onCreate.
     *
     * @param toolBar toolBar
     */
    public void setToolbar(Toolbar toolBar) {
        mToolbar = toolBar;
        if (mBlurDialogEngine != null) {
            mBlurDialogEngine.setToolbar(toolBar);
        }
    }
}
