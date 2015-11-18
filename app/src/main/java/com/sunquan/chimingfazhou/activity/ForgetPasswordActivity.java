package com.sunquan.chimingfazhou.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.util.IntentUtils;
import com.sunquan.chimingfazhou.util.MobileJudge;

/**
 * 忘记密码页
 *
 * Created by Administrator on 2015/6/9.
 */
public class ForgetPasswordActivity extends BaseSwipeBackActivity implements View.OnClickListener ,TextWatcher {

    /** 手机号输入 */
    private EditText mInput_phone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不自动弹出软键盘
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentLayout(R.layout.activity_forgive_password);
        setLeftContentIcon(R.drawable.back_icon_selector);
        setCenterText(getString(R.string.phone_check_login));

        mInput_phone = (EditText) findViewById(R.id.forgive_input_tel);
        mInput_phone.addTextChangedListener(this);
        findViewById(R.id.forgive_next).setOnClickListener(this);
        findViewById(R.id.account_del_click).setOnClickListener(this);

    }

    @Override
    protected boolean isShowTitle() {
        return true;
    }

    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //下一步
            case R.id.forgive_next:
                checkLogin();
                break;
            //清空输入
            case R.id.account_del_click:
                mInput_phone.setText(null);
                break;
            default:
                break;
        }

    }

    /**
     * 登陆校验
     */
    private void checkLogin() {
        final String phone = mInput_phone.getText().toString().replace(" ","");
        //手机号为空时候
        if (TextUtils.isEmpty(phone)) {
            showMessage(getString(R.string.account));
            return;
        }
        //手机不合法
        if (!MobileJudge.isMobileNO(phone)) {
            showMessage(getString(R.string.hint_input_phone_check));
            return;
        }
        //手机号格式正确
        showDialog(phone);
    }

    /**
     * 显示对话框
     */
    private void showDialog(final String phone) {
        SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle(getString(R.string.phone_check))
                .setMessage(getString(R.string.confirmation_tip)+phone)
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        //发送短信
                        NetController.newInstance(ForgetPasswordActivity.this).sendIdentifyCode(phone);
                        IntentUtils.goToVerificationCodeActivity(ForgetPasswordActivity.this,phone,IntentUtils.ACTION_FROM_FORGET);
                    }
                }).setTag(getString(R.string.phone_check)).showSinglton();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence str, int start, int before, int count) {
        String contents = str.toString();
        final int length = contents.length();
        if (length == 4) {
            if (contents.substring(3).equals(" ")) {
                contents = contents.substring(0, 3);
                mInput_phone.setText(contents);
                mInput_phone.setSelection(contents.length());
            } else {
                contents = contents.substring(0, 3) + " " + contents.substring(3);
                mInput_phone.setText(contents);
                mInput_phone.setSelection(contents.length());
            }
        } else if (length == 9) {
            if (contents.substring(8).equals(" ")) {
                contents = contents.substring(0, 8);
                mInput_phone.setText(contents);
                mInput_phone.setSelection(contents.length());
            } else {
                contents = contents.substring(0, 8) + " " + contents.substring(8);
                mInput_phone.setText(contents);
                mInput_phone.setSelection(contents.length());
            }
        }
    }
    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void finish() {
        super.finish();
        InputMethodManager mInput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInput.hideSoftInputFromWindow(mInput_phone.getWindowToken(), 0);
    }

}
