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

import android.content.Context;
import android.widget.Toast;
/**
 * Toast帮助类
 * @author 杜卫宾<br><br>
 *
 */
public class RLUiUtil {
	static Toast toast;
	/**
	 * @param context
	 * @param msg 显示内容
	 * @param duration 时间长短
	 */
	public static void toast(Context context, String msg, int duration){
		if (toast == null) {
			toast = Toast.makeText(context, msg, duration);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
	/**
	 * 短吐丝
	 * @param context
	 * @param msg 显示内容
	 */
	public static void toast(Context context, String msg){
		toast(context, msg, Toast.LENGTH_SHORT);
	}
	/**
	 * 
	 * @param context
	 * @param strResId 字符串ID
	 * @param duration
	 */
	public static void toast(Context context, int strResId, int duration){
		toast(context,context.getString(strResId),duration);
	}
	/**
	 * 
	 * @param context
	 * @param strResId 字符串ID
	 */
	public static void toast(Context context, int strResId){
		toast(context,context.getString(strResId));
	}
}