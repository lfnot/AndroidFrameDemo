package com.example.wangjun.mytestdemo.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.http.API;
import com.example.wangjun.mytestdemo.utils.MyLog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.PostRequest;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 微信文章精选
 * Created by wangjun on 2016/7/21.
 */
public class AlipayFragment extends BaseFragment {


    public static final String TAG = "AlipayFragment";
    @BindView(R.id.recyclerview_main)
    XRecyclerView mRecyclerView;

    @Override
    public void onFragmentCreate(Bundle savedInstanceState,View rootView) {

        setContentView(R.layout.fragment_alipay, savedInstanceState);
        ButterKnife.bind(getActivity(),rootView);
        MyLog.d(TAG, "初始化");
        //initView();
        initData();
    }

    //初始化视图
    private void initView() {

        GridLayoutManager layoutManager = new GridLayoutManager(mContext,3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            //刷新
            @Override
            public void onRefresh() {

            }

            //下拉更多
            @Override
            public void onLoadMore() {

            }
        });
    }

    //初始化数据
    private void initData() {
        //请求网络
        getNata(1,30);
    }

    /**
     * 网络请求
     * @param curPage 当前页数
     * @param pageSize 每页返回的条目数
     */
    private void getNata(int curPage ,int pageSize) {
        PostRequest postRequest = OkHttpUtils.post(API.JHWX_API)
                .params("pno", curPage + "")
                .params("ps", pageSize + "")
                .params("key", "ceb28e6fca6f7812e08401c78fada917")
                .params("dtype", "json");
        setPostParamsRequest(postRequest);
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
        MyLog.d(TAG,"失败");
    }

    @Override
    protected void onNetSucess(String result, int code) {
        MyLog.d(TAG,"获取首页list结果："+result);
    }

    @Override
    protected void onNetSucess(File file, int code) {

    }




    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }*/
}
