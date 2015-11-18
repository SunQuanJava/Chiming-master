package com.baizhi.baseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author sunquan
 *         多布局Base适配器
 */
public abstract class MutilLayoutBaseAdapter<T> extends BaseAdapter {

    protected List<T> mItems;
    protected Context mContext;

    public LayoutInflater mInflater;

    public MutilLayoutBaseAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position);
    }

    @Override
    public int getViewTypeCount() {
        return getItemTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BasePageAdapter.ViewHolder viewHolder;
        if (null == convertView) {
            convertView = getLayoutByItemType(getItemType(position), parent);
            viewHolder = getViewHolder(getItemType(position), convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BasePageAdapter.ViewHolder) convertView.getTag();
        }
        bindData(convertView, position, viewHolder);
        return convertView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public abstract int getItemType(int position);

    public abstract BasePageAdapter.ViewHolder getViewHolder(int type, View convertView);

    public abstract View getLayoutByItemType(int itemType, ViewGroup parent);

    public abstract void bindData(View convertView, int position, BasePageAdapter.ViewHolder viewHolder);

    public abstract int getItemTypeCount();

    public List<T> getmItems() {
        return mItems;
    }

    public void setmItems(List<T> mItems) {
        this.mItems = mItems;
    }

}
