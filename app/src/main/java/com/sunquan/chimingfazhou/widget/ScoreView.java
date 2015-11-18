package com.sunquan.chimingfazhou.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sunquan.chimingfazhou.R;

/**
 * 评分view
 * <p/>
 * Created by Administrator on 2015/5/21.
 */
public class ScoreView extends ImageView {

    private Drawable mGrayStar;
    private Drawable mYellowStar;
    private int mDrawableWidth;
    private int mDrawableHeight;
    private RelativeLayout.LayoutParams mLayoutParams;
    private int mMaxScore = 5;
    private int mScore = 4;
    private int mOffset;

    public ScoreView(Context context) {
        super(context);
        init();
    }

    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScoreView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init();
    }

    private void init() {
        final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.star_gray);
        mGrayStar = new BitmapDrawable(getResources(),bitmap1);
        final Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.star_yellow);
        mYellowStar = new BitmapDrawable(getResources(),bitmap2);
        mDrawableWidth = mYellowStar.getIntrinsicWidth();
        mDrawableHeight = mYellowStar.getIntrinsicHeight();
        mOffset = getResources().getDimensionPixelSize(R.dimen.score_padding);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mMaxScore; i++) {
            int left = i == 0 ? 0 : (mDrawableWidth + mOffset) * i;
            int top = getMeasuredHeight() / 2 - mDrawableHeight / 2;
            if(i< mScore) {
                mYellowStar.setBounds(left, top, left + mDrawableWidth, top + mDrawableHeight);
                mYellowStar.draw(canvas);
            } else {
                mGrayStar.setBounds(left, top, left + mDrawableWidth, top + mDrawableHeight);
                mGrayStar.draw(canvas);
            }
        }
    }

    public void setMaxScore(int maxScore) {
        this.mMaxScore = maxScore;
    }

    public void setScore(int score) {
        this.mScore = score;
        if(mLayoutParams != null) {
            invalidate();
        }
    }
}
