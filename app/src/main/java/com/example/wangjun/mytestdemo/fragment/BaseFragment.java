package com.example.wangjun.mytestdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.http.HttpCallback;
import com.example.wangjun.mytestdemo.utils.BaseContants;
import com.example.wangjun.mytestdemo.utils.MyLog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;
import com.lzy.okhttputils.request.BaseRequest;

import java.io.File;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * basic fragment
 */
public abstract class BaseFragment extends Fragment {

    private View rootView;//缓存Fragment view
    public Context mContext;//上下文
    public RelativeLayout mRlError;
    public ProgressBar mBaseProgress;
    protected FrameLayout rootContent;

    protected MainHandler mHandler = new MainHandler();

    protected BaseFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    /**
     * This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_base, null);
            rootContent = (FrameLayout) rootView.findViewById(R.id.content);
            mRlError = (RelativeLayout) rootView.findViewById(R.id.rl_error);
            mBaseProgress = (ProgressBar) rootView.findViewById(R.id.base_progress);
            onFragmentCreate(savedInstanceState);
            MyLog.d("onCreateView", "onCreateView");
        }
        //缓存的rootView需要判断是否已经被加过parent,如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
            MyLog.d("removeView", "removeView");
        }
        return rootView;
    }

    public abstract void onFragmentCreate(Bundle savedInstanceState);

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) rootContent.findViewById(id);
    }


    public View findViewById(int id) {
        return rootContent.findViewById(id);
    }

    private void clearContentView() {
        rootContent.removeAllViews();
    }

    public void setContentView(int layoutResID ,Bundle savedInstanceState) {
        clearContentView();
        getLayoutInflater(savedInstanceState).inflate(layoutResID, rootContent, true);
    }

    public void setContentView(View view) {
        clearContentView();
        rootContent.addView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        clearContentView();
        rootContent.addView(view, params);
    }

    /**
     * This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyLog.d("onActivityCreated", "onActivityCreated");
        mRlError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAgainLoadNetData();
            }
        });
    }

    /**
     * 处理UI Handler消息
     *
     * @param msg
     */
    protected abstract void onHandleMessage(Message msg);

    public class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            onHandleMessage(msg);
        }
    }

    /**
     * 发送空消息
     *
     * @param msgid
     */
    public void sendEmptyMessages(int msgid) {
        mHandler.sendEmptyMessage(msgid);
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMessages(Message msg) {
        mHandler.sendMessage(msg);
    }

    /**
     * 发送文件下载完成消息
     */
    public void sendMessagesFileDownloadDone() {
        mHandler.sendEmptyMessage(BaseContants.SysMessageWhat.DOWNLOAD_FILE_DONE);
    }


    /**
     * 简易消息提示框
     *
     * @param s 显示消息字符串
     */
    public void showToast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * 简易消息提示框
     *
     * @param s 显示消息字符串
     */
    public void showToast(String s, int time) {
//        ToastUtils.init(mContext);
        Toast.makeText(mContext, s, time).show();
    }

    /**
     * 简易消息提示框
     * 显示消息id
     */
    public void showToast(int id) {
        Toast.makeText(mContext, id, Toast.LENGTH_SHORT).show();
    }


    /**
     * 点击失败的页面重新加载处理
     */
    protected abstract void onAgainLoadNetData();

    //网络请求
    /**
     * 请求 文件下载--这里请求的是图片下载
     *
     * @param url
     * @param jpg :这里的如果是加上下载相关文件的扩展名
     */
    public void setFileDoloadRequst(String url, String jpg) {

        OkHttpUtils.get(url)
                .tag(mContext)
                .execute(new FileCallback("/sdcard/temp/", jpg) {
                    @Override
                    public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {
                        rootContent.setVisibility(View.VISIBLE);
                        mRlError.setVisibility(View.GONE);
                        int code = response.code();
                        //请求成功
                        onNetSucess(file,code);

                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        rootContent.setVisibility(View.GONE);
                        mRlError.setVisibility(View.VISIBLE);
                        //请求失败
                        onNetError();
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        rootContent.setVisibility(View.VISIBLE);
                        mBaseProgress.setVisibility(View.VISIBLE);
                        //请求之前的调用
                        onNetBefore();
                    }

                    @Override
                    public void onAfter(boolean isFromCache, @Nullable File file, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onAfter(isFromCache, file, call, response, e);
                        mBaseProgress.setVisibility(View.GONE);
                        //请求之后的调用
                        onNetAfter();
                    }

                    @Override
                    public File parseNetworkResponse(Response response) throws Exception {
                        return parseleFileResponse(response);
                    }
                });
    }

    protected class NetWorkCallBack<T> extends HttpCallback<T>{

        public NetWorkCallBack(Class<T> clazz) {
            super(clazz);
        }

        public NetWorkCallBack(Type type) {
            super(type);
        }

        @Override
        public void onResponse(boolean isFromCache, T t, Request request, @Nullable Response response) {
            rootContent.setVisibility(View.VISIBLE);
            mRlError.setVisibility(View.GONE);
            //请求成功
            onNetSucess(t);
        }

        @Override
        public void onBefore(BaseRequest request) {
            super.onBefore(request);
            rootContent.setVisibility(View.VISIBLE);
            mBaseProgress.setVisibility(View.VISIBLE);
            //请求之前的回调--可以用来显示对话框
            onNetBefore();
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable T t, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, t, call, response, e);
            mBaseProgress.setVisibility(View.GONE);
            //请求无论失败成功进行回调---可用关闭对话框
            onNetAfter();
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            rootContent.setVisibility(View.GONE);
            mRlError.setVisibility(View.VISIBLE);
            //失败
            onNetError();
        }
    }


    //请求成功的回掉
    protected abstract <T> void onNetSucess(T t);

    /**
     * 子线程耗时操作
     * @param response
     * @return
     */
    protected abstract File parseleFileResponse(Response response);

    //请求无论失败成功进行回调-
    protected abstract void onNetAfter();

    //请求之前的回调
    protected abstract void onNetBefore();

    /**
     * 请求失败
     */
    protected abstract void onNetError();

    /**
     * 请求成功
     *
     * @param file
     */
    protected abstract void onNetSucess(File file ,int code);

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        MyLog.d("onResume", "onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        MyLog.d("onPause", "onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        MyLog.d("onStop", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.d("onDestroy", "onDestroy");
        //Activity销毁时，取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
    }

}
