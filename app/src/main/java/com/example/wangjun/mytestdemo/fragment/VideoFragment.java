package com.example.wangjun.mytestdemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.activity.VideoActivity;
import com.example.wangjun.mytestdemo.adapter.LolAdapter;
import com.example.wangjun.mytestdemo.entity.VideoBean;
import com.example.wangjun.mytestdemo.http.API;
import com.example.wangjun.mytestdemo.http.LOLHttpCallback;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.BaseRequest;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 当前类注释：
 * Author :LeonWang
 * Created  2016/8/11.13:54
 * Description:
 * E-mail:lijiawangjun@gmail.com
 */

public class VideoFragment extends LazyFragment {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.recyclerview)
    XRecyclerView mRecyclerView;
    @BindView(R.id.video_error)
    RelativeLayout mVideoError;
    private int type = 0;
    private int curPage = 1;
    private int totalPage = 25;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_REFREH = 1;
    public static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;

    private static final int DELAY_TIME = 2000;
    Handler handler = new Handler();
    private Context mContext;
    private String t_;
    int catid, catwordid, page, p_;
    private LolAdapter mLolAdapter;

    public static VideoFragment NewInstance(Bundle bundle) {
        VideoFragment newFragment = new VideoFragment();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null);
        ButterKnife.bind(this, view);
        type = getArguments().getInt("type");
//        mPresenter = new NewsPresenter(this);
        mContext = getActivity();
        initView();
        return view;
    }

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
    }

    private void initEvent() {

        switch (type) {
            case 0:
                catid = 10172;
                catwordid = 146;
                page = curPage;
                t_ = "1467794913084";
                p_ = 11597;
                break;
            case 1:
                catid = 10172;
                catwordid = 113;
                page = 1;
                t_ = "1467794446790";
                p_ = 22715;
                break;
            case 2:
                catid = 10173;
                catwordid = 114;
                page = 1;
                t_ = "1467794563851";
                p_ = 13898;
                break;
            case 3:
                catid = 10174;
                catwordid = 48;
                page = 1;
                t_ = "1467794689385";
                p_ = 30500;
                break;
            case 4:
                catid = 10174;
                catwordid = 91;
                page = 1;
                t_ = "1470366703179";
                p_ = 11882;
                break;
            default:
                catid = 10172;
                catwordid = 27;
                page = 1;
                t_ = "1470365360836";
                p_ = 4853;
                break;

        }

        //初始化网络数据
        getNata(1);

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

    private void loadMoreData() {
        page = ++page;
        state = STATE_MORE;
        getNata(page);
    }

    private void refreshData() {
        page = 1;
        state = STATE_REFREH;
        getNata(page);
    }

    @Override
    protected void lazyLoad() {

    }

    /**
     * 网络请求
     *
     * @param curPage 当前页数
     */
    private void getNata(int curPage) {
        OkHttpUtils.get(API.LOL)
                .tag(this)
                .params("catid", catid + "")
                .params("catwordid", catwordid + "")
                .params("page", curPage + "")
                .params("t_", t_)
                .params("p_", p_ + "")
                .execute(new LolCallBack(VideoBean.class));

    }

    @OnClick(R.id.video_error)
    public void onClick() {//加载失败
        //初始化网络数据
        getNata(1);
    }

    protected class LolCallBack extends LOLHttpCallback<VideoBean> {


        public LolCallBack(Class<VideoBean> clazz) {
            super(clazz);
        }

        @Override
        public void onResponse(boolean isFromCache, VideoBean videoBean, Request request, @Nullable Response response) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mVideoError.setVisibility(View.GONE);
            onNetSucess(videoBean);
        }

        @Override
        public void onBefore(BaseRequest request) {
            super.onBefore(request);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable VideoBean videoBean, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, videoBean, call, response, e);
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            mRecyclerView.setVisibility(View.GONE);
            mVideoError.setVisibility(View.VISIBLE);
        }
    }

    private void onNetSucess(VideoBean videoBean) {
        List<VideoBean.DataEntity> list = videoBean.getData();

        if (list != null && list.size() > 0) {
            switch (state) {
                case STATE_NORMAL:
                    mLolAdapter = new LolAdapter(list, mContext);
                    mRecyclerView.setAdapter(mLolAdapter);

                    break;
                case STATE_REFREH:
                    mLolAdapter.clearData();
                    mLolAdapter.addData(list);
                    mLolAdapter.notifyDataSetChanged();
                    mRecyclerView.refreshComplete();
                    break;
                case STATE_MORE:
                    mLolAdapter.addData(mLolAdapter.getDatas().size(), list);
                    //mMainAdapter.notifyDataSetChanged();
                    mLolAdapter.notifyDataSetChanged();
                    mRecyclerView.loadMoreComplete();
                    break;
            }
        }

        mLolAdapter.setOnItemClickListener(new LolAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, VideoBean.DataEntity listBean) {
                Intent intent = new Intent(mContext, VideoActivity.class);
                intent.putExtra("Url",listBean.getVideo_url());
                intent.putExtra("Title",listBean.getTitle());
                startActivity(intent);
            }
        });
    }


}
