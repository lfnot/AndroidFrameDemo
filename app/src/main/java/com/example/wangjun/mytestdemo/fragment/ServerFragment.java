package com.example.wangjun.mytestdemo.fragment;

import android.os.Bundle;
import android.os.Message;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.utils.MyLog;

import java.io.File;

/**
 * Created by wangjun on 2016/7/21.
 */
public class ServerFragment extends BaseFragment {


    public static final String TAG = "MoneyFragment";

    @Override
    public void onFragmentCreate(Bundle savedInstanceState) {

        setContentView(R.layout.fragment_server,savedInstanceState);
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
    protected void onNetSucess(String result) {

    }

    @Override
    protected void onNetSucess(File file) {

    }

}