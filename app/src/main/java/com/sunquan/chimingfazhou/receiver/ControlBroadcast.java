package com.sunquan.chimingfazhou.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.baizhi.baseapp.receiver.BaseBroadcastReceiver;
import com.sunquan.chimingfazhou.event.MusicControlEvent;

import de.greenrobot.event.EventBus;

/**
 * 音乐notification控制广播
 */
public class ControlBroadcast extends BaseBroadcastReceiver {

    public static final String PAUSE_BROADCAST_NAME = "com.chimingfazhou.music.pause.broadcast";
    public static final String NEXT_BROADCAST_NAME = "com.chimingfazhou.music.next.broadcast";
    public static final String PRE_BROADCAST_NAME = "com.chimingfazhou.music.pre.broadcast";
    public static final String CLOSE_BROADCAST_NAME = "com.chimingfazhou.music.close.broadcast";
    public static final int PAUSE_FLAG = 1;
    public static final int NEXT_FLAG = 2;
    public static final int PRE_FLAG = 3;
    public static final int CLOSE_FLAG = 4;

    public ControlBroadcast(Context context) {
        super(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int flag = intent.getIntExtra("FLAG", -1);
        if (flag != -1) {
            EventBus.getDefault().post(new MusicControlEvent(flag));
        }
    }

    @Override
    public IntentFilter getIntentFilter() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(PAUSE_BROADCAST_NAME);
        filter.addAction(NEXT_BROADCAST_NAME);
        filter.addAction(PRE_BROADCAST_NAME);
        filter.addAction(CLOSE_BROADCAST_NAME);
        return filter;
    }
}