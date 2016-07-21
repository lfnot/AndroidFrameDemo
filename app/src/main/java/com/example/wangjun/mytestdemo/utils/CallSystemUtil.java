package com.example.wangjun.mytestdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.wangjun.mytestdemo.MyApplication;

import java.io.File;

/**
 * 调用系统应用工具类
 * 如：打电话、发短信、写邮件等等
 *
 */
public class CallSystemUtil {
    /**
     * 拨打指定号码的电话
     *
     * @param telNum 电话号码
     */
    public static void callTel(String telNum){
        if(StringUtil.notEmpty(telNum)){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("tel:" + telNum));
            MyApplication.application.startActivity(intent);
        }
    }

    /**
     * 调用系统默认发短信程序给指定号码发送短信
     * 若号码为空时，则默认收件人为空
     * @param telNum 电话号码
     * @param messageContent 短信内容
     */
    public static void callSendMsg(String telNum, String messageContent){
        Uri uri = Uri.parse("smsto:");
        if(StringUtil.notEmpty(telNum)){
            uri = Uri.parse("smsto:" + telNum);
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", messageContent);
        MyApplication.application.startActivity(intent);
    }

    /**
     * 调用发短信给指定号码发短信
     *
     * @param telNum 电话号码
     */
    public static void callSendMsg(String telNum){
        if(StringUtil.notEmpty(telNum)){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("smsto:" + telNum));
            MyApplication.application.startActivity(intent);
        }
    }

    /**
     * 调用邮件客户端发送邮件
     * @param emailRecivers 收件人
     * @param emailSubject 主题
     * @param emailBody 内容
     */
    public static void callSendMail(String[] emailRecivers, String emailSubject, String emailBody){

        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/plain");
        //设置邮件默认地址
        email.putExtra(Intent.EXTRA_EMAIL, emailRecivers);
        //设置邮件默认标题
        email.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        //设置要默认发送的内容
        email.putExtra(Intent.EXTRA_TEXT, emailBody);
        Intent chooserIntent = Intent.createChooser(email, "请选择邮件发送软件");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //调用系统的邮件系统
        MyApplication.application.startActivity(chooserIntent);
    }

    /**
     * 调用系统分享
     * @param shareMsg 分享内容
     */
    public static void callSendShareMsg(String shareMsg){

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        //设置要默认发送的内容
        share.putExtra(Intent.EXTRA_TEXT, shareMsg);
        Intent chooserIntent = Intent.createChooser(share, "请选择分享方式");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //调用系统的邮件系统
        MyApplication.application.startActivity(chooserIntent);
    }

    /**
     * 启动本地浏览器访问指定的网络地址
     * @param httpUrl 网络地址
     */
    public static void launchBrowser(String httpUrl){
        if(StringUtil.notEmpty(httpUrl)){
            Uri uriUrl = Uri.parse(httpUrl);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            MyApplication.application.startActivity(launchBrowser);
        }
    }


    /**
     * 初装程序
     * @param apkFile
     */
    public static void installApk(String apkFileName) {
        if(StringUtil.isEmpty(apkFileName)){
            MyLog.log("安装程序文件名为空");
            return;
        }
        try {
            File apkFile = new File(apkFileName);
            Uri uri = Uri.fromFile(apkFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            MyApplication.application.startActivity(intent);
        } catch (Exception e) {
            MyLog.log("安装文件不存在或有错误已停止执行");
            showToast("安装文件不存在或有错误已停止执行");
        }
    }

    /**
     * 显示系统级简消息
     * @param msg 显示消息内容
     */
    public static void showToast(String msg){
        Toast.makeText(MyApplication.application, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 隐藏输入法面板
     * @param activity
     */
    public static void hideInputMethod(Activity activity) {
        if(null == activity){
            return;
        }
        if(null != activity.getCurrentFocus() && null != activity.getCurrentFocus().getWindowToken()){
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 退出当前应用程序
     */
    public static void exitSystem(){
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 将当前应用程序转到后台运行
     */
    public static void appIntoBackgroud(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }


}
