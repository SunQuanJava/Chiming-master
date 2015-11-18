package com.baizhi.baseapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.baizhi.baseapp.R;


/**
 * 自定义表情面板下方的pageIndicator
 *
 * @intent instead canvas for drawable
 */
public class EmotionPageIndicator extends View implements PageIndicator {

    public static final int NO_ACTIVE_DOT = -1;

    public interface DotType {
        /**
         * Represents the single dot type. Only one selected dot may be drawn.
         */
        int SINGLE = 0;
        /**
         * Represents the multiple dot type. Several selected dot may be drawn.
         */
        int MULTIPLE = 1;
    }

    private static final int MIN_DOT_COUNT =0;

    private static Rect sInRect = new Rect();
    private static Rect sOutRect = new Rect();

    private int mGravity;
    private int mDotSpacing;
    private Drawable mDotDrawable;
    private int mDotCount;
    private int mDotType;

    private int mActiveDot;

    private int[] mExtraState;

    private boolean mInitializing;
    private ViewPager mViewPager;



    public EmotionPageIndicator(Context context) {
        this(context, null);
    }

    public EmotionPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.gdPageIndicatorStyle);
    }

    public EmotionPageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPageIndicator();

        mInitializing = true;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmotionPageIndicator, defStyle, 0);

        setDotCount(a.getInt(R.styleable.EmotionPageIndicator_dotCount, mDotCount));
        setActiveDot(a.getInt(R.styleable.EmotionPageIndicator_activeDot, mActiveDot));
        setDotDrawable(a.getDrawable(R.styleable.EmotionPageIndicator_dotDrawable));
        setDotSpacing(a.getDimensionPixelSize(R.styleable.EmotionPageIndicator_dotSpacing, mDotSpacing));
        setGravity(a.getInt(R.styleable.EmotionPageIndicator_gravity, mGravity));
        setDotType(a.getInt(R.styleable.EmotionPageIndicator_dotType, mDotType));

        a.recycle();

        mInitializing = false;
    }

    private void initPageIndicator() {
        mDotCount = MIN_DOT_COUNT;
        mGravity = Gravity.CENTER;
        mActiveDot = 0;
        mDotSpacing = 20;
        mDotType = DotType.SINGLE;
        
        mExtraState = onCreateDrawableState(1);
        mergeDrawableStates(mExtraState, SELECTED_STATE_SET);
    }

    public void setDotCount(int dotCount) {
        if (dotCount < MIN_DOT_COUNT) {
            dotCount = MIN_DOT_COUNT;
        }

        if (mDotCount != dotCount) {
            mDotCount = dotCount;
            requestLayout();
            invalidate();
        }
    }


    public int getActiveDot() {
        return mActiveDot;
    }


    public void setActiveDot(int activeDot) {
        if (activeDot < 0) {
            activeDot = NO_ACTIVE_DOT;
        }

        switch (mDotType) {
            case DotType.SINGLE:
                if (activeDot > mDotCount - 1) {
                    activeDot = NO_ACTIVE_DOT;
                }
                break;

            case DotType.MULTIPLE:
                if (activeDot > mDotCount) {
                    activeDot = NO_ACTIVE_DOT;
                }
        }

        mActiveDot = activeDot;
        invalidate();
    }

    public Drawable getDotDrawable() {
        return mDotDrawable;
    }

  
    public void setDotDrawable(Drawable dotDrawable) {
        if (dotDrawable != mDotDrawable) {
            mDotDrawable = dotDrawable;
            if (dotDrawable != null) {

                if (dotDrawable.getIntrinsicHeight() == -1 || dotDrawable.getIntrinsicWidth() == -1) {
               
                    return;
                }

                dotDrawable.setBounds(0, 0, dotDrawable.getIntrinsicWidth(), dotDrawable.getIntrinsicHeight());
                dotDrawable.setCallback(this);
                if (dotDrawable.isStateful()) {
                    dotDrawable.setState(getDrawableState());
                }
            }

            requestLayout();
            invalidate();
        }
    }

 
    public int getDotSpacing() {
        return mDotSpacing;
    }


    public void setDotSpacing(int dotSpacing) {
        if (dotSpacing != mDotSpacing) {
            mDotSpacing = dotSpacing;
            requestLayout();
            invalidate();
        }
    }

  
    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            invalidate();
        }
    }
  
    public void setDotType(int dotType) {
        if (dotType == DotType.SINGLE || dotType == DotType.MULTIPLE) {
            if (mDotType != dotType) {
                mDotType = dotType;
                invalidate();
            }
        }
    }

    @Override
    public void requestLayout() {
        if (!mInitializing) {
            super.requestLayout();
        }
    }

    @Override
    public void invalidate() {
        if (!mInitializing) {
            super.invalidate();
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mDotDrawable;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mExtraState = onCreateDrawableState(1);
        mergeDrawableStates(mExtraState, SELECTED_STATE_SET);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Drawable d = mDotDrawable;

        int width = 0;
        int height = 0;
        if (d != null) {
            width = mDotCount * (d.getIntrinsicWidth() + mDotSpacing) - mDotSpacing;
            height = d.getIntrinsicHeight();
        }

        width += getPaddingRight() + getPaddingLeft();
        height += getPaddingBottom() + getPaddingTop();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {

    	//setDotCount(mViewPager.getAdapter().getCount());
    	setDotCount(mtotalDot);
    	getDotDrawable();
    	//setActiveDot(mViewPager.getCurrentItem());
    	setActiveDot(mcurrentDot);
        final Drawable d = mDotDrawable;
        if (d != null) {

            final int count = mDotType == DotType.SINGLE ? mDotCount : mActiveDot;

            if (count <= 0) {
                return;
            }

            final int h = d.getIntrinsicHeight();
            final int w = Math.max(0, count * (d.getIntrinsicWidth() + mDotSpacing) - mDotSpacing);

            final int pRight = getPaddingRight();
            final int pLeft = getPaddingLeft();
            final int pTop = getPaddingTop();
            final int pBottom = getPaddingBottom();

            sInRect.set(pLeft, pTop, getWidth() - pRight, getHeight() - pBottom);
            Gravity.apply(mGravity, w, h, sInRect, sOutRect);

            canvas.save();
            canvas.translate(sOutRect.left, sOutRect.top);
            for (int i = 0; i < count; i++) {
                if (d.isStateful()) {
                    int[] state = getDrawableState();
                    if (mDotType == DotType.MULTIPLE || i == mActiveDot) {
                        state = mExtraState;
                    }
                    d.setState(state);
                }
                d.draw(canvas);
                canvas.translate(mDotSpacing + d.getIntrinsicWidth(), 0);
            }
            canvas.restore();
        }
    }

    private static class SavedState extends BaseSavedState {

        private int activeDot;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            activeDot = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(activeDot);
        }
        
        @SuppressWarnings("unused")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.activeDot = mActiveDot;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            try {
                super.onRestoreInstanceState(state);
            } catch (Exception e) {}
            return;
        }
        
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mActiveDot = ss.activeDot;
        requestLayout();
    }

	@Override
	public void onPageScrollStateChanged(int arg0) {

		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	
		//setActiveDot(mActiveDot);
	}

	@Override
	public void onPageSelected(int arg0) {
	
	
	}

	@Override
	public void setViewPager(ViewPager view) {
	    if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have mAdapter instance.");
        }
        mViewPager = view;
        //mViewPager.setOnPageChangeListener(this);
        invalidate();
		
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition) {
	    setViewPager(view);
        setCurrentItem(initialPosition);
		
	}

	@Override
	public void setCurrentItem(int item) {
		   if (mViewPager == null) {
	            throw new IllegalStateException("ViewPager has not been bound.");
	        }
	        mViewPager.setCurrentItem(item);
	        mActiveDot = item;
	        invalidate();
		
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		
	}

	public void setIndicator(int totalDot, int currentPageIndex) {
		this.mtotalDot = totalDot;
		if(currentPageIndex>-1){
			mcurrentDot=currentPageIndex;
		} 
	}
    private int mtotalDot=-1;
    private int mcurrentDot=-1;
}
