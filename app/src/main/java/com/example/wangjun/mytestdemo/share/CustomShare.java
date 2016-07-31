package com.example.wangjun.mytestdemo.share;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wangjun.mytestdemo.R;
import com.example.wangjun.mytestdemo.utils.MyLog;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 社会化分享--自定义分享
 * Created by 程恭纯 on 2016/5/31
 */
public class CustomShare extends PopupWindow implements View.OnClickListener {
    private static final String TAG = CustomShare.class.getSimpleName();
    private Context context;
    private String id;
    protected PlatformActionListener paListener;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private PlatformActionListener platformActionListener;


    //分享到文字
    private TextView tv_share_to;

    //微信图标按钮
    private ImageButton ibtn_wechat;
    //微信文字
    private TextView tv_wechat;

    //朋友圈图标按钮
    private ImageButton ibtn_wechat_moments;
    //朋友圈文字
    private TextView tv_wechat_moments;

    //手机短信图标按钮
    private ImageButton ibtn_short_message;
    //手机短信文字
    private TextView tv_short_message;

    private ImageButton ibtn_qq;
    private TextView tv_qq;

    private ImageButton ibtn_qzone;
    private TextView tv_qzone;

    private ImageButton ibtn_weibo;
    private TextView tv_weibo;

    //取消按钮
    private Button btn_cancel;

    //分享标题
    private String title;
    //分享文本
    private String text;
    //分享URL
    private String url;
    //分享图片URL
    private String imageUrl;
    //分享本地图片
    private String mimagePath;
    private Dialog dialogs;

    public CustomShare(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public PlatformActionListener getPlatformActionListener() {
        return platformActionListener;
    }

    public void setPlatformActionListener(
            PlatformActionListener platformActionListener) {
        this.platformActionListener = platformActionListener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    private void initView() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.share_area, null);
        //分享到文字
        tv_share_to = (TextView) rootView.findViewById(R.id.tv_share_to);

        //微信图标按钮
        ibtn_wechat = (ImageButton) rootView.findViewById(R.id.ibtn_wechat);
        //微信文字
        tv_wechat = (TextView) rootView.findViewById(R.id.tv_wechat);

        //朋友圈图标按钮
        ibtn_wechat_moments = (ImageButton) rootView.findViewById(R.id.ibtn_wechat_moments);
        //朋友圈文字
        tv_wechat_moments = (TextView) rootView.findViewById(R.id.tv_wechat_moments);

        //手机短信图标按钮
        ibtn_short_message = (ImageButton) rootView.findViewById(R.id.ibtn_short_message);
        //手机短信文字
        tv_short_message = (TextView) rootView.findViewById(R.id.tv_short_message);

        //qq图标按钮
        ibtn_qq = (ImageButton) rootView.findViewById(R.id.ibtn_qq);
        //qq文字
        tv_qq = (TextView) rootView.findViewById(R.id.tv_qq);

        //qzone图标按钮
        ibtn_qzone = (ImageButton) rootView.findViewById(R.id.ibtn_qzone);
        //qzone文字
        tv_qzone = (TextView) rootView.findViewById(R.id.tv_qzone);

        //weibo图标按钮
        ibtn_weibo = (ImageButton) rootView.findViewById(R.id.ibtn_weibo);
        //weibo文字
        tv_weibo = (TextView) rootView.findViewById(R.id.tv_weibo);

        //取消按钮
        btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);

        ibtn_wechat.setOnClickListener(this);
        ibtn_wechat_moments.setOnClickListener(this);
        ibtn_short_message.setOnClickListener(this);
        ibtn_qq.setOnClickListener(this);
        ibtn_qzone.setOnClickListener(this);
        ibtn_weibo.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        setContentView(rootView);

        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        backgroundAlpha(0.5f);

        tv_share_to.setText("分享到");

        tv_wechat.setText(R.string.ssdk_wechat);
        tv_wechat_moments.setText(R.string.ssdk_wechatmoments);
        tv_short_message.setText(R.string.ssdk_shortmessage);
        tv_qq.setText(R.string.ssdk_qq);
        tv_qzone.setText(R.string.ssdk_qzone);
        tv_weibo.setText(R.string.ssdk_sinaweibo);

        btn_cancel.setText("更多...");

