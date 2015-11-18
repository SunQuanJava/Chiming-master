package com.sunquan.chimingfazhou.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baizhi.baseapp.adapter.BasePageAdapter;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.models.XiuInfo;
import com.sunquan.chimingfazhou.util.IntentUtils;

/**
 * 修adapter
 * <p/>
 * Created by Administrator on 2015/5/13.
 */
public class XiuPageAdapter extends BasePageAdapter<XiuInfo> {

    private static final int XIU_TYPE_COUNT = 3;

    public XiuPageAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public int getViewTypeCount() {
        return XIU_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getShowType()==XiuInfo.TYPE_DEFAULT?XiuInfo.TYPE_DEFAULT:position==2?2:XiuInfo.TYPE_MODIFIABLE;
    }

    @Override
    protected int getResource(int position) {
        return R.layout.xiu_item_layout;
    }

    @Override
    protected void renderConvertView(int position, View convertView, ViewGroup parent) {
        XiuViewHolder viewHolder = (XiuViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new XiuViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        final XiuInfo xiuInfo = getItem(position);
        viewHolder.text.setText(xiuInfo.getTask_name());
        final int resId = getResIdByTaskName(xiuInfo.getTask_name());
        viewHolder.text.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        if (position == 1 && getCount()>2) {
            viewHolder.dividerView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.dividerView.setVisibility(View.GONE);
        }
        if (position == 1 || position == getCount() - 1) {
            viewHolder.line.setVisibility(View.GONE);
        } else {
            viewHolder.line.setVisibility(View.VISIBLE);
        }

        if (xiuInfo.getShowType() == XiuInfo.TYPE_DEFAULT) {
            convertView.setOnTouchListener(new View.OnTouchListener() {

                private int offset = 10;
                private float dx;
                private float dy;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final float x = event.getX();
                    final float y = event.getY();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            dx = x;
                            dy = y;
                            v.setPressed(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            v.setPressed(false);
                            if (Math.abs(dx - x) < offset && Math.abs(dy - y) < offset) {
                                IntentUtils.goToXiuSubPage(mContext, xiuInfo);
                            }
                            break;
                    }
                    return true;
                }
            });
        } else {
            convertView.setOnTouchListener(null);
        }
    }

    private int getResIdByTaskName(String task_name) {
        int resId;
        if (task_name.equals(mContext.getString(R.string.chizhousongjing))) {
            resId = R.drawable.xiu_songjing;
        } else if (task_name.equals(mContext.getString(R.string.chengxinlifo))) {
            resId = R.drawable.xiu_lifo;
        } else {
            resId = R.drawable.xiu_manja;
        }
        return resId;
    }

    static class XiuViewHolder extends ViewHolder {
        public TextView text;
        public View dividerView;
        public View line;

        public XiuViewHolder(View convertView) {
            super(convertView);
            text = (TextView) convertView.findViewById(R.id.xiu_name);
            dividerView = convertView.findViewById(R.id.dividerView);
            line = convertView.findViewById(R.id.line);
        }
    }
}
