package com.example.wangjun.mytestdemo.http;

import android.text.TextUtils;

import com.example.wangjun.mytestdemo.utils.MyLog;
import com.example.wangjun.mytestdemo.utils.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.AbsCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * 当前类注释：子线程处理事件
 * Author: LeonWang
 * Time: 2016/7/29.23:35
 * E-mail:lijiawangjun@gmail.com
 * Description：这里的返回码是根据聚合数据来的   具体的可根据业务来调整
 */
public abstract class HttpCallback<T> extends AbsCallback<T> {

    private Class<T> clazz;
    private Type type;

    public  HttpCallback(Class<T> clazz){
        this.clazz = clazz;
    }

    public HttpCallback(Type type) {
        this.type = type;
    }

    @Override
    public T parseNetworkResponse(Response response) throws Exception {
        MyLog.d("TAG","子线程");
        String responseData = response.body().string();
        if (TextUtils.isEmpty(responseData)) return null;
        JSONObject jsonObject = new JSONObject(responseData);
        final int error_code = jsonObject.optInt("error_code", 0);
        final String reason = jsonObject.optString("reason", "");
        String result = jsonObject.optString("result", "");
        if (error_code == 0){
            if (clazz == String.class) return (T) reason;
            if (clazz != null) return new Gson().fromJson(result, clazz);
            if (type != null) return new Gson().fromJson(result, type);
        }else {
            throw new IllegalStateException("错误代码：" + error_code + "，错误信息：" + reason);

        }
        //如果要更新UI，需要使用handler，可以如下方式实现，也可以自己写handler
        OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.getInstance().showToast(error_code+reason);
            }
        });
        return null;
    }


}
