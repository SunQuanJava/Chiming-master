package com.sunquan.chimingfazhou.event;

/**
 * 回退event
 *
 * Created by Administrator on 2015/5/7.
 */
public class BackEvent {
    private Class<?> mClazz;
    public BackEvent(Class<?> clazz) {
        this.mClazz = clazz;
    }

    public Class<?> getClazz() {
        return mClazz;
    }
}
