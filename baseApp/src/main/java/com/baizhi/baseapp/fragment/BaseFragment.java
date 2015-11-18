package com.baizhi.baseapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.baizhi.baseapp.R;
import com.baizhi.baseapp.util.DialogUtil;
import com.baizhi.widget.ActionSheet;
import com.baizhi.baseapp.widget.TopBarView;
import com.baizhi.baseapp.widget.WToast;


/**
 * 基类fragment
 *
 * @author sunquan1
 * @date 2015-4-30
 * @since 1.0.0
 */
public abstract class BaseFragment extends Fragment implements ActionSheet.ActionSheetListener {

    protected static final int LEFT_BUTTON = 0;
    protected static final int RIGHT_BUTTON = 1;
    protected ViewGroup mContainer;
    private TopBarView mTopView;
    private WToast mWToast;
    private Dialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWToast = new WToast(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View wrapper = inflater.inflate(R.layout.base_main, container, false);
        mTopView = (TopBarView) wrapper.findViewById(R.id.top);
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

        mContainer = (ViewGroup) wrapper.findViewById(R.id.container);
        mContainer.addView(getContentView(inflater, mContainer), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return wrapper;
    }


    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

    protected abstract void handleClickEvent(int event);

    protected abstract boolean isShowTitle();

    protected void setCenterContentIcon(int resId) {
        mTopView.setCenterImageContent(resId);
    }

    protected void setRightContentIcon(int resId) {
        mTopView.setRightImageContent(resId);
    }

    protected void setLeftContentIcon(int resId) {
        mTopView.setLeftImageContent(resId);
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
        mWToast.showMessage(message);
    }

    protected void showMessage(int message) {
        mWToast.showMessage(message);
    }

    protected void showProgress(String message) {
        if(getActivity()!=null) {
            mProgressDialog = DialogUtil.createProgressDialog(getActivity(), message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    protected Dialog createProgressDialog(String message) {
        if(getActivity()!=null) {
            return DialogUtil.createProgressDialog(getActivity(),message);
        }
        return null;
    }

    protected void dismessProgress() {
        try {
            if(mProgressDialog !=null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception ignored) {}
    }

    public void showActionSheet(String... itemStr) {
        getActivity().setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet.createBuilder(getActivity(), getFragmentManager()).setCancelButtonTitle(R.string.cancel)
                .setOtherButtonTitles(itemStr).setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index, String text) {
    }

}
