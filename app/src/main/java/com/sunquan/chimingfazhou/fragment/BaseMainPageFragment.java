package com.sunquan.chimingfazhou.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baizhi.baseapp.controller.LoadController;
import com.kennyc.view.MultiStateView;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.adapter.MainPageAdapter;
import com.sunquan.chimingfazhou.adapter.MainPageSimpleSectionedListAdapter;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;
import com.sunquan.chimingfazhou.models.MainPageInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;
import com.sunquan.chimingfazhou.widget.PullDownView;

import java.util.ArrayList;

/**
 * 增加了加载状态，loading状态的主页fragment
 * <p/>
 * Created by Administrator on 2015/5/8.
 */
public abstract class BaseMainPageFragment extends BaseLoadingFragment implements MainPageAdapter.MainPageClickListener, PullDownView.UpdateHandle, LoadController.DataCallback<MainPageInfo> {

    protected PullDownView mPullDownView;
    protected MainPageAdapter mAdapter;
    protected MainPageSimpleSectionedListAdapter mSimpleSectionedGridAdapter;
    protected ListView mListView;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        final View view = super.getContentView(inflater, container);
        setLoadingContent(R.layout.main_page_loading_state_layout);
        //初始化视图
        initViews(view);
        return view;
    }

    @SuppressWarnings("ConstantConditions")
    protected void initViews(View view) {
        mListView = (ListView) view.findViewById(R.id.list);
        mPullDownView = (PullDownView) view.findViewById(R.id.pull_down_list);
        mPullDownView.setUpdateHandle(this);

        mAdapter = new MainPageAdapter(getActivity());
        mAdapter.setmItems(new ArrayList<MainPageBodyInfo>());
        mAdapter.setOnMainPageClickListener(this);
        mSimpleSectionedGridAdapter = new MainPageSimpleSectionedListAdapter(getActivity(), mAdapter);
        mListView.setAdapter(mSimpleSectionedGridAdapter);
    }


    /**
     * 处理数据
     *
     * @param mainPageInfo
     */
    protected abstract void postExecutePageInfos(MainPageInfo mainPageInfo);

    /**
     * url
     *
     * @return
     */
    protected abstract String getUrl();


    @Override
    protected void onRetry() {
        onUpdate();
    }

    @Override
    public void onUpdate() {
        if (getCurrentViewSate() != MultiStateView.ViewState.CONTENT) {
            NetController.newInstance(getActivity()).loadMainInfo(getUrl(), BaseMainPageFragment.this, LoadController.HTTP_FIRST);
        } else {
            NetController.newInstance(getActivity()).loadMainInfo(getUrl(), BaseMainPageFragment.this, LoadController.HTTP_ONLY);
        }

    }

    @Override
    public void success(MainPageInfo mainPageInfo) {
        if (mainPageInfo.getBody().isEmpty()) {
            showEmptyState();
            return;
        }
        //处理数据
        postExecutePageInfos(mainPageInfo);

        mAdapter.setmItems(mainPageInfo.getBody());
        mSimpleSectionedGridAdapter.notifyDataSetChanged();

        if (getCurrentViewSate() != MultiStateView.ViewState.CONTENT) {
            showContentState();
        } else {
            mPullDownView.clearLoadingStatus(true);
        }

    }

    @Override
    public void fail(Object... objects) {
        if (getCurrentViewSate() == MultiStateView.ViewState.CONTENT) {
            mPullDownView.clearLoadingStatus(false);
            showMessage(getString(R.string.error_message));
        } else {
            showErrorState();
        }
    }

    @Override
    public void onMainPageItemClicked(MainPageBodyInfo mainPageBodyInfo) {
        IntentUtils.goToDetailPageByBodyInfo(getActivity(), mainPageBodyInfo);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.scale_hold);
    }
}
