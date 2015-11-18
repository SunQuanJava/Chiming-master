package com.sunquan.chimingfazhou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sunquan.chimingfazhou.R;

import java.util.ArrayList;

import dev.dworks.libs.astickyheader.SimpleSectionedListAdapter;

/**
 * 主页dapter
 *
 * Created by Administrator on 2015/5/6.
 */
public class MainPageSimpleSectionedListAdapter extends SimpleSectionedListAdapter {
    public MainPageSimpleSectionedListAdapter(Context context, BaseAdapter baseAdapter) {
        super(context, baseAdapter, 0,0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isSectionHeaderPosition(position)) {
            if(null == convertView){
                convertView = new View(parent.getContext());
            }
            return convertView;
        } else {
            return mBaseAdapter.getView(sectionedPositionToPosition(position), convertView, parent);
        }
    }

    public void setPinnedPosition(int... positions) {
        final ArrayList<Section> sections = new ArrayList<>();
        for(Integer position: positions) sections.add(new Section(position, ""));
        setSections(sections.toArray(new Section[sections.size()]));
    }
}
