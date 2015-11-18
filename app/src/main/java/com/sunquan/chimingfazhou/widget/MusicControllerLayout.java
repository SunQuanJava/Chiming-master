package com.sunquan.chimingfazhou.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baizhi.baseapp.util.CalendarUtils;
import com.sunquan.chimingfazhou.R;
import com.sunquan.chimingfazhou.application.MyApplication;
import com.sunquan.chimingfazhou.event.PauseSongEvent;
import com.sunquan.chimingfazhou.service.MusicPlayService;

import de.greenrobot.event.EventBus;

/**
 * 章节播放控制view
 *
 * Created by Administrator on 2015/5/28.
 */
public class MusicControllerLayout extends RelativeLayout implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{

    public static final long INTERVAL = 1000;
    private MusicPlayService mService;
    private SeekBar seekBar;
    private TextView currentTimeView;
    private TextView totalTimeView;
    private TextView pauseView;

    private Handler handler = new Handler();
    final Runnable updateThread = new Runnable() {

        public void run() {
            renderProgress();
            // 每次延迟100毫秒再启动线程
            handler.postDelayed(this, INTERVAL);
        }
    };

    public MusicControllerLayout(Context context) {
        super(context);
    }

    public MusicControllerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MusicControllerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final MyApplication application = (MyApplication) ((Activity)getContext()).getApplication();
        mService = application.getService();
        currentTimeView = (TextView) findViewById(R.id.tv_curcentTime);
        totalTimeView = (TextView) findViewById(R.id.tv_allTime);
        final TextView presongView = (TextView) findViewById(R.id.presong);
        final TextView nextsongView = (TextView) findViewById(R.id.nextsong);
        pauseView = (TextView) findViewById(R.id.playsong);
        pauseView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.play_playing_song_selector,0,0);
        changePlayButton(mService.isPlay());
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        presongView.setOnClickListener(this);
        nextsongView.setOnClickListener(this);
        pauseView.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        renderProgress();
        // 启动
        handler.postDelayed(updateThread,INTERVAL);
    }

    private void renderProgress() {
        seekBar.setMax(mService.getDuration());
        seekBar.setProgress(mService.getCurrent());
        final int second = (int) (seekBar.getMax()* (float) (mService.getSecondPercent()/100));
        seekBar.setSecondaryProgress(second);
        final String duration = CalendarUtils.formatTime(seekBar.getMax());
        final String current = CalendarUtils.formatTime(seekBar.getProgress());
        // 获得歌曲现在播放位置并设置成播放进度条的值
        currentTimeView.setText(current);
        totalTimeView.setText(duration);
    }

    public void changePlayButton(boolean isPlay) {
        if (isPlay) {
            pauseView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.play_pause_song_selector,0,0);
            pauseView.setText(getContext().getString(R.string.pause_message));
        } else {
            pauseView.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.play_playing_song_selector,0,0);
            pauseView.setText(getContext().getString(R.string.play_message));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.presong:
                mService.frontMusic();
                break;
            case R.id.nextsong:
                mService.nextMusic();
                break;
            case R.id.playsong:
                mService.pausePlay();
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mService.movePlay(progress);
            final String duration = CalendarUtils.formatTime(seekBar.getMax());
            final String current = CalendarUtils.formatTime(progress);
            // 获得歌曲现在播放位置并设置成播放进度条的值
            currentTimeView.setText(current);
            totalTimeView.setText(duration);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(updateThread);
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(PauseSongEvent event) {
        changePlayButton(event.isPlaying());
    }

}
