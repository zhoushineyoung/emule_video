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

import java.util.EnumSet;
import java.util.LinkedList;

import com.svo.library.R;
import com.svo.library.app.RLAPICompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Scroller;

@SuppressLint("HandlerLeak")
public class ViewFlow extends AdapterView<Adapter>{
	private static final int SNAP_VELOCITY=100;
	private static final int INVALID_SCREEN=-1;
	private static final int TOUCH_STATE_REST=0;
	private static final int TOUCH_STATE_SCROLLING=1;
	private LinkedList<View> mLoadedViews;
	private LinkedList<View> mRecycledViews;
	private int mCurrentBufferIndex;
	private int mCurrentAdapterIndex;
	private int mBufferedItemCount=3;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mTouchState=TOUCH_STATE_REST;
	private float mLastMotionX;
	private int TOUCH_SLOP;
	private int MAX_VELOCITY;
	private int mCurrentScreen;
	private int mNextScreen=INVALID_SCREEN;
	private boolean mFirstLayout=true;
	private EnumSet<LazyInit> mLazyInit=EnumSet.allOf(LazyInit.class);
	private Adapter mAdapter;
	private int mLastScrollDirection;
	private AdapterDataSetObserver mDataSetObserver;
	private ViewSwitchListener viewSwitchListener;
	private int mLastOrientation=-1;
	private int mAutoScrollDuration=1000;
	private ScrollMode scrollMode;
	private boolean isTaskRunning=false;
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public ViewFlow(Context context,AttributeSet attrs){
		super(context,attrs);
		TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.ViewFlow);
		setAutoScrollDuration(typedArray.getInt(R.styleable.ViewFlow_autoScrollDuration,1000));
		setBufferedItemCount(typedArray.getInt(R.styleable.ViewFlow_bufferedItemCount,3));
		typedArray.recycle();
		mLoadedViews=new LinkedList<View>();
		mRecycledViews=new LinkedList<View>();
		mScroller=new Scroller(getContext());
		final ViewConfiguration configuration=ViewConfiguration.get(getContext());
		TOUCH_SLOP=configuration.getScaledTouchSlop();
		MAX_VELOCITY=configuration.getScaledMaximumFlingVelocity();
		scrollMode=ScrollMode.MANUAL;
		runTask();
	}

	private OnGlobalLayoutListener orientationChangeListener=new OnGlobalLayoutListener(){
		@Override
		public void onGlobalLayout(){
			RLAPICompat.removeGlobalLayoutListener(getViewTreeObserver(), orientationChangeListener);
			setSelection(mCurrentAdapterIndex);
		}
	};
	
	private void runTask(){
		if(!isTaskRunning){
			new Thread(){
				@Override
				public void run(){
					isTaskRunning=true;
					while(true){
						try{
							Thread.sleep(mAutoScrollDuration);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
						if(mAdapter!=null&&mAdapter.getCount()>0&&scrollMode!=ScrollMode.MANUAL){
							handler.sendEmptyMessage(scrollMode==ScrollMode.AUTO_PREVIOUS?
											MSG_AUTO_PREVIOUS:MSG_AUTO_NEXT);
						}
					}
				}
			}.start();
		}
	}
	
	private static final int MSG_AUTO_PREVIOUS=0x1111;
	private static final int MSG_AUTO_NEXT=0x2222;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_AUTO_PREVIOUS:
				previous();
				break;
			case MSG_AUTO_NEXT:
				next();
				break;
			}
		}
	};
	
	/**
	 * 
	 */
	public static interface ViewSwitchListener{
		/**
		 * 
		 * @param view
		 */
		public void setViewFlow(ViewFlow view);
		/**
		 * 
		 * @param h
		 * @param v
		 * @param oldh
		 * @param oldv
		 */
		public void onScrolled(int h,int v,int oldh,int oldv);
		/**
		 * 
		 * @param view
		 * @param position
		 */
		public void onSwitched(View view,int position);
	}
	
	/**
	 */
	public static interface ViewLazyInitializeListener{
		void onViewLazyInitialize(View view,int position);
	}
	
	/**
	 * 
	 */
	private enum LazyInit{
		LEFT,RIGHT
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.view.View#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		if(newConfig.orientation!=mLastOrientation){
			mLastOrientation=newConfig.orientation;
			getViewTreeObserver().addOnGlobalLayoutListener(orientationChangeListener);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getViewsCount(){
		return mAdapter==null?0:mAdapter.getCount();
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.view.View#onMeasure(int,int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		final int width=MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode=MeasureSpec.getMode(widthMeasureSpec);
		if(widthMode!=MeasureSpec.EXACTLY&&!isInEditMode()){
			throw new IllegalStateException("ViewFlow can only be used in EXACTLY mode.");
		}
		final int heightMode=MeasureSpec.getMode(heightMeasureSpec);
		if(heightMode!=MeasureSpec.EXACTLY&&!isInEditMode()){
			throw new IllegalStateException("ViewFlow can only be used in EXACTLY mode.");
		}
		final int count=getChildCount();
		for(int i=0;i<count;i++){
			getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
		}
		if(mFirstLayout){
			mScroller.startScroll(0,0,mCurrentScreen*width,0,0);
			mFirstLayout=false;
		}
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.widget.AdapterView#onLayout(boolean,int,int,int,int)
	 */
	@Override
	protected void onLayout(boolean changed,int l,int t,int r,int b){
		int childLeft=0;
		final int count=getChildCount();
		for(int i=0;i<count;i++){
			final View child=getChildAt(i);
			if(child.getVisibility()!=View.GONE){
				final int childWidth=child.getMeasuredWidth();
				child.layout(childLeft,0,childLeft+childWidth,child.getMeasuredHeight());
				childLeft+=childWidth;
			}
		}
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event){
		return handleTouchEvent(event, true);
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event){
		return handleTouchEvent(event, false);
	}
	
	private boolean handleTouchEvent(MotionEvent event, boolean isIntercept){
		if(getChildCount()==0){
			return false;
		}
		if(mVelocityTracker==null){
			mVelocityTracker=VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		final int action=event.getAction();
		final float x=event.getX();
		switch(action){
		case MotionEvent.ACTION_DOWN:
			if(!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			mLastMotionX=x;
			mTouchState=mScroller.isFinished()?TOUCH_STATE_REST:TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_MOVE:
			final int deltaX=(int)(mLastMotionX-x);
			boolean xMoved=Math.abs(deltaX)>TOUCH_SLOP;
			if(xMoved){
				mTouchState=TOUCH_STATE_SCROLLING;
			}
			if(mTouchState==TOUCH_STATE_SCROLLING){
				if(getParent()!=null){
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				mLastMotionX=x;
				final int scrollX=getScrollX();
				if(deltaX<0){
					if(scrollX>0){
						scrollBy(Math.max(-scrollX,deltaX),0);
					}
				}else if(deltaX>0){
					final int availableToScroll=getChildAt(
							getChildCount()-1).getRight()-scrollX-getWidth();
					if(availableToScroll>0){
						scrollBy(Math.min(availableToScroll,deltaX),0);
					}
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if(mTouchState==TOUCH_STATE_SCROLLING){
				final VelocityTracker velocityTracker=mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000,MAX_VELOCITY);
				int velocityX=(int) velocityTracker.getXVelocity();
				if(velocityX>SNAP_VELOCITY){
					previous();
				}else if(velocityX<-SNAP_VELOCITY){
					next();
				}else{
					snapToDestination();
				}
				if(mVelocityTracker!=null){
					mVelocityTracker.recycle();
					mVelocityTracker=null;
				}
			}
			mTouchState=TOUCH_STATE_REST;
			if(getParent()!=null){
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState=TOUCH_STATE_REST;
			if(getParent()!=null){
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			if(!isIntercept){
				snapToDestination();
			}
			break;
		}
		return !isIntercept;
	}
	
	/**
	 * 
	 */
	public void next(){
		if(mCurrentScreen<getChildCount()-1){
			snapToScreen(mCurrentScreen+1);
		}else{
			setSelection(0);
		}
	}
	
	/**
	 * 
	 */
	public void previous(){
		if(mCurrentScreen>0){
			snapToScreen(mCurrentScreen-1);
		}else{
			setSelection(mAdapter.getCount()-1);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCurrentIndex(){
		return mCurrentScreen;
	}
	
	/**
	 * 
	 * @param duration
	 */
	public void setAutoScrollDuration(int duration){
		if(duration<1000){
			throw new IllegalArgumentException("AutoScrollDuration should be larger than 1000.");
		}
		mAutoScrollDuration=duration;
	}
	
	/**
	 * 
	 * @param count
	 */
	public void setBufferedItemCount(int count){
		if(count<0){
			throw new IllegalArgumentException("BufferedItemCount should be larger than 0.");
		}
		mBufferedItemCount=count;
	}
	
	/**
	 * 
	 */
	public enum ScrollMode{
		MANUAL,
		AUTO_PREVIOUS,
		AUTO_NEXT;
	}
	
	/**
	 * 
	 * @param scrollMode
	 */
	public void setScrollMode(ScrollMode scrollMode){
		this.scrollMode=scrollMode;
	}
	
	/**
	 * 
	 * @return
	 */
	public ScrollMode getScrollMode(){
		return scrollMode;
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.view.View#onScrollChanged(int,int,int,int)
	 */
	@Override
	protected void onScrollChanged(int h,int v,int oldh,int oldv){
		super.onScrollChanged(h,v,oldh,oldv);
		if(viewSwitchListener!=null){
			int hPerceived=h+(mCurrentAdapterIndex-mCurrentBufferIndex)*getWidth();
			viewSwitchListener.onScrolled(hPerceived,v,oldh,oldv);
		}
	}

	private void snapToDestination(){
		final int screenWidth=getWidth();
		final int whichScreen=(getScrollX()+(screenWidth/2))/screenWidth;
		snapToScreen(whichScreen);
	}
	
	private void snapToScreen(int whichScreen){
		mLastScrollDirection=whichScreen-mCurrentScreen;
		if(mScroller.isFinished()){
			whichScreen=Math.max(0,Math.min(whichScreen,getChildCount()-1));
			mNextScreen=whichScreen;
			final int newX=whichScreen*getWidth();
			final int delta=newX-getScrollX();
			mScroller.startScroll(getScrollX(),0,delta,0,Math.abs(delta)*2);
			invalidate();
		}
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.view.View#computeScroll()
	 */
	@Override
	public void computeScroll(){
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
			postInvalidate();
		}else if(mNextScreen!=INVALID_SCREEN){
			mCurrentScreen=Math.max(0,Math.min(mNextScreen,getChildCount()-1));
			mNextScreen=INVALID_SCREEN;
			postViewSwitched(mLastScrollDirection);
		}
	}

	private void setVisibleView(int indexInBuffer,boolean uiThread){
		mCurrentScreen=Math.max(0,Math.min(indexInBuffer,getChildCount()-1));
		int dx=(mCurrentScreen*getWidth())-mScroller.getCurrX();
		mScroller.startScroll(mScroller.getCurrX(),mScroller.getCurrY(),dx,0,0);
		if(dx==0){
			onScrollChanged(mScroller.getCurrX()+dx,mScroller.getCurrY(),mScroller.getCurrX()+dx,mScroller.getCurrY());
		}
		if(uiThread){
			invalidate();
		}else{
			postInvalidate();
		}
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.widget.AdapterView#getAdapter()
	 */
	@Override
	public Adapter getAdapter(){
		return mAdapter;
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.widget.AdapterView#setAdapter(android.widget.Adapter)
	 */
	@Override
	public void setAdapter(Adapter adapter){
		if(mAdapter!=null){
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
		}
		mAdapter=adapter;
		if(mAdapter!=null){
			mDataSetObserver=new AdapterDataSetObserver();
			mAdapter.registerDataSetObserver(mDataSetObserver);
		}
		if(mAdapter!=null&&mAdapter.getCount()>0){
			setSelection(0);
		}		
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.widget.AdapterView#getSelectedView()
	 */
	@Override
	public View getSelectedView(){
		return(mCurrentBufferIndex<mLoadedViews.size()?mLoadedViews
				.get(mCurrentBufferIndex):null);
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.widget.AdapterView#getSelectedItemPosition()
	 */
    @Override
    public int getSelectedItemPosition(){
        return mCurrentAdapterIndex;
    }

	/**
	 * Set the FlowIndicator
	 * @param flowIndicator
	 */
	public void setIndicator(ViewSwitchListener indicator){
		viewSwitchListener=indicator;
		viewSwitchListener.setViewFlow(this);
	}
	
	/**
	 * 
	 */
	protected void recycleViews(){
		while(!mLoadedViews.isEmpty()){
			recycleView(mLoadedViews.remove());
		}
	}
	
	/**
	 * 
	 * @param view
	 */
	protected void recycleView(View view){
		if(view!=null){
			mRecycledViews.add(view);
			detachViewFromParent(view);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected View getRecycledView(){
		return(mRecycledViews.isEmpty()?null:mRecycledViews.remove(0));
	}
	
	/*
	 *(non-Javadoc)
	 * @see android.widget.AdapterView#setSelection(int)
	 */
	@Override
	public void setSelection(int position){
		mNextScreen=INVALID_SCREEN;
		mScroller.forceFinished(true);
		if(mAdapter!=null){
			position=Math.max(position,0);
			position=Math.min(position,mAdapter.getCount()-1);
			recycleViews();
			View currentView=makeAndAddView(position,true);
			mLoadedViews.addLast(currentView);
			for(int offset=1;mBufferedItemCount-offset>=0;offset++){
				int leftIndex=position-offset;
				int rightIndex=position+offset;
				if(leftIndex>=0){
					mLoadedViews.addFirst(makeAndAddView(leftIndex,false));
				}
				if(rightIndex<mAdapter.getCount()){
					mLoadedViews.addLast(makeAndAddView(rightIndex,true));
				}
			}
			mCurrentBufferIndex=mLoadedViews.indexOf(currentView);
			mCurrentAdapterIndex=position;
			requestLayout();
			setVisibleView(mCurrentBufferIndex,false);
			if(viewSwitchListener!=null){
				viewSwitchListener.onSwitched(currentView,mCurrentAdapterIndex);
			}
		}
	}

	private void resetFocus(){
		recycleViews();
		removeAllViewsInLayout();
		mLazyInit.addAll(EnumSet.allOf(LazyInit.class));
		for(int i=Math.max(0,mCurrentAdapterIndex-mBufferedItemCount);
				i<Math.min(mAdapter.getCount(),mCurrentAdapterIndex+mBufferedItemCount+1);
				i++){
			mLoadedViews.addLast(makeAndAddView(i,true));
			if(i==mCurrentAdapterIndex){
				mCurrentBufferIndex=mLoadedViews.size()-1;
			}
		}
		requestLayout();
	}

	private void postViewSwitched(int direction){
		if(direction!=0){
			if(direction>0){
				mCurrentAdapterIndex++;
				mCurrentBufferIndex++;
				mLazyInit.remove(LazyInit.LEFT);
				mLazyInit.add(LazyInit.RIGHT);
				if(mCurrentAdapterIndex>mBufferedItemCount){
					recycleView(mLoadedViews.removeFirst());
					mCurrentBufferIndex--;
				}
				int newBufferIndex=mCurrentAdapterIndex+mBufferedItemCount;
				if(newBufferIndex<mAdapter.getCount())
					mLoadedViews.addLast(makeAndAddView(newBufferIndex,true));
			}else{
				mCurrentAdapterIndex--;
				mCurrentBufferIndex--;
				mLazyInit.add(LazyInit.LEFT);
				mLazyInit.remove(LazyInit.RIGHT);
				if(mAdapter.getCount()-1-mCurrentAdapterIndex>mBufferedItemCount){
					recycleView(mLoadedViews.removeLast());
				}
				int newBufferIndex=mCurrentAdapterIndex-mBufferedItemCount;
				if(newBufferIndex>-1){
					mLoadedViews.addFirst(makeAndAddView(newBufferIndex,false));
					mCurrentBufferIndex++;
				}
			}
			requestLayout();
			setVisibleView(mCurrentBufferIndex,true);
			if(viewSwitchListener!=null){
				viewSwitchListener.onSwitched(mLoadedViews.get(mCurrentBufferIndex),mCurrentAdapterIndex);
			}
		}
	}

	private View makeAndAddView(int position,boolean addToEnd){
		View convertView=getRecycledView();
		View view=mAdapter.getView(position,convertView,this);
		if(view!=convertView){
			mRecycledViews.add(convertView);
		}
		ViewGroup.LayoutParams p=view.getLayoutParams();
		if(p==null){
			p=new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT,0);
		}
		if(view==convertView){
			attachViewToParent(view,(addToEnd?-1:0),p);
		}else{
			addViewInLayout(view,(addToEnd?-1:0),p,true);
		}
		return view;
	}
	
	/**
	 * 
	 */
	private class AdapterDataSetObserver extends DataSetObserver{
		@Override
		public void onChanged(){
			View v=getChildAt(mCurrentBufferIndex);
			if(v!=null){
				for(int index=0;index<mAdapter.getCount();index++){
					if(v.equals(mAdapter.getItem(index))){
						mCurrentAdapterIndex=index;
						break;
					}
				}
			}
			resetFocus();
		}
		@Override
		public void onInvalidated(){
		}
	}
}