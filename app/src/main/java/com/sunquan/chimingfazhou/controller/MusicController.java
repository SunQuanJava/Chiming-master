package com.sunquan.chimingfazhou.controller;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IPositiveButtonDialogListener;
import com.baizhi.baseapp.util.NetworkState;
import com.baizhi.baseapp.widget.WToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.activity.AppBaseActivity;
import com.sunquan.chimingfazhou.activity.WenDetailActivity;
import com.sunquan.chimingfazhou.application.MyApplication;
import com.sunquan.chimingfazhou.download.module.DownloadModule;
import com.sunquan.chimingfazhou.event.ChangeSongEvent;
import com.sunquan.chimingfazhou.event.MusicControlEvent;
import com.sunquan.chimingfazhou.event.PauseSongEvent;
import com.sunquan.chimingfazhou.models.WenDetailInfo;
import com.sunquan.chimingfazhou.receiver.ControlBroadcast;
import com.sunquan.chimingfazhou.util.IntentUtils;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 后台播放音乐控制类
 */
public class MusicController implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    private static final int NOTIFICATION_ID = 1;

    private Activity mActivityContext;
    /* MediaPlayer对象 */
    private MediaPlayer mMediaPlayer = null;
    private int mCurrentTime = 0;//歌曲播放进度
    private int mCurrentListItem = -1;//当前播放第几首歌
    private List<DownloadModule> mSongs;//要播放的歌曲集合
    private boolean mIsError;
    private ControlBroadcast mControlBroadcast;
    private boolean isInit;
    private WToast mWToast;
    private int mSecondTime;
    private WenDetailInfo wenDetailInfo;

    private Service mServiceContext;
    private boolean isShowNotification;

    public MusicController(Service service) {
        mServiceContext = service;
    }

    public void init() {
        EventBus.getDefault().registerSticky(this);
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
        }
        if (mControlBroadcast == null) {
            mControlBroadcast = new ControlBroadcast(mServiceContext);
        }
        mWToast = new WToast(mServiceContext);
    }


    public int getSecondPercent() {
        return mSecondTime;
    }

    /**
     * 得到当前播放进度
     */
    public int getCurrent() {
        try {
            if (!isInit) {
                mCurrentTime = 0;
                return mCurrentTime;
            }
            if (mIsError) {
                mCurrentTime = 0;
                return mCurrentTime;
            }
            if (mMediaPlayer.isPlaying()) {
                return mMediaPlayer.getCurrentPosition();
            } else {
                return mCurrentTime;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 跳到输入的进度
     */
    public void movePlay(int progress) {
        if (mSongs == null || mSongs.isEmpty()) {
            return;
        }
        mMediaPlayer.seekTo(progress);
        mCurrentTime = progress;
    }

    public void playMusicWithNetCheck(final int index, final String path) {
        if (TextUtils.isEmpty(path)) {
            mWToast.showMessage(mServiceContext.getString(R.string.play_error_message));
        }
        //播放网络歌曲
        else if (path.startsWith("http")) {
            //网络异常
            if (!NetworkState.isActiveNetworkConnected(mServiceContext)) {
                mWToast.showMessage(mServiceContext.getString(R.string.no_network));
            }
            //非wifi环境
            else {
                if (!NetworkState.isWifiNetworkConnected(mServiceContext) && mActivityContext != null && mActivityContext instanceof AppBaseActivity) {
                    SimpleDialogFragment.createBuilder(mActivityContext, ((AppBaseActivity) mActivityContext).getSupportFragmentManager())
                            .setTitle(mActivityContext.getString(R.string.warm_tip))
                            .setMessage(mActivityContext.getString(R.string.play_tip_message))
                            .setPositiveButtonText(android.R.string.ok)
                            .setNegativeButtonText(android.R.string.cancel)
                            .setPositiveListener(new IPositiveButtonDialogListener() {
                                @Override
                                public void onPositiveButtonClicked(int requestCode) {
                                    mCurrentListItem = index;
                                    playMusic(path);
                                }
                            })
                            .setTag("play_prompt")
                            .showSinglton();
                } else {
                    mCurrentListItem = index;
                    playMusic(path);
                }
            }
        } else {
            mCurrentListItem = index;
            playMusic(path);
        }
    }

    public boolean isShowNotification() {
        return isShowNotification;
    }

    /**
     * 根据歌曲存储路径播放歌曲
     */
    private boolean playMusic(String path) {
        try {
            if (mSongs == null || mSongs.isEmpty()) {
                return false;
            }
            isInit = false;
            mIsError = false;
            mSecondTime = 0;
            /* 重置MediaPlayer */
            mMediaPlayer.reset();
            /* 设置要播放的文件的路径 */

            mMediaPlayer.setDataSource(path);
            EventBus.getDefault().post(new PauseSongEvent(isInit));
            EventBus.getDefault().post(new ChangeSongEvent(mCurrentListItem, mSongs.get(mCurrentListItem)));
            updateNotification();
            /* 准备播放 */
            mMediaPlayer.prepareAsync();
        } catch (IOException ignored) {
            mIsError = true;
            mWToast.showMessage(mServiceContext.getString(R.string.play_error_message));
        }
        return !mIsError;
    }

    /* 下一首 */
    public void nextMusic() {
        if (mSongs == null || mSongs.isEmpty()) {
            return;
        }
        int tempIndex = mCurrentListItem;
        if (++tempIndex >= mSongs.size()) {
            tempIndex = 0;
        }
        playMusicWithNetCheck(tempIndex, mSongs.get(tempIndex).getPlayPath());
    }

    /* 上一首 */
    public void frontMusic() {
        if (mSongs == null || mSongs.isEmpty()) {
            return;
        }
        int tempIndex = mCurrentListItem;
        if (--tempIndex < 0) {
            tempIndex = mSongs.size() - 1;
        }
        playMusicWithNetCheck(tempIndex, mSongs.get(tempIndex).getPlayPath());
    }

    /**
     * 歌曲是否真在播放
     */
    public boolean isPlay() {
        try {
            return mMediaPlayer.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 暂停或开始播放歌曲
     */
    public void pausePlay() {
        try {
            if (mSongs == null || mSongs.isEmpty()) {
                return;
            }
            if (!isInit) {
                return;
            }
            boolean isPlaying = mMediaPlayer.isPlaying();
            if (isPlaying) {
                mCurrentTime = mMediaPlayer.getCurrentPosition();
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
            EventBus.getDefault().post(new PauseSongEvent(!isPlaying));
            updateNotification();
        } catch (Exception ignored) {
        }
    }

    public String getSongName() {
        return mSongs.get(mCurrentListItem).getTitle();
    }

    public void setActivityContext(Activity context) {
        this.mActivityContext = context;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mMediaPlayer) {
        this.mMediaPlayer = mMediaPlayer;
    }

    public int getCurrentListItem() {
        return mCurrentListItem;
    }

    public void setCurrentListItem(int mCurrentListItem) {
        this.mCurrentListItem = mCurrentListItem;
    }

    public int getDuration() {
        if (!isInit) {
            return 0;
        }
        int duration = mMediaPlayer.getDuration();
        return (mSongs == null || mSongs.isEmpty()) ? 0 : mIsError ? 0 : duration;
    }

    public List<DownloadModule> getSongs() {
        return mSongs;
    }

    public DownloadModule getCurrentSong() {
        if (mSongs != null && !mSongs.isEmpty()) {
            return mSongs.get(mCurrentListItem);
        }
        return null;
    }

    public void setSongs(List<DownloadModule> mSongs) {
        this.mSongs = mSongs;
    }


    public void release() {
        try {
            stopMusic();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mServiceContext.unregisterReceiver(mControlBroadcast);
            EventBus.getDefault().unregister(this);
            isShowNotification = false;
        } catch (Exception ignored){}

    }

    /**
     * 更新notification
     */
    public void updateNotification() {
        if (mSongs == null || mSongs.isEmpty() || mSongs.size() <= mCurrentListItem) {
            return;
        }
        isShowNotification = true;
        final String title = mSongs.get(mCurrentListItem).getAlbumName();
        final String name = mSongs.get(mCurrentListItem).getTitle();
        //创建remoteView
        final int remoteResLayout;
        if (Build.VERSION.SDK_INT < 16) {
            remoteResLayout = R.layout.notification;
        } else {
            remoteResLayout = R.layout.notification_new;
        }
        final RemoteViews rv = new RemoteViews(mServiceContext.getPackageName(), remoteResLayout);
        //渲染标题，设置图片
        rv.setTextViewText(R.id.title, title + "-" + name);
        if (isPlay()) {
            rv.setImageViewResource(R.id.iv_pause, R.drawable.notification_pause);
        } else {
            rv.setImageViewResource(R.id.iv_pause, R.drawable.notification_play);
        }
        /*此处action不能是一样的 如果一样的 接受的flag参数只是第一个设置的值*/
        //暂停播放点击事件
        final Intent pauseIntent = new Intent(ControlBroadcast.PAUSE_BROADCAST_NAME);
        pauseIntent.putExtra("FLAG", ControlBroadcast.PAUSE_FLAG);
        final PendingIntent pausePIntent = PendingIntent.getBroadcast(mServiceContext, 0, pauseIntent, 0);
        rv.setOnClickPendingIntent(R.id.iv_pause, pausePIntent);
        //下一曲点击事件
        final Intent nextIntent = new Intent(ControlBroadcast.NEXT_BROADCAST_NAME);
        nextIntent.putExtra("FLAG", ControlBroadcast.NEXT_FLAG);
        final PendingIntent nextPIntent = PendingIntent.getBroadcast(mServiceContext, 0, nextIntent, 0);
        rv.setOnClickPendingIntent(R.id.iv_next, nextPIntent);
        //上一曲点击事件
        final Intent preIntent = new Intent(ControlBroadcast.PRE_BROADCAST_NAME);
        preIntent.putExtra("FLAG", ControlBroadcast.PRE_FLAG);
        final PendingIntent prePIntent = PendingIntent.getBroadcast(mServiceContext, 0, preIntent, 0);
        rv.setOnClickPendingIntent(R.id.iv_previous, prePIntent);
        //关闭点击事件
        final Intent closeIntent = new Intent(ControlBroadcast.CLOSE_BROADCAST_NAME);
        closeIntent.putExtra("FLAG", ControlBroadcast.CLOSE_FLAG);
        final PendingIntent closePIntent = PendingIntent.getBroadcast(mServiceContext, 0, closeIntent, 0);
        rv.setOnClickPendingIntent(R.id.close, closePIntent);
        //通知栏跳转点击事件
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setClass(mServiceContext, WenDetailActivity.class);
        intent.putExtra(IntentUtils.EXTRA_WEN_NOTIFICATION_DETAIL, wenDetailInfo);
        final PendingIntent contextIntent = PendingIntent.getActivity(mServiceContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.layout, contextIntent);
        //创建通知
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(mServiceContext).setContent(rv).setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mServiceContext.getResources(), R.drawable.ic_launcher)).setOngoing(true)
                .setTicker(title + "-" + name).setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= 16) {
            builder.setPriority(Notification.PRIORITY_MAX);
        }
        final Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = rv;
        }
        ImageLoader.getInstance().loadImage(mSongs.get(mCurrentListItem).getAlbumUrl(),new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                rv.setImageViewBitmap(R.id.image, loadedImage);
                if (isPlay()) {
                    rv.setImageViewResource(R.id.iv_pause, R.drawable.notification_pause);
                } else {
                    rv.setImageViewResource(R.id.iv_pause, R.drawable.notification_play);
                }
                final NotificationManager notificationManager = (NotificationManager) mServiceContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID,notification);
            }
        });
        //开启通知栏进程
        mServiceContext.startForeground(NOTIFICATION_ID, notification);
    }

    public void stopMusic() {
        try{
            mServiceContext.stopForeground(true);
            mMediaPlayer.stop();
            if(mSongs!=null) {
                mSongs.clear();
                mSongs = null;
            }
            isInit = false;
            wenDetailInfo = null;
            mActivityContext = null;
            isShowNotification = false;
        } catch (Exception ignored) {}
    }

    @SuppressWarnings("unused")
    public void onEvent(MusicControlEvent event) {
        switch (event.getFlag()) {
            case ControlBroadcast.PAUSE_FLAG:
                pausePlay();
                break;
            case ControlBroadcast.NEXT_FLAG:
                nextMusic();
                break;
            case ControlBroadcast.PRE_FLAG:
                frontMusic();
                break;
            case ControlBroadcast.CLOSE_FLAG:
                closeApp();
                break;
        }
    }

    private void closeApp() {
        isShowNotification = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                GlobalDataHolder.getInstance(mServiceContext).saveXiuDatasToDB();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ((MyApplication)mServiceContext.getApplication()).finishAllActivity();
                        mServiceContext.stopSelf();
                        Process.killProcess(Process.myPid());
                    }
                });
            }
        }).start();
    }

    public void setWenDetailInfo(WenDetailInfo wenDetailInfo) {
        this.wenDetailInfo = wenDetailInfo;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mSecondTime = percent;
        Log.i("mSecondTime:", "mSecondTime: " + mSecondTime);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // 播放完成一首之后进行下一首
        nextMusic();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        isInit = true;
        EventBus.getDefault().post(new PauseSongEvent(true));
        updateNotification();
    }
}
