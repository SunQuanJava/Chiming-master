package com.baizhi.baseapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import java.lang.ref.SoftReference;

/**
 * 微博广播接收器
 *
 * @author sunquan1
 * @date 2014-11-12
 * @since 4.7.0
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver {

    public BaseBroadcastReceiver(Context context) {
        final SoftReference<Context> mReference = new SoftReference<>(context);
        mReference.get().registerReceiver(this,
                getIntentFilter());
	}

	/**
	 * 获取该事件监听器关心的事件
	 * 
	 * @return
	 */
	public abstract IntentFilter getIntentFilter();

}
