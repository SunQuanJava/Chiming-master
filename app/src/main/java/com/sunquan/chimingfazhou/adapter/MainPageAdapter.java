package com.sunquan.chimingfazhou.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baizhi.baseapp.adapter.BasePageAdapter;
import com.baizhi.baseapp.adapter.MutilLayoutBaseAdapter;
import com.baizhi.baseapp.util.DeviceUtils;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.models.MainPageBodyInfo;

import java.util.ArrayList;

/**
 * 主页adapter
 * <p/>
 * Created by Administrator on 2015/5/4.
 */
public class MainPageAdapter extends MutilLayoutBaseAdapter<MainPageBodyInfo> {

    private static final int TOTAL_ITEM_TYPE_COUNT = 3;
    private MainPageClickListener mMainPageClickListener;

    public MainPageAdapter(Context context) {
        super(context);
    }

    public void setOnMainPageClickListener(MainPageClickListener mainPageClickListener) {
        this.mMainPageClickListener = mainPageClickListener;
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).getType().equals(MainPageBodyInfo.PLASE_HOLDER);
    }

    @Override
    public int getItemType(int position) {
        final MainPageBodyInfo pageBodyInfo = getItem(position);
        final String type = pageBodyInfo.getType();
        return Integer.valueOf(type);
    }

    @Override
    public BasePageAdapter.ViewHolder getViewHolder(int itemType, View convertView) {
        BasePageAdapter.ViewHolder viewHolder = null;
        if (itemType == Integer.valueOf(MainPageBodyInfo.WEN)) {
            viewHolder = new WenViewHolder(convertView);
        } else if (itemType == Integer.valueOf(MainPageBodyInfo.SI)) {
            viewHolder = new SiViewHolder(convertView);
        } else if (itemType == Integer.valueOf(MainPageBodyInfo.PLASE_HOLDER)) {
            viewHolder = new PlaceHolderViewHolder(convertView);
        }
        return viewHolder;
    }

    @Override
    public View getLayoutByItemType(int itemType, ViewGroup parent) {
        int resId = -1;
        if (itemType == Integer.valueOf(MainPageBodyInfo.WEN)) {
            resId = R.layout.shouye_wen_item_layout;
        } else if (itemType == Integer.valueOf(MainPageBodyInfo.SI)) {
            resId = R.layout.shouye_si_item_layout;
        } else if (itemType == Integer.valueOf(MainPageBodyInfo.PLASE_HOLDER)) {
            resId = R.layout.shouye_placeholder_item_layout;
        }
        return mInflater.inflate(resId, parent, false);
    }

    @Override
    public void bindData(View convertView, int position, BasePageAdapter.ViewHolder viewHolder) {
        if (viewHolder instanceof WenViewHolder) {
            bindWenData((WenViewHolder) viewHolder, getItem(position));
        } else if (viewHolder instanceof SiViewHolder) {
            bindSiData((SiViewHolder) viewHolder, getItem(position));
        } else if (viewHolder instanceof PlaceHolderViewHolder) {
            bindPlaceHolderData((PlaceHolderViewHolder) viewHolder, getItem(position));
        }
    }

    /**
     * 绑定闻item的数据
     *
     * @param viewHolder
     * @param item
     */
    private void bindWenData(WenViewHolder viewHolder, MainPageBodyInfo item) {
        final ArrayList<MainPageBodyInfo> mainPageBodyInfos = item.getItems();
        if (mainPageBodyInfos == null || mainPageBodyInfos.isEmpty()) {
            return;
        }
        if (mainPageBodyInfos.size() > 0) {
            final MainPageBodyInfo leftItem = mainPageBodyInfos.get(0);
            viewHolder.leftWrapper.setVisibility(View.VISIBLE);
            viewHolder.leftTitle.setText(leftItem.getTitle());
            viewHolder.leftCount.setText(leftItem.getSet_count());
            NetController.newInstance(mContext).renderImage(viewHolder.leftThumbnail, leftItem.getThumbnail());
            viewHolder.leftWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMainPageClickListener != null) {
                        mMainPageClickListener.onMainPageItemClicked(leftItem);
                    }
                }
            });
        } else {
            viewHolder.leftWrapper.setVisibility(View.INVISIBLE);
            viewHolder.centerWrapper.setVisibility(View.INVISIBLE);
            viewHolder.rightWrapper.setVisibility(View.INVISIBLE);
            return;
        }
        if (mainPageBodyInfos.size() > 1) {
            final MainPageBodyInfo centerItem = mainPageBodyInfos.get(1);
            viewHolder.centerWrapper.setVisibility(View.VISIBLE);
            viewHolder.centerTitle.setText(centerItem.getTitle());
            viewHolder.centerCount.setText(centerItem.getSet_count());
            NetController.newInstance(mContext).renderImage(viewHolder.centerThumbnail, centerItem.getThumbnail());
            viewHolder.centerWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMainPageClickListener != null) {
                        mMainPageClickListener.onMainPageItemClicked(centerItem);
                    }
                }
            });
        } else {
            viewHolder.centerWrapper.setVisibility(View.INVISIBLE);
            viewHolder.rightWrapper.setVisibility(View.INVISIBLE);
            return;
        }
        if (mainPageBodyInfos.size() > 2) {
            final MainPageBodyInfo rightItem = mainPageBodyInfos.get(2);
            viewHolder.rightWrapper.setVisibility(View.VISIBLE);
            viewHolder.rightTitle.setText(rightItem.getTitle());
            viewHolder.rightCount.setText(rightItem.getSet_count());
            NetController.newInstance(mContext).renderImage(viewHolder.rightThumbnail, rightItem.getThumbnail());
            viewHolder.rightWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMainPageClickListener != null) {
                        mMainPageClickListener.onMainPageItemClicked(rightItem);
                    }
                }
            });
        } else {
            viewHolder.rightWrapper.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 绑定思item的数据
     *
     * @param viewHolder
     * @param item
     */
    private void bindSiData(SiViewHolder viewHolder, final MainPageBodyInfo item) {
        viewHolder.title.setText(item.getTitle());
        viewHolder.desc.setText(item.getAuthor());
        viewHolder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainPageClickListener != null) {
                    mMainPageClickListener.onMainPageItemClicked(item);
                }
            }
        });
        //渲染日期
        if (!TextUtils.isEmpty(item.getCreate_date())) {
            viewHolder.createDateView.setVisibility(View.VISIBLE);
            viewHolder.createDateView.setText("发布日期：" + item.getCreate_date());
        } else {
            viewHolder.createDateView.setVisibility(View.GONE);
        }
        //渲染图片
        if (!TextUtils.isEmpty(item.getThumbnail())) {
            viewHolder.icon.setVisibility(View.VISIBLE);
            NetController.newInstance(mContext).renderImage(viewHolder.icon, item.getThumbnail(), R.drawable.avatar_default_circle);
        } else {
            viewHolder.icon.setVisibility(View.GONE);
        }
    }

    /**
     * 绑定分割item的数据
     *
     * @param viewHolder
     * @param item
     */
    private void bindPlaceHolderData(PlaceHolderViewHolder viewHolder, MainPageBodyInfo item) {
        viewHolder.title.setText(item.getTitle());
        viewHolder.wrapper.setEnabled(false);
        viewHolder.iconView.setBackgroundResource(item.getPlaceHolderResId());
    }

    @Override
    public int getItemTypeCount() {
        return TOTAL_ITEM_TYPE_COUNT;
    }

    static class WenViewHolder extends BasePageAdapter.ViewHolder {
        public View leftWrapper;
        public View centerWrapper;
        public View rightWrapper;
        public ImageView leftThumbnail;
        public ImageView centerThumbnail;
        public ImageView rightThumbnail;
        public TextView leftTitle;
        public TextView centerTitle;
        public TextView rightTitle;
        public TextView leftCount;
        public TextView centerCount;
        public TextView rightCount;

        public WenViewHolder(View convertView) {
            super(convertView);
            leftWrapper = convertView.findViewById(R.id.left_wrapper);
            centerWrapper = convertView.findViewById(R.id.center_wrapper);
            rightWrapper = convertView.findViewById(R.id.right_wrapper);
            leftThumbnail = (ImageView) convertView.findViewById(R.id.left_thumbnail);
            centerThumbnail = (ImageView) convertView.findViewById(R.id.center_thumbnail);
            rightThumbnail = (ImageView) convertView.findViewById(R.id.right_thumbnail);
            leftTitle = (TextView) convertView.findViewById(R.id.left_title);
            centerTitle = (TextView) convertView.findViewById(R.id.center_title);
            rightTitle = (TextView) convertView.findViewById(R.id.right_title);
            final int tempWidth = (int) ((float) DeviceUtils.getWindowWidth(convertView.getContext()) / 3 - convertView.getContext().getResources().getDimensionPixelSize(R.dimen.shouye_wen_item_padding_offset));
            final float scale = (float) 230 / 206;
            final int height = Math.round((float) tempWidth * scale);
            leftThumbnail.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
            centerThumbnail.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
            rightThumbnail.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
            leftCount = (TextView) convertView.findViewById(R.id.left_count);
            centerCount = (TextView) convertView.findViewById(R.id.center_count);
            rightCount = (TextView) convertView.findViewById(R.id.right_count);
        }
    }

    static class SiViewHolder extends BasePageAdapter.ViewHolder {

        public TextView title;
        public TextView desc;
        public View wrapper;
        public ImageView icon;
        public TextView createDateView;

        public SiViewHolder(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            desc = (TextView) convertView.findViewById(R.id.desc);
            wrapper = convertView.findViewById(R.id.si_wrapper);
            icon = (ImageView) convertView.findViewById(R.id.icon);
            createDateView = (TextView) convertView.findViewById(R.id.createDate);
        }
    }

    static class PlaceHolderViewHolder extends BasePageAdapter.ViewHolder {

        public TextView title;
        public View wrapper;
        public ImageView iconView;

        public PlaceHolderViewHolder(View convertView) {
            super(convertView);
            title = (TextView) convertView.findViewById(R.id.title);
            wrapper = convertView.findViewById(R.id.placeholder_wrapper);
            iconView = (ImageView) convertView.findViewById(R.id.icon);
        }
    }

    /**
     * 每一项item的点击事件监听
     */
    public interface MainPageClickListener {
        void onMainPageItemClicked(MainPageBodyInfo mainPageBodyInfo);
    }
}
