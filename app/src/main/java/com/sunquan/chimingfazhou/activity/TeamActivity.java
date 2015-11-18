package com.sunquan.chimingfazhou.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.adapter.TeamPageAdapter;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.models.TeamMember;

import java.util.ArrayList;

/**
 * 开发团队页
 * Created by Administrator on 2015/6/16.
 */
public class TeamActivity extends BaseSwipeBackActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_team);
        setLeftContentIcon(R.drawable.back_icon_selector);
        setCenterText(getString(R.string.wo_team));
        init();
    }

    /**
     * 初始化listView
     */
    private void init(){
        //获取数据
        final ArrayList<TeamMember> data = GlobalDataHolder.getInstance(this).getCommonConfigBean().getTeams();
        final ListView listView = (ListView) findViewById(R.id.team_list);
        //设置emptyView
        final View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, listView, false);
        final ViewGroup parentView = (ViewGroup) listView.getParent();
        parentView.addView(emptyView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setEmptyView(emptyView);
        //设置adapter
        final TeamPageAdapter adapter = new TeamPageAdapter(this);
        adapter.setItems(data);
        listView.setAdapter(adapter);
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
}
