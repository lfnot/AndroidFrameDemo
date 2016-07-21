package com.example.wangjun.mytestdemo.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.example.wangjun.mytestdemo.utils.BaseContants;
import com.example.wangjun.mytestdemo.utils.MyLog;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.lzy.okhttputils.request.GetRequest;
import com.lzy.okhttputils.request.PostRequest;

import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * basic fragment
 */
public abstract class Base1Fragment extends Fragment {

    private View rootView;//缓存Fragment view
    public Context mContext;//上下文
    private RelativeLayout mRlError;
    private ProgressBar mBaseProgress;
    private RelativeLayout mRlBefore;
    protected FrameLayout rootContent;

    protected MainHandler mHandler = new MainHandler();

    protected Base1Fragment() {
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
            mRlBefore = (RelativeLayout) rootView.findViewById(R.id.rl_before);
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
        initEvent();
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
     * 初始化事件
     */
    protected abstract void initEvent();


    /**
     * 点击失败的页面重新加载处理
     */
    protected abstract void onAgainLoadNetData();

    //网络请求


    /**
     * 演示：
     * HashMap<String, String> params = new HashMap<>();
     * params.put("key1", "value1");
     * params.put("key2", "这里是需要提交的json格式数据");
     * params.put("key3", "也可以使用三方工具将对象转成json字符串");
     * params.put("key4", "其实你怎么高兴怎么写都行");
     * JSONObject jsonObject = new JSONObject(params);
     * <p/>
     * 普通Post，直接上传Json类型的文本
     *
     * @param url
     * @param jsonObject
     */
    public void setPostJsonRequest(String url, JSONObject jsonObject) {

        OkHttpUtils.post(url)
                .tag(mContext)
                .postJson(jsonObject.toString())
                .execute(netCallBack);
    }


    /**
     * 普通Post，直接上传String类型的文本
     *
     * @param url 请求的url
     * @param str 直接上传的string
     */
    public void setPostStringRequest(String url, String str) {
        OkHttpUtils.post(url)
                .tag(mContext)
                .postString(str)
                .execute(netCallBack);
    }

    /**
     * 演示：
     * PostRequest post.headers("header1", "headerValue1")     // 添加请求头参数
     * .headers("header2", "headerValue2")     // 支持多请求头参数同时添加
     * .params("param1", "paramValue1")        // 添加请求参数
     * .params("param2", "paramValue2")        // 支持多请求参数同时添加
     * .params("file1", new File("filepath1")) // 可以添加文件上传
     * .params("file2", new File("filepath2")) // 支持多文件同时添加上传
     * .addUrlParams("key", List<String> values)                                   //这里支持一个key传多个参数
     * .addFileParams("key", List<File> files)                                     //这里支持一个key传多个文件
     * .addFileWrapperParams("key", List<HttpParams.FileWrapper> fileWrappers)     //这里支持一个key传多个文件
     * 带有请求头以及params参数的post请求
     *
     * @param post
     */
    public void setPostParamsRequest(PostRequest post) {

        post.execute(netCallBack);
    }


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
                        mRlBefore.setVisibility(View.GONE);
                        //请求成功
                        onNetSucess(file);

                    }

                    @Override
                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                        super.onError(isFromCache, call, response, e);
                        rootContent.setVisibility(View.GONE);
                        mRlError.setVisibility(View.VISIBLE);
                        mRlBefore.setVisibility(View.GONE);
                        //请求失败
                        onNetError();

                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        rootContent.setVisibility(View.GONE);
                        mRlBefore.setVisibility(View.VISIBLE);
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
                });
    }

    /**
     * 演示：
     * GetRequest getRequest.headers("header1", "headerValue1")     // 添加请求头参数
     * .headers("header2", "headerValue2")     // 支持多请求头参数同时添加
     * .params("param1", "paramValue1")        // 添加请求参数
     * .params("param2", "paramValue2")        // 支持多请求参数同时添加
     * .params("file1", new File("filepath1")) // 可以添加文件上传
     * .params("file2", new File("filepath2")) // 支持多文件同时添加上传
     * .addUrlParams("key", List<String> values)                                   //这里支持一个key传多个参数
     * .addFileParams("key", List<File> files)                                     //这里支持一个key传多个文件
     * .addFileWrapperParams("key", List<HttpParams.FileWrapper> fileWrappers)     //这里支持一个key传多个文件
     * 带有header以及params等请求参数的get请求
     *
     * @param getRequest
     */
    public void setGetParamsRequest(GetRequest getRequest) {

        getRequest.execute(netCallBack);
    }


    /**
     * 请求的回调
     */
    private StringCallback netCallBack = new StringCallback() {
        @Override
        public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
            rootContent.setVisibility(View.VISIBLE);
            mRlError.setVisibility(View.GONE);
            mRlBefore.setVisibility(View.GONE);
            //请求成功
            onNetSucess(s);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            rootContent.setVisibility(View.GONE);
            mRlError.setVisibility(View.VISIBLE);
            mRlBefore.setVisibility(View.GONE);
            //失败
            onNetError();
        }

        @Override
        public void onBefore(BaseRequest request) {
            super.onBefore(request);
            rootContent.setVisibility(View.GONE);
            mRlBefore.setVisibility(View.VISIBLE);
            mBaseProgress.setVisibility(View.VISIBLE);
            //请求之前的回调--可以用来显示对话框
            onNetBefore();
        }

        @Override
        public void onAfter(boolean isFromCache, @Nullable String s, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onAfter(isFromCache, s, call, response, e);
            mBaseProgress.setVisibility(View.GONE);
            //请求无论失败成功进行回调---可用关闭对话框
            onNetAfter();
        }
    };

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
     * @param result
     */
    protected abstract void onNetSucess(String result);

    /**
     * 请求成功
     *
     * @param file
     */
    protected abstract void onNetSucess(File file);

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        MyLog.d("onResume", "onResume");
        initResume();
    }

    protected abstract void initResume();

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
