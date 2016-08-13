package com.example.wangjun.mytestdemo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.widget.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 当前类注释：视频播放
 * Author :LeonWang
 * Created  2016/8/11.16:29
 * Description:
 * E-mail:lijiawangjun@gmail.com
 */

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.video_progress)
    ProgressBar mVideoProgress;
    @BindView(R.id.rl_bg)
    RelativeLayout mRlBg;
    private float mDownX;
    private float mDownY;
    String url, title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        ViewUtil.setFullScreen(this);
        Intent i = getIntent();
        url = i.getStringExtra("Url");
        title = i.getStringExtra("Title");
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}
