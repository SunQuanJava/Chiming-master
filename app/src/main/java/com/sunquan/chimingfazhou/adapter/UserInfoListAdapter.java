package com.sunquan.chimingfazhou.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baizhi.baseapp.adapter.BasePageAdapter;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.models.UserInfoItem;

/**
 * 用户个人资料页adapter
 */
public class UserInfoListAdapter extends BasePageAdapter<UserInfoItem> {

    private static final int VIEW_TYPE_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    private ImageLoadingListener mCallback;
    private boolean isUpdatePhoto;

    public UserInfoListAdapter(Context ctx) {
        super(ctx);
    }

    public void setCallback(ImageLoadingListener callback) {
        this.mCallback = callback;
    }

    @Override
    protected int getResource(int position) {
        final int resLayout;
        switch (getItem(position).getType()) {
            case VIEW_TYPE_PLACEHOLDER:
                resLayout = R.layout.user_center_item_placeholder;
                break;
            case VIEW_TYPE_NORMAL:
            default:
                resLayout = R.layout.user_info_item;
                break;
        }
        return resLayout;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != VIEW_TYPE_PLACEHOLDER;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType() == UserInfoItem.PLACE_HOLDER ? VIEW_TYPE_PLACEHOLDER : VIEW_TYPE_NORMAL;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    protected void renderConvertView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            final UserInfoItem item = getItem(position);
            UserCenterViewHolder viewHolder = (UserCenterViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new UserCenterViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            viewHolder.key.setText(item.getKey());
            if (item.getType() == UserInfoItem.NORMAL) {
                viewHolder.value.setText(item.getValue());
                viewHolder.value.setVisibility(View.VISIBLE);
                viewHolder.photo.setVisibility(View.GONE);
            } else {
                viewHolder.value.setVisibility(View.GONE);
                viewHolder.photo.setVisibility(View.VISIBLE);
                if (!isUpdatePhoto) {
                    if (!TextUtils.isEmpty(item.getValue())) {
                        NetController.newInstance(mContext).renderImage(viewHolder.photo, item.getValue(), R.drawable.user_default_icon);
                    }
                } else {
                    if (!TextUtils.isEmpty(item.getValue())) {
                        if (mCallback != null) {
                            NetController.newInstance(mContext).renderImage(viewHolder.photo, item.getValue(), R.drawable.user_default_icon, mCallback);
                        } else {
                            NetController.newInstance(mContext).renderImage(viewHolder.photo, item.getValue(), R.drawable.user_default_icon);
                        }
                    } else {
                        if (mCallback != null) {
                            mCallback.onLoadingFailed(item.getValue(),viewHolder.photo,null);
                        }
                    }
                }
            }
        }
    }

    public void notifyDataSetChanged(boolean isUpdatePhoto) {
        this.isUpdatePhoto = isUpdatePhoto;
        notifyDataSetChanged();
    }

    static class UserCenterViewHolder extends ViewHolder {

        public TextView key;
        public TextView value;
        public ImageView photo;

        public UserCenterViewHolder(View convertView) {
            super(convertView);
            key = (TextView) convertView.findViewById(R.id.key);
            value = (TextView) convertView.findViewById(R.id.value);
            photo = (ImageView) convertView.findViewById(R.id.user_icon);
        }
    }
}

