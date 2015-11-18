package com.sunquan.chimingfazhou.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kennyc.view.MultiStateView;
import com.sunquan.chimingfazhou.R;

/**
 * 包含加载状态的fragment
 *
 * Created by Administrator on 2015/5/12.
 */
public abstract class BaseLoadingFragment extends AppBaseFragment implements View.OnClickListener{

    private MultiStateView mMultiStateView;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        final View view = inflater.inflate(R.layout.base_loading_state_layout,container,false);
        mMultiStateView = (MultiStateView) view.findViewById(R.id.multiStateView);
        mMultiStateView.setViewState(MultiStateView.ViewState.LOADING);
        //noinspection ConstantConditions
        mMultiStateView.getView(MultiStateView.ViewState.ERROR).setOnClickListener(this);

        return view;
    }

    protected void setLoadingContent(int resId) {
        final View contentView = LayoutInflater.from(getActivity()).inflate(resId, mMultiStateView, false);
        setLoadingContent(contentView);
    }

    protected void setLoadingContent(View view) {
        mMultiStateView.setViewForState(view, MultiStateView.ViewState.CONTENT);
    }

    protected void showErrorState() {
        mMultiStateView.setViewState(MultiStateView.ViewState.ERROR);
    }

    protected void showLoadingState() {
        mMultiStateView.setViewState(MultiStateView.ViewState.LOADING);
    }

    protected void showEmptyState() {
        mMultiStateView.setViewState(MultiStateView.ViewState.EMPTY);
    }

    protected void showContentState() {
        mMultiStateView.setViewState(MultiStateView.ViewState.CONTENT);
    }

    protected MultiStateView.ViewState getCurrentViewSate() {
        return mMultiStateView.getViewState();
    }

    /**
     * 重新加载
     */
    protected void onRetry() {

    }

    @Override
    public void onClick(View v) {
        if (v == mMultiStateView.getView(MultiStateView.ViewState.ERROR)) {
            mMultiStateView.setViewState(MultiStateView.ViewState.LOADING);
            onRetry();
        }
    }
}
