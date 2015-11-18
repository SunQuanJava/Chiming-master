package com.sunquan.chimingfazhou.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.baizhi.baseapp.util.IOCUtil;
import com.baizhi.baseapp.util.PreferencesUtil;
import com.baizhi.baseapp.widget.EmotionPageIndicator;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.util.IntentUtils;

import java.util.ArrayList;

/**
 * 进入引导界面
 * <p>
 * Created by Administrator on 2015/6/18.
 */
public class WelcomeActivity extends AppBaseActivity implements ViewPager.OnPageChangeListener {

    private ArrayList<ImageView> mImageViews;
    private EmotionPageIndicator mIndicator;
    private Button mButtonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_welcome_layout);
        initView();

    }

    protected void initView() {
        final Bitmap[] images = getResourceList();
        mImageViews = new ArrayList<>(images.length);
        for (Bitmap resId : images) {
            final ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(resId);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViews.add(imageView);
        }
        mButtonStart = (Button) findViewById(R.id.welcome_inter);
        mButtonStart.setVisibility(View.GONE);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.welcome_viewpager);
        mIndicator = (EmotionPageIndicator) findViewById(R.id.indicater);
        viewPager.setOnPageChangeListener(this);
        viewPager.setAdapter(new ViewPagerAdapter());
        mIndicator.setViewPager(viewPager);
        mIndicator.setIndicator(mImageViews.size(), 0);
        mIndicator.setDotCount(mImageViews.size());
        mIndicator.setDotDrawable(getResources().getDrawable(R.drawable.gd_page_indicator_dot));
    }

    @Override
    protected boolean isShowTitle() {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.setIndicator(mImageViews.size(), position);
        if (mImageViews.size() - 1 == position) {
            mButtonStart.setVisibility(View.VISIBLE);
            final Animation scaleAnimation = AnimationUtils.loadAnimation(this
                    , R.anim.welcome_bt_start);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mButtonStart.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mButtonStart.setAnimation(scaleAnimation);

            mButtonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferencesUtil.setBooleanByName(WelcomeActivity.this, "isShowWelcome", false);
                    IntentUtils.goToMainPage(WelcomeActivity.this);
                }
            });
        } else {
            mButtonStart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class ViewPagerAdapter extends PagerAdapter {


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
            view.addView(imageView);
            return imageView;
        }
    }

    private Bitmap[] getResourceList() {
        final Bitmap[] arrayList;
        try{
            final String[] data = GlobalDataHolder.getInstance(this).getCommonConfigBean().getWelcome_photo().split(",");
            arrayList= new Bitmap [data.length] ;
            final int size = data.length;
            for (int i = 0; i < size; i++) {
                arrayList[i] = IOCUtil.getImageFromAssetsFile(this, "guide/" + data[i]);
            }
        } catch (Exception e) {
            throw new RuntimeException("parse welcome pictures error");
        }
        return arrayList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mImageViews != null && !mImageViews.isEmpty()) {
            mImageViews.clear();
            mImageViews = null;
        }
        mButtonStart.clearAnimation();
    }
}