package com.sunquan.chimingfazhou.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.adapter.MainPageAdapter;
import com.sunquan.chimingfazhou.adapter.MainPageSimpleSectionedListAdapter;
import com.sunquan.chimingfazhou.constants.Constants;
import com.sunquan.chimingfazhou.event.SameTabClickEvent;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;
import com.sunquan.chimingfazhou.models.MainPageHeaderInfo;
import com.sunquan.chimingfazhou.models.MainPageInfo;
import com.sunquan.chimingfazhou.util.ParamsUtil;
import com.sunquan.chimingfazhou.widget.PhotoBrowseView;
import com.sunquan.chimingfazhou.widget.PullDownView;

import java.util.ArrayList;
import java.util.Collections;

import de.greenrobot.event.EventBus;


/**
 * 首页fragment
 *
 * Created by Administrator on 2015/4/30.
 */
public class MainFragment extends BaseMainPageFragment implements PhotoBrowseView.OnItemPhotoClickedListener{

    private PhotoBrowseView mPhotoBrowseView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        final View view = super.getContentView(inflater,container);
        setLoadingContent(R.layout.main_page_loading_state_layout);
        setCenterText(getString(R.string.app_name));
        initListView(view);
        onUpdate();
        return view;
    }

    @Override
    protected void handleClickEvent(int event) {
    }

    private void initListView(View view) {
        mListView = (ListView) view.findViewById(R.id.list);
        mPullDownView = (PullDownView) view.findViewById(R.id.pull_down_list);
        mPullDownView.setUpdateHandle(this);
        mAdapter = new MainPageAdapter(getActivity());
        mAdapter.setmItems(new ArrayList<MainPageBodyInfo>());
        mAdapter.setOnMainPageClickListener(this);
        mSimpleSectionedGridAdapter = new MainPageSimpleSectionedListAdapter(getActivity(), mAdapter);
        mPhotoBrowseView = new PhotoBrowseView(getActivity());
        mPhotoBrowseView.setOnItemPhotoClickedListener(this);
        mListView.addHeaderView(mPhotoBrowseView);
        mListView.setAdapter(mSimpleSectionedGridAdapter);
    }

    @Override
    protected void postExecutePageInfos(MainPageInfo mainPageInfo) {
        final ArrayList<MainPageBodyInfo> bodyInfos = mainPageInfo.getBody();
        //step1,排序，把所有闻的item放到前面
        Collections.sort(bodyInfos);

        //step2，提取所有闻得item
        final ArrayList<MainPageBodyInfo> tempBodyInfos = new ArrayList<>();
        for(MainPageBodyInfo bodyInfo: bodyInfos) {
            if(bodyInfo.getType().equals(MainPageBodyInfo.WEN)) {
                tempBodyInfos.add(bodyInfo);
            }
        }

        //step3，将之前的闻得item重新组装，三个一组
        bodyInfos.removeAll(tempBodyInfos);
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
        bodyInfos.addAll(tempBodyInfos2);
        Collections.sort(bodyInfos);

        //step5，增加占位item
        final MainPageBodyInfo wenPlaceHolder = new MainPageBodyInfo();
        wenPlaceHolder.setType(MainPageBodyInfo.PLASE_HOLDER);
        wenPlaceHolder.setTitle(getString(R.string.tab_wen_tip));
        wenPlaceHolder.setPlaceHolderResId(R.drawable.shouye_wen_icon);
        bodyInfos.add(0,wenPlaceHolder);
        final MainPageBodyInfo siPlaceHolder = new MainPageBodyInfo();
        siPlaceHolder.setType(MainPageBodyInfo.PLASE_HOLDER);
        siPlaceHolder.setTitle(getString(R.string.tab_si_tip));
        siPlaceHolder.setPlaceHolderResId(R.drawable.shouye_si_icon);
        bodyInfos.add(tempBodyInfos2.size() + 1, siPlaceHolder);
        mSimpleSectionedGridAdapter.setPinnedPosition(1,tempBodyInfos2.size()+2);
        mPhotoBrowseView.setPhotos(mainPageInfo.getHeader());
    }

    @Override
    protected String getUrl() {
        return ParamsUtil.getFirstPageUrlByType(getActivity(), Constants.PARAMETER_TYPE_ALL);
    }

    @Override
    protected boolean isShowTitle() {
        return true;
    }

    @Override
    public void onItemPhotoClicked(View itemView, MainPageHeaderInfo headerInfo, int index) {
    }

    @SuppressWarnings("unused")
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
