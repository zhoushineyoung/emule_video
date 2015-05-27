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
package com.svo.library.widget;

import com.svo.library.R;
import com.svo.library.util.RLSysUtil;
import com.svo.library.widget.dialog.RLListDialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RLSpinner extends ViewGroup{
	private TextView tv;
	private String[] items;
	private Listener listener;
	
	/**
	 * 
	 * @param context
	 */
	public RLSpinner(Context context) {
		super(context);
		init(context,null);
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public RLSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RLSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
		View rootView = LayoutInflater.from(context).inflate(R.layout.spinner, null);
		tv=(TextView) rootView.findViewById(R.id.tv);
		if(attrs!=null){
			TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.RLSpinner);
			int resouceId = typedArray.getResourceId(R.styleable.RLSpinner_defaultText, 0);
			tv.setText(resouceId > 0 ? typedArray.getResources().getText(resouceId)
					: typedArray.getString(R.styleable.RLSpinner_defaultText));
			tv.setTextColor(typedArray.getColor(R.styleable.RLSpinner_textColor, Color.BLACK));
			tv.setTextSize(RLSysUtil.px2dip(context, typedArray.getDimension(R.styleable.RLSpinner_textSize, 15)));
			typedArray.recycle();
		}
		tv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(items!=null&&listener!=null){
					new RLListDialog(context,
							context.getString(R.string.select_item),
							items,new RLListDialog.Listener(){
								@Override
								public void onItemClick(int position) {
									tv.setText(items[position]);
									listener.onItemSeleted(position);
								}
								@Override
								public void onCancel() {
								}
					}).show();
				}
			}
		});
		this.addView(rootView);
	}
	
	/**
	 * 
	 * @param defaultText
	 */
	public void setDefaultText(String defaultText){
		tv.setText(defaultText);
	}
	
	/**
	 * 
	 * @param color
	 */
	public void setTextColor(int color){
		tv.setTextColor(color);
	}
	
	/**
	 * 
	 * @param size
	 */
	public void setTextSize(float size){
		tv.setTextSize(size);
	}
	
	/**
	 * 
	 * @param items
	 * @param listener
	 */
	public void setItems(String[] items, Listener listener){
		this.items=items;
		this.listener=listener;
	}
	
	/**
	 * 
	 */
	public interface Listener{
		public void onItemSeleted(int position);
	}
}