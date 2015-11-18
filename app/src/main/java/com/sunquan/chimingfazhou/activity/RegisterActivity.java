package com.sunquan.chimingfazhou.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.baizhi.baseapp.controller.BaseHandler;
import com.baizhi.baseapp.widget.RecyclingImageView;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.util.IntentUtils;
import com.sunquan.chimingfazhou.util.MobileJudge;

/**
 * 用户注册
 * Created by czh on 2015/6/8.
 */
public class RegisterActivity extends BaseSwipeBackActivity implements View.OnClickListener, TextWatcher, ViewTreeObserver.OnGlobalLayoutListener{
    private Handler mHandler = new BaseHandler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEYBOARD_HIDE:
                    if(input_soft_offset==-1){
                        //按钮view的高度
                        input_soft_offset = findViewById(R.id.scroll_top).getHeight();
                    }
                    mScrollView.scrollTo(0,input_soft_offset);
                    break;
                default:
                    break;
            }
        }

    };
    private static final int KEYBOARD_HIDE = 0X20;
    /**软键盘弹出,scroll向上滚动的距离*/
    private int input_soft_offset = -1;
    private ScrollView mScrollView;
    /** 底部按钮 */
    private TextView mRegister;

    private LinearLayout mKeyboardLayout;

    /**国家地区输入框*/
    private EditText mCountry;
    /**电话号码输入框*/
    private EditText mPhone;
    /**电话号码字符串*/
    private String mPhoneStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentLayout(R.layout.activity_register_layout);

        mKeyboardLayout = (LinearLayout)findViewById(R.id.register_layout);
        mScrollView = (ScrollView)findViewById(R.id.mScrollView_register);
        mRegister = (TextView)findViewById(R.id.register);
        mKeyboardLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        initTitleBar();
        initViews();
    }

    @Override
    protected boolean isAddToActivityContainer() {
        return true;
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
        setCenterText(getString(R.string.register));
        setLeftContentIcon(R.drawable.back_icon_selector);
    }

    private void initViews() {
        final TextView register = (TextView) findViewById(R.id.register);
        final RecyclingImageView delete = (RecyclingImageView) findViewById(R.id.register_delete);
        mCountry = (EditText) findViewById(R.id.register_country);
        mPhone = (EditText) findViewById(R.id.register_phone);
        mCountry.setKeyListener(null);
        mPhone.addTextChangedListener(this);
        register.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.register://注册
                mPhoneStr = mPhone.getText().toString().replace(" ", "");
                if (mPhoneStr.isEmpty()) {
                    showMessage(getString(R.string.register_error1));
                } else if (MobileJudge.isMobileNO(mPhoneStr)) {
                    showCancelDialog();
                } else {
                    showMessage(getString(R.string.register_error2));
                }
                break;
            case R.id.register_delete://电话号码清空
                mPhoneStr = null;
                mPhone.setText("");
                break;


        }

    }

    private void showCancelDialog() {
        SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setTitle(getString(R.string.confirmation_phone_number))
                .setMessage(getString(R.string.confirmation_tip) + mPhoneStr)
                .setPositiveButtonText(android.R.string.ok)
                .setNegativeButtonText(android.R.string.cancel)
                .setPositiveListener(new IPositiveButtonDialogListener() {
                    @Override
                    public void onPositiveButtonClicked(int requestCode) {
                        NetController.newInstance(RegisterActivity.this).sendIdentifyCode(mPhoneStr);
                        IntentUtils.goToVerificationCodeActivity(RegisterActivity.this,mPhoneStr,IntentUtils.ACTION_FROM_REGISTER);
                    }
                })
                .setTag("register_phone_tip_message")
                .showSinglton();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence str, int start, int before, int count) {
        String contents = str.toString();
        final int length = contents.length();
        if (length == 4) {
            if (contents.substring(3).equals(" ")) { // -
                contents = contents.substring(0, 3);
                mPhone.setText(contents);
                mPhone.setSelection(contents.length());
            } else { // +
                contents = contents.substring(0, 3) + " " + contents.substring(3);
                mPhone.setText(contents);
                mPhone.setSelection(contents.length());
            }
        } else if (length == 9) {
            if (contents.substring(8).equals(" ")) { // -
                contents = contents.substring(0, 8);
                mPhone.setText(contents);
                mPhone.setSelection(contents.length());
            } else {// +
                contents = contents.substring(0, 8) + " " + contents.substring(8);
                mPhone.setText(contents);
                mPhone.setSelection(contents.length());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 监听软键盘是否弹出
     */
    public void onGlobalLayout() {
        int offset = mKeyboardLayout.getRootView().getHeight() - mKeyboardLayout.getHeight();
        /**
         * 根据视图的偏移值来判断键盘是否显示
         * 可以解决手机高度不同的问题
         */
//        Log.i("offset","偏移量:"+offset);
        if (offset > 300) {
            mHandler.sendMessage(mHandler.obtainMessage(KEYBOARD_HIDE));
        } else {
            mHandler.sendMessage(mHandler.obtainMessage(KEYBOARD_HIDE));
        }
    }

    @Override
    public void finish() {
        super.finish();
        InputMethodManager mInput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);;
        mInput.hideSoftInputFromWindow(mPhone.getWindowToken(), 0);
    }
}
