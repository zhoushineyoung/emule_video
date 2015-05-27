package com.svo.library.util;

import android.content.Context;
import android.util.Log;

import com.svo.library.common.persistence.http.AsyncHttpClient;
import com.svo.library.common.persistence.http.AsyncHttpResponseHandler;
import com.svo.library.common.persistence.http.BinaryHttpResponseHandler;
import com.svo.library.common.persistence.http.JsonHttpResponseHandler;
import com.svo.library.common.persistence.http.PersistentCookieStore;
import com.svo.library.common.persistence.http.RequestParams;

/**
 * 
 * 类名:HttpUtil
 * 
 * 陕西朗恩科技信息有限公司
 * 
 * 标准版
 * 
 * 功能说明:网络请求工具类
 * 
 * @author 李悝
 * 
 * 2014-10-30 下午15:37:12
 * 
 */
public class HttpUtil {
	private static final String TAG = "http";
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象

	public static void get(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler paramAsyncHttpResponseHandler) {
		PersistentCookieStore localPersistentCookieStore = new PersistentCookieStore(context);
		params.put("version", "android."+RLSysUtil.getVersion(context));
		client.setCookieStore(localPersistentCookieStore);
		client.get(url, params, paramAsyncHttpResponseHandler);
		Log.i(TAG, "get,url:"+url.toString());
		Log.i(TAG, "get,params:"+params.toString());
	}

	public static void get(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res) // url里面带参数
	{
		client.get(urlString, params, res);
	}

	public static void get(String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
	{
		client.get(urlString, params, res);
	}

	public static void get(String uString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
	{
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
	//上传文件专用
	public static void postFile(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler paramAsyncHttpResponseHandler) {
		PersistentCookieStore localPersistentCookieStore = new PersistentCookieStore(context);
		params.put("version", "android."+RLSysUtil.getVersion(context));
		client.setTimeout(180*1000);
		client.setCookieStore(localPersistentCookieStore);
		client.post(url, params, paramAsyncHttpResponseHandler);
		Log.i(TAG, "postfile,url:"+url.toString());
		Log.i(TAG, "postfile,params:"+params.toString());
	}
	public static void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler paramAsyncHttpResponseHandler) {
		PersistentCookieStore localPersistentCookieStore = new PersistentCookieStore(context);
		params.put("version", "android."+RLSysUtil.getVersion(context));
		client.setTimeout(10*1000);
		client.setCookieStore(localPersistentCookieStore);
		client.post(url, params, paramAsyncHttpResponseHandler);
		Log.i(TAG, "post,url:"+url.toString());
		Log.i(TAG, "post,params:"+params.toString());
	}
}