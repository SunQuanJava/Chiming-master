package com.sunquan.chimingfazhou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/6/10.
 */
public class KeyboardLayout extends LinearLayout {

    private onSizeChangedListener mChangedListener;
    private static final String TAG ="KeyboardLayoutTAG";
    private boolean mShowKeyboard = false;

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "--------------------------------------------------------------");
        Log.d(TAG, "w----" + w + "\n" + "h-----" + h + "\n" + "oldW-----" + oldw + "\noldh----" + oldh);
        if (null != mChangedListener && 0 != oldw && 0 != oldh) {
            if (h < oldh) {
                mShowKeyboard = true;
            } else {
                mShowKeyboard = false;
            }
            mChangedListener.onChanged(mShowKeyboard);
            Log.d(TAG, "mShowKeyboard-----      " + mShowKeyboard);
        }
    }

    public void setOnSizeChangedListeners(onSizeChangedListener listener) {
        mChangedListener = listener;
    }

    public interface onSizeChangedListener{

        void onChanged(boolean showKeyboard);
    }

}
