package com.example.wangjun.mytestdemo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.share.CustomShare;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.Response;

/**
 * 当前类注释：webview
 * Author: LeonWang
 * Time: 2016/7/30.10:56
 * E-mail:lijiawangjun@gmail.com
 * Description：
 */
public class WebViewActivity extends BaseActivity {


    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    // html
    private String content;
    private String imageUrl;
    private String title;
    private String source;
    private WebSettings mWebSettings;
    private CustomShare mShareBoard;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        ShareSDK.initSDK(mContext);
    }

    @Override
    protected void initToolBar() {

        //mIbRight.setVisibility(View.INVISIBLE);
        mIbRight.setImageResource(R.mipmap.icon_share);
        mTvRight.setVisibility(View.INVISIBLE);
        mTvBack.setVisibility(View.INVISIBLE);
        setTitleBackground(R.color.haijun);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mTintManager.setTintColor(this.getResources().getColor(R.color.haijun));
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void initEvent() {

        Bundle bundle = getIntent().getExtras();
        int item = bundle.getInt("item");
        if (item == 100) {
            setTitle("微信精选");
            String url = bundle.getString("url");
            imageUrl = bundle.getString("imageUrl");
            title = bundle.getString("title");
            source = bundle.getString("content");
            if (null != url) {
                content = url;
            }
        }else if (item == 200){
            setTitle("新闻");
            String url = bundle.getString("url");
            imageUrl = bundle.getString("imageUrl");
            title = bundle.getString("title");
            source = bundle.getString("content");
            if (null != url) {
                content = url;
            }
        }else if (item == 300){
            String url = bundle.getString("url");
            imageUrl = bundle.getString("imageUrl");
            title = bundle.getString("title");
            setTitle(title);
            source = bundle.getString("content");
            if (null != url) {
                content = url;
            }
        }

        // 设置支持JavaScript等
        mWebSettings = mWebview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setLightTouchEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setDefaultTextEncodingName("UTF -8");
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 设置缓存模式
        mWebview.setHapticFeedbackEnabled(false);

        // 点击链接继续在当前browser中响应
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view,
                                                    final String url) {
                view.loadUrl(url);// 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                mProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               /* if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (mProgressBar.getVisibility() == View.GONE)
                        mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }*/
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });


        // 覆盖默认后退按钮的作用，替换成WebView里的查看历史页面
        mWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
                        mWebview.goBack();
                        return true;
                    }
                }
                return false;
            }
        });

        // js处理
        // mWebview.addJavascriptInterface(this, "jsjtrips");

        mWebview.loadUrl(this.content);
        //mWebview.loadDataWithBaseURL(null, this.content, "text/html", "utf-8", null);
    }


    @Override
    protected void onAgainLoadNetData() {

    }

    @Override
    protected void onRightClick() {

        //分享
        showSharePopWindow();

    }

    private void showSharePopWindow() {
        mShareBoard = new CustomShare(mContext);
//        mShareBoard.setPlatformActionListener(WebViewActivity.this);
        mShareBoard.setTitle(title + "");
        mShareBoard.setText("微信精选");
        mShareBoard.setUrl(content);
        mShareBoard.setImageUrl(imageUrl);
        mShareBoard.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onBackClick() {

        if (mWebview.canGoBack()) {
            mWebview.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onHandleMessage(Message msg) {

    }


    /***************************
     * 以下是网络请求
     *******************************/
    @Override
    protected <T> void onNetSucess(T t) {

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
    protected void onNetSucess(File file) {

    }

    @Override
    protected File parseleFileResponse(Response response) {

        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebview.setVisibility(View.GONE);
        ShareSDK.stopSDK(this);
    }


}
