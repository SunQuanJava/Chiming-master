package com.sunquan.chimingfazhou.event;

/**
 *  音乐控制event
 *
 * Created by Administrator on 2015/6/2.
 */
public class MusicControlEvent {

    private int mFlag;

    public MusicControlEvent(int flag){
        this.mFlag = flag;
    }

    public int getFlag() {
        return mFlag;
    }
}
