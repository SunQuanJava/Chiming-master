package com.sunquan.chimingfazhou.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.constants.Constants;
import com.sunquan.chimingfazhou.event.BackEvent;
import com.sunquan.chimingfazhou.event.SameTabClickEvent;
import com.sunquan.chimingfazhou.event.TabChangeEvent;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;
import com.sunquan.chimingfazhou.models.MainPageInfo;
import com.sunquan.chimingfazhou.util.ParamsUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 闻fragment
 *
 * Created by Administrator on 2015/4/25.
 */
public class WenFragment extends BaseMainPageFragment {

    public boolean mIsInitLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        setLeftContentIcon(R.drawable.back_icon_selector);
        setCenterText(getString(R.string.tab_wen_tip));
        return super.getContentView(inflater,container);
    }



    @Override
    protected void postExecutePageInfos(MainPageInfo mainPageInfo) {
        final ArrayList<MainPageBodyInfo> bodyInfos = mainPageInfo.getBody();
        //step2，过滤掉所有的非闻的item
        final ArrayList<MainPageBodyInfo> tempBodyInfos = new ArrayList<>();
        for(MainPageBodyInfo bodyInfo: bodyInfos) {
            if(bodyInfo.getType().equals(MainPageBodyInfo.WEN)) {
                tempBodyInfos.add(bodyInfo);
            }
        }

        //step3，将之前的闻得item重新组装，三个一组
        int count = tempBodyInfos.size();
        final ArrayList<MainPageBodyInfo> tempBodyInfos2 = new ArrayList<>();
        for(int i=0;i<count;i++) {
            final MainPageBodyInfo result = new MainPageBodyInfo();
            final ArrayList<MainPageBodyInfo> tempItems = new ArrayList<>();
            result.setType(MainPageBodyInfo.WEN);
            if(i<count) {
                MainPageBodyInfo item1 = tempBodyInfos.get(i);
                tempItems.add(item1);
                i++;
            }
            if(i<count) {
                MainPageBodyInfo item2 = tempBodyInfos.get(i);
                tempItems.add(item2);
                i++;
            }
            if(i<count) {
                MainPageBodyInfo item3 = tempBodyInfos.get(i);
                tempItems.add(item3);
            }
            result.setItems(tempItems);
            tempBodyInfos2.add(result);
        }

        //step4，将组装好的item放进去
        bodyInfos.clear();
        bodyInfos.addAll(tempBodyInfos2);
    }

    @Override
    protected String getUrl() {
        return ParamsUtil.getFirstPageUrlByType(getActivity(), Constants.PARAMETER_TYPE_WEN);
    }


    @Override
    protected void handleClickEvent(int event) {
        if(event==LEFT_BUTTON) EventBus.getDefault().post(new BackEvent(WenFragment.class));
    }

    @Override
    protected boolean isShowTitle() {
        return true;
    }

    public void onEvent(TabChangeEvent event) {
        final String className = event.getClassName();
        if(className.equals(((Object)this).getClass().getSimpleName()) && !mIsInitLoading) {
            mIsInitLoading = true;
            onUpdate();
        }
    }

    public void onEvent(SameTabClickEvent event) {
        final String className = event.getClassName();
        if(className.equals(((Object)this).getClass().getSimpleName()) && mListView!=null && mListView.getFirstVisiblePosition()!=0) {
            mListView.smoothScrollToPosition(0);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
