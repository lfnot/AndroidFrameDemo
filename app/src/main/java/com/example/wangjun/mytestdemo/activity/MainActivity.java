package com.example.wangjun.mytestdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.wangjun.mytestdemo.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {


    @BindView(R.id.uu)
    TextView mUu;
    private TextView mTvm;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void initToolBar() {
        mTvRight.setVisibility(View.INVISIBLE);
        mIbRight.setVisibility(View.VISIBLE);
        mTvBack.setVisibility(View.INVISIBLE);
        setTitle("大王叫我来巡山");
        setTitleBackground(R.color.limegreen);
    }

    /*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mTintManager.setTintColor(this.getResources().getColor(R.color.pink));
    }*/

    @Override
    protected void initEvent() {
       // setPostStringRequest("https://www.qiecaijing.com/","你是谁啊");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onAgainLoadNetData() {


    }


    @Override
    protected void onRightClick() {
        startActivity(new Intent(mContext,ShowFragmentActivity.class));
        //finish();
    }

    @Override
    protected void onBackClick() {
        showToast("我被点击Lee");
    }

    @Override
    protected void onHandleMessage(Message msg) {

    }

    /*****************************以下是网络请求的结果***********************/
    @Override
    protected void onNetAfter() {
        showToast("请求之后");
    }

    @Override
    protected void onNetBefore() {
        showToast("请求之前");
    }

    @Override
    protected void onNetError() {
        showToast("失败");
    }

    @Override
    protected void onNetSucess(String result) {
        showToast("成功");
    }

    @Override
    protected void onNetSucess(File file) {

    }



}
