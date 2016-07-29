package com.example.wangjun.mytestdemo.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.utils.MyLog;

import java.io.File;

/**
 * Created by wangjun on 2016/7/21.
 */
public class ClothesFragment extends BaseFragment {


    public static final String TAG = "ClothesFragment";

    @Override
    public void onFragmentCreate(Bundle savedInstanceState,View rootView) {

        setContentView(R.layout.fragment_clothes,savedInstanceState);

        MyLog.d(TAG,"初始化");
    }

    @Override
    protected void onHandleMessage(Message msg) {

    }


    @Override
    protected void onAgainLoadNetData() {

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
    protected void onNetSucess(String result,int code) {

    }

    @Override
    protected void onNetSucess(File file,int code) {

    }

}
