package com.sunquan.chimingfazhou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baizhi.baseapp.adapter.BasePageAdapter;
import com.baizhi.baseapp.util.CalendarUtils;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.models.XiuSubInfo;

/**
 * 修详情adapter
 * <p/>
 * Created by Administrator on 2015/5/14.
 */
public class XiuDetailPageAdapter extends BasePageAdapter<XiuSubInfo> {

    public XiuDetailPageAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected int getResource(int position) {
        return R.layout.xiu_sub_item_layout;
    }


    @Override
    protected void renderConvertView(int position, View convertView, ViewGroup parent) {
        XiuSubViewHolder viewHolder = (XiuSubViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new XiuSubViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        final XiuSubInfo xiuSubInfo = getItem(position);
        viewHolder.countView.setText(xiuSubInfo.getCount());
        viewHolder.nameView.setText(xiuSubInfo.getTask_name());
        viewHolder.dateView.setText(CalendarUtils.getTimeForList(Long.valueOf(xiuSubInfo.getLast_practice_time())));
    }

    static class XiuSubViewHolder extends ViewHolder {

        public TextView countView;
        public TextView nameView;
        public TextView dateView;

        public XiuSubViewHolder(View convertView) {
            super(convertView);
            countView = (TextView) convertView.findViewById(R.id.count_text);
            nameView = (TextView) convertView.findViewById(R.id.name);
            dateView = (TextView) convertView.findViewById(R.id.date);
        }
    }
}
