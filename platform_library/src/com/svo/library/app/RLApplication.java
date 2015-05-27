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
import android.app.Application;
import android.content.res.Configuration;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.svo.library.common.persistence.http.AsyncHttpClient;
import com.svo.library.entity.RLDisplayInfo;
import com.svo.library.util.RLSysUtil;
/**
 * 快速开发平台 (http://1.playandroid.duapp.com/).
 * @author 杜卫宾
 * <br>应用程序入口。推送初始化。图片加载初始化。网络请求初始化<br><br>
 * 使用：项目中的activity继承于RLActivity
 */
public class RLApplication extends Application{
	private RLDisplayInfo displayInfo;
	public ImageLoader imgLoader;
	public DisplayImageOptions defaultDisplayImageOptions;
	public AsyncHttpClient httpClient;
	
	@Override
	public void onCreate(){
		super.onCreate();
		String enablePush=RLSysUtil.getApplicationMetaData(this, "ENABLE_PUSH");
		initImageCache();
		initHttpClient();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onLowMemory(){
		super.onLowMemory();
	}
	
	@Override
	public void onTerminate(){
		super.onTerminate();
	}

	public RLDisplayInfo getDisplayInfo() {
		return displayInfo;
	}

	public void setDisplayInfo(RLDisplayInfo displayInfo) {
		this.displayInfo = displayInfo;
	}
	
	private void initImageCache(){//TODO
		ImageLoader imageLoader = ImageLoader.getInstance();
		if (!imageLoader.isInited()) {
			ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(getApplicationContext());
			imageLoader.init(config);
		}
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.displayer(new FadeInBitmapDisplayer(1200));
	}
	
	private void initHttpClient(){//TODO
		httpClient = new AsyncHttpClient();
    	httpClient.addHeader("Connection", "Close");
    	httpClient.addHeader("Accept", "*/*"); 
    	httpClient.setTimeout(1000*60);
	}
}