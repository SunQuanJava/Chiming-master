package com.sunquan.chimingfazhou.event;

/**
 * 暂停、播放歌曲event
 *
 * Created by Administrator on 2015/6/2.
 */
public class PauseSongEvent {

    private boolean isPlaying;

    public PauseSongEvent(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

}
