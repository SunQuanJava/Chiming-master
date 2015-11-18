package com.sunquan.chimingfazhou.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baizhi.baseapp.widget.NavigationBarView;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.event.BackEvent;
import com.sunquan.chimingfazhou.event.SameTabClickEvent;
import com.sunquan.chimingfazhou.event.TabChangeEvent;

import de.greenrobot.event.EventBus;

/**
 * 思fragment
 * <p/>
 * Created by Administrator on 2015/4/25.
 */
public class SiFragment extends AppBaseFragment implements NavigationBarView.OnTabChangeListener {

    private NavigationBarView mTHost;
    private ImageView mTriangleLeftView;
    private ImageView mTriangleRightView;
    private int mCurrentTab;
    public boolean mIsInitLoading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        setLeftContentIcon(R.drawable.back_icon_selector);
        setCenterText(getString(R.string.tab_si_tip));
        final View view = inflater.inflate(R.layout.fragment_si_layout, container, false);
        mTHost = (NavigationBarView) view.findViewById(R.id.tabHost);
        mTriangleLeftView = (ImageView) view.findViewById(R.id.triangleLeft);
        mTriangleRightView = (ImageView) view.findViewById(R.id.triangleRight);
        mTriangleRightView.setVisibility(View.INVISIBLE);
        mTriangleLeftView.setVisibility(View.VISIBLE);

        return view;
    }

    private void initTabHost() {
        mTHost.setSmoothly(true);
        mTHost.addTab(getString(R.string.si_tab_sub1), SiSubfragment.class);
        mTHost.addTab(getString(R.string.si_tab_sub2), SiSubfragment.class);
        mTHost.setup(getFragmentManager());
        mTHost.setOnTabChangedListener(this);
        mTHost.setCurrentTab(0);
    }

    @Override
    protected void handleClickEvent(int event) {
        if (event == LEFT_BUTTON) EventBus.getDefault().post(new BackEvent(SiFragment.class));
    }

    @Override
    protected boolean isShowTitle() {
        return true;
    }

    @Override
    public void onTabChanged(int i, String tabName) {
        mCurrentTab = i;
        if (i == 0) {
            mTriangleLeftView.setVisibility(View.VISIBLE);
            mTriangleRightView.setVisibility(View.INVISIBLE);
        } else {
            mTriangleLeftView.setVisibility(View.INVISIBLE);
            mTriangleRightView.setVisibility(View.VISIBLE);
        }
        if(i == 1) {
            EventBus.getDefault().post(new TabChangeEvent(SiSubfragment.class.getSimpleName()));
        }
    }

    public void onEvent(TabChangeEvent event) {
        final String className = event.getClassName();
        if (className.equals(((Object) this).getClass().getSimpleName()) && !mIsInitLoading) {
            mIsInitLoading = true;
            initTabHost();
        }
    }

    public void onEvent(SameTabClickEvent event) {
        final String className = event.getClassName();
        if (className.equals(((Object) this).getClass().getSimpleName())) {
            EventBus.getDefault().post(new SameTabClickEvent(SiSubfragment.class.getSimpleName(), mCurrentTab));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
