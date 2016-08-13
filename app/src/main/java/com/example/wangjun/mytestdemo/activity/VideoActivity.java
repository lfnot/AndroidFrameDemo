package com.example.wangjun.mytestdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.widget.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 当前类注释：视频播放
 * Author :LeonWang
 * Created  2016/8/11.16:29
 * Description:
 * E-mail:lijiawangjun@gmail.com
 */

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.video_progress)
    ProgressBar mVideoProgress;
    @BindView(R.id.rl_bg)
    RelativeLayout mRlBg;
    @BindView(R.id.videoplayer)
    JCVideoPlayerStandard mVideoplayer;
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

        mVideoplayer.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVideoplayer.startFullscreen(this, JCVideoPlayerStandard.class, url, title);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mVideoplayer.releaseAllVideos();
    }


}
