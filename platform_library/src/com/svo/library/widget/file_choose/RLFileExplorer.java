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
package com.svo.library.widget.file_choose;

import java.io.File;
import java.util.ArrayList;
import com.svo.library.R;
import com.svo.library.util.RLUiUtil;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class RLFileExplorer extends ViewGroup implements OnItemClickListener{
	private ListView lv = null;
	private String currentPath;
	private String homePath;
	private ArrayList<Item> list=new ArrayList<Item>();
	private Context context;
	private Listener listener;
	private int textColor=Color.BLACK;
	
	/**
	 * 
	 * @param context
	 */
	public RLFileExplorer(Context context) {
		super(context);
		init(context,null);
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public RLFileExplorer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) { 
        for (int i = 0; i < getChildCount(); i++) {  
            View child = getChildAt(i);  
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());  
        }
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int childCount = getChildCount() ;  
		for(int i=0 ;i<childCount ;i++){  
		  View child = getChildAt(i) ;  
		  child.measure(widthMeasureSpec, heightMeasureSpec);
		 }
	}
	
	private void init(final Context context, AttributeSet attrs){
		this.context=context;
		View rootView = LayoutInflater.from(context).inflate(R.layout.file_explorer, null);
		lv = (ListView) rootView.findViewById(R.id.lv);
		lv.setAdapter(new BaseAdapter(){
			@Override
			public int getCount() {
				return list.size();
			}
			@Override
			public Object getItem(int position) {
				return list.get(position);
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView=LayoutInflater.from(context).inflate(R.layout.file_explorer_item, null);
				Item item=list.get(position);
				ImageView iv=(ImageView) convertView.findViewById(R.id.iv);
				iv.setImageResource(item.getIcon());
				TextView tv=(TextView)convertView.findViewById(R.id.tv);
				tv.setTextColor(textColor);
				tv.setText(item.getName());
				return convertView;
			}
		});
		lv.setOnItemClickListener(this);
		this.addView(rootView);
	}
	
	private void refresh(String path) {
		File[] files = new File(path).listFiles();
		ArrayList<Item> tempList = new ArrayList<Item>(files.length);
		Item parent = new Item();
		parent.setIcon(R.drawable.icon_explorer_folder);
		parent.setName("..");
		tempList.add(parent);
		for (File file : files){
			Item item=new Item();
			if(file.isDirectory()){
				item.setIcon(R.drawable.icon_explorer_folder);
			}else{
				item.setIcon(R.drawable.icon_explorer_file);
			}
			item.setName(file.getName());
			item.setPath(file.getPath()); 
			tempList.add(item);
		}
		if(!list.isEmpty()){
			list.clear();
		}
		list.addAll(tempList);
		lv.setSelection(0);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if (position == 0) {
			File fileParent = new File(currentPath).getParentFile();
			if(!currentPath.equals(homePath)){
				currentPath = fileParent.getAbsolutePath();
				if(listener!=null){
					listener.onPathChanged(currentPath);
				}
				refresh(currentPath);
			}	
		} else {
			String pathx = list.get(position).getPath();
			File file = new File(pathx);
			if(file.canRead()){
				if (file.isDirectory()){
					currentPath=pathx;
					if(listener!=null){
						listener.onPathChanged(currentPath);
					}
					refresh(currentPath);
				}else{
					if(listener!=null){
						listener.onFileSelected(pathx);
					}
				}
			}else{
				RLUiUtil.toast(context,R.string.cannot_access_file);
			}
		}
	}
	
	/**
	 *
	 */
	public interface Listener{
		public void onPathChanged(String path);
		public void onFileSelected(String file);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	/**
	 * 
	 * @param color
	 */
	public void setTextColor(int color){
		this.textColor=color;
	}
	
	public void openPath(String path){
		homePath=path;
		currentPath=path;
		refresh(currentPath);
	}
	
	private class Item{
		private int icon;
		private String name;
		private String path;
		public int getIcon() {
			return icon;
		}
		public void setIcon(int icon) {
			this.icon = icon;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
	}
}