package com.sunquan.chimingfazhou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baizhi.baseapp.controller.LoadController;
import com.baizhi.baseapp.widget.RecyclingImageView;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.download.util.MD5;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;
import com.sunquan.chimingfazhou.util.MobileJudge;

/**
 * 设置密码
 * Created by czh on 2015/6/10.
 */
public class SetPasswordActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    /**
     * 字符串最小长度
     */
    private static final int MIN_LIMIT_LENGTH = 6;
    /**
     * 字符串全是数字长度不能小于9
     */
    private static final int MIN_NUMBER_LENGTH = 9;
    /**
     * 设置密码
     */
    private EditText mSetPassword;
    /**
     * 设置密码Str
     */
    private String mSetPasswordStr;
    /**
     * 确认密码
     */
    private EditText mConfirmPassword;
    /**
     * 设置密码Str
     */
    private String mConfirmPasswordStr;
    /**
     * 验证码
     */
    private String mVerificationCode;
    /**
     * 电话
     */
    private String mPhoneStr;

    private String mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentLayout(R.layout.activity_set_password_layout);
        final Intent intent = getIntent();
        mVerificationCode = intent.getStringExtra("code");
        mPhoneStr = intent.getStringExtra("phone");
        mAction = intent.getAction();
        initTitleBar();
        initViews();
    }

    private void initTitleBar() {
        if(IntentUtils.ACTION_USER_VERIFICATION_CODE.equals(mAction)){
            setCenterText(getString(R.string.set_password));
        }else {
            setCenterText(getString(R.string.modify_password));
        }

        setLeftContentIcon(R.drawable.back_icon_selector);
    }

    private void initViews() {
        mSetPassword = (EditText) findViewById(R.id.set_password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        final RecyclingImageView delete1 = (RecyclingImageView) findViewById(R.id.set_password_delete1);
        final RecyclingImageView delete2 = (RecyclingImageView) findViewById(R.id.set_password_delete2);
        final TextView mSuccess = (TextView) findViewById(R.id.set_password_success);

        mSuccess.setOnClickListener(this);
        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);

    }


    @Override
    protected boolean isShowTitle() {
        return true;
    }

    @Override
    protected boolean isAddToActivityContainer() {
        return true;
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //删除设置密码文本
            case R.id.set_password_delete1:
                doText1DeleteClick();
                break;
            //删除确认密码文本
            case R.id.set_password_delete2:
                doText2DeleteClick();
                break;
            //提交
            case R.id.set_password_success:
                doSubmitClick();
                break;
        }
    }

    private void doText1DeleteClick() {
        mSetPassword.setText("");
        mSetPasswordStr = null;
    }

    private void doText2DeleteClick() {
        mConfirmPassword.setText("");
        mConfirmPasswordStr = null;
    }

    private void doSubmitClick() {
        mSetPasswordStr = mSetPassword.getText().toString();
        mConfirmPasswordStr = mConfirmPassword.getText().toString();
        if(!checkPasswordFormat()) {
            return;
        }
        //成功之后根据action去进行网络操作
        //设置密码，进行注册操作
        if(IntentUtils.ACTION_USER_VERIFICATION_CODE.equals(mAction)){
            registerTask();
        }
        //修改密码
        else {
            modifyPasswordTask();
        }
    }

    /**
     * 注册网络操作
     */
    private void registerTask() {
        showProgress(getString(R.string.operating));
        NetController.newInstance(this).register(mPhoneStr, MD5.getMD5(mSetPasswordStr), new LoadController.DataCallback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo) {
                dismissProgress();
                IntentUtils.goToUserCenterPage(SetPasswordActivity.this, IntentUtils.ACTION_USER_SET_PASSWORD);
            }

            @Override
            public void fail(Object... objects) {
                dismissProgress();
                if(objects!=null&&objects.length>0) {
                    final String errmsg = (String) objects[0];
                    if(!TextUtils.isEmpty(errmsg)) {
                        showErrorDialog(errmsg);
                        return;
                    }
                }
                showErrorDialog(getString(R.string.register_fail));
            }
        });
    }

    /**
     * 修改密码网络操作
     */
    private void modifyPasswordTask() {
        showProgress(getString(R.string.operating));
        NetController.newInstance(this).modifyPassword(MD5.getMD5(mSetPasswordStr), new LoadController.DataCallback<UserInfo>() {
            @Override
            public void success(UserInfo userInfo) {
                dismissProgress();
                SetPasswordActivity.this.onBackPressed();
                showMessage(getString(R.string.modify_password_success_tip));
            }

            @Override
            public void fail(Object... objects) {
                dismissProgress();
                showMessage(getString(R.string.modify_password_fail_tip));
            }
        });
    }

    /**
     * 密码格式的校验
     *
     * @return true：如果格式正确
     */
    private boolean checkPasswordFormat() {
        //
        if (mSetPasswordStr.isEmpty() || mConfirmPasswordStr.isEmpty()) {
            showMessage(getString(R.string.set_password_tip1));
            return false;
        }
        //
        if (!mSetPasswordStr.equals(mConfirmPasswordStr)) {
            showMessage(getString(R.string.set_password_tip2));
            return false;
        }
        //
        if (mSetPasswordStr.length() < MIN_LIMIT_LENGTH) {
            showMessage(getString(R.string.set_password_tip3));
            return false;
        }
        //
        if (MobileJudge.isNumeric(mSetPasswordStr) && mSetPasswordStr.length() < MIN_NUMBER_LENGTH) {
            showMessage(getString(R.string.set_password_tip4));
            return false;
        }

        return true;
    }

    @Override
    public void finish() {
        super.finish();
        InputMethodManager mInput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);;
        mInput.hideSoftInputFromWindow(mSetPassword.getWindowToken(), 0);
    }
}
