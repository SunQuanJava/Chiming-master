package com.sunquan.chimingfazhou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

import com.sunquan.chimingfazhou.R;


/**
 * 下载列表文字TextView
 * <p/>
 * Created by sunquan1 on 2015/5/27.
 */
public class CheckedText extends TextView implements Checkable {

    private boolean isChecked;

    public CheckedText(Context context) {
        super(context);
    }

    public CheckedText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckedText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            this.setTextColor(0xffFF6600);
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_play, 0, 0, 0);
        } else {
            this.setTextColor(0xff313131);
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_play_disable, 0, 0, 0);
        }
        this.isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        if (isChecked) {
            setChecked(false);
        } else {
            setChecked(true);
        }
    }

}
