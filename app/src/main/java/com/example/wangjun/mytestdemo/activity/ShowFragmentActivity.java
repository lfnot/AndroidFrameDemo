package com.example.wangjun.mytestdemo.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.fragment.TextFragment;
import com.lzy.widget.AlphaIndicator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangjun on 2016/7/21.
 */
public class ShowFragmentActivity extends BaseActivity {


    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.alphaIndicator)
    AlphaIndicator mAlphaIndicator;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_showfragment);
        ButterKnife.bind(this);
    }


    @Override
    protected void initToolBar() {
        setTitle("fragment");
        setTitleBackground(R.color.limegreen);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mTintManager.setTintColor(this.getResources().getColor(R.color.limegreen));
    }

    @Override
    protected void initEvent() {
        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        mAlphaIndicator.setViewPager(mViewPager);
    }

    private class MainAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private String[] titles = {//
                "第一页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度",//
                "第二页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度",//
                "第三页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度", //
                "第四页\n\n重点看下面的的图标是渐变色，随着滑动距离的增加，颜色逐渐过度"};

        public MainAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(TextFragment.newInstance(titles[0]));
            fragments.add(TextFragment.newInstance(titles[1]));
            fragments.add(TextFragment.newInstance(titles[2]));
            fragments.add(TextFragment.newInstance(titles[3]));
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @Override
    protected void onAgainLoadNetData() {

    }

    @Override
    protected void onRightClick() {

    }

    @Override
    protected void onBackClick() {

    }

    @Override
    protected void onHandleMessage(Message msg) {

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
    protected void onNetSucess(String result) {

    }

    @Override
    protected void onNetSucess(File file) {

    }
}
