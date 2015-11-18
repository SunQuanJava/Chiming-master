package com.sunquan.chimingfazhou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baizhi.baseapp.controller.LoadController;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;
import com.sunquan.chimingfazhou.util.MoblieFormat;
import org.json.JSONObject;


/**
 * 填写验证码
 * Created by Administrator on 2015/6/9.
 */
public class VerificationCodeActivity extends BaseSwipeBackActivity implements View.OnClickListener, Runnable {
    /**
     * 最大读秒数
     */
    private static final int MAXTIME = 60;
    /**
     * 倒计时
     */
    private TextView mTvTime;
    /**
     * 验证码
     */
    private EditText mEtCode;
    /**
     * 电话号码
     */
    private String mPhoneStr = null;

    private int mCount = MAXTIME;
    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentLayout(R.layout.activity_verification_code_layout);
        final Intent intent = getIntent();
        mPhoneStr = intent.getStringExtra("phone");
        mAction = intent.getAction();
        if (mPhoneStr == null) {
            showMessage("error");
        }
        initTitleBar();
        initViews();

    }

    @Override
    protected boolean isShowTitle() {
        return true;
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) {
            super.onBackPressed();
        }
    }

    private void initTitleBar() {
        setCenterText(getString(R.string.verification_code_title));
        setLeftContentIcon(R.drawable.back_icon_selector);
    }

    private void initViews() {
        final TextView mTv1 = (TextView) findViewById(R.id.verification_code_str1);
        final TextView mTvPhone = (TextView) findViewById(R.id.verification_code_phone);
        mEtCode = (EditText) findViewById(R.id.verification_code_in);
        mTvTime = (TextView) findViewById(R.id.verification_code_time);
        final TextView tvSubmit = (TextView) findViewById(R.id.verification_code_submit);
        final SpannableString ss = new SpannableString(getString(R.string.verification_code_message));
        ss.setSpan(new ForegroundColorSpan(0xff0E3AE4), 3, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTv1.setText(ss);
        mTvPhone.setText("+86 " + MoblieFormat.format(mPhoneStr));
        mTvTime.setEnabled(false);
        mTvTime.postDelayed(this, 0);
        tvSubmit.setOnClickListener(this);
        mTvTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.verification_code_submit:
                final String str =mEtCode.getText().toString();
                if(str.isEmpty()||mEtCode==null){
                    showMessage(getString(R.string.verification_code_nokong));
                    return;
                }else if(6>str.length()){
                    showMessage(getString(R.string.verification_code_length));
                    return;
                }
                //登陆
                if (IntentUtils.ACTION_FROM_FORGET.equals(mAction)) {
                    doLoginTask();
                }
                //校验验证码
                else {
                    checkCodeTask();
                }
                break;
            case R.id.verification_code_time:
                regainCode();
                break;
        }
    }

    private void regainCode() {
        mEtCode.setEnabled(true);
        NetController.newInstance(this).sendIdentifyCode(mPhoneStr);
        mCount = MAXTIME;
        mTvTime.removeCallbacks(this);
        mTvTime.post(this);
        mTvTime.setEnabled(false);
    }

    private void checkCodeTask() {
        showProgress(getString(R.string.operating));
        NetController.newInstance(this).checkIdentifyCode(mPhoneStr, mEtCode.getText().toString(), new LoadController.DataCallback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject) {
                dismissProgress();
                IntentUtils.goToSetPasswordActivity(VerificationCodeActivity.this, mPhoneStr);
                stateSetting();
                VerificationCodeActivity.this.showMessage(getString(R.string.verification_success_tip));
            }

            @Override
            public void fail(Object... objects) {
                //提示验证码校验失败
                dismissProgress();
                VerificationCodeActivity.this.showMessage(getString(R.string.verification_code_tipfail));
            }
        });
    }

    private void doLoginTask() {
        showProgress(getString(R.string.operating));
        NetController.newInstance(this).loginWithCode(mPhoneStr, mEtCode.getText().toString(), new LoadController.DataCallback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo) {
                dismissProgress();
                IntentUtils.goToMainPage(VerificationCodeActivity.this);
                stateSetting();
            }

            @Override
            public void fail(Object... objects) {
                dismissProgress();
                if (objects != null && objects.length > 0) {
                    final String errmsg = (String) objects[0];
                    if (!TextUtils.isEmpty(errmsg)) {
                        showErrorDialog(errmsg);
                        return;
                    }
                }
                showErrorDialog(getString(R.string.net_error));
            }
        });
    }

    @Override
    protected boolean isAddToActivityContainer() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtCode.getWindowToken(), 0);
    }

    @Override
    public void run() {
        mTvTime.setText(mCount + getString(R.string.verification_code_time));
        mCount--;
        if (mCount >= 0) {
            mTvTime.postDelayed(this, 1000);
        } else {
            stateSetting();
        }
    }

    private void stateSetting(){
        mTvTime.removeCallbacks(this);
        mTvTime.setText(getString(R.string.verification_code_again));
        mTvTime.setEnabled(true);
        mEtCode.setText("");
        mEtCode.setEnabled(false);
    }

}
