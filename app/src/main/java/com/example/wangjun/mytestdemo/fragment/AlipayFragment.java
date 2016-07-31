package com.example.wangjun.mytestdemo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.activity.WebViewActivity;
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
    private List<WXNews.ResultBean.ListBean> bannerList2 = new ArrayList<>();
    private MainAdapter mMainAdapter;
    private LayoutInflater mInflater;
    private BannerLayout mBannerLayout;
    private int curPage;
    private int totalPage;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_REFREH = 1;
    public static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;
    private RecyclerViewHeader mHeader;
    private boolean isHeader = true;

    @Override
    public void onFragmentCreate(Bundle savedInstanceState) {

        setContentView(R.layout.fragment_alipay, savedInstanceState);
        //mHeader = (RecyclerViewHeader) findViewById(R.id.header);
        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerview_main);
        //mBannerLayout = (BannerLayout)findViewById(R.id.banner_main);
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
        // mHeader.attachTo(mRecyclerView);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(Color.TRANSPARENT)
                .sizeResId(R.dimen.space_10)
                .marginResId(R.dimen.space_1, R.dimen.space_1)
                .build());
//        mRecyclerView.setRefreshing(false);
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
                    //mRecyclerView.setNoMore(true);
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
            if (null != bannerList2) {
                bannerList2.clear();
            }
            if (list.size() > 6) {
                for (int i = 1; i < 6; i++) {
                    bannerList.add(list.get(i).getFirstImg());
                    bannerList2.add(list.get(i));
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
                    mMainAdapter.notifyDataSetChanged();
                    mRecyclerView.refreshComplete();
                    break;
                case STATE_MORE:
                    mMainAdapter.addData(mMainAdapter.getDatas().size(), list);
                    //mMainAdapter.notifyDataSetChanged();
                    mMainAdapter.notifyDataSetChanged();
                    mRecyclerView.loadMoreComplete();
                    break;
            }

        }

        mMainAdapter.setOnItemClickListener(new MainAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, WXNews.ResultBean.ListBean listBean) {
                Bundle bundle = new Bundle();
                bundle.putInt("item", 100);
                bundle.putString("url", listBean.getUrl());
                Intent intent = new Intent(mContext, WebViewActivity.class);
                bundle.putString("imageUrl",listBean.getFirstImg());
                bundle.putString("title",listBean.getTitle());
                bundle.putString("content",listBean.getSource());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mBannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (null != bannerList2) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("item", 100);
                    bundle.putString("url", bannerList2.get(position).getUrl());
                    bundle.putString("imageUrl",bannerList2.get(position).getFirstImg());
                    bundle.putString("title",bannerList2.get(position).getTitle());
                    bundle.putString("content",bannerList2.get(position).getSource());
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        MyLog.d(TAG, "成功");
        MyLog.d(TAG, "总页数" + totalPage);
    }

    private void showBanner() {
        mInflater = LayoutInflater.from(mContext);
        if (isHeader) {//防止刷新后重复加载
            View view = mInflater.inflate(R.layout.banner_item, null);
            mBannerLayout = (BannerLayout) view.findViewById(R.id.banner_main);
            mRecyclerView.addHeaderView(view);
            isHeader = false;
        }
        mBannerLayout.setViewUrls(bannerList);
    }

    private void loadMoreData() {
        curPage = ++curPage;
        state = STATE_MORE;
        getNata(curPage, 30);
    }

    private void refreshData() {
        curPage = 1;
        state = STATE_REFREH;
        getNata(curPage, 30);
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
