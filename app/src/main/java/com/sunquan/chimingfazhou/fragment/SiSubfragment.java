package com.sunquan.chimingfazhou.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.adapter.MainPageAdapter;
import com.sunquan.chimingfazhou.constants.Constants;
import com.sunquan.chimingfazhou.event.SameTabClickEvent;
import com.sunquan.chimingfazhou.event.TabChangeEvent;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;
import com.sunquan.chimingfazhou.models.MainPageInfo;
import com.sunquan.chimingfazhou.util.ParamsUtil;
import com.sunquan.chimingfazhou.widget.PullDownView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 思的二级页fragment
 * <p/>
 * Created by Administrator on 2015/5/7.
 */
public class SiSubfragment extends BaseMainPageFragment implements MainPageAdapter.MainPageClickListener, PullDownView.UpdateHandle, View.OnClickListener {

    public boolean mIsInitLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        final View view = super.getContentView(inflater, container);
        if (getString(R.string.si_tab_sub1).equals(getArguments().getString("tag"))) {
            onUpdate();
        }
        return view;
    }

    @Override
    protected void postExecutePageInfos(MainPageInfo mainPageInfo) {
        final ArrayList<MainPageBodyInfo> bodyInfos = mainPageInfo.getBody();
        //step2，过滤掉所有的非si的item
        final ArrayList<MainPageBodyInfo> tempBodyInfos = new ArrayList<>();
        for (MainPageBodyInfo bodyInfo : bodyInfos) {
            if (bodyInfo.getType().equals(MainPageBodyInfo.SI)) {
                tempBodyInfos.add(bodyInfo);
            }
        }
        //step4，将组装好的item放进去
        bodyInfos.clear();
        bodyInfos.addAll(tempBodyInfos);
    }

    @Override
    protected String getUrl() {
        return ParamsUtil.getFirstPageUrlByType(getActivity(), Constants.PARAMETER_TYPE_SI, getString(R.string.si_tab_sub1).equals(getArguments().getString("tag")) ? Constants.PARAMETER_SUB_TYPE_SHYJ : Constants.PARAMETER_SUB_TYPE_XMFY);
    }

    @Override
    protected void handleClickEvent(int event) {
    }

    @Override
    protected boolean isShowTitle() {
        return false;
    }

    public void onEvent(TabChangeEvent event) {
        final String className = event.getClassName();
        if (className.equals(((Object) this).getClass().getSimpleName()) && !mIsInitLoading && getString(R.string.si_tab_sub2).equals(getArguments().getString("tag"))) {
            mIsInitLoading = true;
            onUpdate();
        }
    }

    public void onEvent(SameTabClickEvent event) {
        final String className = event.getClassName();
        if (className.equals(((Object) this).getClass().getSimpleName()) && isSmoothTab(event) && mListView != null && mListView.getFirstVisiblePosition() != 0) {
            mListView.smoothScrollToPosition(0);
        }
    }

    private boolean isSmoothTab(SameTabClickEvent event) {
        return ((Integer) event.getExt() == 0 && getString(R.string.si_tab_sub1).equals(getArguments().getString("tag"))) || ((Integer) event.getExt() == 1 && getString(R.string.si_tab_sub2).equals(getArguments().getString("tag")));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
