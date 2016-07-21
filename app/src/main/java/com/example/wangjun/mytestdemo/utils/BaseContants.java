/**
 * 
 */
package com.example.wangjun.mytestdemo.utils;

/**
 *在这里描述类的用途，编写详细注释信息
 *
 * @author wangjun
 */

public class BaseContants {
	
	/**
	 * 
	 * 已知文件类型
	 */
	public interface FileType{
		
		/**
		 * 音频文件
		 */
		public final static String AUDIO = "audio";

		/**
		 * 视频文件
		 */
		public final static String VIDEO = "video";

		/**
		 * 图片文件
		 */
		public final static String PICTURE = "picture";

		/**
		 * APK安装文件
		 */
		public final static String APK = "apk";
		
	}
	
	/**
	 * 
	 * 系统消息WHAT值
	 */
	public interface SysMessageWhat{

		/**
		 * 文件下载完成
		 */
		public static final int DOWNLOAD_FILE_DONE = 999;
		
	}


}
