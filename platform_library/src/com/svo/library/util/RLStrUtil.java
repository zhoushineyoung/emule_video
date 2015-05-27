/**
 * Copyright (c) 2013-2014, 杜卫宾 快速开发平台(http://1.playandroid.duapp.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.svo.library.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
/**
 * 字符串操作工具类
 * @author 杜卫宾<br><br>
 *
 */
public class RLStrUtil {
	private static final String URL_REG_EXPRESSION = "^(https?://)?([a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+)+(/*[A-Za-z0-9/\\-_&:?\\+=//.%]*)*";
	private static final String EMAIL_REG_EXPRESSION = "^[\\w-]+(\\.[\\w-]+)*@([a-z0-9-]+(\\.[a-z0-9-]+)*?\\.[a-z]{2,6}|(\\d{1,3}\\.){3}\\d{1,3})(:\\d{4})?$";
	/**
     * 根据一个文件的绝对路径得到文件所在的目录路径和文件名
     * @param filePath 某文件的绝对路径
     * @return 长度为2的数组,第一个元素是目录路径,第二个元素是文件名.可能为null
     */
    public  static String[] sepPath(String filePath) {
    	if (filePath == null || !filePath.contains("/") || (filePath.lastIndexOf("/") == (filePath.length() - 1))) {
			return null;
		}
    	int index = filePath.lastIndexOf("/")+1;
    	String[] arr = new String[2];
    	arr[0] = filePath.substring(0,index);
    	arr[1] = filePath.substring(index);
    	return arr;
	}
    
	
	/**
	 * 获得应用程序名字
	 * @param context
	 * @return
	 */
	public static String getApplicationName(Context context) { 
        PackageManager packageManager = null; 
        ApplicationInfo applicationInfo = null; 
        try { 
            packageManager = context.getPackageManager(); 
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0); 
        } catch (PackageManager.NameNotFoundException e) { 
            applicationInfo = null; 
        } 
        String applicationName =  
        (String) packageManager.getApplicationLabel(applicationInfo); 
        return applicationName; 
    } 
	/**
	 * 获得url中键值对
	 * @param url
	 * @return
	 */
	public static HashMap<String, String> paserParams(String url) {
		String params = url.substring(url.indexOf("?")+1);
		String[] arr = params.split("&");
		HashMap<String, String> maps = new HashMap<String, String>();
		for (String s : arr) {
			maps.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=")+1));
		}
		return maps;
	}
	/**
	 * 判断是否是url链接地址
	 * @param s
	 * @return
	 */
	public static boolean isUrl(String s) {
		if (s == null) {
			return false;
		}
		return Pattern.matches(URL_REG_EXPRESSION, s);
	}
	
	/**
	 * 判断是否是email格式的字符串
	 * @param s
	 * @return
	 */
	public static boolean isEmail(String s) {
		if (TextUtils.isEmpty(s)) {
			return false;
		}
		return Pattern.matches(EMAIL_REG_EXPRESSION, s);
	}
	/**
	 * 换算时间,换算为日期格式,如:2012-10-29,昨天,前天,1分钟前等等
	 * @param t 长整型时间
	 */
	public static String formatDate(long t) {
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String systemTime = sdf.format(new Date()).toString();
		String time = sdf.format(t);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		String yesterday = sdf.format(c.getTime()).toString();
		c.add(Calendar.DAY_OF_MONTH, -1);
		String yesterday_before = sdf.format(c.getTime()).toString();

		try {
			java.util.Date begin = sdf.parse(time);
			java.util.Date end = sdf.parse(systemTime);
			long between = (end.getTime() - begin.getTime()) / (1000 * 60);
			if (time.substring(0, 10).equals(yesterday.substring(0, 10))) {
				strDate = "昨天";
			} else if (time.substring(0, 10).equals(
					yesterday_before.substring(0, 10))) {
				strDate = "前天";
			} else if (between <= 0) {
				strDate = "1分钟前";
			} else if (between < 60 && between > 0) {
				strDate = Math.round(between) + "分钟前";
			} else if (between >= 60 && between < 60 * 24) {
				strDate = Math.round(between / 60) + "小时前";
			} else {
				strDate = time.substring(0, 10);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strDate;
	}
	
	/**
	 * 将长整型毫秒数转换为日期格式yyyy-MM-dd
	 * @param t 毫秒数
	 * @return
	 */
	public static String format2Date(long t) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(t);
		return time;
	}
	/**
	 * 判断空白字符串
	 * @param s
	 * @return
	 */
	public static boolean isBlank(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches("\\s*", s);
	}
}