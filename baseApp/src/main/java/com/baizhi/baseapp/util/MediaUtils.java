package com.baizhi.baseapp.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * 播放音频的工具类
 * <p/>
 * Created by Administrator on 2015/5/19.
 */
public final class MediaUtils {

    private static MediaPlayer gMediaPlayer = null;
    private static final Uri XIU_COUNTER_URI = Uri.parse("android.resource://com.sunquan.chimingfazhou/raw/xiu");

    public static void playXiuAudio(Context context) {
        playAudio(context, XIU_COUNTER_URI);
    }

    public static void playAudio(Context context, Uri uri) {
        playAudio(context, uri, null);
    }

    private static void playAudio(Context context, Uri uri, MediaPlayer.OnCompletionListener onCompletionListener) {

        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        if (current == 0) {// silence mode
            return;
        }
        if (gMediaPlayer == null) {
            gMediaPlayer = new MediaPlayer();
        }
        if (gMediaPlayer.isLooping() || gMediaPlayer.isPlaying()) {
            gMediaPlayer.stop();
        }
        try {
            gMediaPlayer.reset();
            gMediaPlayer.setDataSource(context, uri);
            gMediaPlayer.setOnCompletionListener(onCompletionListener);
            gMediaPlayer.prepare();
            gMediaPlayer.start();
        } catch (Exception e) {
            gMediaPlayer.release();
            gMediaPlayer = null;
            gMediaPlayer = new MediaPlayer();
        }
    }
}
