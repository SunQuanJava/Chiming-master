package com.sunquan.chimingfazhou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.INegativeButtonDialogListener;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.baizhi.baseapp.util.MediaUtils;
import com.baizhi.baseapp.util.PreferencesUtil;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.models.XiuSubInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;
import com.sunquan.chimingfazhou.widget.CarouselImageView;

/**
 * 计数页面
 * <p/>
 * Created by Administrator on 2015/5/15.
 */
public class XiuCounterActivity extends AppBaseActivity implements View.OnClickListener {

    private static String XIU_COUNT = "xiu_count";

    public static final int MAX_COUNT = 9999999;
    private XiuSubInfo mXiuSubInfo;
    private TextView mCountNumberView;
    private int mCountNumber;
    private boolean mIsSoundOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_xiu_counter_layout);
        mXiuSubInfo = (XiuSubInfo) getIntent().getSerializableExtra(IntentUtils.EXTRA_XIU_COUNTER);
        mCountNumber = Integer.valueOf(mXiuSubInfo.getCount());
        initTitleBar();
        initViews();
    }

    private void initTitleBar() {
        setLeftContentIcon(R.drawable.back_icon_selector);
        mIsSoundOpen = PreferencesUtil.getBooleanByName(this, "counter_music", true);
        setRightContentIcon(mIsSoundOpen ? R.drawable.xiu_music : R.drawable.xiu_none_music);
        setCenterText(mXiuSubInfo.getParent_task_name());
    }

    private void initViews() {
        final CarouselImageView mBackgroundView = (CarouselImageView) findViewById(R.id.carouse_background);
        final int[] resIds = {R.drawable.xiu_counter_background_1, R.drawable.xiu_counter_background_2, R.drawable.xiu_counter_background_3, R.drawable.xiu_counter_background_4, R.drawable.xiu_counter_background_5};
        mBackgroundView.setBackgroundResIds(resIds).play();
        final TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(mXiuSubInfo.getTask_name());
        final View okView = findViewById(R.id.ok);
        final View cancelView = findViewById(R.id.cancel);
        final View counterView = findViewById(R.id.count_button);
        mCountNumberView = (TextView) findViewById(R.id.count);
        mCountNumberView.setText(String.valueOf(mCountNumber));
        okView.setOnClickListener(this);
        cancelView.setOnClickListener(this);
        counterView.setOnClickListener(this);

        checkNewGuidePage(XIU_COUNT,R.layout.xiu_cover_count_pager,R.id.cover_cancel);
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) {
            onBackPressed();
        } else if (event == RIGHT_BUTTON) {
            mIsSoundOpen = !mIsSoundOpen;
            PreferencesUtil.setBooleanByName(this, "counter_music", mIsSoundOpen);
            setRightContentIcon(mIsSoundOpen ? R.drawable.xiu_music : R.drawable.xiu_none_music);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.count_button:
                if (mIsSoundOpen) MediaUtils.playXiuAudio(this);
                if (!isExceed()) {
                    mCountNumber++;
                    mCountNumberView.setText(String.valueOf(mCountNumber));
                }
                break;
            case R.id.ok:
                if (isModify()) saveCount();
                break;
            case R.id.cancel:
                if (isModify()) showCancelDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isModify()) {
            showSavePromptDialog();
        }
    }

    private boolean isExceed() {
        if (mCountNumber >= MAX_COUNT) {
            showMessage(getString(R.string.counter_max_massage));
            return true;
        }
        return false;
    }

    private boolean isModify() {
        if (mCountNumber == Integer.valueOf(mXiuSubInfo.getCount())) {
            showMessage(R.string.no_add_counter_message);
            super.onBackPressed();
            return false;
        }
        return true;
    }

    private void saveCount() {
        mXiuSubInfo.setCount(String.valueOf(mCountNumber));
        mXiuSubInfo.setLast_practice_time(String.valueOf(System.currentTimeMillis()));
        showMessage(getString(R.string.counter_saved_message));
        IntentUtils.backToXiuDetailPage(XiuCounterActivity.this, mXiuSubInfo, getIntent().getIntExtra(IntentUtils.EXTRA_XIU_COUNTER_POSITION, -1));
    }

    private void showCancelDialog() {
        SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle(getString(R.string.cancel_counter_title_message))
                .setMessage(getString(R.string.cancel_counter_message))
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        XiuCounterActivity.super.onBackPressed();
                    }
                })
                .setTag("xiu_counter_cancel_message")
                .showSinglton();
    }

    private void showSavePromptDialog() {
        SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle(getString(R.string.save_count_title_message))
                .setMessage(getString(R.string.save_count_message))
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        saveCount();
                    }
                })
                .setNegativeListener(new INegativeButtonDialogListener() {
                    @Override
                    public void onNegativeButtonClicked(int requestCode) {
                        XiuCounterActivity.super.onBackPressed();
                    }
                })
                .setTag("xiu_counter_save_message")
                .showSinglton();
    }

}
