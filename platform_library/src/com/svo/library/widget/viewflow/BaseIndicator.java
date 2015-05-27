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
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

public abstract class BaseIndicator extends View implements ViewFlow.ViewSwitchListener{
	private ViewFlow viewFlow;
	private int position=0;
	private int fadeOutTime=-1;
	private FadeTimer timer;
	private float innerMargin=3;
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	protected BaseIndicator(Context context,AttributeSet attrs){
		super(context,attrs);
		TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.Indicator);
		innerMargin=typedArray.getDimension(R.styleable.Indicator_innerMargin,3.0f);
		if(innerMargin<0){
			throw new IllegalArgumentException("InnerMargin should be larger than 0.");
		}
		fadeOutTime=typedArray.getInt(R.styleable.Indicator_fadeOutTime,-1);
		typedArray.recycle();
	}
	
	/**
	 * 
	 * @param fadeOutTime
	 */
	public void setFadeOutTime(int fadeOutTime){
		this.fadeOutTime=fadeOutTime;
	}
	
	/**
	 * 
	 * @return
	 */
	protected float getInnerMargin(){
		return innerMargin;
	}
	
	/**
	 * 
	 * @return
	 */
	protected ViewFlow getViewFlow(){
		return viewFlow;
	}
	
	/**
	 * 
	 * @return
	 */
	protected int getPosition(){
		return position;
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(getViewFlow()==null){
			return;
		}
	}

	/*
	 *(non-Javadoc)
	 * @see android.view.View#onMeasure(int,int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
		if(viewFlow==null){
			return;
		}
		setMeasuredDimension(onMeasureWidth(widthMeasureSpec),onMeasureHeight(heightMeasureSpec));
	}
	
	/**
	 * 
	 * @param measureSpec
	 * @return
	 */
	protected abstract int onMeasureWidth(int measureSpec);
	
	/**
	 * 
	 * @param measureSpec
	 * @return
	 */
	protected abstract int onMeasureHeight(int measureSpec);

	@Override
	public void setViewFlow(ViewFlow view){
		resetTimer();
		viewFlow=view;
		invalidate();
	}

	@Override
	public void onScrolled(int h,int v,int oldh,int oldv){
		setVisibility(View.VISIBLE);
		resetTimer();
		if(viewFlow!=null&&viewFlow.getWidth()>0){
			position=h/viewFlow.getWidth();
		}
		invalidate();
	}
	
	@Override
	public void onSwitched(View view,int position){
	}
	
	private void resetTimer(){
		if(fadeOutTime > 0){
			if(timer == null || timer.isRun == false){
				timer=new FadeTimer();
				timer.execute();
			} else{
				timer.resetTimer();
			}
		}
	}
	
	private class FadeTimer extends AsyncTask<Void,Void,Void>{
		private int timer=0;
		private boolean isRun=true;
		public void resetTimer(){
			timer=0;
		}
		@Override
		protected Void doInBackground(Void... arg0){
			while(isRun){
				try{
					Thread.sleep(1);
					timer++;
					if(timer == fadeOutTime){
						isRun=false;
					}
				} catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result){
			Animation animation=AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
			animation.setAnimationListener(new AnimationListener(){
				@Override
				public void onAnimationEnd(Animation animation){
					setVisibility(View.INVISIBLE);
				}
				@Override
				public void onAnimationRepeat(Animation animation){
				}
				@Override
				public void onAnimationStart(Animation animation){
				}
			});
			startAnimation(animation);
		}
	}
}