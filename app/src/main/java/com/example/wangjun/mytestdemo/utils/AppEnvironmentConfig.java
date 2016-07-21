package com.example.wangjun.mytestdemo.utils;

import android.os.Environment;

import com.example.wangjun.mytestdemo.MyApplication;

/**
 * Created by wangjun on 2016/7/20.
 */
public class AppEnvironmentConfig {

    /**
     * 是否启动系统日志记录,启用：true;停用：false;
     */
    public final static boolean isWriteLog = true;

    /**
     * 返回当前应用的包名称
     * @return
     */
    public static String getPackageName() {
        if (MyApplication.application == null) {
            return "";
        }
        return MyApplication.application.getPackageName();
    }

    /**
     * 是否存在SD卡
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
