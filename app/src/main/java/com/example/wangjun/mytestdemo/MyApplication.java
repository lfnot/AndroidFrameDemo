package com.example.wangjun.mytestdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wangjun.mytestdemo.utils.CallSystemUtil;
import com.example.wangjun.mytestdemo.utils.ToastUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpHeaders;
import com.lzy.okhttputils.model.HttpParams;

import java.util.LinkedList;

/**
 * Created by wangjun on 2016/7/20.
 */
public class MyApplication extends Application {


    public static MyApplication application = null;

    /**
     * 应用创建activity集合 在创建activity时调用addActivity方法将新创建的活动添加到集合中
     */
    private static LinkedList<Activity> activitys = new LinkedList<Activity>();


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initOkHttp();
    }

    private void initOkHttp() {

        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //所有的 header 都 不支持 中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //所有的 params 都 支持 中文
        params.put("commonParamsKey2", "这里支持中文参数");

        //必须调用初始化
        OkHttpUtils.init(this);
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                //.setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
                //.setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);                                          //设置全局公共参数

        ToastUtils.init(this);
    }


    public static void setImage(Context context,ImageView image, String url){
        Glide.with(context).load(url).into(image);
    }

    /**
     * 添加活动到应用活动集合中
     *
     * @param activity 活动对象
     */
    public void addActivity(Activity activity) {
        if (null != activitys) {
            activitys.add(activity);
//            MyLog.log("正在添加：" + activity.getLocalClassName() + " 到Activitys中");
        }
    }

    /**
     * 结束活动集合中的一个对象
     *
     * @param activity
     */
    public void killActivity(Activity activity) {
        if (null != activitys && null != activity) {
            int position = activitys.indexOf(activity);
            if (position >= 0) {
                activitys.remove(position);
//                MyLog.log("正在删除：" + activity.getLocalClassName() + " 从Activitys中");
            }
            activity.finish();
        }
    }

    /**
     * 结束活动的Activity
     */
    public static void killAllActivitys() {
        if (null != activitys) {
            for (Activity activity : activitys) {
//                MyLog.log("正在结束：" + activity.getLocalClassName());
                activity.finish();
            }
            activitys.clear();
        }
    }

    /**
     * 结束最后一个活动之前的Activity
     */
    public static void killBeforeActivitys() {
        if (null != activitys) {
            int size = activitys.size() - 1;
            for (int i = 0; i < size; i++) {
                Activity activity = activitys.get(i);
//                MyLog.log("正在结束：" + activity.getLocalClassName());
                activity.finish();
            }
            System.out.println(activitys.size());
        }
    }

    /**
     * 获取队列中最后一个activity
     *
     * @return Activity
     */
    public static Activity getLastActivity() {
        Activity activity = null;
        if (null != activitys) {
            activity = activitys.getLast();
        }
        return activity;
    }

    /**
     * This method is for use in emulated process environments. It will never be called on a production Android device, where processes are removed by simply
     * killing them; no user code (including this callback) is executed when doing so.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onTerminate() {
        for (Activity activity : activitys) {
            if (activity != null) {
//                MyLog.log("APP退出时结束：" + activity.getLocalClassName());
                activity.finish();
            }
        }
        CallSystemUtil.exitSystem();
    }
}
