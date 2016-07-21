package com.example.wangjun.mytestdemo.utils;

import android.util.Log;

/**
 * 自定义日志记录器，系统内所有日志记录需使用此类
 */
public class MyLog {

	/**
	 * 创建日志记录TAG标记
	 * @param className 执行日志记录的类名称
	 * @return
	 */
	public static String createTag(String className){
//		StringBuffer sb = new StringBuffer();
//		sb.append(AppEnvironmentConfig.getPackageName());
//		sb.append(" --> ");
//		sb.append(className);
//		return sb.toString();
		return className;
	}

	/**
	 * 公共日志输出
	 * @param msg 消息
	 */
	public static void log(String msg){
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
			MyLog.e(AppEnvironmentConfig.getPackageName(), msg);
		}
	}

	/**
	 * 公共日志输出
	 * @param tag 日志标签
	 * @param msg 消息
	 */
	public static void log(String TAG, String msg){
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
			MyLog.e(AppEnvironmentConfig.getPackageName(), msg);
		}
	}

	/**
	 * 输出详细信息
	 * @param tag 标记符
	 * @param msg 信息
	 */
	public static void v(String tag, String msg) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
//    		Log.v(tag, msg);
			Log.e(tag, msg);
		}
	}

	/**
	 * 输出详细信息
	 * @param tag 标记符
	 * @param msg 信息
	 * @param tr 抛出的异常信息
	 */
	public static void v(String tag, String msg, Throwable tr) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
//    		Log.v(tag, msg, tr);
			Log.e(tag, msg, tr);
		}
	}

	/**
	 * 输出调试信息
	 * @param tag 标记符
	 * @param msg 信息
	 */
	public static void d(String tag, String msg) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
//    		Log.d(tag, msg);
			Log.e(tag, msg);
		}
	}

	/**
	 * 输出调试信息
	 * @param tag 标记符
	 * @param msg 信息
	 * @param tr 抛出的异常信息
	 */
	public static void d(String tag, String msg, Throwable tr) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
//    		Log.d(tag, msg, tr);
			Log.d(tag, msg, tr);
		}
	}

	/**
	 * 输出正常信息
	 * @param tag 标记符
	 * @param msg 信息
	 */
	public static void i(String tag, String msg) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
//    		Log.i(tag, msg);
			Log.e(tag, msg);
		}
	}

	/**
	 * 输出正常信息
	 * @param tag 标记符
	 * @param msg 信息
	 * @param tr 抛出的异常信息
	 */
	public static void i(String tag, String msg, Throwable tr) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
//    		Log.i(tag, msg, tr);
			Log.e(tag, msg, tr);
		}
	}

	/**
	 * 输出警告信息
	 * @param tag 标记符
	 * @param msg 信息
	 */
	public static void w(String tag, String msg) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
//    		Log.w(tag, msg);
			Log.e(tag, msg);
		}
	}

	/**
	 * 输出警告信息
	 * @param tag 标记符
	 * @param msg 信息
	 * @param tr 抛出的异常信息
	 */
	public static void w(String tag, String msg, Throwable tr) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
//    		Log.w(tag, msg, tr);
			Log.e(tag, msg, tr);
		}
	}

	/**
	 * 输出错误信息
	 * @param tag 标记符
	 * @param msg 信息
	 */
	public static void e(String tag, String msg) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
			Log.e(tag, msg);
		}
	}

	/**
	 * 输出错误信息
	 * @param tag 标记符
	 * @param msg 信息
	 * @param tr 抛出的异常信息
	 */
	public static void e(String tag, String msg, Throwable tr) {
		if(AppEnvironmentConfig.isWriteLog && StringUtil.notEmpty(msg)){
			Log.e(tag, msg, tr);
		}
	}

}