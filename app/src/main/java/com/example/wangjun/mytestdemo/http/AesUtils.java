package com.example.wangjun.mytestdemo.http;


import com.alibaba.fastjson.JSONObject;
import com.example.wangjun.mytestdemo.utils.Aes;
import com.example.wangjun.mytestdemo.utils.Md5;
import com.example.wangjun.mytestdemo.utils.PostServer;



/**
 * 加密请求类
 * @author 图灵机器人
 *
 */
public class AesUtils {
	

	public static  String  Aes(String secrets,String apikeys ,String name){
		//图灵网站上的secret
		String secret = secrets;
		//图灵网站上的apiKey
		String apiKey = apikeys;
		String cmd = name;//测试用例
		//待加密的json数据
		String data = "{\"key\":\""+apiKey+"\",\"info\":\""+cmd+"\"}";
		//获取时间戳
		String timestamp = String.valueOf(System.currentTimeMillis());
		//生成密钥
		String keyParam = secret+timestamp+apiKey;
		String key = Md5.MD5(keyParam);
		//加密
		Aes mc = new Aes(key);
		data = mc.encrypt(data);
		/*//封装请求参数
		JSONObject json = new JSONObject();
		json.put("key", apiKey);
		json.put("timestamp", timestamp);
		json.put("data", data);
		//请求图灵api
		String result = PostServer.SendPost(json.toString(), "http://www.tuling123.com/openapi/api");*/
		System.out.println(data+"");
		return data;
	}
	
}