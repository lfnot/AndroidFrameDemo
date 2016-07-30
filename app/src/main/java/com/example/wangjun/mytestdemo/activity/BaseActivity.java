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
import com.example.wangjun.mytestdemo.http.HttpCallback;
import com.example.wangjun.mytestdemo.utils.BaseContants;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;
import com.lzy.okhttputils.request.BaseRequest;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected SystemBarTintManager mTintManager;
    /*  private TitleToolbar titleToolbar;
      private ActionBar mActionBar;*/
    private boolean visible = true;
    public FrameLayout rootContent;
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
    public RelativeLayout mRlError;
    public ProgressBar mBaseProgress;

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

                    @Override
                    public File parseNetworkResponse(Response response) throws Exception {
                        return parseleFileResponse(response);
                    }
                });
    }


    protected class NetWorkCallBack<T> extends HttpCallback<T> {

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

    //请求成功的回调
    protected abstract <T> void onNetSucess(T t);


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
    protected abstract void onNetSucess(File file);

    /**
     * 子线程耗时操作
     * @param response
     * @return
     */
    protected abstract File parseleFileResponse(Response response);




}
