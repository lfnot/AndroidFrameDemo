package com.example.wangjun.mytestdemo.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangjun.mytestdemo.MyApplication;
import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.utils.BaseContants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;
import com.lzy.okhttputils.callback.StringCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.lzy.okhttputils.request.GetRequest;
import com.lzy.okhttputils.request.PostRequest;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected SystemBarTintManager mTintManager;
    /*  private TitleToolbar titleToolbar;
      private ActionBar mActionBar;*/
    private boolean visible = true;
    protected FrameLayout rootContent;
    public RelativeLayout mRlMain;
    public ImageButton mIbBack;
    public ImageButton mIbRight;
    public TextView mTvBack;
    public TextView mTvRight;
    public TextView mTvTitle;
    public Context mContext;
    /**
     * 销毁时是否从列表中移除
     */
    protected boolean onDestoryDoKill = true;

    /**
     * UI Handler
     */
    protected MainHandler mHandler = new MainHandler();
    private RelativeLayout mRlError;
    private ProgressBar mBaseProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setContentView(R.layout.activity_base);
        Window window = getWindow();
        mRlMain = (RelativeLayout) window.findViewById(R.id.rl_main);
        mIbBack = (ImageButton) window.findViewById(R.id.ib_back);
        mIbRight = (ImageButton) window.findViewById(R.id.ib_right);
        mTvBack = (TextView) window.findViewById(R.id.tv_back);
        mTvRight = (TextView) window.findViewById(R.id.tv_right);
        mTvTitle = (TextView) window.findViewById(R.id.tv_title);
        rootContent = (FrameLayout) window.findViewById(R.id.content);
        mRlError = (RelativeLayout) window.findViewById(R.id.rl_error);
        mBaseProgress = (ProgressBar) window.findViewById(R.id.base_progress);
        mContext = this;
        MyApplication.application.addActivity(this);
        initMainListener();
        initToolBar();
        onActivityCreate(savedInstanceState);
        initEvent();
    }

    private void initMainListener() {
        mIbBack.setOnClickListener(this);
        mIbRight.setOnClickListener(this);
        mTvBack.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mRlError.setOnClickListener(this);
    }


    protected abstract void initToolBar();

    protected abstract void initEvent();

    protected abstract void onActivityCreate(Bundle savedInstanceState);


    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) rootContent.findViewById(id);
    }

    @Override
    public View findViewById(int id) {
        return rootContent.findViewById(id);
    }

    private void clearContentView() {
        rootContent.removeAllViews();
    }

    @Override
    public void setContentView(int layoutResID) {
        clearContentView();
        getLayoutInflater().inflate(layoutResID, rootContent, true);
    }

    @Override
    public void setContentView(View view) {
        clearContentView();
        rootContent.addView(view);
    }

    public void setTitleBackground(int drawable) {
        mRlMain.setBackgroundResource(drawable);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        clearContentView();
        rootContent.addView(view, params);
    }

    //沉浸式状态栏
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.setTranslucentStatus(true);
        this.mTintManager = new SystemBarTintManager(this);
        this.mTintManager.setStatusBarTintEnabled(true);
        this.mTintManager.setTintColor(this.getResources().getColor(R.color.haijun));
    }


    //toolbar设置标题
    protected void setTitle(String title) {
        mTvTitle.setText(title);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window w = this.getWindow();
        WindowManager.LayoutParams params = w.getAttributes();
        if (on) {
            params.flags |= 67108864;
        } else {
            params.flags &= -67108865;
        }

        w.setAttributes(params);
    }


    //解决内存溢出的问题
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
            case R.id.ib_back:
                onBackClick();
                break;
            case R.id.tv_right:
            case R.id.ib_right:
                onRightClick();
                break;
            case R.id.rl_error:
                onAgainLoadNetData();
                break;
        }
    }

    /**
     * 点击失败的页面重新加载处理
     */
    protected abstract void onAgainLoadNetData();


    /**
     * 标题栏右边按钮的点击事件
     */
    protected abstract void onRightClick();

    /**
     * 标题栏左边的按钮点击事件
     */
    protected abstract void onBackClick();


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
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    /**
     * 简易消息提示框
     *
     * @param s 显示消息字符串
     */
    public void showToast(String s, int time) {
        Toast.makeText(this, s, time).show();
    }

    /**
     * 简易消息提示框
     * 显示消息id
     */
    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    protected void onDestroy() {
        if (onDestoryDoKill) {
            MyApplication.application.killActivity(this);
        }
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkHttpUtils.getInstance().cancelTag(this);
    }


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
                        //请求成功
                        onNetSucess(file);

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
            //请求成功
            onNetSucess(s);
        }

        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            rootContent.setVisibility(View.GONE);
            mRlError.setVisibility(View.VISIBLE);
            //失败
            onNetError();
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


}
