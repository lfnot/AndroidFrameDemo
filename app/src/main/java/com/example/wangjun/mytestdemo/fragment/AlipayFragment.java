package com.example.wangjun.mytestdemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.adapter.MainAdapter;
import com.example.wangjun.mytestdemo.entity.WXNews;
import com.example.wangjun.mytestdemo.http.API;
import com.example.wangjun.mytestdemo.utils.BannerLayout;
import com.example.wangjun.mytestdemo.utils.MyLog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okhttputils.OkHttpUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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
    private MainAdapter mMainAdapter;
    private LayoutInflater mInflater;
    private BannerLayout mBannerLayout;
    private int curPage;
    private int totalPage;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_REFREH = 1;
    public static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(Color.TRANSPARENT)
                .sizeResId(R.dimen.space_10)
                .marginResId(R.dimen.space_1, R.dimen.space_1)
                .build());
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            //刷新
            @Override
            public void onRefresh() {
                refreshData();
            }

            //下拉更多
            @Override
            public void onLoadMore() {
                if (curPage < totalPage) {
                    loadMoreData();
                } else {
                    //不做处理
                    mRecyclerView.loadMoreComplete();
                }
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
        totalPage = resultBean.getTotalPage();
        List<WXNews.ResultBean.ListBean> list = resultBean.getList();
       /* if (null != mRecyclerView){
            mRecyclerView.removeAllViews();
        }*/
        if (list != null && list.size() > 0) {

            if (null != bannerList) {
                bannerList.clear();
            }
            if (list.size() > 6) {
                for (int i = 1; i < 6; i++) {
                    bannerList.add(list.get(i).getFirstImg());
                }
                showBanner();
            }

            switch (state) {
                case STATE_NORMAL:
                    mMainAdapter = new MainAdapter(list, mContext);
                    mRecyclerView.setAdapter(mMainAdapter);
                    break;
                case STATE_REFREH:
                    mMainAdapter.clearData();
                    mMainAdapter.addData(list);
                    mRecyclerView.refreshComplete();
                    break;
                case STATE_MORE:
                    mMainAdapter.addData(mMainAdapter.getDatas().size(), list);
                    //mMainAdapter.notifyDataSetChanged();
                    mRecyclerView.loadMoreComplete();
                    break;
            }

        }

        MyLog.d(TAG, "成功");
        MyLog.d(TAG, "总页数" + totalPage);
    }

    private void showBanner() {
        mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.banner_item, null);
        mBannerLayout = (BannerLayout) view.findViewById(R.id.banner_main);
        mRecyclerView.addHeaderView(view);
        mBannerLayout.setViewUrls(bannerList);
    }

    private void loadMoreData() {
        curPage = ++curPage;
        state = STATE_MORE;
        getNata(curPage, curPage);
    }

    private void refreshData() {

        curPage = 1;
        state = STATE_REFREH;
        getNata(curPage, 30);
        ;//加载列表
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
