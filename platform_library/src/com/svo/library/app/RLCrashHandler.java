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
package com.svo.library.app;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
/**
 * 
 * @author 杜卫宾
 * <br>程序崩溃的处理类。默认不会弹出对话框
 * <br>在清单文件里配置即可.配置如下：
 * <meta-data 
		    android:name="ENABLE_CRASH_HANDLER"
		    android:value="true"/>
 */
public class RLCrashHandler implements UncaughtExceptionHandler {
	private Context mContext;
	private static RLCrashHandler instance;
	
	/**
	 * @return
	 */
	public static RLCrashHandler getInstance() {
		if(instance==null){
			instance=new RLCrashHandler();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		ex.printStackTrace();
		System.err.println(ex.getMessage());
		/*
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
//		Intent intent=new Intent(Intent.ACTION_MAIN);
//		intent.addCategory(Intent.CATEGORY_HOME);
//		mContext.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
//		((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE))
//    		.killBackgroundProcesses(mContext.getPackageName());
	}
}