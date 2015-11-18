package com.sunquan.chimingfazhou.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.baizhi.baseapp.controller.LoadController;
import com.kennyc.view.MultiStateView;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.adapter.JingangPageAdapter;
import com.sunquan.chimingfazhou.constants.Constants;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.models.UserInfos;
import com.sunquan.chimingfazhou.widget.PullDownView;


/**
 * 金刚道友页
 * Created by Administrator on 2015/6/16.
 */
public class JingangFriendActivity extends BaseLoadingWithSwipeBackActivity implements PullDownView.UpdateHandle,LoadController.DataCallback<UserInfos> {

    private JingangPageAdapter mAdapter;

    /** 下拉刷新 */
    private PullDownView mPullDownView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoadingContent(R.layout.activity_jingang);
        setLeftContentIcon(R.drawable.back_icon_selector);
        setCenterText(getString(R.string.wo_user_friend));

        mPullDownView = (PullDownView)findViewById(R.id.jingang_PullDown);
        mPullDownView.setUpdateHandle(this);

        init_listview();
        onUpdate();
    }

    /**
     * 初始化listView
     */
    private void init_listview(){
        final ListView listView = (ListView) findViewById(R.id.jingang_list);
        mAdapter = new JingangPageAdapter(this);
        mAdapter.setItems(new UserInfos());
        listView.setAdapter(mAdapter);
    }

    @Override
    protected boolean isShowTitle() {
        return true;
    }

    @Override
    protected void handleClickEvent(int event) {
        if(event == LEFT_BUTTON){
            super.onBackPressed();
        }
    }

    @Override
    public void onUpdate() {
        final String url = Constants.HOST+Constants.MEMBER_URL+"?uid="+ GlobalDataHolder.getInstance(this).getUid();
        if (getCurrentViewSate() != MultiStateView.ViewState.CONTENT) {
            NetController.newInstance(this).getMembersData(url,this, LoadController.HTTP_FIRST);
        } else {
            NetController.newInstance(this).getMembersData(url,this,LoadController.HTTP_ONLY);
        }
    }

    @Override
    public void success(UserInfos userInfos) {
        mAdapter.setItems(userInfos);
        mAdapter.notifyDataSetChanged();
        if(userInfos.isEmpty()) {
            showEmptyState();
        }
        else if (getCurrentViewSate() != MultiStateView.ViewState.CONTENT) {
            showContentState();
        }
        else {
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
    protected void onRetry() {
        onUpdate();
    }
}
