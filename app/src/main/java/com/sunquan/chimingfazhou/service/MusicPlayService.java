package com.sunquan.chimingfazhou.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.sunquan.chimingfazhou.controller.MusicController;
import com.sunquan.chimingfazhou.download.module.DownloadModule;
import com.sunquan.chimingfazhou.models.WenDetailInfo;

import java.util.List;

/**
 * 后台播放音乐服务
 */
public class MusicPlayService extends Service {

    private final IBinder mBinder = new LocalBinder();

    private MusicController mMusicController;
    private TelephonyManager mTelephonyManager;
    private PhoneStateListener mPhoneStateListener;

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicController = new MusicController(this);
        mMusicController.init();
        mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new MobilePhoneStateListener();
        mTelephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
    }

    public int getSecondPercent() {
        return mMusicController.getSecondPercent();
    }

    /**
     * 得到当前播放进度
     */
    public int getCurrent() {
        return mMusicController.getCurrent();
    }

    /**
     * 跳到输入的进度
     */
    public void movePlay(int progress) {
        mMusicController.movePlay(progress);
    }

    public void playMusicWithNetCheck(final int index, final String path) {
        mMusicController.playMusicWithNetCheck(index, path);
    }

    /* 下一首 */
    public void nextMusic() {
        mMusicController.nextMusic();
    }

    /* 上一首 */
    public void frontMusic() {
        mMusicController.frontMusic();
    }

    /**
     * 歌曲是否真在播放
     */
    public boolean isPlay() {
        return mMusicController.isPlay();
    }

    /**
     * 暂停或开始播放歌曲
     */
    public void pausePlay() {
        mMusicController.pausePlay();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public String getSongName() {
        return mMusicController.getSongName();
    }

    /**
     * 自定义绑定Service类，通过这里的getService得到Service，之后就可调用Service这里的方法了
     */
    public class LocalBinder extends Binder {
        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

    public void setActivityContext(Activity activity) {
        mMusicController.setActivityContext(activity);
    }

    public int getCurrentListItem() {
        return mMusicController.getCurrentListItem();
    }

    public void setCurrentListItem(int mCurrentListItem) {
        mMusicController.setCurrentListItem(mCurrentListItem);
    }

    public int getDuration() {
        return mMusicController.getDuration();
    }

    public List<DownloadModule> getSongs() {
        return mMusicController.getSongs();
    }

    public DownloadModule getCurrentSong() {
        return mMusicController.getCurrentSong();
    }

    public void setSongs(List<DownloadModule> mSongs) {
        mMusicController.setSongs(mSongs);
    }

    // 在2.0以后的版本如果重写了onStartCommand，那onStart将不会被调用，注：在2.0以前是没有onStartCommand方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 如果服务进程在它启动后(从onStartCommand()返回后)被kill掉, 那么让他呆在启动状态但不取传给它的intent.
        // 随后系统会重写创建service，因为在启动时，会在创建新的service时保证运行onStartCommand
        // 如果没有任何开始指令发送给service，那将得到null的intent，因此必须检查它.
        // 该方式可用在开始和在运行中任意时刻停止的情况，例如一个service执行音乐后台的重放
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTelephonyManager.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        mMusicController.release();
    }

    public void setWenDetailInfo(WenDetailInfo wenDetailInfo) {
        mMusicController.setWenDetailInfo(wenDetailInfo);
    }

    public void stopMusic() {
        mMusicController.stopMusic();
    }

    public boolean isShowNotification() {
        return mMusicController.isShowNotification();
    }

    /**
     * 电话监听类
     */
    final class MobilePhoneStateListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE://通话结束
                    break;

                case TelephonyManager.CALL_STATE_RINGING://来电
                    if(mMusicController.isPlay()){
                        mMusicController.pausePlay();
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
