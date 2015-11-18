package com.sunquan.chimingfazhou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.util.IntentUtils;

/**
 * 编辑信息的Activity（修改个人简介、编辑举报信息等）
 * <p/>
 * Created by sunquan1 on 2015/1/4.
 */
public class EditActivity extends BaseSwipeBackActivity {

    public static final int RESPOND_CODE_DESCRIPTION_EDIT = 1;
    public static final int RESPOND_CODE_NICKNAME_EDIT = 2;
    public static final int RESPOND_CODE_FARMINTON_EDIT = 3;
    private static final int NICKNAME_MAX_LENGTH = 20; //昵称最多20个字
    private static final int INTRO_MAX_LENGTH = 100; // 简介最多100个字

    private String mContentText;
    private EditText mEtContent;
    private TextView mTextNum;
    private String mAction;
    private int mMaxLength;
    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            final int number = mMaxLength - s.toString().length();
            mTextNum.setText("" + number);
            if (number < 0) {
                mTextNum.setTextColor(0xfffc4052);
            } else {
                mTextNum.setTextColor(0xffaaaaaa);
            }
        }

    };
    private String hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.edittext);
        setLeftContentIcon(R.drawable.back_icon_selector);
        setRightText(getString(R.string.user_save) ,0);
        mAction = getIntent().getAction();
        final RelativeLayout wrapper = (RelativeLayout) findViewById(R.id.wrapper);
        mEtContent = (EditText) findViewById(R.id.etContent);
        mTextNum = (TextView) findViewById(R.id.textNum);
        mTextNum.setTextColor(0xffaaaaaa);
        //修改简介
        switch (mAction) {
            case IntentUtils.ACTION_FROM_USER_CENTER_FOR_DESCRIPTION:
                setCenterText(getString(R.string.editactivity_description));
                mContentText = getIntent().getStringExtra(IntentUtils.EXTRA_EDIT_DESCRIPTION);
                mMaxLength = INTRO_MAX_LENGTH;
                hintText = getString(R.string.edtiactivity_description_hint);
                wrapper.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.edit_layout_description_height)));
                break;
            //修改法名
            case IntentUtils.ACTION_FROM_USER_CENTER_FOR_FARMINTON:
                setCenterText(getString(R.string.editactivtiy_faming));
                mContentText = getIntent().getStringExtra(IntentUtils.EXTRA_EDIT_FARMINTON);
                mMaxLength = NICKNAME_MAX_LENGTH;
                hintText = getString(R.string.editactivity_faming_hint);
                wrapper.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.edit_layout_nickname_height)));
                mEtContent.setSingleLine(true);
                mEtContent.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.edit_text_number_limit), 0);
                break;
            //修改昵称
            case IntentUtils.ACTION_FROM_USER_CENTER_FOR_NICKNAME:
                setCenterText(getString(R.string.editactivity_name));
                mContentText = getIntent().getStringExtra(IntentUtils.EXTRA_EDIT_NICKNAME);
                mMaxLength = NICKNAME_MAX_LENGTH;
                hintText = getString(R.string.editactivity_name_hint);
                wrapper.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.edit_layout_nickname_height)));
                mEtContent.setSingleLine(true);
                mEtContent.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.edit_text_number_limit), 0);
                break;
        }
        initViews();
    }

    private void initViews() {
        mEtContent.setHint(hintText);
        if (!TextUtils.isEmpty(mContentText)) {
            mEtContent.setText(mContentText);
            mEtContent.setSelection(mEtContent.getText().length());
            final int number = mMaxLength - mContentText.length();
            mTextNum.setText(String.valueOf(number));
            if (number < 0) {
                mTextNum.setTextColor(0xfffc4052);
            } else {
                mTextNum.setTextColor(0xffaaaaaa);
            }
        } else {
            mTextNum.setText(String.valueOf(mMaxLength));
        }
        mEtContent.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEtContent.removeTextChangedListener(mTextWatcher);
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == RIGHT_BUTTON) {
            //保存信息
            saveInput();
        } else if (event == LEFT_BUTTON) {
            onBackPressed();
        }
    }

    private void saveInput() {
        final String inputContent = mEtContent.getText().toString();
        //修改简介
        switch (mAction) {
            case IntentUtils.ACTION_FROM_USER_CENTER_FOR_DESCRIPTION:
                if (checkIsValid(inputContent)) {
                    final Intent intent = new Intent(this, UserCenterActivity.class);
                    intent.putExtra(IntentUtils.EXTRA_EDIT_DESCRIPTION, inputContent);
                    setResult(RESPOND_CODE_DESCRIPTION_EDIT, intent);
                    onBackPressed();
                }
                break;
            //修改昵称
            case IntentUtils.ACTION_FROM_USER_CENTER_FOR_NICKNAME:
                if (checkIsValid(inputContent)) {
                    final Intent intent = new Intent(this, UserCenterActivity.class);
                    intent.putExtra(IntentUtils.EXTRA_EDIT_NICKNAME, inputContent);
                    setResult(RESPOND_CODE_NICKNAME_EDIT, intent);
                    onBackPressed();
                }
                break;
            //修改法名
            case IntentUtils.ACTION_FROM_USER_CENTER_FOR_FARMINTON:
                if (checkIsValid(inputContent)) {
                    final Intent intent = new Intent(this, UserCenterActivity.class);
                    intent.putExtra(IntentUtils.EXTRA_EDIT_FARMINTON, inputContent);
                    setResult(RESPOND_CODE_FARMINTON_EDIT, intent);
                    onBackPressed();
                }
                break;
        }
    }

    private boolean checkIsValid(String txt) {
        if(TextUtils.isEmpty(txt)) {
            if(mAction.equals(IntentUtils.ACTION_FROM_USER_CENTER_FOR_NICKNAME)) {
                showMessage(getString(R.string.edit_nickname_no_empty_message));
                return false;
            }
            else if(mAction.equals(IntentUtils.ACTION_FROM_USER_CENTER_FOR_FARMINTON)) {
                showMessage(getString(R.string.edit_farmington_no_empty_message));
                return false;
            }
        }
        else if (txt.length() > mMaxLength) {
            showMessage(getString(R.string.editactivity_morelength_tip));
            return false;
        }
        return true;
    }
}
