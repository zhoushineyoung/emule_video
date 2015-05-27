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

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.svo.library.R;
import com.svo.library.widget.RLOnClickListener;

public class RLListDialog extends RLDialog{
	private Context context;
	private String title;
	private int[] icons;
	private String[] items;
	private Listener listener;
	
	/**
	 * 
	 * @param context
	 * @param title
	 * @param items
	 * @param listener
	 */
	public RLListDialog(Context context,String title, String[] items, Listener listener) {
		super(context);
		this.title=title;
		this.items=items;
		this.listener=listener;
		this.context=context;
		super.createView();
		super.setCanceledOnTouchOutside(false);
		super.setWindowAnimations(R.style.ANIMATIONS_SLIDE_FROM_BOTTOM);
		super.setWindowGravity(Gravity.CENTER);
	}
	
	/**
	 * 
	 * @param context
	 * @param theme
	 * @param title
	 * @param icons
	 * @param items
	 * @param listener
	 */
	public RLListDialog(Context context, String title, int[] icons, String[] items, Listener listener) {
		super(context);
		this.title=title;
		this.icons=icons;
		this.items=items;
		this.listener=listener;
		this.context=context;
		super.createView();
		super.setCanceledOnTouchOutside(false);
		super.setWindowAnimations(R.style.ANIMATIONS_SLIDE_FROM_BOTTOM);
		super.setWindowGravity(Gravity.BOTTOM);
	}
	
	@Override
	protected View getView() {
		View view=LayoutInflater.from(context).inflate(R.layout.dialog_list, null);
		Button bt_cancel=(Button) view.findViewById(R.id.bt_cancel);
		TextView tv_title=(TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		ListView lv=(ListView) view.findViewById(R.id.lv_menu);
		lv.setAdapter(new LVAdapter());
		lv.getLayoutParams().width=LayoutParams.MATCH_PARENT;
		lv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				dismiss();
				if (listener != null) {
					listener.onItemClick(arg2);
				}
			}			
		});
		bt_cancel.setOnClickListener(new RLOnClickListener(){
			public void onClickX(View view) {
				dismiss();
				if (listener != null) {
					listener.onCancel();
				}
			}
    	});
		return view;
	}
	
	/**
	 * 
	 */
	public interface Listener{
		public void onItemClick(int position);
		public void onCancel();
	}
	
	private class LVAdapter extends BaseAdapter{
		@Override
		public int getCount() {		
			return items.length;
		}
		@Override
		public Object getItem(int position) {		
			return null;
		}
		@Override
		public long getItemId(int position) {		
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(icons!=null){
				convertView=LayoutInflater.from(context).inflate(R.layout.dialog_list_item1,null);
			}else{
				convertView=LayoutInflater.from(context).inflate(R.layout.dialog_list_item2,null);
			}
			if(icons!=null){
				ImageView iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
				iv_icon.setImageResource(icons[position]);
			}
			TextView tv_item=(TextView)convertView.findViewById(R.id.tv_item);
			tv_item.setText(items[position]);
			return convertView;
		}		
	}
}