package com.sunquan.chimingfazhou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baizhi.baseapp.adapter.BasePageAdapter;
import com.baizhi.baseapp.util.IOCUtil;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.models.TeamMember;

/**
 * 开发团队adapter
 * Created by Administrator on 2015/6/16.
 */
public class TeamPageAdapter extends BasePageAdapter<TeamMember> {

    public TeamPageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getResource(int position) {
        return R.layout.team_listview;
    }

    @Override
    protected void renderConvertView(int position, View convertView, ViewGroup parent) {

        TeamViewHolder viewHolder = (TeamViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new TeamViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        final TeamMember tm = getItem(position);
        viewHolder.team_image.setImageBitmap(IOCUtil.getImageFromAssetsFile(mContext, "team/" + tm.getPhoto()));
        viewHolder.team_name.setText(String.format(mContext.getString(R.string.team),tm.getName(),tm.getJob()));
    }


    static class TeamViewHolder extends ViewHolder {
        ImageView team_image;
        TextView team_name;

        public TeamViewHolder(View convertView) {
            super(convertView);
            team_image = (ImageView) convertView.findViewById(R.id.team_image);
            team_name = (TextView) convertView.findViewById(R.id.team_name);
        }
    }
}
