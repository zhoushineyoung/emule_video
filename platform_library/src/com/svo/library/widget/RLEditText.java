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

import com.svo.library.util.RLSysUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;
/**
 * 
 * @author 杜卫宾<br><br>
 * 重写了EditText，使得在各个版本中展示一样，展示样式同4.0版本的EditText。
 */
public class RLEditText extends EditText {
	private Paint paint;
	private Context context;
	private int paintWidth = 1;
	private int multiple = 1;
	
	public RLEditText(Context context) {
		super(context);
		this.context = context;
		paint = new Paint();
		this.setBackgroundColor(0);
		paint.setStyle(Paint.Style.STROKE);
	}

	public RLEditText(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.context = context;
		paint = new Paint();
		this.setBackgroundColor(0);
		paint.setStyle(Paint.Style.STROKE);
	}
	public RLEditText(Context context,AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
		this.context = context;
		paint = new Paint();
		this.setBackgroundColor(0);
		paint.setStyle(Paint.Style.STROKE);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(this.hasFocus()){
			paint.setColor(0xFF0099cc);
			paintWidth = 1;
			multiple = 1;
			paint.setStrokeWidth(RLSysUtil.dip2px(context, paintWidth));
		}else{
			paint.setColor(0xFFa9a9a9);
			paintWidth = 1;
			multiple = 2;
			paint.setStrokeWidth(RLSysUtil.dip2px(context, paintWidth));
		}
		int w = getWidth();
		int h = getHeight();
		int x = this.getScrollX();
		if(x>0){
			w = w + x;
		}
		int y = this.getScrollY();
		if(y>0){
			h = h + y;
		}
		double d = RLSysUtil.dip2px(context, paintWidth)/2.0;
		int len = (int) Math.ceil(((Math.ceil(d))/multiple));
		canvas.drawLine(0, h-len, w, h-len, paint);
		if(h>=10){
			canvas.drawLine(w-len, h-10, w-len, h, paint);
			canvas.drawLine(0, h-10, 0, h, paint);
		}
	}
}