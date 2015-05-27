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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;

import com.svo.library.entity.RLDisplayInfo;
/**
 * 系统相关工具类
 * @author 杜卫宾<br><br>
 *
 */
public class RLSysUtil {
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
	    try {
	        PackageManager manager = context.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	        String version = info.versionName;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "1.0";
	    }
	}

	public static String getDevId(Context context) {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}
	
	/**
	 * 浏览某种类型的文件
	 * @param context
	 * @param files 待浏览的文件夹
	 * @param mimeTypes 文件类型
	 */
	public static void scanMediaFiles(final Context context, String[] files, String[] mimeTypes){
		MediaScannerConnection.scanFile(context, files, mimeTypes, new OnScanCompletedListener(){
			@Override
			public void onScanCompleted(String arg0, Uri arg1) {
				Log.i("weitu", "arg0:"+arg0+";arg1:"+arg1);
			}
		});
    }
	
	/**
	 * 得到应用程序的元数据
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getApplicationMetaData(Context context, String key){
		String data=null;
		ApplicationInfo info=null;
		try {
			info=context.getPackageManager().getApplicationInfo(context.getPackageName(), 
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(info!=null && info.metaData!= null){
			try {
				data=info.metaData.get(key).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	/**
	 * 得到某activity的元数据
	 * @param activity
	 * @param key
	 * @return
	 */
	public static String getActivityMetaData(Activity activity, String key){
		String data=null;
		ActivityInfo info=null;
		try {
			info=activity.getPackageManager().getActivityInfo(activity.getComponentName(),
			        PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(info!=null){
			data=info.metaData.get(key).toString();
		}
		return data;
	}
	
	/**
	 * 得到service的元数据
	 * @param context
	 * @param serviceClass
	 * @param key
	 * @return
	 */
	public static String getServiceMetaData(Context context, Class<?> serviceClass, String key){
		String data=null;
		ServiceInfo info=null;
		try {
			info=context.getPackageManager().getServiceInfo(
					new ComponentName(context, serviceClass), 
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(info!=null){
			data=info.metaData.get(key).toString();
		}
		return data;
	}
	
	/**
	 * 得到广播的元数据
	 * @param context
	 * @param receiverClass
	 * @param key
	 * @return
	 */
	public static String getReceiverMetaData(Context context, Class<?> receiverClass, String key){
		String data=null;
		ActivityInfo info=null;
		try {
			info=context.getPackageManager().getReceiverInfo(
					new ComponentName(context, receiverClass), 
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(info!=null){
			data=info.metaData.get(key).toString();
		}
		return data;
	}
	
	/**
	 * 得到手机的宽度高度像素等信息
	 * @param activity
	 * @return
	 */
	public static RLDisplayInfo getDisplayInfo(final Activity activity){
		final Resources res=activity.getResources();
		final RLDisplayInfo di=new RLDisplayInfo();
		Display display=activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics dm=res.getDisplayMetrics();
		display.getMetrics(dm);
		di.setDisplayWidth(dm.widthPixels);
		di.setDisplayHeight(dm.heightPixels);
		di.setDisplayDensity(dm.density);
		int statusResId=res.getIdentifier("status_bar_height","dimen","android");
		if(statusResId>0){
			di.setStatusBarHeight(res.getDimensionPixelSize(statusResId));
		}
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			int portraitResId=res.getIdentifier("navigation_bar_height","dimen","android");
			if(portraitResId>0){
				di.setPortraitNavigationBarHeight(res.getDimensionPixelSize(portraitResId));
			}
		}else{
			new Thread(){
				public void run(){
					try {
						Thread.sleep(200);
					}catch(Exception e){
						e.printStackTrace();
					} 
			        Window window=activity.getWindow();  
			        int contentViewTop=window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
			        int titleBarHeight=contentViewTop - di.getStatusBarHeight();
			        Configuration config=res.getConfiguration();
			        if(config.orientation == Configuration.ORIENTATION_PORTRAIT){ 
			        	di.setPortraitNavigationBarHeight(titleBarHeight); 
			        }else if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
			        	di.setLandscapeNavigationBarHeight(titleBarHeight); 
			        }
				}
	    	}.start();
		}
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
			int landscapeResId=res.getIdentifier("navigation_bar_height_landscape","dimen","android");
			if(landscapeResId>0){
				di.setLandscapeNavigationBarHeight(res.getDimensionPixelSize(landscapeResId));
			}
		}else{
			//TODO
		}
		return di;
	}
	
	/**
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dip2px(Context context, float dp){ 
		final float scale=context.getResources().getDisplayMetrics().density;
		return(int)(dp*scale+0.5f);
	}
	
	/**
	 * 
	 * @param context
	 * @param px
	 * @return
	 */
	public static int px2dip(Context context, float px){
		final float scale=context.getResources().getDisplayMetrics().density;
		return(int)(px/scale+0.5f);
	}
	
	/**
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue){
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;  
		return (int) (spValue*scale + 0.5f);
	}
	
	/**
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;  
		return (int) (pxValue/scale + 0.5f);
	}
	
	/**
	 * 
	 * @param context
	 */
	public static void notifyScanMediaFiles(Context context){  
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
        		Uri.parse("file://"+Environment.getExternalStorageDirectory())));  
    }
	
	/**
	 * 内存卡是否可用
	 * @return
	 */
	public static boolean isExternalStorageAvailable(){
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				||!isExternalStorageRemovable();
	}
	
	/**
	 * 
	 * @return
	 */
    static boolean isExternalStorageRemovable(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD){
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }
    
    /**
     * 得到可用的外存储空间
     * @return
     */
	public static long getAvailableExternalSpace(){
		File path=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		StatFs stat=new StatFs(path.getPath());
		long blockSize=stat.getBlockSize();
		long availableBlocks=stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 得到可用的内存储空间
	 * @return
	 */
	public static long getAvailableInnerSpace(){
		File path=Environment.getDataDirectory();
		StatFs stat=new StatFs(path.getPath());
		long blockSize=stat.getBlockSize();
		long availableBlocks=stat.getAvailableBlocks();
		long realSize=blockSize * availableBlocks;
		return realSize;
	}
    
    /**
     * 得到外存储的缓存目录
     * @param context
     * @return
     */
    public static String getExternalCacheDir(Context context){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO){
			File f1=context.getExternalCacheDir();
			if(f1!=null){
				return f1.getPath();
			}else{
				return null;
			}
		}else{
			final String cacheDir="/Android/data/"+context.getPackageName()+"/cache/";
			File f2=Environment.getExternalStorageDirectory();
			if(f2!=null){
				return f2.getPath() + cacheDir;
			}else{
				return null;
			}
		}
	}
    
	
	/**
	 * 查看所有的sd路径
	 * @return
	 */
	public static  List<String> getSDCardPaths() {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;

				if (line.contains("fat")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						list.add(columns[1]);
					}
				} else if (line.contains("fuse")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						list.add(columns[1]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return list;
	}
	
	// 获取当前路径，可用空间
	public static long getAvailableSize(String path) {
		try {
			File base = new File(path);
			StatFs stat = new StatFs(base.getPath());
			long nAvailableCount = stat.getBlockSize()
					* ((long) stat.getAvailableBlocks());
			return nAvailableCount;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}