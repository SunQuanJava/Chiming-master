package com.sunquan.chimingfazhou.event;

/**
 * 遮盖层
 *
 * Created by Administrator on 2015/5/7.
 */
public class CoverDownloadEvent {
    public boolean isFirst = false;

    public CoverDownloadEvent(boolean isFirst){
        this.isFirst = isFirst;
    }
}
