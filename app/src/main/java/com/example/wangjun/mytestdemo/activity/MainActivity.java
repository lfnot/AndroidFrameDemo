package com.example.wangjun.mytestdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.fragment.Maingment;
import com.example.wangjun.mytestdemo.fragment.ClothesFragment;
import com.example.wangjun.mytestdemo.fragment.MoneyFragment;
import com.example.wangjun.mytestdemo.fragment.ChatFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

public class MainActivity extends BaseActivity {


    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {Maingment.class,ClothesFragment.class,
            ChatFragment.class,MoneyFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.alipay_selector,
            R.drawable.clothes_selector,
            R.drawable.server_selector,
            R.drawable.money_selector};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "便笺", "聊天", "我的"};

    @BindView(R.id.realtabcontent)
    FrameLayout mRealtabcontent;
    @BindView(android.R.id.tabcontent)
    FrameLayout mTabcontent;
    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabhost;

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
        setTitle("首页");
        setTitleBackground(R.color.haijun);
    }

    /*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mTintManager.setTintColor(this.getResources().getColor(R.color.pink));
    }*/

    @Override
    protected void initEvent() {
        initView();
    }

    //初始化组件
    private void initView() {
        mTabhost.setup(mContext,getSupportFragmentManager(),R.id.realtabcontent);
        //得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            //为每一个tab按钮设置图标文字和内容
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将tab按钮添加到tab选项卡中
            mTabhost.addTab(tabSpec,fragmentArray[i],null);
            //设置tab按钮的背景
           /* mTabhost.getTabWidget().getChildAt(i).setBackgroundResource(mImageViewArray[i]);*/
        }

        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "首页":
                        setTitle("首页");
                        break;
                    case "便笺":
                        setTitle("便笺");
                        break;
                    case "聊天":
                        setTitle("聊天");
                        break;
                    case "我的":
                        setTitle("我的");
                        break;

                }
            }
        });
    }

    /*
    * 给tab按钮设置图标和文字
    * */
    private View getTabItemView(int index){

        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
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
        startActivity(new Intent(mContext, ShowFragmentActivity.class));
        //finish();
    }

    @Override
    protected void onBackClick() {
        showToast("我被点击Lee");
    }

    @Override
    protected void onHandleMessage(Message msg) {

    }

    @Override
    protected <T> void onNetSucess(T t) {

    }

    /*****************************
     * 以下是网络请求的结果
     ***********************/
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
    protected void onNetSucess(File file) {

    }

    @Override
    protected File parseleFileResponse(Response response) {
        return null;
    }


}
