package com.example.wangjun.mytestdemo.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.utils.MyLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by wangjun on 2016/7/21.
 */
public class ClothesFragment extends BaseFragment {


    public static final String TAG = "ClothesFragment";

    TabLayout mTabSelector;
    ViewPager mViewPager;
    private List<VideoFragment> mFragmentList;
    private String[] mTitles = new String[]{"若风解说","最新解说","最新赛事","懵逼瞬间","其他视频"};
    private VideoPagerAdapter mAdapter;

    @Override
    public void onFragmentCreate(Bundle savedInstanceState) {

        setContentView(R.layout.fragment_clothes,savedInstanceState);
        mTabSelector = (TabLayout) findViewById(R.id.tab_selector);
        mViewPager = (ViewPager) findViewById(R.id.vp_video);
        initData();
        initView();
        MyLog.d(TAG,"初始化");
    }


    private void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.clear();
        for(int i = 0;i<mTitles.length;i++){
            Bundle bundle = new Bundle();
            bundle.putInt("type",i);
            mFragmentList.add(VideoFragment.NewInstance(bundle));
        }
    }

    private void initView() {
        mAdapter = new VideoPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabSelector.setupWithViewPager(mViewPager);
        mTabSelector.setTabMode(TabLayout.MODE_SCROLLABLE);
    }


    private class VideoPagerAdapter extends FragmentStatePagerAdapter {

        public VideoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

    @Override
    protected void onHandleMessage(Message msg) {

    }


    @Override
    protected void onAgainLoadNetData() {

    }


    @Override
    protected <T> void onNetSucess(T t) {

    }

    @Override
    protected File parseleFileResponse(Response response) {
        return null;
    }

    @Override
    protected void onNetAfter() {

    }

    @Override
    protected void onNetBefore() {

    }

    @Override
    protected void onNetError() {

    }

    @Override
    protected void onNetSucess(File file,int code) {

    }

}
