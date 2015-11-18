package com.sunquan.chimingfazhou.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 文本时间的textview
 * <p/>
 * Created by Administrator on 2015/5/18.
 */
public class ClockTextView extends TextView {
    private static final int msgKey1 = 1;

    private boolean mIsStop;

    public ClockTextView(Context context) {
        super(context);
    }

    public ClockTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd hh:mm:ss", sysTime);
        setText(sysTimeStr);
        new TimeThread().start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsStop = true;
    }

    public class TimeThread extends Thread {
        @Override
        public void run() {
            while (!mIsStop) {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long sysTime = System.currentTimeMillis();
                    final CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd hh:mm:ss", sysTime);
                    setText(sysTimeStr);
                    break;

                default:
                    break;
            }
        }
    };
}
