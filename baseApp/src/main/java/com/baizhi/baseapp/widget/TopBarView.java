package com.baizhi.baseapp.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baizhi.baseapp.R;


/**
 * 顶部view
 * 
 * @author SunQuan
 * 
 */
public class TopBarView extends RelativeLayout {

    private ImageView mImage_left;
    private TextView mText_center;
    private ImageView mImage_right;
    private TextView mText_right;
    private TextView mText_left;
    private ImageView mImage_center;

    public TopBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TopBarView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.top_bar, this);
        mImage_center = (ImageView) view.findViewById(R.id.center_icon);
        mImage_left = (ImageView) view.findViewById(R.id.left_icon);
        mImage_right = (ImageView) view.findViewById(R.id.right_icon);
        mText_center = (TextView) view.findViewById(R.id.center_text);
        mText_right = (TextView) view.findViewById(R.id.right_text);
        mText_left = (TextView) view.findViewById(R.id.left_text);
        mImage_left.setVisibility(View.INVISIBLE);
        mImage_right.setVisibility(View.INVISIBLE);
        mImage_center.setVisibility(View.INVISIBLE);
        mText_center.setVisibility(View.INVISIBLE);
        mText_right.setVisibility(View.INVISIBLE);
        mText_left.setVisibility(View.INVISIBLE);
        setLeftEnable(true);
        setRightEnable(true);
    }

    public void setLeftTextContent(String textContent, int drawableLeft) {
        if(TextUtils.isEmpty(textContent)) {
            textContent = getContext().getString(R.string.holder_text);
            mText_left.setVisibility(INVISIBLE);
            mText_left.setText(textContent);
            mText_left.setClickable(false);
            return;
        }
        mText_left.setText(textContent);
        if (drawableLeft != 0) {
            mText_left.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, 0, 0);
        }
        if (mImage_left.getVisibility() == View.VISIBLE)
            mImage_left.setVisibility(View.INVISIBLE);
        if (mText_left.getVisibility() != View.VISIBLE)
            mText_left.setVisibility(View.VISIBLE);
    }


    public void setLeftImageContent(int resId) {
        mImage_left.setImageResource(resId);
        if (mImage_left.getVisibility() != View.VISIBLE)
            mImage_left.setVisibility(View.VISIBLE);
        if (mText_left.getVisibility() == View.VISIBLE)
            mText_left.setVisibility(View.INVISIBLE);
    }

    public void setRightTextContent(String textContent, int drawableRight) {
        if(TextUtils.isEmpty(textContent)) {
            textContent = getContext().getString(R.string.holder_text);
            mText_right.setVisibility(INVISIBLE);
            mText_right.setText(textContent);
            mText_right.setClickable(false);
            return;
        }
        mText_right.setText(textContent);
        if (drawableRight != 0) {
            mText_right.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRight, 0);
        }
        if (mImage_right.getVisibility() == View.VISIBLE)
            mImage_right.setVisibility(View.INVISIBLE);
        if (mText_right.getVisibility() != View.VISIBLE)
            mText_right.setVisibility(View.VISIBLE);
    }


    public void setRightImageContent(int resId) {
        mImage_right.setImageResource(resId);
        if (mText_right.getVisibility() == View.VISIBLE)
            mText_right.setVisibility(View.INVISIBLE);
        if (mImage_right.getVisibility() != View.VISIBLE)
            mImage_right.setVisibility(View.VISIBLE);
    }


    public void setCenterTextContent(String textContent) {
        mText_center.setText(textContent);
        if (mText_center.getVisibility() != View.VISIBLE)
            mText_center.setVisibility(View.VISIBLE);
        if (mImage_center.getVisibility() == View.VISIBLE) {
            mImage_center.setVisibility(View.INVISIBLE);
        }
    }

    public void setLeftEnable(boolean isEnable) {
        if(isEnable) {
            mImage_left.setClickable(true);
            mText_left.setClickable(true);
            mText_left.setTextColor(getResources().getColorStateList(R.color.top_bar_text_color));
        } else {
            mImage_left.setClickable(false);
            mText_left.setClickable(false);
            mText_left.setTextColor(0x4cffffff);
        }
    }

    public void setRightEnable(boolean isEnable) {
        if(isEnable) {
            mImage_right.setClickable(true);
            mText_right.setClickable(true);
            mText_right.setTextColor(getResources().getColorStateList(R.color.top_bar_text_color));
        } else {
            mImage_right.setClickable(false);
            mText_right.setClickable(false);
            mText_right.setTextColor(0x4cffffff);
        }
    }

    public void setCenterImageContent(int resId) {
        mImage_center.setImageResource(resId);
        if (mImage_center.getVisibility() != View.VISIBLE)
            mImage_center.setVisibility(View.VISIBLE);
        if (mText_center.getVisibility() == View.VISIBLE)
            mText_center.setVisibility(View.INVISIBLE);
    }


    public void setLeftOnClickListener(OnClickListener onClickListener) {
            mImage_left.setOnClickListener(onClickListener);
            mText_left.setOnClickListener(onClickListener);
    }

    public void setRightOnClickListener(OnClickListener onClickListener) {
           mImage_right.setOnClickListener(onClickListener);
           mText_right.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

}
