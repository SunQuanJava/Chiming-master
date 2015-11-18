package com.sunquan.chimingfazhou.event;

import com.sunquan.chimingfazhou.download.module.DownloadModule;

/**
 * 切换歌曲
 *
 * Created by Administrator on 2015/5/29.
 */
public class ChangeSongEvent {

    private int mCurrentPosition;
    private DownloadModule downloadModule;

    public ChangeSongEvent(int currentPosition,DownloadModule downloadModule) {
        this.mCurrentPosition = currentPosition;
        this.downloadModule = downloadModule;
    }



    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public DownloadModule getDownloadModule() {
        return downloadModule;
    }
}
