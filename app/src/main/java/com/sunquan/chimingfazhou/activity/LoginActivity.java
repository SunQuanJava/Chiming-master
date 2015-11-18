package com.sunquan.chimingfazhou.activity;

import android.os.*;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.baizhi.baseapp.controller.BaseHandler;
import com.baizhi.baseapp.controller.LoadController;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.download.util.MD5;
import com.sunquan.chimingfazhou.models.UserInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;
import com.sunquan.chimingfazhou.util.MobileJudge;
import com.sunquan.chimingfazhou.widget.KeyboardLayout;

/**
 * 登陆页面
 *Created by Administrator on 2015/6/9.
 */
public class LoginActivity extends AppBaseActivity implements View.OnClickListener,TextWatcher,ViewTreeObserver.OnGlobalLayoutListener,LoadController.DataCallback<UserInfo> {
    private Handler mHandler = new BaseHandler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEYBOARD_HIDE:
//                    final int mRootBottom = mLinearLayout.getBottom() - mLinearLayout.getTop();
                    if(input_soft_offset==-1){
                        //忘记密码view的高度 和 按钮view的高度
                        input_soft_offset = mLinearLayout.getMeasuredHeight();
                    }
//                    mScrollView.fullScroll(View.FOCUS_DOWN);//滚到底部
                    mScrollView.scrollTo(0,input_soft_offset);
                    break;
                default:
                    break;
            }
        }

    };
    private static final int KEYBOARD_HIDE = 0X20;

    /** 手机号输入 */
    private EditText mLogin_accounts;

    /** 密码输入 */
    private EditText mLogin_password;

    /** 布局文件 */
    private KeyboardLayout mKeyboardLayout;

    /** 底部按钮 */
    private LinearLayout mLinearLayout;

    /**软键盘弹出,scroll向上滚动的距离*/
    private int input_soft_offset = -1;
    private ScrollView mScrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentLayout(R.layout.activity_login);
        setCenterText(getString(R.string.login));

        mLinearLayout = (LinearLayout)findViewById(R.id.login_bottom);
        mScrollView = (ScrollView)findViewById(R.id.mScrollView);

        mLogin_accounts = (EditText)findViewById(R.id.lgoin_accounts);
        mLogin_password = (EditText)findViewById(R.id.lgoin_password);
        mKeyboardLayout = (KeyboardLayout)findViewById(R.id.mKeyboardLayout);

        mKeyboardLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mLogin_accounts.addTextChangedListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.account_del_click).setOnClickListener(this);
        findViewById(R.id.password_del_click).setOnClickListener(this);
        findViewById(R.id.forgive_password_click).setOnClickListener(this);
        findViewById(R.id.user_register).setOnClickListener(this);
    }

    /**
     * 校检密码和账号
     * @return
     */
    public void check(){
        final String account = mLogin_accounts.getText().toString().replace(" ", "");
        final String password = mLogin_password.getText().toString();

        //手机号为空
        if(TextUtils.isEmpty(account)){
            return ;
        }

        //手机号不合法
        if(!MobileJudge.isMobileNO(account)){
            showMessage(R.string.hint_input_phone_check);
            return;
        }

        //密码长度小于六位
        if(MobileJudge.isMobileNO(account)&&password.length() < 6){
            showMessage(getString(R.string.set_password_tip3));
            return;
        }

        //不能是九位一下纯数字
        if (MobileJudge.isNumeric(password) && password.length() < 9) {
            showMessage(getString(R.string.set_password_tip4));
            return;
        }

        //合法手机号,密码合法
        if(MobileJudge.isMobileNO(account)&&!TextUtils.isEmpty(password)) {
            //登陆
            login(account,password);
            return;
        }
        showMessage(R.string.password);
    }

    /**
     * 登陆
     * @param account
     * @param password
     */
    private void login(final String account, final String password) {
        //提交账号和密码，验证登陆,密码最大16位，作了限制

        showProgress(getString(R.string.login_ing));
        NetController.newInstance(this).loginWithPassword(account, MD5.getMD5(password),this);
    }

    @Override
    protected boolean isShowTitle() {
        return true;
    }

    /**
     * 点击事件
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login://登陆
                check();
                break;
            case R.id.account_del_click:
                mLogin_accounts.setText(null);
                break;
            case R.id.password_del_click:
                mLogin_password.setText(null);
                break;
            case R.id.forgive_password_click:
                //忘记密码
                IntentUtils.goToUpdatePasswordPage(this);
                break;
            case R.id.user_register:
                //注册
                IntentUtils.goToRegisterPage(this);
                break;

            default:
                break;
        }
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
                mLogin_accounts.setText(contents);
                mLogin_accounts.setSelection(contents.length());
            } else {
                contents = contents.substring(0, 3) + " " + contents.substring(3);
                mLogin_accounts.setText(contents);
                mLogin_accounts.setSelection(contents.length());
            }
        } else if (length == 9) {
            if (contents.substring(8).equals(" ")) {
                contents = contents.substring(0, 8);
                mLogin_accounts.setText(contents);
                mLogin_accounts.setSelection(contents.length());
            } else {
                contents = contents.substring(0, 8) + " " + contents.substring(8);
                mLogin_accounts.setText(contents);
                mLogin_accounts.setSelection(contents.length());
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
         * 寻找当前的view层次中处在最顶层的view
         * 根据视图的偏移值来判断键盘是否显示
         */
        if (offset > 200) {
            mHandler.sendMessage(mHandler.obtainMessage(KEYBOARD_HIDE));
        } else {
            mHandler.sendMessage(mHandler.obtainMessage(KEYBOARD_HIDE));
        }
    }

    @Override
    public void success(UserInfo userInfo) {
        dismissProgress();
        IntentUtils.goToMainPage(this);
    }

    @Override
    public void fail(Object... objects) {
        dismissProgress();
        if(objects!=null&&objects.length>0) {
            final String errmsg = (String) objects[0];
            if(!TextUtils.isEmpty(errmsg)){
                showErrorDialog(errmsg);
                return;
            }
        }
        showErrorDialog(getString(R.string.net_error));
    }

    @Override
    public void onBackPressed() {
        IntentUtils.goToHome(this);
    }

}
