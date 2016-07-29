package com.example.wangjun.mytestdemo.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.entity.WXNews;
import com.example.wangjun.mytestdemo.http.API;
import com.example.wangjun.mytestdemo.utils.MyLog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okhttputils.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * 微信文章精选
 * Created by wangjun on 2016/7/21.
 */
public class AlipayFragment extends BaseFragment {


    public static final String TAG = "AlipayFragment";
    private XRecyclerView mRecyclerView;
    private List<String> bannerList = new ArrayList<>();

    @Override
    public void onFragmentCreate(Bundle savedInstanceState) {

        setContentView(R.layout.fragment_alipay, savedInstanceState);
        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerview_main);
        MyLog.d(TAG, "初始化");
        initView();
        initData();
    }

    //初始化视图
    private void initView() {

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
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
        getNata(1, 30);
    }

    /**
     * 网络请求
     *
     * @param curPage  当前页数
     * @param pageSize 每页返回的条目数
     */
    private void getNata(int curPage, int pageSize) {
        OkHttpUtils.post(API.JHWX_API)
                .tag(this)//
                .params("pno", curPage + "")
                .params("ps", pageSize + "")
                .params("key", "ceb28e6fca6f7812e08401c78fada917")
                .params("dtype", "json")
                .execute(new NetWorkCallBack<>(WXNews.ResultBean.class));

    }

    @Override
    protected void onHandleMessage(Message msg) {

    }


    @Override
    protected void onAgainLoadNetData() {

    }


    @Override
    protected <T> void onNetSucess(T t) {
        WXNews.ResultBean resultBean = (WXNews.ResultBean) t;
        int totalPage = resultBean.getTotalPage();
        MyLog.d(TAG, "成功");
        MyLog.d(TAG, "总页数" + totalPage);
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
        MyLog.d(TAG, "失败");
    }

    @Override
    protected void onNetSucess(File file, int code) {

    }


}
