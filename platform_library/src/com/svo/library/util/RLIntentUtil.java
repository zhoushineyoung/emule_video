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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
/**
 * Intent工具类
 * @author 杜卫宾<br><br>
 *
 */
public class RLIntentUtil {
	/**
	 * 获得相机Intent
	 * @param outputFile
	 * @return
	 */
	public static Intent getCameraIntent(String outputFile){
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputFile)));
		return intent;
	}
	
	/**
	 * 获得图库Intent
	 * @return
	 */
	public static Intent getGalleryIntent(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
	}
	
	/**
	 * 得到分享的Intent
	 * @param context
	 * @param chooserTitle
	 * @param shareTitle
	 * @param shareText
	 * @param mime
	 * @param uri 可为null
	 */
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
	 * 调用某个安卓市场去搜索某个APP
	 * @param context
	 * @return
	 */
	public static Intent getSysAppSearchIntent(String key){
		return new Intent(Intent.ACTION_VIEW,
       		 Uri.parse("market://search?q="+key));
	}
	
	/**
	 * 调用某个安卓市场去搜索某个APP的详细信息
	 * @param context
	 * @return
	 */
	public static Intent getSysAppDetailIntent(Context context){
		return new Intent(Intent.ACTION_VIEW,
       		 Uri.parse("market://details?id="+context.getPackageName()));
	}
}