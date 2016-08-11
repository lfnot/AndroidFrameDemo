package com.example.wangjun.mytestdemo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.widget.VideoPlayController;
import com.example.wangjun.mytestdemo.widget.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * 当前类注释：视频播放
 * Author :LeonWang
 * Created  2016/8/11.16:29
 * Description:
 * E-mail:lijiawangjun@gmail.com
 */

public class VideoActivity extends AppCompatActivity implements View.OnTouchListener{

    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.video_progress)
    ProgressBar mVideoProgress;
    @BindView(R.id.rl_bg)
    RelativeLayout mRlBg;
    private float mDownX;
    private float mDownY;
    String url, title;
    private VideoPlayController mPlayController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        ViewUtil.setFullScreen(this);
        mRlBg.setOnTouchListener(this);
        Intent i = getIntent();
        url = i.getStringExtra("Url");
        title = i.getStringExtra("Title");
        playVideo(url);
    }

    public void playVideo(String path) {
        if (Vitamio.isInitialized(getApplicationContext())) {

            mVideoView.setVideoPath(path);

            mPlayController = new VideoPlayController(this, mVideoView, mRlBg,title);

            mVideoView.requestFocus();
            mVideoView.setOnTouchListener(this);

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setPlaybackSpeed(1.0f);
//                    Presente.getVideoSuccess();

                }
            });
            mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mVideoProgress.setVisibility(View.INVISIBLE);

                   // Presente.getVideoFailer("视频播放出错了╮(╯Д╰)╭");
                    return true;
                }
            });
        } else {
            //Presente.getVideoFailer("播放器还没初始化完哎，等等咯╮(╯Д╰)╭ ");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDownX - event.getX()) > 50 || Math.abs(mDownY - event.getY()) > 50) {
                    // 移动超过一定距离，ACTION_UP取消这次事件
                    mDownX = Integer.MAX_VALUE;
                    mDownY = Integer.MAX_VALUE;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mPlayController != null && Math.abs(mDownX - event.getX()) <= 50 && Math
                        .abs(mDownY - event.getY()) <= 50) {
                    // 解决与背景点击事件的冲突
                    if (mPlayController.isShowing()) {
                        mPlayController.hide();
                    } else {
                        mPlayController.show();
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        mVideoView.setVisibility(View.INVISIBLE);
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 保持屏幕比例正确
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        mPlayController.hide();
    }
}
