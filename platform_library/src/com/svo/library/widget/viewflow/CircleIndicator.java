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
package com.svo.library.widget.viewflow;

import com.svo.library.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

public class CircleIndicator extends BaseIndicator{
	private static final int STYLE_STROKE=0;
	private static final int STYLE_FILL=1;
	private float radius=3;
	private final Paint mPaintInactive=new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint mPaintActive=new Paint(Paint.ANTI_ALIAS_FLAG);

	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public CircleIndicator(Context context,AttributeSet attrs){
		super(context,attrs);
		TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.Indicator);
		int activeType=typedArray.getInt(R.styleable.Indicator_circleActiveType,STYLE_FILL);
		int activeDefaultColor=0xFFFFFFFF;
		int activeColor=typedArray.getColor(R.styleable.Indicator_circleActiveColor,activeDefaultColor);
		int inactiveType=typedArray.getInt(R.styleable.Indicator_circleInactiveType,STYLE_FILL);
		int inactiveDefaultColor=0x44FFFFFF;
		int inactiveColor=typedArray.getColor(R.styleable.Indicator_circleInactiveColor,inactiveDefaultColor);
		radius=typedArray.getDimension(R.styleable.Indicator_circleRadius,3.0f);
		typedArray.recycle();
		switch(inactiveType){
		case STYLE_FILL:
			mPaintInactive.setStyle(Style.FILL);
			break;
		default:
			mPaintInactive.setStyle(Style.STROKE);
		}
		mPaintInactive.setColor(inactiveColor);
		switch(activeType){
		case STYLE_STROKE:
			mPaintActive.setStyle(Style.STROKE);
			break;
		default:
			mPaintActive.setStyle(Style.FILL);
		}
		mPaintActive.setColor(activeColor);
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		float mLeft=getPaddingLeft()+radius;
		float mTop=getPaddingTop()+radius;
		float mInnerMargin=getInnerMargin()+2*radius;
		int index=getPosition();
		for(int i=0;i<getViewFlow().getViewsCount();i++){
			if(i==index){
				canvas.drawCircle(
						mLeft+mInnerMargin*index,
						mTop,radius,mPaintActive);
			}else{
				canvas.drawCircle(
						mLeft+mInnerMargin*i,
						mTop,radius,mPaintInactive);
			}
		}
	}
	
	@Override
	protected int onMeasureWidth(int measureSpec){
		int specMode=MeasureSpec.getMode(measureSpec);
		int specSize=MeasureSpec.getSize(measureSpec);
		int width=specSize;
		if(specMode!=MeasureSpec.EXACTLY){
			int count=getViewFlow().getAdapter().getCount();
			width=(int)(getPaddingLeft()+getPaddingRight()
					+(count*2*radius)+(count-1)*getInnerMargin()+1);
			if(specMode==MeasureSpec.AT_MOST){
				width=Math.min(width,specSize);
			}
		}
		return width;
	}

	@Override
	protected int onMeasureHeight(int measureSpec){
		int specMode=MeasureSpec.getMode(measureSpec);
		int specSize=MeasureSpec.getSize(measureSpec);
		int height=specSize;
		if(specMode!=MeasureSpec.EXACTLY){
			height=(int)(2*radius+getPaddingTop()+getPaddingBottom()+1);
			if(specMode==MeasureSpec.AT_MOST){
				height=Math.min(height,specSize);
			}
		}
		return height;
	}
}