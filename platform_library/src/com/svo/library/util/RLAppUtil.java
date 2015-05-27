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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.svo.library.R;
import com.svo.library.entity.RLAppInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
/**
 * 
 * @author 杜卫宾<br><br>
 *	应用程序工具类<br>
 */
public class RLAppUtil {
	
	public interface Listener{
		public void onReceived(ArrayList<RLAppInfo> list);
	}
	
	/**
	 * 获取安装程序列表
	 * @param context
	 * @param listener 监听类
	 * @return
	 */
	public static void getInstalledAppList(final Context context, final Listener listener){
		new AsyncTask<Object,Object,ArrayList<RLAppInfo>>(){
			@Override
			protected ArrayList<RLAppInfo> doInBackground(Object... params) {
				PackageManager pManager=context.getPackageManager();
				ArrayList<RLAppInfo> appList = new ArrayList<RLAppInfo>();
				List<PackageInfo> packages = pManager.getInstalledPackages(0);
				for(int i=0;i<packages.size();i++) { 
			        PackageInfo pInfo = packages.get(i); 
			        RLAppInfo app = new RLAppInfo();  
			        app.setPackageName(pInfo.packageName); 
			        app.setVersionName(pInfo.versionName); 
			        app.setVersionCode(pInfo.versionCode); 
			        app.setAppName(pInfo.applicationInfo.loadLabel(pManager).toString());
			        app.setAppIcon(pInfo.applicationInfo.loadIcon(pManager));
			        if((pInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
			        	app.setSys(false);
			        }else{
			        	app.setSys(true);
			        }
			        app.setDataDir(pInfo.applicationInfo.dataDir);
			        app.setTargetSdkVersion(pInfo.applicationInfo.targetSdkVersion);
			        String os=null;
			        switch(app.getTargetSdkVersion()){
			        case 1:
			        	os="Android 1.0(BASE)";
			        	break;
			        case 2:
			        	os="Android 1.1(BASE_1_1)";
			        	break;
			        case 3:
			        	os="Android 1.5(CUPCAKE)";
			        	break;
			        case 4:
			        	os="Android 1.6(DONUT)";
			        	break;
			        case 5:
			        	os="Android 2.0(ECLAIR)";
			        	break;
			        case 6:
			        	os="Android 2.0.1(ECLAIR_0_1)";
			        	break;
			        case 7:
			        	os="Android 2.1(ECLAIR_MR1)";
			        	break;
			        case 8:
			        	os="Android 2.2(FROYO)";
			        	break;
			        case 9:
			        	os="Android 2.3(GINGERBREAD)";
			        	break;
			        case 10:
			        	os="Android 2.3.3(GINGERBREAD_MR1)";
			        	break;
			        case 11:
			        	os="Android 3.0(HONEYCOMB)";
			        	break;
			        case 12:
			        	os="Android 3.1(HONEYCOMB_MR1)";
			        	break;
			        case 13:
			        	os="Android 3.2(HONEYCOMB_MR2)";
			        	break;
			        case 14:
			        	os="Android 4.0(ICE_CREAM_SANDWICH)";
			        	break;
			        case 15:
			        	os="Android 4.0.3(ICE_CREAM_SANDWICH_MR1)";
			        	break;
			        case 16:
			        	os="Android 4.1(JELLY_BEAN)";
			        	break;
			        case 17:
			        	os="Android 4.2(JELLY_BEAN_MR1)";
			        	break;
			        }
			        app.setTargetOsVersion(os);
			        app.setProcessName(pInfo.applicationInfo.processName);
			        app.setPublicSourceDir(pInfo.applicationInfo.publicSourceDir);
			        app.setNativeLibDir(pInfo.applicationInfo.nativeLibraryDir);
			        app.setFirstInstallTime(pInfo.firstInstallTime);
			        app.setLastUpdateTime(pInfo.lastUpdateTime);
			        appList.add(app);
			    }
				return appList;
			}
			@Override
			public void onPostExecute(ArrayList<RLAppInfo> result){
				super.onPostExecute(result);
				listener.onReceived(result);
			}
		}.execute();
	}
	
	/**
	 * 打开某应用程序
	 * @param context
	 * @param packageName
	 */
	@SuppressLint("InlinedApi")
	public static void openSys(Context context,String packageName){
		Intent intent = new Intent();  
	    final int apiLevel = Build.VERSION.SDK_INT;  
	    if (apiLevel >= 9) {
	        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);   
	        intent.setData(Uri.fromParts("package", packageName, null));  
	    } else {  
	        intent.setAction(Intent.ACTION_VIEW);  
	        intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");  
	        String appPkgName=(apiLevel==8?"pkg":"com.android.settings.ApplicationPkgName");
	        intent.putExtra(appPkgName, packageName);  
	    }  
	    context.startActivity(intent);  
	}
	
