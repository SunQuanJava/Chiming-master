package com.sunquan.chimingfazhou.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.baizhi.baseapp.widget.NavigationBarView;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.event.BackEvent;
import com.sunquan.chimingfazhou.event.CoverEvent;
import com.sunquan.chimingfazhou.event.SameTabClickEvent;
import com.sunquan.chimingfazhou.event.TabChangeEvent;
import com.sunquan.chimingfazhou.fragment.MainFragment;
import com.sunquan.chimingfazhou.fragment.SiFragment;
import com.sunquan.chimingfazhou.fragment.WenFragment;
import com.sunquan.chimingfazhou.fragment.WoFragment;
import com.sunquan.chimingfazhou.fragment.XiuFragment;
import com.sunquan.chimingfazhou.util.IntentUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * 主页面activity
 */
public class MainActivity extends AppBaseActivity implements NavigationBarView.OnTabChangeListener, NavigationBarView.OnSameTabClickListener {

    @Bind(R.id.tabHost) NavigationBarView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        setContentLayout(R.layout.main_tab_radiogroup);
        ButterKnife.bind(this);
        setCenterText(getString(R.string.app_name));

        mNavigation.addTab(getString(R.string.tab_shouye_tip), MainFragment.class);
        mNavigation.addTab(getString(R.string.tab_wen_tip), WenFragment.class);
        mNavigation.addTab(getString(R.string.tab_si_tip), SiFragment.class);
        mNavigation.addTab(getString(R.string.tab_xiu_tip), XiuFragment.class);
        mNavigation.addTab(getString(R.string.tab_wo_tip), WoFragment.class);
        mNavigation.setup(getSupportFragmentManager());
        mNavigation.setOnTabChangedListener(this);
        mNavigation.setOnSameTabClickListener(this);
        mNavigation.setCurrentTab(0);

    }

    private Class getClassByIndex(int i) {
        Class clazz = null;
        switch (i) {
            case 0:
                clazz = MainFragment.class;
                break;
            case 1:
                clazz = WenFragment.class;
                break;
            case 2:
                clazz = SiFragment.class;
                break;
            case 3:
                clazz = XiuFragment.class;
                break;
            case 4:
                clazz = WoFragment.class;
                break;
            default:
                break;
        }
        return clazz;
    }

    @Override
    public void onTabChanged(int i, String tabName) {
        final Class clazz = getClassByIndex(i);
        if (clazz != null) {
            EventBus.getDefault().post(new TabChangeEvent(clazz.getSimpleName()));
        }
    }

    @Override
    public void onSameTabClicked(int i, String tabName) {
        final Class clazz = getClassByIndex(i);
        if (clazz != null) {
            EventBus.getDefault().post(new SameTabClickEvent(clazz.getSimpleName()));
        }
    }

    @Override
    protected boolean isShowTitle() {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mNavigation.getCurrentTab() != 0) {
            mNavigation.setCurrentTab(0);
        } else {
//            //退到后台
            IntentUtils.goToHome(this);
        }

    }

    public void onEvent(BackEvent backEvent) {
        Class<?> fragmentClass = backEvent.getClazz();
        if (fragmentClass == WenFragment.class || fragmentClass == SiFragment.class || fragmentClass == XiuFragment.class || fragmentClass == WoFragment.class) {
            if (mNavigation.getCurrentTab() != 0) {
                mNavigation.setCurrentTab(0);
            }
        }
    }

    public void onEvent(CoverEvent coverEvent) {
        final String XIU_DETAIL = "xiu_detail";
        checkNewGuidePage(XIU_DETAIL,R.layout.xiu_cover_del_pager,R.id.cover_cancel);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
