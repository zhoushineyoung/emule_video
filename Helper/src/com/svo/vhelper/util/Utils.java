package com.svo.vhelper.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;

import com.svo.library.util.RLSysUtil;

public class Utils {
	public static  boolean isAppInstalled(Context context, String packagename) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 打开链接
	 * @param activity
	 * @param link
	 */
	public static void open(Activity activity,String link) {
		Intent intent = new Intent("com.svo.v.playvideo");
		intent.setData(Uri.parse(link));
		activity.startActivity(intent);
	}
	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	/**
	 * 以友好的方式显示时间
	 * 
	 * @param time
	 *            毫秒数
	 * @return
	 */
	public static String friendly_time(long msTime) {
		if (msTime <= 0) {
			return "Unknown";
		}
		Date time = new Date(msTime);
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}
	public static String getEd2kName(String ed2k) {
		try {
			String[] ss = ed2k.split("\\|");
			return URLDecoder.decode(ss[2], "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 将字符串转成MD5值
	 * @param string
	 * @return
	 */
	public static String getMD5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
	public static String makeTimeString(long milliSecs) {
		StringBuffer sb = new StringBuffer();
		long h = milliSecs / (60 * 60 * 1000);
		if (h > 0) {
			sb.append(h < 10 ? "0" + h : h);
			sb.append(":");
		}
		long m = (milliSecs % (60 * 60 * 1000)) / (60 * 1000);
		sb.append(m < 10 ? "0" + m : m);
		sb.append(":");
		long s = (milliSecs % (60 * 1000)) / 1000;
		sb.append(s < 10 ? "0" + s : s);
		return sb.toString();
	}
	public static void callSysShare(Context context, String chooserTitle, String shareTitle, String shareText, String mime, Uri uri){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.putExtra(Intent.EXTRA_TEXT, shareText); 
        intent.putExtra(Intent.EXTRA_SUBJECT, shareTitle);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
        intent.setType(mime);
        if(uri!=null){
        	intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        context.startActivity(Intent.createChooser(intent,chooserTitle));
	}
	/**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
	// 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
 
    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
	public static String encode(String url) {
		if (!isChinese(url)) {
			return url;
		}
		try {
			return URLEncoder.encode(url, "UTF-8").replace("%2F","/").replace("%3A",":").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 构造URL
	 * @param path
	 * @param context
	 * @return
	 */
	public static String conUrl(String path,Context context) {
		path = path.replace("%2F","/").replace("%3A",":").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
		String token = context.getSharedPreferences("huang", 0).getString("token", "").trim();
		return "https://d.pcs.baidu.com/rest/2.0/pcs/file?method=download&access_token="+token+"&path="+path;
	}
	public static boolean is2Sdcard() {
		return RLSysUtil.getSDCardPaths().size()>1;
	}
	/**
	 * 得到空间比较大的sdcard根目录
	 * @return
	 */
	public static String getGen() {
		if (is2Sdcard()) {
			String gen1 = RLSysUtil.getSDCardPaths().get(0);
			String gen2 = RLSysUtil.getSDCardPaths().get(1);
			if (RLSysUtil.getAvailableSize(gen1) > RLSysUtil.getAvailableSize(gen2)) {
				return gen1;
			}else {
				return gen2;
			}
		}else {
			return RLSysUtil.getSDCardPaths().get(0);
		}
	}
	/**
	 * 获取当前的时间
	 * @return
	 */
	public static String getTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		int m = calendar.get(Calendar.MINUTE);
		StringBuilder sb = new StringBuilder();
		sb.append(h<10?"0"+h:h);
		sb.append(":");
		sb.append(m<10?"0"+m:m);
		return sb.toString();
	}
	public static  String getVideoName(String url) {
		try {
			if (url.startsWith("http://pcs.baidu")) {
				String path = url.substring(url.lastIndexOf("=")+1);
				path = URLDecoder.decode(path);
				return path.substring(path.lastIndexOf("/")+1);
			} else {
				String name = url.substring(url.lastIndexOf("/")+1);
				return URLDecoder.decode(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 将文件转换为KB或者M
	 * @param size
	 * @return
	 */
	public static String sizeConvert(long size) {
		if (size <= 1024) {
			return "1Kb";
		}else if (size > 1024 && size <= 1024*1024) {
			return size/1024+"Kb";
		}else if (size > 1024*1024 && size < 1024*1024*1024){
			DecimalFormat df = new DecimalFormat(".00");
			return df.format(size/1024/1024f)+"M";
		}else {
			DecimalFormat df = new DecimalFormat(".00");
			return df.format(size/1024/1024/1024f)+"G";
		}
	}
	public static String[] list2Arr(List<String> list) {
		String[] ss = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ss[i]=list.get(i);
		}
		return ss;
	}
	/**
	 * 根据链接获取下载的类型
	 * @param link
	 * @return magnet 4;ed2k 3;http 0;-1  未知类型
	 */
	public static String getType(String link) {
		if (link.startsWith("magnet:")) {
			return "4";
		} else if (link.startsWith("ed2k://")){
			return "3";
		}else if (link.startsWith("http:") || link.startsWith("https:")){
			return "0";
		}else {
			return "-1";
		}
	}
	/**
	 * 根据路径获得文件名
	 * @param path
	 * @return
	 */
	public static String getName(String path) {
		if (path.contains("/")) {
			return path.substring(path.lastIndexOf("/")+1);
		}else {
			return path;
		}
	}
}