	/**
	 * 卸载某个应用程序
	 * @param context
	 * @param packageName 包名
	 */
	public static void uninstall(Context context,String packageName){
		Uri uri = Uri.fromParts("package", packageName, null);
		Intent it = new Intent(Intent.ACTION_DELETE, uri);
		context.startActivity(it);
	}

	/**
	 * 清除某应用程序的缓存文件
	 * @param context
	 * @param packageName 包名
	 */
	public static void clearCache(Context context,String packageName){
		Context pContext=null;
		try {
			pContext = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(pContext==null){
			return;
		}
		if(pContext.getCacheDir()!=null){
			File root1 = pContext.getCacheDir();
			if(root1.exists()){
				for(String dir:root1.list()){
					RLFileUtil.deleteFile(new File(root1, dir));
				}
				RLUiUtil.toast(context, R.string.inner_cache_cleared);
	        }else{
	        	RLUiUtil.toast(pContext, R.string.request_root_failed);
	        }
		}
		String path2=null;
		if(RLSysUtil.isExternalStorageAvailable()
				&&(path2=RLSysUtil.getExternalCacheDir(pContext))!=null){
			File root2 = new File(path2);
			if(root2.exists()){
				for(String dir:root2.list()){
					RLFileUtil.deleteFile(new File(root2, dir));
				}
				RLUiUtil.toast(context, R.string.external_cache_cleared);
	        }else{
	        	RLUiUtil.toast(pContext, R.string.request_root_failed);
	        }
		}
	}
	
	/**
	 * 清除某应用程序的缓存文件
	 * @param context
	 * @param packageName 包名
	 */
	public static void clearData(Context context,String packageName){
		Context pContext=null;
		try {
			pContext = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(pContext==null){
			return;
		}
		if(pContext.getCacheDir()!=null&&pContext.getCacheDir().getParent()!=null){
			String path1=pContext.getCacheDir().getParent();
			File root1 = new File(path1);
			if(root1.exists()){
				for(String dir:root1.list()){
					RLFileUtil.deleteFile(new File(root1, dir));
				}
				RLUiUtil.toast(context, R.string.inner_data_cleared);
	        }else{
	        	RLUiUtil.toast(pContext, R.string.request_root_failed);
	        }
		}
		String path2=null;
		if(RLSysUtil.isExternalStorageAvailable()
				&&(path2=RLSysUtil.getExternalCacheDir(pContext))!=null&&new File(path2).getParent()!=null){
			File root2 = new File(new File(path2).getParent());
			if(root2.exists()){
				for(String dir:root2.list()){
					RLFileUtil.deleteFile(new File(root2, dir));
				}
				RLUiUtil.toast(context, R.string.external_data_cleared);
	        }else{
	        	RLUiUtil.toast(pContext, R.string.request_root_failed);
	        }
		}
	}
	
	/**
	 * 启动某个应用程序
	 * @param context 上下文
	 * @param packageName 包名
	 */
	public static void launch(Context context,String packageName){
		PackageManager pManager=context.getPackageManager();
		Intent intent=pManager.getLaunchIntentForPackage(packageName);
		if(intent!=null){
			context.startActivity(intent);
		}else{
			RLUiUtil.toast(context, R.string.launch_app_failed);
		}
	}
}