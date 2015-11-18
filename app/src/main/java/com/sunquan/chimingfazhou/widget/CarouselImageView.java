package com.sunquan.chimingfazhou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.baizhi.baseapp.widget.RecyclingImageView;

/**
 * 轮播背景图片
 * <p/>
 * Created by Administrator on 2015/5/18.
 */
public class CarouselImageView extends RecyclingImageView {

    private static final long DEFAULT_INTERVAL = 1000 * 5;
    private static final int DURATION = 1000;
    private long mInterval = DEFAULT_INTERVAL;
    private boolean mIsPlaying;
    private int mCurrentCount;
    private Paint mPaint;
    private SparseArray<Drawable> mDrawables = new SparseArray<>();
    private int[] mResIds;
    private Runnable mPollingRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mIsPlaying) {
                return;
            }
            int resId1 = mResIds[mCurrentCount % mResIds.length];
            int resId2 = mResIds[(mCurrentCount + 1) % mResIds.length];
            final Drawable drawable1 = getDrawableByResId(resId1);
            final Drawable drawable2 = getDrawableByResId(resId2);
            final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{
                    drawable1,//实现从0 1 2 3 4 5 0 1 2.。。这样的不停转变
                    drawable2});
            mCurrentCount++;
            setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(DURATION);
            postDelayed(this, mInterval);
        }
    };

    public CarouselImageView(Context context) {
        super(context);
    }

    public CarouselImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarouselImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setColor(0x7f000000);
        }
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }

    private Drawable getDrawableByResId(int resId) {
        Drawable drawable = mDrawables.get(resId);
        if (drawable == null) {
            drawable = getResources().getDrawable(resId);
            mDrawables.put(resId, drawable);
        }
        return drawable;
    }

    /**
     * 设置背景资源图片
     *
     * @param resIds
     * @return
     */
    public CarouselImageView setBackgroundResIds(final int[] resIds) {
        this.mResIds = resIds;
        setImageDrawable(getDrawableByResId(resIds[0]));
        return this;
    }

    /**
     * 设置间隔时间
     *
     * @param interval
     * @return
     */
    public CarouselImageView setInterval(long interval) {
        this.mInterval = interval;
        return this;
    }

    /**
     * 开始轮播背景图片
     */
    public void play() {
        if (mResIds == null || mResIds.length == 0) {
            throw new IllegalArgumentException();
        }
        if (mIsPlaying) {
            return;
        }
        mIsPlaying = true;
        postDelayed(mPollingRunnable, mInterval);
    }

    /**
     * 停止轮播
     */
    public void stop() {
        mIsPlaying = false;
        removeCallbacks(mPollingRunnable);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
        if (mDrawables != null) {
            mDrawables.clear();
            mDrawables = null;
        }
    }


}
