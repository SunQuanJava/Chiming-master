package com.sunquan.chimingfazhou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baizhi.baseapp.controller.LoadController;
import com.baizhi.baseapp.util.PreferencesUtil;
import com.baizhi.baseapp.widget.NavigationBarView;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.event.CoverDownloadEvent;
import com.sunquan.chimingfazhou.event.GoToPlayListEvent;
import com.sunquan.chimingfazhou.fragment.WenDetailIntroductionFragment;
import com.sunquan.chimingfazhou.fragment.WenDetailListFragment;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;
import com.sunquan.chimingfazhou.models.WenDetailInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;
import com.sunquan.chimingfazhou.util.ParamsUtil;

import de.greenrobot.event.EventBus;

/**
 * 闻的详情页
 *
 * Created by Administrator on 2015/5/11.
 */
public class WenDetailActivity extends BaseLoadingWithSwipeBackActivity implements NavigationBarView.OnTabChangeListener,LoadController.DataCallback<WenDetailInfo> {

    public static final String WEN_INTRODUCTION = "WenDetailIntroduction";
    public static final String WEN_LIST = "WenDetailList";

    public static final String WEN_EXTRA = "wen_detailInfo";
    private MainPageBodyInfo mBodyInfo;

    private ImageView mTriangleLeftView;
    private ImageView mTriangleRightView;

    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        setLoadingContent(R.layout.activity_wen_detail_layout);
        isFirst = PreferencesUtil.getBooleanByName(this, WEN_LIST, false);
        mBodyInfo = GlobalDataHolder.getInstance(this).getCurrentWenPageBodyInfo();
        setCenterText(mBodyInfo.getTitle());
        setLeftContentIcon(R.drawable.back_icon_selector);
        setRightContentIcon(R.drawable.share_icon_selector);
        final WenDetailInfo wenDetailInfo = (WenDetailInfo) getIntent().getSerializableExtra(IntentUtils.EXTRA_WEN_NOTIFICATION_DETAIL);
        if(wenDetailInfo!=null) {
            showContentState();
            initTabs(wenDetailInfo);
        } else {
            //加载数据
            loadData();
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        final String url = ParamsUtil.getWenDetailUrl(this, mBodyInfo.getId());
        NetController.newInstance(this).loadWenDetailInfo(url, this);
    }

    private void initTabs(WenDetailInfo wenDetailInfo) {
        final NavigationBarView tHost = (NavigationBarView) findViewById(R.id.tabHost);
        final Bundle bundle = new Bundle();
        bundle.putSerializable(WEN_EXTRA, wenDetailInfo);
        tHost.reset();
        tHost.setSmoothly(true);
        tHost.addTab(getString(R.string.wen_detail_introduction_message), WenDetailIntroductionFragment.class, bundle);
        tHost.addTab(getString(R.string.wen_detail_list_message), WenDetailListFragment.class, bundle);
        tHost.setOnTabChangedListener(this);
        tHost.setup(getSupportFragmentManager());
        mTriangleLeftView = (ImageView) findViewById(R.id.triangleLeft);
        mTriangleRightView = (ImageView) findViewById(R.id.triangleRight);
        if(Intent.ACTION_MAIN.equals(getIntent().getAction())) {
            tHost.setCurrentTab(1);
            mTriangleRightView.setVisibility(View.VISIBLE);
            mTriangleLeftView.setVisibility(View.INVISIBLE);
        } else {
            tHost.setCurrentTab(0);
            mTriangleRightView.setVisibility(View.INVISIBLE);
            mTriangleLeftView.setVisibility(View.VISIBLE);
        }

        checkNewGuidePage(WEN_INTRODUCTION,R.layout.wen_cover_pager,R.id.cover_cancel,null);

    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRetry() {
        loadData();
    }

    @Override
    public void onTabChanged(int i, String tabName) {
        if (i == 0) {
            mTriangleLeftView.setVisibility(View.VISIBLE);
            mTriangleRightView.setVisibility(View.INVISIBLE);
        } else {
            mTriangleLeftView.setVisibility(View.INVISIBLE);
            mTriangleRightView.setVisibility(View.VISIBLE);

            if(!isFirst){
                EventBus.getDefault().post(new CoverDownloadEvent(true));
                checkNewGuidePage(WEN_LIST,R.layout.wen_cover_download_pager,R.id.cover_cancel,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new CoverDownloadEvent(false));
                    }
                });
            }

        }
    }

    @Override
    public void success(WenDetailInfo wenDetailInfo) {
        showContentState();
        initTabs(wenDetailInfo);
    }

    @Override
    public void fail(Object... objects) {
        showErrorState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(GoToPlayListEvent event) {
        final NavigationBarView tHost = (NavigationBarView) findViewById(R.id.tabHost);
        if(tHost.getCurrentTab()!=1) {
            tHost.setCurrentTab(1);
        }
    }

}
