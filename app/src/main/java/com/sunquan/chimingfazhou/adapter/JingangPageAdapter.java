package com.sunquan.chimingfazhou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baizhi.baseapp.adapter.BasePageAdapter;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.models.UserInfo;

/**
 * 金刚道友adapter
 * Created by Administrator on 2015/6/16.
 */
public class JingangPageAdapter extends BasePageAdapter<UserInfo> {

    public JingangPageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getResource(int position) {
        return R.layout.jingang_listview;
    }

    @Override
    protected void renderConvertView(int position, View convertView, ViewGroup parent) {

        JingangViewHolder viewHolder = (JingangViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new JingangViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        final UserInfo userInfo = getItem(position);
        viewHolder.jingang_nickname.setText(userInfo.getFarmington());
        viewHolder.jingang_name.setText(userInfo.getNickname());
        NetController.newInstance(mContext).renderImage(viewHolder.jingang_image, userInfo.getPhoto(), R.drawable.wo_user_picture);
    }

    static class JingangViewHolder extends ViewHolder {
        ImageView jingang_image;
        TextView jingang_nickname;
        TextView jingang_name;

        public JingangViewHolder(View convertView) {
            super(convertView);
            jingang_image = (ImageView) convertView.findViewById(R.id.jingang_image);
            jingang_nickname = (TextView) convertView.findViewById(R.id.jingang_nickname);
            jingang_name = (TextView) convertView.findViewById(R.id.jingang_name);
        }
    }
}
