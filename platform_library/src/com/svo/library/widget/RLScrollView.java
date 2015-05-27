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

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class RLScrollView extends ScrollView{

	public RLScrollView(Context context) {
		super(context);
	}
	
	public RLScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public RLScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public interface OnScrollChangedListener{
		public void onScrollChanged(int x, int y, int oldxX, int oldY);
	}
	
	private OnScrollChangedListener onScrollChangedListener;
	
	/**
	 * 
	 * @param onScrollChangedListener
	 */
	public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener){
		this.onScrollChangedListener=onScrollChangedListener;
	}
	
	@Override
	protected void onScrollChanged(int x, int y, int oldX, int oldY){
		super.onScrollChanged(x, y, oldX, oldY);
		if(onScrollChangedListener!=null){
			onScrollChangedListener.onScrollChanged(x, y, oldX, oldY);
		}
	}
	
	/**
	 * 
	 * @param child
	 * @return
	 */
	public boolean isChildVisible(View child){
		if(child==null){
			return false;
		}
		Rect scrollBounds = new Rect();
		getHitRect(scrollBounds);
		return child.getLocalVisibleRect(scrollBounds);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAtTop(){
		return getScrollY()<=0;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAtBottom(){
		return getChildAt(getChildCount()-1).getBottom()+getPaddingBottom()
				==getHeight()+getScrollY();
	}
}