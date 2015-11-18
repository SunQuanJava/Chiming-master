package com.sunquan.chimingfazhou.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baizhi.baseapp.util.DeviceUtils;
import com.baizhi.baseapp.widget.AutoScrollViewPager;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.NetController;
import com.sunquan.chimingfazhou.models.MainPageHeaderInfo;

import java.util.ArrayList;

/**
 * 首页顶部轮播图
 * <p/>
 * Created by sunquan1 on 2015/1/9.
 */
public class PhotoBrowseView extends RelativeLayout {

    private ArrayList<ImageView> mImageViews;
    private TextView mTitleView;
    private TextView mIndicator;
    private AutoScrollViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private OnItemPhotoClickedListener mOnItemPhotoClickedListener;

    private boolean mIsChanged;
    private int mCurrentPagePosition;
    private ArrayList<MainPageHeaderInfo> mPhotos;

    public PhotoBrowseView(Context context) {
        super(context);
        init();
    }

    public PhotoBrowseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoBrowseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        final View headerView = LayoutInflater.from(getContext()).inflate(R.layout.user_info_header, this, true);
        final float scale = (float) 360 / 640;
        int width = DeviceUtils.getWindowWidth(getContext());
        final int height = (int) (width * scale);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height));
        mImageViews = new ArrayList<>();
        mViewPager = (AutoScrollViewPager) headerView.findViewById(R.id.viewpager);
        mTitleView = (TextView) headerView.findViewById(R.id.text);
        mIndicator = (TextView) headerView.findViewById(R.id.indicater);
        mViewPager.setBorderAnimation(false);
        mViewPager.startAutoScroll();
        mViewPager.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, DeviceUtils.getWindowWidth(getContext())));
    }

    private void initPhotos(ArrayList<MainPageHeaderInfo> photos) {
        mPhotos = photos;
        if (!mImageViews.isEmpty()) {
            mImageViews.clear();
        }
        final int count = photos.size();
        //无论是否多于1个，都要初始化第一个（index:0）
        mImageViews.add(getImageView(photos, count - 1));
        //注意，如果不只1个，mViews比mList多两个（头尾各多一个）
        //假设：mList为mList[0~N-1], mViews为mViews[0~N+1]
        // mViews[0]放mList[N-1], mViews[i]放mList[i-1], mViews[N+1]放mList[0]
        // mViews[1~N]用于循环；首位之前的mViews[0]和末尾之后的mViews[N+1]用于跳转
        // 首位之前的mViews[0]，跳转到末尾（N）；末位之后的mViews[N+1]，跳转到首位（1）
        if (count > 1) {//多于1个要循环
            //中间的N个（index:1~N）
            for (int i = 0; i < count; i++) {
                mImageViews.add(getImageView(photos, i));
            }
            //最后一个
            mImageViews.add(getImageView(photos, 0));
        }
        if (mImageViews.size() <= 1) {
            mIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private ImageView getImageView(ArrayList<MainPageHeaderInfo> photos, final int index) {
        final ImageView imageView = new ImageView(getContext());
        imageView.setTag(photos.get(index));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemPhotoClickedListener != null) {
                    mOnItemPhotoClickedListener.onItemPhotoClicked(imageView, (MainPageHeaderInfo) imageView.getTag(), index);
                }
            }
        });
        NetController.newInstance(getContext()).renderImage(imageView, ((MainPageHeaderInfo) imageView.getTag()).getThumbnail(), R.drawable.avatar_default_horizontal);
        return imageView;
    }

    public void setOnItemPhotoClickedListener(OnItemPhotoClickedListener onItemPhotoClickedListener) {
        this.mOnItemPhotoClickedListener = onItemPhotoClickedListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mViewPager != null && mViewPager.isAutoScroll()) {
            mViewPager.stopAutoScroll();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mViewPager != null && !mViewPager.isAutoScroll()) {
            mViewPager.stopAutoScroll();
        }
    }

    /**
     * 设置图片
     *
     * @param photos
     */
    public void setPhotos(final ArrayList<MainPageHeaderInfo> photos) {
        if (photos == null || photos.isEmpty()) {
            throw new IllegalArgumentException("Photos should not be null or empty");
        }
        if (mPhotos != null && !mImageViews.isEmpty() && mPhotos.equals(photos)) {
            resetViewPager();
            return;
        }
        initPhotos(photos);
        if (mViewPagerAdapter == null) {
            mViewPagerAdapter = new ViewPagerAdapter();
            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (mImageViews.size() > 1) { //多于1，才会循环跳转
                        if (position < 1) { //首位之前，跳转到末尾（N）
                            mIsChanged = true;
                            mCurrentPagePosition = photos.size();
                        } else if (position > photos.size()) { //末位之后，跳转到首位（1）
                            mIsChanged = true;
                            mCurrentPagePosition = 1;
                        } else {
                            mCurrentPagePosition = position;
                        }
                    } else {
                        mCurrentPagePosition = position;
                    }
                    mTitleView.setText(((MainPageHeaderInfo) mImageViews.get(position).getTag()).getDesc());
                    mIndicator.setText(mCurrentPagePosition + "/" + photos.size());
                }

                @Override
                public void onPageScrollStateChanged(int pState) {
                    if (ViewPager.SCROLL_STATE_IDLE == pState) {
                        if (mIsChanged) {
                            mIsChanged = false;
                            mViewPager.setCurrentItem(mCurrentPagePosition, false);
                        }
                    }
                }
            });
        } else {
            mViewPager.post(new Runnable() {
                @Override
                public void run() {
                    mViewPagerAdapter.notifyDataSetChanged();
                }
            });
        }
        resetViewPager();
    }

    private void resetViewPager() {
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(1, false);
                mIndicator.setText(1 + "/" + mPhotos.size());
                mTitleView.setText(((MainPageHeaderInfo) mImageViews.get(1).getTag()).getDesc());
            }
        });
    }

    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            final ImageView imageView = mImageViews.get(position);
            view.removeView(imageView);
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            final ImageView imageView = mImageViews.get(position);

            view.removeView(imageView);
            view.addView(imageView);
            return imageView;
        }

    }

    public interface OnItemPhotoClickedListener {
        void onItemPhotoClicked(View itemView, MainPageHeaderInfo headerInfo, int index);
    }
}
