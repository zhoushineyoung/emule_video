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
package com.svo.library.widget.dialog;

import com.svo.library.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RLAlertDialog extends RLDialog{
	private Listener listener;
	private String title, msg;
	private Context context;
	private Button btn_left, btn_right;
	private TextView tv_msg;
	private String leftBtnStr, rightBtnStr;
	private int linkify=-1;
	
	/**
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param leftBtnStr
	 * @param rightBtnStr
	 * @param listener
	 */
	public RLAlertDialog(Context context, String title, String msg, String leftBtnStr, String rightBtnStr, Listener listener) {
		super(context);
		this.title=title;
		this.msg=msg;
		this.leftBtnStr=leftBtnStr;
		this.rightBtnStr=rightBtnStr;
		this.listener=listener;
		this.context=context;
		super.createView();
		super.setCanceledOnTouchOutside(false);
	}
	
	/**
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param msgLinkify
	 * @param leftBtnStr
	 * @param rightBtnStr
	 * @param listener
	 */
	public RLAlertDialog(Context context, String title, String msg, int msgLinkify, 
			String leftBtnStr, String rightBtnStr, Listener listener) {
		super(context);
		this.title=title;
		this.msg=msg;
		this.linkify=msgLinkify;
		this.leftBtnStr=leftBtnStr;
		this.rightBtnStr=rightBtnStr;
		this.listener=listener;
		this.context=context;
		super.createView();
		super.setCanceledOnTouchOutside(false);
	}
	
	@Override
	protected View getView() {
		View view=LayoutInflater.from(context).inflate(R.layout.dialog_alert, null);
		TextView tv_title=(TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		tv_msg=(TextView) view.findViewById(R.id.tv_msg);
		tv_msg.setText(msg);
		btn_left=(Button) view.findViewById(R.id.btn_left);
		btn_left.setText(leftBtnStr);
		btn_right=(Button) view.findViewById(R.id.btn_right);
		btn_right.setText(rightBtnStr); 
		btn_left.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				dismiss();
				if (listener != null) {
					listener.onLeftClick();
				}
			}    		
    	});
		btn_right.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				dismiss();
				if (listener != null) {
					listener.onRightClick();
				}
			}    		
    	});
		return view;
	}
	
	/**
	 * 
	 */
	public interface Listener{
		public void onLeftClick();
		public void onRightClick();
	}
}