        //分享标题
        title = "分享标题";
        //分享文本
        text = "分享的内容";
        //分享URL
        url = "https://www.baidu.com";
        //分享图片URL
        imageUrl = "https://www.baidu.com/img/baidu_jgylogo3.gif";
        //分享本地图片
        mimagePath = "file:///android_asset/logo.png";

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //微信
            case R.id.ibtn_wechat:
                onWechatClick();
                break;
            //朋友圈
            case R.id.ibtn_wechat_moments:
                onWechatMomentsClick();
                break;
            //手机短信
            case R.id.ibtn_short_message:
                onShortMessageClick();
                break;
            //QQ
            case R.id.ibtn_qq:
                onQQClick();
                break;
            //Q-Zone
            case R.id.ibtn_qzone:
                onQZoneClick();
                break;
            //weibo
            case R.id.ibtn_weibo:
                onWeiboClick();
                break;
            //更多
            case R.id.btn_cancel:
                onMoreClick();
                break;
            default:
                break;
        }
    }

    /*
    * 微信分享
    */
    private void onWechatClick() {
        MyLog.e(TAG, "微信分享");
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        //platform.setshareType(Platform.SHARE_TEXT);
        //platform.shareType(Platform.SHARE_TEXT);

        Wechat.ShareParams sp = new Wechat.ShareParams();
        //分享内容的标题
        sp.setTitle(title);
        //分享的文本
        sp.setText(text);
        //待分享的本地图片
        //sp.setImagePath(mimagePath);
        //待分享的网络图片
        sp.setImageUrl(imageUrl);

        // 分享内容的url、在微信和易信中也使用为视频文件地址
        sp.setUrl(url);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        // 设置分享事件回调
        platform.setPlatformActionListener(paListener);
        // 执行图文分享
        platform.share(sp);
    }

    /*
     * 朋友圈分享按钮
     */
    private void onWechatMomentsClick() {
        MyLog.e(TAG, "朋友圈");
        Platform platform = ShareSDK.getPlatform(context, WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        //分享内容的标题
        sp.setTitle(title + " " + text);
        //分享的文本
        sp.setText(text);
        //待分享的本地图片
        //sp.setImagePath(mimagePath);
        //待分享的网络图片
        sp.setImageUrl(imageUrl);
        // 分享内容的url、在微信和易信中也使用为视频文件地址
        sp.setUrl(url);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        // 设置分享事件回调
        platform.setPlatformActionListener(paListener);
        // 执行图文分享
        platform.share(sp);
    }

    /*
     * 手机短信分享
     */
    private void onShortMessageClick() {
        MyLog.e(TAG, "手机短信");
        Platform platform = ShareSDK.getPlatform(ShortMessage.NAME);
        ShortMessage.ShareParams sp = new ShortMessage.ShareParams();
        //分享内容的标题
        sp.setTitle(title);
        //分享的文本
        sp.setText(title + " " + text + " " + url);

        // 执行文本分享
        platform.share(sp);


    }

    /*
     * QQ分享
     */
    private void onQQClick() {

        MyLog.e(TAG, "QQ");
        showDialog();
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        QQ.ShareParams sp = new QQ.ShareParams();
        //分享内容的标题
        sp.setTitle(title);
        //分享的文本
        sp.setText(text + " " + url);
        // 分享内容的url
        sp.setUrl(url);
        //待分享图标
        sp.setImageUrl(imageUrl);
        platform.setPlatformActionListener(platformActionListener);
        // 执行图文分享
        platform.share(sp);

    }

    protected Dialog onCreateDialog() {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("正在分析中...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);//点击以外区域不消失
        dialog.setCancelable(true);
        return dialog;
    }

    public void showDialog(){
        dialogs =  onCreateDialog();
        if (null != dialogs){
            dialogs.show();
        }
    }

    public void hideDilog(){
        if (null != dialogs){
            dialogs.dismiss();
        }

    }

    /*
     * Q-Zone分享按钮
     */
    private void onQZoneClick() {

        MyLog.e(TAG, "Q-Zone");
        Platform platform = ShareSDK.getPlatform(QZone.NAME);

        QZone.ShareParams sp = new QZone.ShareParams();
        //分享内容的标题
        //sp.setTitle(title);
        //分享的文本
        sp.setText(title + " " + text + " " + url);
        // 执行图文分享
        platform.share(sp);

    }

    /*
     * weibo分享按钮
     */
    private void onWeiboClick() {

        MyLog.e(TAG, "weibo");
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);

        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        //分享内容的标题
        //sp.setTitle(title);
        //分享的文本
        sp.setText(title + " " + url + " " + imageUrl);
        //sp.setImageUrl(imageUrl);
        //sp.setUrl(url);
        // 执行图文分享
        platform.share(sp);

    }

    /*
     * 底部更多按钮
     */
    private void onMoreClick() {
        //调用系统分享
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");

        intent.putExtra(Intent.EXTRA_TEXT, url);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        this.context.startActivity(Intent.createChooser(intent, title));
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ////将背景透明度改回来
        backgroundAlpha(1f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }

}
