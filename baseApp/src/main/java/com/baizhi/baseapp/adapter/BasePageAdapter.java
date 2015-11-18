package com.baizhi.baseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BasePageAdapter<T> extends BaseAdapter {

	protected List<T> mItems =new ArrayList<T>();
	protected Context mContext;

	public List<T> getItems() {
		return mItems;
	}

	public BasePageAdapter(Context ctx) {
		this.mContext = ctx;
	}

	public void setItems(List<T> mItems) {
			this.mItems = mItems;
	}
	
	@Override
	public int getCount() {
        return mItems == null ? 0 : mItems.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(getResource(position), parent,false);
		}
		renderConvertView(position, convertView, parent);
		return convertView;
	}

	/**
	 * 需要渲染的视图的ID
	 * 
	 * @return
	 */
	protected abstract int getResource(int position);

	/**
	 * 渲染视图
	 * 
	 * @param position
	 * @param convertView
	 */
	protected abstract void renderConvertView(int position, View convertView, ViewGroup parent);
	
	public void notifyChangedWithNoUpdate() {
	}
	public void addAll(Collection<T> list) {
		mItems.addAll(list);
	}

	public void clear() {
		if(!mItems.isEmpty())
			mItems.clear();
	}

    public void remove(int arg0) {//删除指定位置的item
        mItems.remove(arg0);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(T item, int arg0) {//在指定位置插入item
        mItems.add(arg0, item);
        this.notifyDataSetChanged();
    }

    public static abstract class ViewHolder {
        private View convertView;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }
    }
}
