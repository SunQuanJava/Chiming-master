package com.sunquan.chimingfazhou.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.kennyc.view.MultiStateView;
import com.sunquan.chimingfazhou.R;

import butterknife.Bind;

/**
 * 包含加载状态的activity
 * <p/>
 * Created by Administrator on 2015/5/12.
 */
public abstract class BaseLoadingActivity extends AppBaseActivity implements View.OnClickListener {

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.base_loading_state_layout);

        multiStateView.setViewState(MultiStateView.ViewState.LOADING);
        //noinspection ConstantConditions
        multiStateView.getView(MultiStateView.ViewState.ERROR).setOnClickListener(this);

    }

    protected void setLoadingContent(int resId) {
        final View contentView = LayoutInflater.from(this).inflate(resId, multiStateView, false);
        setLoadingContent(contentView);
    }

    protected void setLoadingContent(View view) {
        multiStateView.setViewForState(view, MultiStateView.ViewState.CONTENT);
    }

    protected void showErrorState() {
        multiStateView.setViewState(MultiStateView.ViewState.ERROR);
    }

    protected void showLoadingState() {
        multiStateView.setViewState(MultiStateView.ViewState.LOADING);
    }

    protected void showEmptyState() {
        multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
    }

    protected void showContentState() {
        multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
    }

    protected MultiStateView.ViewState getCurrentViewSate() {
        return multiStateView.getViewState();
    }

    /**
     * 重新加载
     */
    protected void onRetry() {

    }

    @Override
    public void onClick(View v) {
        if (v == multiStateView.getView(MultiStateView.ViewState.ERROR)) {
            multiStateView.setViewState(MultiStateView.ViewState.LOADING);
            onRetry();
        }
    }

}
